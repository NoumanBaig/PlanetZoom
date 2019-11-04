package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.CategoryAdapter;
import com.angadi.tripmanagementa.models.CategoriesResponse;
import com.angadi.tripmanagementa.models.Result;
import com.angadi.tripmanagementa.models.VerifyOtp;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.AbstractBaseActivity;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateQrActivity extends AppCompatActivity {

    @BindView(R.id.recyclerCat)
    RecyclerView recyclerView;
//    @BindView(R.id.loading_layout)
//    View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getAllCategories();
    }

//    @Override
//    public void initView() {
//
//    }

//    @Override
//    public void bindViewActions() {
//
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getAllCategories(){
       // showLoadingLayout(loadingIndicator);
        MyProgressDialog.show(CreateQrActivity.this,"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("show_all_categories", "true")
//                .build();
        Call<CategoriesResponse> responseCall = apiInterface.getAllCategories("true");
        responseCall.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                Log.e("getAllCategories", new Gson().toJson(response));
              //  hideLoadingLayout(loadingIndicator);
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    List<Result> results = response.body().getResults();
                    Log.e("results",""+results);

                    setAdapter(results);
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Log.e("getAllCat_fail", ""+t);
              // hideLoadingLayout(loadingIndicator);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void setAdapter(List<Result> results){
        recyclerView.setLayoutManager(new LinearLayoutManager(CreateQrActivity.this));
        CategoryAdapter categoryAdapter = new CategoryAdapter(CreateQrActivity.this,results);
        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setClickListener(new CategoryAdapter.CategoryClickListener() {
            @Override
            public void onClick(View view, int position, String id, String cat_name, String image) {
                startActivity(new Intent(CreateQrActivity.this,CreateQrTwoActivity.class)
                .putExtra("cat_id",id)
                .putExtra("cat_name",id));
            }
        });
    }
}
