package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.QrScanResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Qr Code Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent().getExtras() != null){
            String string = getIntent().getStringExtra("qr_id");
            Log.e("string",""+string);
            getScanResult(string);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getScanResult(String id){
        // loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(ScanResultActivity.this).getString("token","");
        Log.e("token",token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<QrScanResponse> responseCall = apiInterface.scanResult("true",id,token);
        responseCall.enqueue(new Callback<QrScanResponse>() {
            @Override
            public void onResponse(Call<QrScanResponse> call, Response<QrScanResponse> response) {
                Log.e("scan_res", new Gson().toJson(response));
                // loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")){

                }else {
                    // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QrScanResponse> call, Throwable t) {
                Log.e("scan_res", ""+t);
                // loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

}
