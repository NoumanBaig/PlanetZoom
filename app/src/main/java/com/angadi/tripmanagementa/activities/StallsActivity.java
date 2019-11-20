package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.InfluencersAdapter;
import com.angadi.tripmanagementa.adapters.StallsAdapter;
import com.angadi.tripmanagementa.models.Influencers;
import com.angadi.tripmanagementa.models.InfluencersResponse;
import com.angadi.tripmanagementa.models.Stalls;
import com.angadi.tripmanagementa.models.StallsResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StallsActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewStalls)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stalls);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Stalls");
        getStalls();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getStalls() {
        MyProgressDialog.show(StallsActivity.this,"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StallsResponse> call = apiInterface.getStalls("show");
        call.enqueue(new Callback<StallsResponse>() {
            @Override
            public void onResponse(Call<StallsResponse> call, Response<StallsResponse> response) {
                Log.e("getInfluencers", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<Stalls> stallsList = response.body().getStall();
                    setAdapter(stallsList);
                } else {
                    Toast.makeText(StallsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StallsResponse> call, Throwable t) {
                Log.e("getInfluencers", "" + t);
                MyProgressDialog.dismiss();
            }
        });

    }


    private void setAdapter(List<Stalls> stallsList){
        recyclerView.setLayoutManager(new LinearLayoutManager(StallsActivity.this));
        StallsAdapter adapter = new StallsAdapter(StallsActivity.this,stallsList);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new StallsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String url) {
                openUri(url);
            }
        });
    }

    private void openUri(String uri) {
        if (!uri.equals("")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }
}
