package com.modernfarm.ui.favorites;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.modernfarm.R;

import static com.modernfarm.services.ApiClient.BASE_URL;

public class FavoritesDetailActivity extends AppCompatActivity {
    private TextView tvNamaTanaman, tvJenisTanaman, tvCreatedAt, tvDeskripsi, tvCaraTanam;
    private ImageView imgTanaman;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_detail);

        // Inisialisasi View
        tvNamaTanaman = findViewById(R.id.tvNamaTanaman);
        tvJenisTanaman = findViewById(R.id.tvJenisTanaman);
        imgTanaman = findViewById(R.id.imgTanaman);
        tvCreatedAt = findViewById(R.id.tvDipublikasikan);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        tvCaraTanam = findViewById(R.id.tvCaraTanam);
        progressBar = findViewById(R.id.progress2);

        // Mengambil data yang didapat dari bypass Adapter FarmListFavoritesAdapter
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();

            // Kemudian data yang didapat akan dimasukkan kemasing2 view
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
            tvNamaTanaman.setText(bundle.getString("NAMATANAMAN"));
            tvJenisTanaman.setText(bundle.getString("JENISTANAMAN"));
            tvCreatedAt.setText(bundle.getString("PUBLIKASI"));
            tvDeskripsi.setText(bundle.getString("DESKRIPSI"));
            tvCaraTanam.setText(bundle.getString("CARATANAM"));
        }
    }

}