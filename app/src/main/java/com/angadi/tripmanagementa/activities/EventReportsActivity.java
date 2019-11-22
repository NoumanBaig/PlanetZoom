package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.ReportsAdapter;
import com.angadi.tripmanagementa.models.EventReportsResponse;
import com.angadi.tripmanagementa.models.PlacesResult;
import com.angadi.tripmanagementa.models.ReportsResult;
import com.angadi.tripmanagementa.models.ReportsTrackData;
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
    ArrayList<String> arrayList,arrayList_id;
    String event_id;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.txt_count)
    TextView txt_count;
    ReportsAdapter adapter;

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
           event_id = getIntent().getStringExtra("str_id");
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
                arrayList_id = new ArrayList<>();
                arrayList.add("All");
                arrayList_id.add("0");
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<PlacesResult> resultList = response.body().getResults();
                    for (int i=0; i<resultList.size(); i++){
                        arrayList.add(resultList.get(i).getEtlaaPlaces());
                        arrayList_id.add(resultList.get(i).getEtlaaId());
                    }
                  setSpinnerAdapter();
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

    private void setSpinnerAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventReportsActivity.this,android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("arrayList_id",arrayList_id.get(i));
                Log.e("event_id",event_id);
                getTrackList(arrayList_id.get(i),event_id,"0");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void getTrackList(String list_id,String event_id,String count) {
        MyProgressDialog.show(EventReportsActivity.this,"Loading...");
        String token = Prefs.with(EventReportsActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EventReportsResponse> call = apiInterface.getEventReport("true",token,list_id, event_id,count);
        call.enqueue(new Callback<EventReportsResponse>() {
            @Override
            public void onResponse(Call<EventReportsResponse> call, Response<EventReportsResponse> response) {
                Log.e("getTrackList", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    txt_count.setText("Data Count = "+response.body().getCounts());
                    List<ReportsResult> reportsResults = response.body().getResults();
                    List<ReportsTrackData> reportsTrackData = reportsResults.get(0).getTrackData();
                    setTrackListAdapter(reportsTrackData);
                } else {
                    txt_count.setText("Data Count = 0");
                    recyclerView.setVisibility(View.GONE);
//                    Toast.makeText(EventReportsActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventReportsResponse> call, Throwable t) {
                Log.e("getTrackList", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void setTrackListAdapter(List<ReportsTrackData> reportsTrackData){
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventReportsActivity.this));
        adapter = new ReportsAdapter(EventReportsActivity.this,reportsTrackData);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
