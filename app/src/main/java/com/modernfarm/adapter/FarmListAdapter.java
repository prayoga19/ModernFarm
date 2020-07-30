package com.modernfarm.adapter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.modernfarm.R;
import com.modernfarm.model.FarmList;
import com.modernfarm.ui.tanaman.TanamanDetailActivity;

import java.util.ArrayList;

import static com.modernfarm.services.ApiClient.BASE_URL;

public class FarmListAdapter extends RecyclerView.Adapter<FarmListAdapter.ViewHolder> {
    private ArrayList<FarmList> farmListArrayList;
    private View view;
    private ImageView imgTanaman;

    // Constructor untuk bypass array yang didapat dari DB
    public FarmListAdapter(ArrayList<FarmList> farmListResponses) {
        farmListArrayList = farmListResponses;
    }

    // Inisialisasi Layputnya yang akan digunakan atau dilooping
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_tanaman, parent, false);
        return new ViewHolder(view);
    }

    // Set tiap iterasi view dengan tiap iterasi array (Selaras)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNamaTanaman.setText(farmListArrayList.get(position).getNamaTanaman());
        holder.tvDescTanaman.setText(farmListArrayList.get(position).getDeskripsi());
        // Load image dari database
        Glide.with(view.getContext())
                .load(BASE_URL + "images/" +farmListArrayList.get(position).getGambar())
                .into(imgTanaman);

        // Jika OS digunakan OS Oreo maka textnya Justify
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            holder.tvDescTanaman.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        // Ketika tiap view ditekan maka akan berpindah ke activity Detail, dengan bypass data
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("ID", farmListArrayList.get(position).getId());
            bundle.putString("GAMBAR", farmListArrayList.get(position).getGambar());
            bundle.putString("NAMATANAMAN", farmListArrayList.get(position).getNamaTanaman());
            bundle.putString("JENISTANAMAN", farmListArrayList.get(position).getNamaJenis());
            bundle.putString("PUBLIKASI", farmListArrayList.get(position).getCreatedAt());
            bundle.putString("DESKRIPSI", farmListArrayList.get(position).getDeskripsi());
            bundle.putString("CARATANAM", farmListArrayList.get(position).getCaratanam());
            bundle.putString("POPULER", farmListArrayList.get(position).getPopuler());
            Intent intent = new Intent(view.getContext(), TanamanDetailActivity.class);
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        });
    }

    // Loop dilakukan sebanyak Jumlah Arrau
    @Override
    public int getItemCount() {
        return farmListArrayList.size();
    }

    // Inisialisasi View
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaTanaman, tvDescTanaman;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNamaTanaman = itemView.findViewById(R.id.tvNamaTanaman);
            tvDescTanaman = itemView.findViewById(R.id.tvDescTanaman);
            imgTanaman = itemView.findViewById(R.id.imgTanaman);
        }
    }
}
