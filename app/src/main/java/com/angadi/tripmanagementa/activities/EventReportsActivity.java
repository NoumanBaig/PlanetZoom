package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.PlacesResult;
import com.angadi.tripmanagementa.models.ShowMembersResponse;
import com.angadi.tripmanagementa.models.ShowMembersResult;
import com.angadi.tripmanagementa.models.ShowPlacesResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventReportsActivity extends AppCompatActivity {

    @BindView(R.id.spinner)
    Spinner spinner;
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_reports);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
           String event_id = getIntent().getStringExtra("str_id");
            getPlaces(event_id);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getPlaces(String event_id) {
        MyProgressDialog.show(EventReportsActivity.this,"Loading...");
        String token = Prefs.with(EventReportsActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShowPlacesResponse> call = apiInterface.showPlaces("true", event_id);
        call.enqueue(new Callback<ShowPlacesResponse>() {
            @Override
            public void onResponse(Call<ShowPlacesResponse> call, Response<ShowPlacesResponse> response) {
                Log.e("getPlaces", new Gson().toJson(response));
                arrayList = new ArrayList<>();
                arrayList.add("All");
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<PlacesResult> resultList = response.body().getResults();
                    for (int i=0; i<resultList.size(); i++){
                        arrayList.add(resultList.get(i).getEtlaaPlaces());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventReportsActivity.this,android.R.layout.simple_spinner_item,arrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                } else {
                    Toast.makeText(EventReportsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowPlacesResponse> call, Throwable t) {
                Log.e("getPlaces", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

}
