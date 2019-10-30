package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.DaysAdapter;
import com.angadi.tripmanagementa.adapters.MembersAdapter;
import com.angadi.tripmanagementa.adapters.SubEventsAdapter;
import com.angadi.tripmanagementa.models.AllEventsResponse;
import com.angadi.tripmanagementa.models.AllEventsResult;
import com.angadi.tripmanagementa.models.BuyTicketResponse;
import com.angadi.tripmanagementa.models.EventDetailsResponse;
import com.angadi.tripmanagementa.models.MembersResponse;
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.models.ShowSubEventResponse;
import com.angadi.tripmanagementa.models.SubEventResult;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.recyclerDates)
    RecyclerView recyclerDates;
    @BindView(R.id.recyclerSponsors)
    RecyclerView recyclerSponsors;
    String event_id;

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

        if (getIntent().getExtras() != null) {
             event_id = getIntent().getStringExtra("event_id");
            getEventDetails(event_id);
            Log.e("event_id",""+event_id);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btn_buy)
    public void onClick(View view){
        checkTicket();
    }

    private void getEventDetails(String event_id) {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<EventDetailsResponse> call = apiInterface.getEventDetails("true", token, event_id);
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

                        if (response.body().getPeaLogo().equalsIgnoreCase("NULL")) {
                            Picasso.get().load(R.drawable.organise_event)
                                    .into(imageView);
                        } else {
                            Picasso.get().load(Constants.BASE_URL + response.body().getPeaLogo()).into(imageView);
                        }
                        getSubEvents(event_id);

                    } else {
                        Toast.makeText(EventDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getSubEvents(String id) {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShowSubEventResponse> call = apiInterface.showSubEvent("show", token, id);
        call.enqueue(new Callback<ShowSubEventResponse>() {
            @Override
            public void onResponse(Call<ShowSubEventResponse> call, Response<ShowSubEventResponse> response) {
                Log.e("getSubEvents", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<SubEventResult> resultList = response.body().getResults();
                    setAdapter(resultList);
                } else {
                    Toast.makeText(EventDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowSubEventResponse> call, Throwable t) {
                Log.e("getSubEventsExp", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });

    }

    private void setAdapter(List<SubEventResult> resultList) {
        recyclerDates.setLayoutManager(new LinearLayoutManager(this));
        SubEventsAdapter subEventsAdapter = new SubEventsAdapter(this, resultList);
        recyclerDates.setAdapter(subEventsAdapter);
        getMembers();
        subEventsAdapter.setClickListener(new SubEventsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String id, String title, String desc) {

            }
        });
    }

    private void getMembers(){
        loadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MembersResponse> call = apiInterface.getMembers("true");
        call.enqueue(new Callback<MembersResponse>() {
            @Override
            public void onResponse(Call<MembersResponse> call, Response<MembersResponse> response) {
                Log.e("getMembers",new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    List<MembersResult> membersResultList = response.body().getResults();
                    setMembersAdapter(membersResultList);
                }else {
                    Toast.makeText(EventDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MembersResponse> call, Throwable t) {
                Log.e("getMembers",""+t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void setMembersAdapter(List<MembersResult> membersResultList){
        recyclerSponsors.setLayoutManager(new LinearLayoutManager(this));
        MembersAdapter adapter = new MembersAdapter(this,membersResultList);
        recyclerSponsors.setAdapter(adapter);

        adapter.setClickListener(new MembersAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String id,String title) {
                Log.e("title---->",""+title);
            }
        });
    }

    private void checkTicket(){
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BuyTicketResponse> call = apiInterface.checkTicket("true",token,event_id);
        call.enqueue(new Callback<BuyTicketResponse>() {
            @Override
            public void onResponse(Call<BuyTicketResponse> call, Response<BuyTicketResponse> response) {
                Log.e("checkTicket",new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    buyTicket();
                }else {
                    Toast.makeText(EventDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BuyTicketResponse> call, Throwable t) {
                Log.e("checkTicket",""+t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void buyTicket(){
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BuyTicketResponse> call = apiInterface.buyTicket("true",token,event_id);
        call.enqueue(new Callback<BuyTicketResponse>() {
            @Override
            public void onResponse(Call<BuyTicketResponse> call, Response<BuyTicketResponse> response) {
                Log.e("buyTicket",new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    Toast.makeText(EventDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(EventDetailsActivity.this,MyTicketActivity.class));
                    finish();
                }else {
                    Toast.makeText(EventDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BuyTicketResponse> call, Throwable t) {
                Log.e("buyTicket",""+t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

}
