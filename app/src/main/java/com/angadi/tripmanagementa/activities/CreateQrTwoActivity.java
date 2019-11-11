package com.angadi.tripmanagementa.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.SubCategoryAdapter;
import com.angadi.tripmanagementa.models.CreateQrResponse;
import com.angadi.tripmanagementa.models.SubCategoriesResponse;
import com.angadi.tripmanagementa.models.SubResults;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.ImageUtil;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;


public class CreateQrTwoActivity extends AppCompatActivity {

    @BindView(R.id.recyclerSubCat)
    RecyclerView recyclerView;
    String str_cat_id,str_sub_id;
//    @BindView(R.id.loading_layout)
//    View loadingIndicator;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    double latitude = 12.9716, longitude = 77.5946;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_website)
    EditText edt_website;
    @BindView(R.id.edt_adds)
    EditText edt_adds;
    @BindView(R.id.edt_whatsapp)
    EditText edt_whatsapp;
    @BindView(R.id.edt_facebook)
    EditText edt_facebook;
    @BindView(R.id.edt_youtube)
    EditText edt_youtube;
    @BindView(R.id.btn_generate)
    Button btn_generate;
    public static final int REQUEST_IMAGE = 100;
    @BindView(R.id.img_logo)
    ImageView img_logo;
    String base64String;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr_two);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create QR Code");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getCurrentLocation();
        if (getIntent().getExtras() != null){
            str_cat_id = getIntent().getStringExtra("cat_id");
            getSubCategories(str_cat_id);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getSubCategories(String cat_id){
        MyProgressDialog.show(CreateQrTwoActivity.this,"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SubCategoriesResponse> responseCall = apiInterface.getSubCategories("true",cat_id);
        responseCall.enqueue(new Callback<SubCategoriesResponse>() {
            @Override
            public void onResponse(Call<SubCategoriesResponse> call, Response<SubCategoriesResponse> response) {
                Log.e("getSubCategories", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    List<SubResults> results = response.body().getResults();
                    Log.e("results",""+results);

                    setAdapter(results);
                }
            }

            @Override
            public void onFailure(Call<SubCategoriesResponse> call, Throwable t) {
                Log.e("getSubCat_fail", ""+t);
               MyProgressDialog.dismiss();
            }
        });

    }

    private void scrollDown(){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(400);
            }
        });
    }


    private void setAdapter(List<SubResults> results){
        recyclerView.setLayoutManager(new LinearLayoutManager(CreateQrTwoActivity.this));
        SubCategoryAdapter categoryAdapter = new SubCategoryAdapter(CreateQrTwoActivity.this,results);
        recyclerView.setAdapter(categoryAdapter);

       categoryAdapter.setClickListener(new SubCategoryAdapter.SubCategoryClickListener() {
           @Override
           public void onClick(View view, int position, String id, String sub_id, Boolean selected, String cat_name, String image) {
//               Toast.makeText(CreateQrTwoActivity.this, ""+position+" "+sub_id+" "+cat_name, Toast.LENGTH_SHORT).show();
               str_sub_id=sub_id;
           }
       });

    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(CreateQrTwoActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.e("latitude", "" + latitude);
                            Log.e("longitude", "" + longitude);
                        }
                    }
                });
    }


    @OnClick({R.id.btn_next,R.id.img_loc,R.id.btn_generate,R.id.img_logo})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_next:
               scrollDown();
               break;
            case R.id.btn_generate:
                validate(edt_name.getText().toString(),edt_email.getText().toString(),edt_mobile.getText().toString(),edt_whatsapp.getText().toString(),
                        edt_facebook.getText().toString(),edt_youtube.getText().toString(),edt_website.getText().toString(),edt_adds.getText().toString());
                break;
            case R.id.img_loc:
                startActivityForResult(new LocationPickerActivity.Builder()
                        .withLocation(latitude, longitude).withSatelliteViewHidden().shouldReturnOkOnBackPressed()
                        .withGooglePlacesEnabled()
                        .build(CreateQrTwoActivity.this), 1);
                break;
            case R.id.img_logo:
                Dexter.withActivity(this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

                break;

        }
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(CreateQrTwoActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(CreateQrTwoActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateQrTwoActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.e("RESULT****", "OK");
            if (requestCode == 1) {
                double latitude = data.getDoubleExtra(LATITUDE, 0.0);
                Log.e("LATITUDE****", "" + latitude);
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.e("LONGITUDE****", "" + longitude);
                String address = data.getStringExtra(LOCATION_ADDRESS);
                Log.e("ADDRESS****", "" + address);
                if (address != null) {
                    edt_adds.setText(address);
                }
            }
        }

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmapProfile = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    base64String = ImageUtil.convert(bitmapProfile);
                    Log.e("base64String","base64String: " + base64String);
//                    Bitmap convertBitmap = ImageUtil.convert(base64String);
//                    Log.e(TAG, "convertBitmap: " + convertBitmap);
                    // loading profile image from local cache
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadProfile(String url) {
        Log.e("", "Image cache path: " + url);
        Glide.with(CreateQrTwoActivity.this).load(url).into(img_logo);
    }


    private void validate(String name,String email, String mobile, String whatsApp, String faceBook, String youTube, String website, String address){
        if (name.equalsIgnoreCase("")){
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
        }else {
           createQRcode( name, email,  mobile,whatsApp,faceBook,youTube,  website,  address );
        }

    }


    private void createQRcode(String name,String email, String mobile, String whatsApp, String faceBook, String youTube, String website, String address){
        MyProgressDialog.show(CreateQrTwoActivity.this,"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CreateQrResponse> responseCall = apiInterface.create_qr(Prefs.with(CreateQrTwoActivity.this).getString("token","")
                ,str_cat_id,str_sub_id,name,email,mobile,whatsApp,faceBook,youTube,website,address,base64String);
        responseCall.enqueue(new Callback<CreateQrResponse>() {
            @Override
            public void onResponse(Call<CreateQrResponse> call, Response<CreateQrResponse> response) {
                Log.e("onResponse",new Gson().toJson(response));
                Log.e("getSubCategories", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    Toast.makeText(CreateQrTwoActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateQrTwoActivity.this,ScanResultActivity.class);
                    intent.putExtra("qr_id",response.body().getQrCodeIdSecure());
                    intent.putExtra("qr_url",response.body().getQrCodeIdSecureLink());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Log.e("getQrCodeId",""+response.body().getQrCodeId());
                    Log.e("getQrCodeIdSecure",""+response.body().getQrCodeIdSecureLink());
                }else {
                    Toast.makeText(CreateQrTwoActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateQrResponse> call, Throwable t) {
                Log.e("onFailure",""+t);
               MyProgressDialog.dismiss();
            }
        });
    }
}
