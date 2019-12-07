package com.angadi.tripmanagementa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.AdminResponse;
import com.angadi.tripmanagementa.models.CheckAdminResponse;
import com.angadi.tripmanagementa.models.LogoutResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    String title, qr_code_id, qr_code_type, token, qr_url;
    @BindView(R.id.event_switch)
    Switch event_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ButterKnife.bind(this);
        token = Prefs.with(SettingsActivity.this).getString("token", "");
        Log.e("token", token);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setEvent_switch();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setEvent_switch() {
        if (Prefs.with(SettingsActivity.this).getString("organiser", "").equalsIgnoreCase("true")) {
            event_switch.setChecked(true);
        } else {
            event_switch.setChecked(false);
        }


        event_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.e("checked", "switch-->");
                    setAdmin("enable");
                } else {
                    Log.e("unchecked", "switch-->");
                    setAdmin("disable");
                }
            }
        });
    }

    @OnClick(R.id.btn_logout)
    public void onLogout(View view) {
        logout();
    }

    private void setAdmin(String enable) {
        MyProgressDialog.show(SettingsActivity.this, "Loading...");
        String token = Prefs.with(SettingsActivity.this).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AdminResponse> responseCall = apiInterface.setAdmin("true", token, enable);
        responseCall.enqueue(new Callback<AdminResponse>() {
            @Override
            public void onResponse(Call<AdminResponse> call, Response<AdminResponse> response) {
                Log.e("setAdmin_res", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        checkAdmin();
                    } else {
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AdminResponse> call, Throwable t) {
                Log.e("setAdmin_res", "" + t);
                MyProgressDialog.dismiss();
            }
        });

    }

    private void checkAdmin() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CheckAdminResponse> call = apiInterface.checkAdmin("true", token);
        call.enqueue(new Callback<CheckAdminResponse>() {
            @Override
            public void onResponse(Call<CheckAdminResponse> call, Response<CheckAdminResponse> response) {
                Log.e("checkAdmin", new Gson().toJson(response));
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    if (response.body().getStatusAdmin().equalsIgnoreCase("ADMIN")) {
                        Prefs.with(SettingsActivity.this).save("organiser", "true");
                    } else {
                        Prefs.with(SettingsActivity.this).save("organiser", "false");
                    }
                } else {
                    Log.e("checkAdmin", "" + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<CheckAdminResponse> call, Throwable t) {
                Log.e("checkAdmin", "" + t);

            }
        });
    }


    private void logout() {
        MyProgressDialog.show(SettingsActivity.this, "Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LogoutResponse> responseCall = apiInterface.logout(token);
        responseCall.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                Log.e("logout", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Prefs.with(SettingsActivity.this).remove();
                    Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Log.e("logout", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }
}