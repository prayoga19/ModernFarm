package com.modernfarm.ui.tanaman;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.modernfarm.MainActivity;
import com.modernfarm.R;
import com.modernfarm.model.GeneralResponse;
import com.modernfarm.model.JenisListResponse;
import com.modernfarm.services.ApiClient;
import com.modernfarm.services.ApiService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahTanamanActivity extends AppCompatActivity {
    private Spinner spinnerJenis;
    private ApiService apiService;
    private ProgressDialog dialog;
    private List<String> listSpinner;
    private List<Integer> listCode;
    private ImageView imgPreviewTanaman;
    private Button btnTambahTanaman;
    private EditText edtNamaTanaman, edtDeskripsi, edtCaraTanam;
    private String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tanaman);

        // Inisialisasi title dan button back
        getSupportActionBar().setTitle("Tambah Tanaman");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Menampilkan loading indicator
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading ...");
        dialog.show();

        // Inisialisasi View
        spinnerJenis = findViewById(R.id.spinnerjenis);
        ImageButton btnTambahJenis = findViewById(R.id.btnTambahJenis);
        btnTambahTanaman = findViewById(R.id.tambahTanaman);
        edtNamaTanaman = findViewById(R.id.edtNamaTanaman);
        edtDeskripsi = findViewById(R.id.edtDeskripsi);
        edtCaraTanam = findViewById(R.id.edtCaraTanam);
        ImageView ic_camera = findViewById(R.id.ic_Camera);
        imgPreviewTanaman = findViewById(R.id.imgTanaman);

        // Memanggil Api Service Method getJenis sebelum ditampilkan kedalam Spinner
        apiService = ApiClient.getApiClient().create(ApiService.class);
        apiService.getJenis().enqueue(new Callback<JenisListResponse>() {
            @Override
            public void onResponse(Call<JenisListResponse> call, Response<JenisListResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();

                    listSpinner = new ArrayList<>();
                    listCode = new ArrayList<>();
                    for (int i = 0; i < response.body().getDataCount(); i++) {
                        /* Data yang ditangkap dari respons JSON dimasukkan kedalam array */
                        listSpinner.add(response.body().getData().get(i).getNamaJenis());
                        listCode.add(Integer.valueOf(response.body().getData().get(i).getId()));
                    }
                    // Data yang sudah diaaray diset ke adapter Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahTanamanActivity.this, R.layout.support_simple_spinner_dropdown_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerJenis.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<JenisListResponse> call, Throwable t) {

            }
        });

        // Action button + untuk menambahkan Jenis
        btnTambahJenis.setOnClickListener(v -> {
            // Menampilkan dialog dulu
            Button btnTambahkan;
            EditText edtJenis;
            Dialog dialogTambah;
            dialogTambah = new Dialog(TambahTanamanActivity.this);
            dialogTambah.setContentView(R.layout.dialog_insert_jenis);

            btnTambahkan = dialogTambah.findViewById(R.id.btnTambahKan);
            edtJenis = dialogTambah.findViewById(R.id.edt_jenis);

            dialogTambah.show();

            // set size dialog
            Window window = dialogTambah.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            /* Ketika Input jenis tanaman baru dan buttonnya ditekan maka akan memanggil
                Api service Insert Jenis
             */
            btnTambahkan.setOnClickListener(v1 -> {
                if (edtJenis.getText().length() != 0) {
                    dialog = new ProgressDialog(this);
                    dialog.setCancelable(false);
                    dialog.setMessage("Loading ...");
                    dialog.show();
                    apiService = ApiClient.getApiClient().create(ApiService.class);
                    apiService.insertJenis(edtJenis.getText().toString()).enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            dialog.dismiss();
                            dialogTambah.dismiss();
                            Toast.makeText(TambahTanamanActivity.this, "Kategori baru Ditambahkan", Toast.LENGTH_SHORT).show();

                            // Jika Success Refresh Activity
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                        }
                    });
                }else {
                    edtJenis.setError("Wajib diisi");
                }
            });
        });

        // Disini sebagai Listener Spinner
        spinnerJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedCode = listCode.get(position);

                /* Jika button utama atau Tambah tanaman diclick maka akan mengambil informasi
                Mengenai path Foto, beserta Form formnya
                 */
                btnTambahTanaman.setOnClickListener(v -> {
                    if (mediaPath != null && edtNamaTanaman != null && edtDeskripsi != null && edtCaraTanam != null) {
                        // Kemudian panggil method dan bypass datanya
                        tambahData(edtNamaTanaman.getText().toString(),
                                selectedCode, edtDeskripsi.getText().toString(), edtCaraTanam.getText().toString());
                    } else {
                        Toast.makeText(TambahTanamanActivity.this, "Isi Semua Data", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* Disini Action ketika icon camera ditekan, dilakukan pengecekan dulu, apakah permission
        Baca Storage sudah aktif atau belum. Kemudian Lakukan intent ke galery dengan Request code 0
        */
        ic_camera.setOnClickListener(v -> {
            String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
                Intent galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeryIntent, 0);
            } else {
                EasyPermissions.requestPermissions(this, "Access for storage",
                        101, galleryPermissions);
            }
        });
    }

    /* Kemudian Hasil intent dari camera dengan Request Code 0 akan diolah lebih lanjut */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);

                // Jika berhasil maka set Imageviewnya dengan image yang didapat dari path file
                imgPreviewTanaman.setImageBitmap(BitmapFactory.decodeFile(mediaPath));

                cursor.close();
            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    // Method final Tambah data kedalam DB
    private void tambahData(String namaTanam, int selectedCode, String Deskripsi, String caraMenanam) {
        // Tampilkan dialog load
        dialog.show();

        // Arahkan kedalam path gambar
        File file = new File(mediaPath);

        // Rubah Form yang diinputkan kedalam Request Body sehingga nantinya ditangkap Api Service
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("gambar", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody namaTanaman = RequestBody.create(MediaType.parse("text/plain"), namaTanam);
        RequestBody idJenis = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedCode));
        RequestBody Desc = RequestBody.create(MediaType.parse("text/plain"), Deskripsi);
        RequestBody CaraTanam = RequestBody.create(MediaType.parse("text/plain"), caraMenanam);

        // Kemudian panggil Api Service method InsertFarmList dengan parameter tersebut
        apiService = ApiClient.getApiClient().create(ApiService.class);
        apiService.insertFarmList(fileToUpload, filename, namaTanaman, idJenis, Desc, CaraTanam).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TambahTanamanActivity.this, "Tanaman Baru Ditambahkan", Toast.LENGTH_SHORT).show();

                    // Jika berhasil kembalikan atau Intent ke Main Activity
                    startActivity(new Intent(TambahTanamanActivity.this, MainActivity.class));
                    finish();
                } else if (response.code() == 400) {

                    // Response Code disini custom tergantung dari API PHP
                    Toast.makeText(TambahTanamanActivity.this, "Gambar melebihi 2Mb", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Toast.makeText(TambahTanamanActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    // Action ketika button back diatas ditekan
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}