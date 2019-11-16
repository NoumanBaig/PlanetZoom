package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.InfluencersAdapter;
import com.angadi.tripmanagementa.models.Influencers;
import com.angadi.tripmanagementa.models.InfluencersResponse;
import com.angadi.tripmanagementa.models.ShowSubEventResponse;
import com.angadi.tripmanagementa.models.SubEventResult;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfluencersActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewInfluencers)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influencers);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Influencers");


        getInfluencers();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getInfluencers() {
        MyProgressDialog.show(InfluencersActivity.this,"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<InfluencersResponse> call = apiInterface.getInfluencers("show");
        call.enqueue(new Callback<InfluencersResponse>() {
            @Override
            public void onResponse(Call<InfluencersResponse> call, Response<InfluencersResponse> response) {
                Log.e("getInfluencers", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<Influencers> influencers = response.body().getInfluencers();
                    setAdapter(influencers);
                } else {
                    Toast.makeText(InfluencersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InfluencersResponse> call, Throwable t) {
                Log.e("getInfluencers", "" + t);
                MyProgressDialog.dismiss();
            }
        });

    }


    private void setAdapter(List<Influencers> influencers){
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        InfluencersAdapter adapter = new InfluencersAdapter(InfluencersActivity.this,influencers);
        recyclerView.setAdapter(adapter);
    }
}
