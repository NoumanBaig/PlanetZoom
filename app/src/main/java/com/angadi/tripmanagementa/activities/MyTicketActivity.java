package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.MyTicketsAdapter;
import com.angadi.tripmanagementa.models.BuyTicketResponse;
import com.angadi.tripmanagementa.models.MyTicketsResponse;
import com.angadi.tripmanagementa.models.MyTicketsResult;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTicketActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewTickets)
    RecyclerView recyclerView;
//    @BindView(R.id.loading_layout)
//    View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Tickets");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getTickets();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getTickets(){
        MyProgressDialog.show(MyTicketActivity.this,"Loading...");
        String token = Prefs.with(MyTicketActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MyTicketsResponse> call = apiInterface.myTickets("true",token);
        call.enqueue(new Callback<MyTicketsResponse>() {
            @Override
            public void onResponse(Call<MyTicketsResponse> call, Response<MyTicketsResponse> response) {
                Log.e("getTickets",new Gson().toJson(response));
               MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")){

                    List<MyTicketsResult> resultList = response.body().getResults();
                    setAdapter(resultList);
                }else {
                    Toast.makeText(MyTicketActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyTicketsResponse> call, Throwable t) {
                Log.e("getTickets",""+t);
               MyProgressDialog.dismiss();
            }
        });

    }

    private void setAdapter(List<MyTicketsResult> resultList){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyTicketsAdapter adapter = new MyTicketsAdapter(this,resultList);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(new MyTicketsAdapter.TicketClickListener() {
            @Override
            public void onClick(View view, int position, String id) {
                startActivity(new Intent(MyTicketActivity.this,MyTicketDetailsActivity.class)
                .putExtra("event_id",id));
            }
        });
    }

}
