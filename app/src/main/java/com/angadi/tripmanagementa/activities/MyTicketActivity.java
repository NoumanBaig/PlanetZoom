package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.MyTicketsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTicketActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewTickets)
    RecyclerView recyclerView;

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

        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            MyTicketsAdapter adapter = new MyTicketsAdapter(this);
            recyclerView.setAdapter(adapter);
            adapter.setClickListener(new MyTicketsAdapter.TicketClickListener() {
                @Override
                public void onClick(View view, int position, String id) {
                    startActivity(new Intent(MyTicketActivity.this,MyTicketDetailsActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
