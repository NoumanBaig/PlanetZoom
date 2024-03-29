package com.angadi.tripmanagementa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.BizQrHistoryActivity;
import com.angadi.tripmanagementa.adapters.CountSectionAdapter;
import com.angadi.tripmanagementa.models.DashboardResponse;
import com.angadi.tripmanagementa.models.DashboardResult;
import com.angadi.tripmanagementa.models.QrCodeUniqueId;
import com.angadi.tripmanagementa.models.Result;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    @BindView(R.id.recyclerDashboard)
    RecyclerView recyclerView;
    @BindView(R.id.layout_not_found)
    LinearLayout layout_not_found;
    //    @BindView(R.id.loading_layout)
//    View loadingIndicator;
    CountSectionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);

        // setupRecycler();
        getDashboardQrCodes();

        return view;
    }


    private void getDashboardQrCodes() {
        MyProgressDialog.show(getActivity(), "Loading...");
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DashboardResponse> responseCall = apiInterface.dashboard("true", token);
        responseCall.enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                Log.e("dashboard_res", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        layout_not_found.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        List<DashboardResult> resultList = response.body().getResults();

                        adapter = new CountSectionAdapter(getActivity(), resultList);
                        recyclerView.setAdapter(adapter);

                        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
                        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
                        layoutManager.setSpanSizeLookup(lookup);
                        recyclerView.setLayoutManager(layoutManager);

                    } else {
                        layout_not_found.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Log.e("dashboard_res", "" + t);
                MyProgressDialog.dismiss();
                layout_not_found.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.fab_bizQrHistory)
    public void onFabClick(View view) {
        startActivity(new Intent(getActivity(), BizQrHistoryActivity.class).putExtra("scan_history","dashboard"));
    }
}
