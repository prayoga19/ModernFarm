package com.modernfarm.ui.favorites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modernfarm.R;
import com.modernfarm.adapter.FarmListAdapter;
import com.modernfarm.adapter.FarmListFavoritesAdapter;
import com.modernfarm.model.FarmList;
import com.modernfarm.model.FarmListResponse;
import com.modernfarm.services.ApiClient;
import com.modernfarm.services.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends Fragment {
    private View view;
    private ApiService apiService;
    private ProgressDialog dialog;
    private ArrayList<FarmList> farmListResponses;
    private FarmListFavoritesAdapter adapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Inisialisasi Recyclerview
        recyclerView = view.findViewById(R.id.rvTanaman);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        loadData();

        return view;
    }

    // Method Load data
    private void loadData() {
        // Menampilkan indicator load
        dialog = new ProgressDialog(view.getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Loading ...");
        dialog.show();

        /* Memanggil Api service method getFarmListFavorite, kemudian data yang didapat dari
            JSON dimasukkan kedalam ARRAY Sebelumnya akhirnya akan dibypass ke Adapter
         */
        apiService = ApiClient.getApiClient().create(ApiService.class);
        apiService.getFarmListFavorite().enqueue(new Callback<FarmListResponse>() {
            @Override
            public void onResponse(Call<FarmListResponse> call, Response<FarmListResponse> response) {
                farmListResponses = new ArrayList<>();
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    farmListResponses.addAll(response.body().getData());
                }
                // Bypass data dari array ke adapter
                adapter = new FarmListFavoritesAdapter(farmListResponses);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<FarmListResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(view.getContext(), "Request Timeout", Toast.LENGTH_LONG).show();
            }
        });
    }
}