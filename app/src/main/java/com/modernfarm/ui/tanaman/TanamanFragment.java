package com.modernfarm.ui.tanaman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.modernfarm.R;
import com.modernfarm.adapter.FarmListAdapter;
import com.modernfarm.model.FarmList;
import com.modernfarm.model.FarmListResponse;
import com.modernfarm.services.ApiClient;
import com.modernfarm.services.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TanamanFragment extends Fragment {
    private View view;
    private ApiService apiService;
    private ProgressDialog dialog;
    private ArrayList<FarmList> farmListResponses;
    private FarmListAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tanaman, container, false);

        // Inisialisasi Recycler View
        recyclerView = view.findViewById(R.id.rvTanaman);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Inisialisasi FloatingActionButton dan Action ketika ditekan
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(view.getContext(), TambahTanamanActivity.class)));

        // Memanggil method load data
        loadData();

        return view;
    }

    private void loadData() {
        dialog = new ProgressDialog(view.getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Loading ...");
        dialog.show();

        /* Didalam method ini memanggil Api Service method getFarmList sehingga data yang didapat
            Dari JSON dapat ditangkap kemudian dimasukkan kedalam ArrayList
         */
        apiService = ApiClient.getApiClient().create(ApiService.class);
        apiService.getFarmList().enqueue(new Callback<FarmListResponse>() {
            @Override
            public void onResponse(Call<FarmListResponse> call, Response<FarmListResponse> response) {
                farmListResponses = new ArrayList<>();
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    farmListResponses.addAll(response.body().getData());
                }
                /* Data yang sudah didalam List kemudian dibypass ke Adapter Sehingga bisa dibaca
                    Oleh Recyclerview*/
                adapter = new FarmListAdapter(farmListResponses);
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