package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.AllEventsResponse;
import com.angadi.tripmanagementa.models.AllEventsResult;
import com.angadi.tripmanagementa.models.EventDetailsResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity {

    @BindView(R.id.loading_layout)
    View loadingIndicator;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_desc)
    TextView txt_desc;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_price)
    TextView txt_price;
    @BindView(R.id.txt_venue)
    TextView txt_venue;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.img)
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null){
            String event_id = getIntent().getStringExtra("event_id");
            getEventDetails(event_id);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getEventDetails(String event_id){
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<EventDetailsResponse> call = apiInterface.getEventDetails("true",token,event_id);
        call.enqueue(new Callback<EventDetailsResponse>() {
            @Override
            public void onResponse(Call<EventDetailsResponse> call, Response<EventDetailsResponse> response) {
                Log.e("getEventDetails", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        txt_name.setText(response.body().getPeaName());
                        txt_desc.setText(response.body().getPeaDesc());
                        txt_address.setText(response.body().getPeaLocation());
                        txt_date.setText(response.body().getPeaDate());
                        txt_venue.setText(response.body().getPeaVenue());
                        txt_price.setText(response.body().getPeaPrice());
//                        if (!response.body().getPeaLogo().equalsIgnoreCase("") || response.body().getPeaLogo() != null){
                            Picasso.get().load(Constants.BASE_URL+response.body().getPeaLogo()).error(R.drawable.explore_event).placeholder(R.drawable.explore_event).into(imageView);

                    } else {
                       // Toast.makeText(ShowEventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EventDetailsResponse> call, Throwable t) {
                Log.e("getEventDetails", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });

    }
}
