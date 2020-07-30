package com.modernfarm.ui.tanaman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.modernfarm.MainActivity;
import com.modernfarm.R;
import com.modernfarm.model.GeneralResponse;
import com.modernfarm.services.ApiClient;
import com.modernfarm.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.modernfarm.services.ApiClient.BASE_URL;

public class TanamanDetailActivity extends AppCompatActivity {
    private TextView tvNamaTanaman, tvJenisTanaman, tvCreatedAt, tvDeskripsi, tvCaraTanam;
    private ImageView imgTanaman;
    private FloatingActionButton fabLove;
    private ApiService apiService;
    private ProgressDialog dialog;
    private String ID;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanaman_detail);

        // Inisialisasi View
        tvNamaTanaman = findViewById(R.id.tvNamaTanaman);
        tvJenisTanaman = findViewById(R.id.tvJenisTanaman);
        imgTanaman = findViewById(R.id.imgTanaman);
        tvCreatedAt = findViewById(R.id.tvDipublikasikan);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        tvCaraTanam = findViewById(R.id.tvCaraTanam);
        fabLove = findViewById(R.id.fabLove);
        progressBar = findViewById(R.id.progress);

        // Mendapatkan Bypass data yang didapat dari Adapter
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            ID = bundle.getString("ID");

            // Load Imagenya dan beri indicator load
            Glide.with(this)
                    .load(BASE_URL + "/images/" + bundle.getString("GAMBAR"))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgTanaman);

            // Set View dari Data yang sudah dibypass
            tvNamaTanaman.setText(bundle.getString("NAMATANAMAN"));
            tvJenisTanaman.setText(bundle.getString("JENISTANAMAN"));
            tvCreatedAt.setText(bundle.getString("PUBLIKASI"));
            tvDeskripsi.setText(bundle.getString("DESKRIPSI"));
            tvCaraTanam.setText(bundle.getString("CARATANAM"));

            // 0 berarti false atau tidak populer, 1 berarti true berarti populer
            int populer = Integer.parseInt(bundle.getString("POPULER"));
            if (populer == 0) {
                // Jika tidak populer set icon dengan border
                fabLove.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_baseline_favorite_border_24));
            } else {
                fabLove.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_baseline_favorite_24));
            }

            // Jika FloatingActionButton ditekan cek dulu apakah sebelumnya dia populer atau tidak
            fabLove.setOnClickListener(v -> {
                if (populer == 0) {
                    likeFarm(bundle.getString("ID"));
                } else {
                    unlikeFarm(bundle.getString("ID"));
                }
            });
        }
    }

    // Method mengubah dari populer ke tidak populer
    private void unlikeFarm(String id) {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading ...");
        dialog.show();

        // Memanggil Api Service dan method unlike
        apiService = ApiClient.getApiClient().create(ApiService.class);
        apiService.unlikeFarm(id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(TanamanDetailActivity.this, "Menghapus dari Favorite", Toast.LENGTH_SHORT).show();
                    fabLove.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_baseline_favorite_border_24));
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TanamanDetailActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method mengubah dari tidak populer ke populer
    private void likeFarm(String id) {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading ...");
        dialog.show();

        // Memanggil Api Service dan method like
        apiService = ApiClient.getApiClient().create(ApiService.class);
        apiService.likeFarm(id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(TanamanDetailActivity.this, "Menambahkan ke Favorite", Toast.LENGTH_SHORT).show();
                    fabLove.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_baseline_favorite_24));
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TanamanDetailActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menampilkan button Hapus di Action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Action Button Hapus
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.miDelete) {
            showDialogDelete();
        }
        return false;
    }

    // Method untuk menghapus data dari DB
    private void showDialogDelete() {
        final Dialog dialogDelete;
        final Button btDelete;
        dialogDelete = new Dialog(this);
        dialogDelete.setContentView(R.layout.dialog_delete);

        // Menampilkan dialog konfirmasi
        dialogDelete.show();
        final Window window = dialogDelete.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Keika button konfirmasi delete ditekan maka memanggil Api service method delete Farm
        btDelete = dialogDelete.findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(v -> {
            dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage("Loading ...");
            dialog.show();

            apiService = ApiClient.getApiClient().create(ApiService.class);
            apiService.deleteFarm(ID).enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    if (response.isSuccessful()){
                        dialog.dismiss();
                        dialogDelete.dismiss();
                        Toast.makeText(TanamanDetailActivity.this, "Berhasil Menghapus", Toast.LENGTH_SHORT).show();

                        // Jika berhasil maka pindahkan ke activity Utama
                        startActivity(new Intent(TanamanDetailActivity.this, MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    dialogDelete.dismiss();
                    Toast.makeText(TanamanDetailActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}