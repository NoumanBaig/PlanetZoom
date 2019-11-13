package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.AdminEventsAdapter;
import com.angadi.tripmanagementa.adapters.EventsAdapter;
import com.angadi.tripmanagementa.models.AdminEventResult;
import com.angadi.tripmanagementa.models.AdminEventsResponse;
import com.angadi.tripmanagementa.models.AllEventsResponse;
import com.angadi.tripmanagementa.models.AllEventsResult;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowEventsActivity extends AppCompatActivity {

    @BindView(R.id.recyclerEvents)
    RecyclerView recyclerEvents;
    @BindView(R.id.recyclerAdminEvents)
    RecyclerView recyclerAdminEvents;
    @BindView(R.id.layout_allEvents)
    LinearLayout layout_allEvents;
    @BindView(R.id.layout_adminEvents)
    LinearLayout layout_adminEvents;
    @BindView(R.id.layout_create)
    FrameLayout layout_create;
    @BindView(R.id.layout_all)
    FrameLayout layout_all;
    @BindView(R.id.layout_not_found)
    LinearLayout layout_not_found;
//    @BindView(R.id.loading_layout)
//    View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            String string = getIntent().getStringExtra("events");
            Log.e("string", string);
            if (string.equalsIgnoreCase("organiser")) {
                layout_allEvents.setVisibility(View.GONE);
                layout_all.setVisibility(View.GONE);
                layout_adminEvents.setVisibility(View.VISIBLE);
                layout_create.setVisibility(View.VISIBLE);
                try {
                    // String event_id = Prefs.with(ShowEventsActivity.this).getString("event_id", "");
                    String user_id = Prefs.with(ShowEventsActivity.this).getString("user_id", "");
//                    Log.e("event_id", event_id);
                    Log.e("user_id", user_id);
//                    if (!event_id.equalsIgnoreCase("")) {
//                        getAdminEvents(event_id);
//                    } else
                    if (!user_id.equalsIgnoreCase("")) {
                        getAdminEvents(user_id);
                    } else {
                        Toast.makeText(this, "No Events right now.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (string.equalsIgnoreCase("created")) {
                layout_allEvents.setVisibility(View.GONE);
                layout_all.setVisibility(View.GONE);
                layout_adminEvents.setVisibility(View.VISIBLE);
                layout_create.setVisibility(View.VISIBLE);
                try {
                    String user_id = Prefs.with(ShowEventsActivity.this).getString("user_id", "");
                    Log.e("user_id", user_id);
                    if (!user_id.equalsIgnoreCase("")) {
                        getAdminEvents(user_id);
                    } else {
                        Toast.makeText(this, "No Events right now.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                layout_allEvents.setVisibility(View.VISIBLE);
                layout_all.setVisibility(View.VISIBLE);
                layout_adminEvents.setVisibility(View.GONE);
                layout_create.setVisibility(View.GONE);
                getAllEvents();
            }
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getAllEvents() {
        MyProgressDialog.show(ShowEventsActivity.this,"Loading...");
        String token = Prefs.with(ShowEventsActivity.this).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AllEventsResponse> call = apiInterface.getAllEvents("true", token);
        call.enqueue(new Callback<AllEventsResponse>() {
            @Override
            public void onResponse(Call<AllEventsResponse> call, Response<AllEventsResponse> response) {
                Log.e("getAllEvents", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<AllEventsResult> resultList = response.body().getResults();
                        if (resultList.size()>0){
                            setAdapter(resultList);
                            layout_not_found.setVisibility(View.GONE);
                        }else {
                            layout_not_found.setVisibility(View.VISIBLE);
                        }

                    } else {
                        layout_not_found.setVisibility(View.VISIBLE);
                        Toast.makeText(ShowEventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AllEventsResponse> call, Throwable t) {
                Log.e("getAllEvents", "" + t);
                MyProgressDialog.dismiss();
                layout_not_found.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getAdminEvents(String id) {
        MyProgressDialog.show(ShowEventsActivity.this,"Loading...");
        String token = Prefs.with(ShowEventsActivity.this).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AdminEventsResponse> call = apiInterface.getAdminEvents("true", token, id);
        call.enqueue(new Callback<AdminEventsResponse>() {
            @Override
            public void onResponse(Call<AdminEventsResponse> call, Response<AdminEventsResponse> response) {
                Log.e("getAdminEvents", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<AdminEventResult> resultList = response.body().getResults();
                        if (resultList.size()>0){
                            setAdminAdapter(resultList);
                            layout_not_found.setVisibility(View.GONE);
                        }else {
                            layout_not_found.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layout_not_found.setVisibility(View.VISIBLE);
                        Toast.makeText(ShowEventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AdminEventsResponse> call, Throwable t) {
                Log.e("getAdminEvents", "" + t);
                MyProgressDialog.dismiss();
                layout_not_found.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setAdapter(List<AllEventsResult> resultList) {
        recyclerEvents.setLayoutManager(new LinearLayoutManager(ShowEventsActivity.this));
        EventsAdapter eventsAdapter = new EventsAdapter(ShowEventsActivity.this, resultList);
        recyclerEvents.setAdapter(eventsAdapter);
        eventsAdapter.setClickListener(new EventsAdapter.EventClickListener() {
            @Override
            public void onClick(View view, int position, String id) {
                startActivity(new Intent(ShowEventsActivity.this, EventDetailsActivity.class).putExtra("event_id", id).putExtra("encoded","no"));
            }
        });
    }

    private void setAdminAdapter(List<AdminEventResult> resultList) {
        recyclerAdminEvents.setLayoutManager(new LinearLayoutManager(ShowEventsActivity.this));
        AdminEventsAdapter eventsAdapter = new AdminEventsAdapter(ShowEventsActivity.this, resultList);
        recyclerAdminEvents.setAdapter(eventsAdapter);
        eventsAdapter.setClickListener(new AdminEventsAdapter.AdminEventClickListener() {
            @Override
            public void onClick(View view, int position, String id) {
//                EventDetailsFragment fragment = new EventDetailsFragment();
//                loadFragment(fragment);
                startActivity(new Intent(ShowEventsActivity.this, CreateEventActivity.class)
                        .putExtra("id", id));
                finish();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @OnClick({R.id.txt_createEvent, R.id.txt_myTickets})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_createEvent:
                startActivity(new Intent(this, CreateEventActivity.class));
                finish();
                break;
            case R.id.txt_myTickets:
                startActivity(new Intent(this, MyTicketActivity.class));
//                finish();
                break;

        }
    }

}
