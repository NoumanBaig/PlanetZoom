package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.EventDashboardAdapter;
import com.angadi.tripmanagementa.fragments.DashboardDialogFragment;
import com.angadi.tripmanagementa.fragments.SettingsDialogFragment;
import com.angadi.tripmanagementa.models.EventVisitorsResponse;
import com.angadi.tripmanagementa.models.PlacesResult;
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

public class DashboardActivity extends AppCompatActivity implements DashboardDialogFragment.MessageDialogListener {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<String> arrayList, arrayListNames, spinnerList;
    String str_day = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setSpinnerAdapter();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setSpinnerAdapter() {
        spinnerList = new ArrayList<>();
        spinnerList.add("23 Nov 2019");
        spinnerList.add("24 Nov 2019");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DashboardActivity.this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String string = adapterView.getSelectedItem().toString();
                Log.e("onItemSelected", string);
                if (string.equalsIgnoreCase("23 Nov 2019")) {
                    str_day = "23";
                    getVisitors(str_day);
                } else {
                    str_day = "24";
                    getVisitors(str_day);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void getVisitors(String date) {
        MyProgressDialog.show(DashboardActivity.this, "Loading...");
        String token = Prefs.with(DashboardActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EventVisitorsResponse> call = apiInterface.getEventVisitors("true", date);
        call.enqueue(new Callback<EventVisitorsResponse>() {
            @Override
            public void onResponse(Call<EventVisitorsResponse> call, Response<EventVisitorsResponse> response) {
                Log.e("getVisitors", new Gson().toJson(response));
                arrayListNames = new ArrayList<>();
                arrayList = new ArrayList<>();
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    arrayListNames.add("Pioneer");
                    arrayListNames.add("Attendee");
                    arrayListNames.add("Influencer");
                    arrayListNames.add("Partner");
                    arrayListNames.add("VIP");
                    arrayListNames.add("CoreTeam");
                    arrayListNames.add("OrganizingTeam");
                    arrayListNames.add("Total");
                    arrayList.add(response.body().getPioneer());
                    arrayList.add(response.body().getAttendee());
                    arrayList.add(response.body().getInfluencer());
                    arrayList.add(response.body().getPartner());
                    arrayList.add(response.body().getVIP());
                    arrayList.add(response.body().getCoreTeam());
                    arrayList.add(response.body().getOrganizingTeam());
                    arrayList.add(response.body().getTotalTeams());

                    setAdapter();

                } else {

                    Log.e("failure", "--->");
                }
            }

            @Override
            public void onFailure(Call<EventVisitorsResponse> call, Throwable t) {
                Log.e("getVisitors", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void setAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(DashboardActivity.this, 2));
        EventDashboardAdapter adapter = new EventDashboardAdapter(DashboardActivity.this, arrayList, arrayListNames);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new EventDashboardAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String name) {
                if (!name.equalsIgnoreCase("Total")) {
                    if (name.equalsIgnoreCase("OrganizingTeam")) {
                        setOnClick("Organizing Team");
                    } else {
                        setOnClick(name);
                    }

                }

            }
        });
    }

    private void setOnClick(String name) {
        DialogFragment dialogFragment = DashboardDialogFragment.newInstance(name, str_day, this);
        dialogFragment.show(DashboardActivity.this.getSupportFragmentManager(), "scan_results");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }
}
