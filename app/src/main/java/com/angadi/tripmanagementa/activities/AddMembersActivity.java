package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.AllMemebersAdapter;
import com.angadi.tripmanagementa.adapters.VolunteersAdapter;
import com.angadi.tripmanagementa.models.AddMembersResponse;
import com.angadi.tripmanagementa.models.AddVolunteerResponse;
import com.angadi.tripmanagementa.models.MembersResponse;
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.models.SearchVolunteerResponse;
import com.angadi.tripmanagementa.models.ShowAllMembersResponse;
import com.angadi.tripmanagementa.models.ShowAllMembersResult;
import com.angadi.tripmanagementa.models.ShowMembersResponse;
import com.angadi.tripmanagementa.models.ShowMembersResult;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMembersActivity extends AppCompatActivity {

    @BindView(R.id.loading_layout)
    View loadingIndicator;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_about)
    EditText edt_about;
    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.layout_normal)
    LinearLayout layout_normal;
    @BindView(R.id.recyclerMembers)
    RecyclerView recyclerMembers;
//    @BindView(R.id.layout_volunteer)
//    LinearLayout layout_volunteer;
//    @BindView(R.id.edt_emailMobile)
//    EditText edt_emailMobile;
//    @BindView(R.id.txt)
//    TextView textView;
    String mem_id, event_id,volunteer_id,volunteer_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent().getExtras() != null) {
            mem_id = getIntent().getStringExtra("mem_id");
            event_id = getIntent().getStringExtra("str_id");
            String title = getIntent().getStringExtra("mem_title");
            getSupportActionBar().setTitle("Add " + title);
            Log.e("mem_id",mem_id);
            Log.e("event_id",event_id);
            getMembers(event_id,mem_id);
        } else {
            getSupportActionBar().setTitle("Add Members");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick({R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                validateMembers();
                break;
            default:
                break;
        }

    }

    private void validateMembers() {
        if (edt_name.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
        } else if (edt_email.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Email ID", Toast.LENGTH_SHORT).show();
        } else if (edt_mobile.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Mobile", Toast.LENGTH_SHORT).show();
        } else if (edt_about.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter About", Toast.LENGTH_SHORT).show();
        } else {
            addMembers(edt_name.getText().toString(), edt_email.getText().toString()
                    , edt_mobile.getText().toString(), edt_about.getText().toString(),event_id, mem_id);
        }
    }



    private void addMembers(String name, String email, String phone, String about,String event_id, String str_id) {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(AddMembersActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddMembersResponse> call = apiInterface.addMembers("true", token, name, email, phone, about,event_id, str_id);
        call.enqueue(new Callback<AddMembersResponse>() {
            @Override
            public void onResponse(Call<AddMembersResponse> call, Response<AddMembersResponse> response) {
                Log.e("addMembers", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(AddMembersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    edt_name.setText("");
                    edt_email.setText("");
                    edt_mobile.setText("");
                    edt_about.setText("");
                    getMembers(event_id,str_id);
                } else {
                    Toast.makeText(AddMembersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddMembersResponse> call, Throwable t) {
                Log.e("getMembers", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });

    }

    private void getMembers(String event_id,String mem_id) {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(AddMembersActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShowAllMembersResponse> call = apiInterface.showAllMembers("true",token,event_id,mem_id);
        call.enqueue(new Callback<ShowAllMembersResponse>() {
            @Override
            public void onResponse(Call<ShowAllMembersResponse> call, Response<ShowAllMembersResponse> response) {
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    if (response.body().getCheckResponse().equalsIgnoreCase("NOT_VOLUNTEERS")){
                        List<ShowAllMembersResult> resultList = response.body().getResults();
                        setMembersAdapter(resultList);
                    }

                } else {
                    Toast.makeText(AddMembersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowAllMembersResponse> call, Throwable t) {
                Log.e("getVolunteers", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });

    }

    private void setMembersAdapter(List<ShowAllMembersResult> resultList) {
        recyclerMembers.setLayoutManager(new LinearLayoutManager(this));
        AllMemebersAdapter allMemebersAdapter = new AllMemebersAdapter(this, resultList);
        recyclerMembers.setAdapter(allMemebersAdapter);
    }


}
