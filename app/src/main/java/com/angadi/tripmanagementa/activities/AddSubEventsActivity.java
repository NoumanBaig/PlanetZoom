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
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.DaysAdapter;
import com.angadi.tripmanagementa.models.DeleteSubEventResponse;
import com.angadi.tripmanagementa.models.MembersResponse;
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.models.ShowSubEventResponse;
import com.angadi.tripmanagementa.models.SubEventResponse;
import com.angadi.tripmanagementa.models.SubEventResult;
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

public class AddSubEventsActivity extends AppCompatActivity {

    @BindView(R.id.loading_layout)
    View loadingIndicator;
    @BindView(R.id.recyclerDays)
    RecyclerView recyclerView;
    @BindView(R.id.edt_title)
    EditText edt_title;
    @BindView(R.id.edt_desc)
    EditText edt_desc;
    @BindView(R.id.btn_add)
    Button btn_add;
    String str_id,click_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_events);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Dates / Days");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (getIntent().getExtras() != null) {
            str_id = getIntent().getStringExtra("id");
            Log.e("str_id",str_id);
            getSubEvents(str_id);
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
                validate();
                break;
            default:
                break;
        }
    }

    private void validate() {
        if (edt_title.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Title", Toast.LENGTH_SHORT).show();
        } else if (edt_desc.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Description", Toast.LENGTH_SHORT).show();
        } else {
            if (btn_add.getText().toString().equalsIgnoreCase("Update")){
                updateSubEvent(str_id,edt_title.getText().toString(), edt_desc.getText().toString(),click_id);
            }else {
                createSubEvent(str_id, edt_title.getText().toString(), edt_desc.getText().toString());
            }
        }
    }

    private void createSubEvent(String id, String title, String desc) {
        loadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String token = Prefs.with(AddSubEventsActivity.this).getString("token", "");
        Call<SubEventResponse> call = apiInterface.createSubEvent("true", token, id, title, desc);
        call.enqueue(new Callback<SubEventResponse>() {
            @Override
            public void onResponse(Call<SubEventResponse> call, Response<SubEventResponse> response) {
                Log.e("createSubEvent", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(AddSubEventsActivity.this, "Day Added Successfully", Toast.LENGTH_SHORT).show();
                    edt_title.setText("");
                    edt_desc.setText("");
                    edt_title.requestFocus();
                    getSubEvents(str_id);
                } else {
                    Toast.makeText(AddSubEventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubEventResponse> call, Throwable t) {
                Log.e("createSubEvent", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void getSubEvents(String id) {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(AddSubEventsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShowSubEventResponse> call = apiInterface.showSubEvent("show",token,id);
        call.enqueue(new Callback<ShowSubEventResponse>() {
            @Override
            public void onResponse(Call<ShowSubEventResponse> call, Response<ShowSubEventResponse> response) {
                Log.e("getSubEvents", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<SubEventResult> resultList = response.body().getResults();
                    setAdapter(resultList);
                } else {
                    Toast.makeText(AddSubEventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DaysAdapter daysAdapter = new DaysAdapter(this, resultList);
        recyclerView.setAdapter(daysAdapter);

        daysAdapter.setClickListener(new DaysAdapter.DeleteClickListener() {
            @Override
            public void onClick(View view, int position, String id) {
                Log.e("id",id);
                deleteSubEvent(id);
                getSubEvents(str_id);
            }
        });

        daysAdapter.setClickListener(new DaysAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String id,String title, String desc) {
                click_id=id;
                edt_title.setText(title);
                edt_desc.setText(desc);
                btn_add.setText("Update");

            }
        });
    }


    private void deleteSubEvent(String id) {
        loadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String token = Prefs.with(AddSubEventsActivity.this).getString("token", "");

        Call<DeleteSubEventResponse> call = apiInterface.deleteSubEvent("true", token, str_id, id);
        call.enqueue(new Callback<DeleteSubEventResponse>() {
            @Override
            public void onResponse(Call<DeleteSubEventResponse> call, Response<DeleteSubEventResponse> response) {
                Log.e("deleteSubEvent", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(AddSubEventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddSubEventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteSubEventResponse> call, Throwable t) {
                Log.e("deleteSubEvent", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });

    }


    private void updateSubEvent(String id, String title, String desc,String click_id) {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(AddSubEventsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SubEventResponse> call = apiInterface.updateSubEvent("true", token, id, title, desc,click_id,"1");
        call.enqueue(new Callback<SubEventResponse>() {
            @Override
            public void onResponse(Call<SubEventResponse> call, Response<SubEventResponse> response) {
                Log.e("createSubEvent", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(AddSubEventsActivity.this, "Day Updated Successfully", Toast.LENGTH_SHORT).show();
                    btn_add.setText("Add");
                    edt_title.setText("");
                    edt_desc.setText("");
                    getSubEvents(str_id);
                } else {
                    Toast.makeText(AddSubEventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubEventResponse> call, Throwable t) {
                Log.e("createSubEvent", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }



}
