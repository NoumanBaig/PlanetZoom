package com.angadi.tripmanagementa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.ShowEventsActivity;
import com.angadi.tripmanagementa.models.AdminResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    @BindView(R.id.loading_layout)
    View loadingIndicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.layout_explore,R.id.layout_organise})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_explore:
                setAdmin("disable");
                break;
            case R.id.layout_organise:
                setAdmin("enable");
                break;
            default:
                break;
        }
    }


    private void setAdmin(String enable) {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AdminResponse> responseCall = apiInterface.setAdmin("true",token,enable);
        responseCall.enqueue(new Callback<AdminResponse>() {
            @Override
            public void onResponse(Call<AdminResponse> call, Response<AdminResponse> response) {
                Log.e("setAdmin_res", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        if (response.body().getMessage().equalsIgnoreCase("Admin Panel Created")){
                            startActivity(new Intent(getActivity(), ShowEventsActivity.class).putExtra("events","organiser"));
                        }else {
                            startActivity(new Intent(getActivity(), ShowEventsActivity.class).putExtra("events","explorer"));

                        }
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AdminResponse> call, Throwable t) {
                Log.e("setAdmin_res", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });

    }


}
