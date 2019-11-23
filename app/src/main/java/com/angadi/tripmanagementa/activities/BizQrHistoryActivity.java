package com.angadi.tripmanagementa.activities;

import android.os.Bundle;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.angadi.tripmanagementa.activities.ui.main.SectionsPagerAdapter;

public class BizQrHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biz_qr_history);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent().getExtras()!=null){
            String string = getIntent().getStringExtra("scan_history");
            assert string != null;
            if (string.equalsIgnoreCase("dashboard")){
                getSupportActionBar().setTitle("BIZ QR History");
                Prefs.with(BizQrHistoryActivity.this).save("scan_history",string);
            }else {
                getSupportActionBar().setTitle("Profile QR History");
                Prefs.with(BizQrHistoryActivity.this).save("scan_history",string);
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}