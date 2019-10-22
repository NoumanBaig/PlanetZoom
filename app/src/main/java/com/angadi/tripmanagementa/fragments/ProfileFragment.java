package com.angadi.tripmanagementa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.LoginActivity;
import com.angadi.tripmanagementa.adapters.CountSectionAdapter;
import com.angadi.tripmanagementa.models.DashboardResponse;
import com.angadi.tripmanagementa.models.DashboardResult;
import com.angadi.tripmanagementa.models.EditProfileResponse;
import com.angadi.tripmanagementa.models.LogoutResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @BindView(R.id.img_edit)
    ImageView img_edit;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_about)
    EditText edt_about;
    @BindView(R.id.edt_address)
    EditText edt_address;
    @BindView(R.id.edt_company)
    EditText edt_company;
    @BindView(R.id.edt_designation)
    EditText edt_designation;
    @BindView(R.id.edt_website)
    EditText edt_website;
    @BindView(R.id.edt_busi_phone)
    EditText edt_busi_phone;
    @BindView(R.id.edt_insta)
    EditText edt_insta;
    @BindView(R.id.edt_facebook)
    EditText edt_facebook;
    @BindView(R.id.edt_linkedin)
    EditText edt_linkedin;
    @BindView(R.id.edt_youtube)
    EditText edt_youtube;
    @BindView(R.id.edt_whatsapp)
    EditText edt_whatsapp;
    @BindView(R.id.loading_layout)
    View loadingIndicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);


        getProfile();
        return view;
    }

    private void getProfile() {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(getActivity()).getString("token", "");
        String uid = Prefs.with(getActivity()).getString("UID", "1234");
        Log.e("token", token);
        Log.e("uid", uid);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileResponse> responseCall = apiInterface.getProfile("true", token);
        responseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                Log.e("profile_res", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")) {

                    String firstName = response.body().getUraFname();
                    displayTexts(firstName);
                } else {
                    Toast.makeText(getActivity(), response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("profile_res", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void displayTexts(String firstName) {
        edt_name.setText(firstName);
    }

    @OnClick({R.id.btn_logout,R.id.btn_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                logout();
                break;
            case R.id.btn_update:
                editProfile(edt_name.getText().toString());
                break;
        }
    }

    private void editProfile(String fname) {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EditProfileResponse> responseCall = apiInterface.editProfile("true",fname,token);
        responseCall.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                Log.e("logout", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getProfile();
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                Log.e("logout", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });

    }


    private void logout() {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
       Call<LogoutResponse> responseCall = apiInterface.logout(token);
       responseCall.enqueue(new Callback<LogoutResponse>() {
           @Override
           public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
               Log.e("logout", new Gson().toJson(response));
               loadingIndicator.setVisibility(View.GONE);
               if (response.body().getStatus().equalsIgnoreCase("success")) {
                   Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                   Prefs.with(getActivity()).remove();
                   Intent i = new Intent(getActivity(), LoginActivity.class);
                   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(i);
               } else {
                   Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<LogoutResponse> call, Throwable t) {
               Log.e("logout", "" + t);
               loadingIndicator.setVisibility(View.GONE);
           }
       });
    }

}
