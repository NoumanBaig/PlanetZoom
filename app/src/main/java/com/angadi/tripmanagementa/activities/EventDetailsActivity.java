package com.angadi.tripmanagementa.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.DaysAdapter;
import com.angadi.tripmanagementa.adapters.MembersAdapter;
import com.angadi.tripmanagementa.adapters.SubEventsAdapter;
import com.angadi.tripmanagementa.models.AllEventsResponse;
import com.angadi.tripmanagementa.models.AllEventsResult;
import com.angadi.tripmanagementa.models.BuyTicketResponse;
import com.angadi.tripmanagementa.models.EventDetailsResponse;
import com.angadi.tripmanagementa.models.MembersResponse;
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.models.ShowSubEventResponse;
import com.angadi.tripmanagementa.models.SubEventResult;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.ImageUtil;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity {

//    @BindView(R.id.loading_layout)
//    View loadingIndicator;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_desc)
    TextView txt_desc;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_price)
    TextView txt_price;
    @BindView(R.id.txt_venue)
    TextView txt_venue;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.img)
    ImageView imageView;
    @BindView(R.id.recyclerDates)
    RecyclerView recyclerDates;
    @BindView(R.id.recyclerSponsors)
    RecyclerView recyclerSponsors;
    String event_id;
    public static final int REQUEST_IMAGE = 100;
    String base64String;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
             event_id = getIntent().getStringExtra("event_id");
            getEventDetails(event_id);
            Log.e("event_id",""+event_id);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btn_buy)
    public void onClick(View view){
        checkTicket();
    }

    private void getEventDetails(String event_id) {
        MyProgressDialog.show(EventDetailsActivity.this,"Loading...");
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<EventDetailsResponse> call = apiInterface.getEventDetails("true", token, event_id);
        call.enqueue(new Callback<EventDetailsResponse>() {
            @Override
            public void onResponse(Call<EventDetailsResponse> call, Response<EventDetailsResponse> response) {
                Log.e("getEventDetails", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        txt_name.setText(response.body().getPeaName());
                        txt_desc.setText(response.body().getPeaDesc());
                        txt_address.setText(response.body().getPeaLocation());
                        txt_date.setText(response.body().getPeaDate());
                        txt_venue.setText(response.body().getPeaVenue());
                        txt_price.setText(response.body().getPeaPrice());

                        if (response.body().getPeaLogo().equalsIgnoreCase("NULL")) {
                            Picasso.get().load(R.drawable.organise_event)
                                    .into(imageView);
                        } else {
                            Picasso.get().load(Constants.BASE_URL + response.body().getPeaLogo()).into(imageView);
                        }
                        getSubEvents(event_id);

                    } else {
                        Toast.makeText(EventDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EventDetailsResponse> call, Throwable t) {
                Log.e("getEventDetails", "" + t);
                MyProgressDialog.dismiss();
            }
        });

    }

    private void getSubEvents(String id) {
        MyProgressDialog.show(EventDetailsActivity.this,"Loading...");
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShowSubEventResponse> call = apiInterface.showSubEvent("show", token, id);
        call.enqueue(new Callback<ShowSubEventResponse>() {
            @Override
            public void onResponse(Call<ShowSubEventResponse> call, Response<ShowSubEventResponse> response) {
                Log.e("getSubEvents", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<SubEventResult> resultList = response.body().getResults();
                    setAdapter(resultList);
                } else {
                    Toast.makeText(EventDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowSubEventResponse> call, Throwable t) {
                Log.e("getSubEventsExp", "" + t);
                MyProgressDialog.dismiss();
            }
        });

    }

    private void setAdapter(List<SubEventResult> resultList) {
        recyclerDates.setLayoutManager(new LinearLayoutManager(this));
        SubEventsAdapter subEventsAdapter = new SubEventsAdapter(this, resultList);
        recyclerDates.setAdapter(subEventsAdapter);
        getMembers();
        subEventsAdapter.setClickListener(new SubEventsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String id, String title, String desc) {

            }
        });
    }

    private void getMembers(){
        MyProgressDialog.show(EventDetailsActivity.this,"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MembersResponse> call = apiInterface.getMembers("true");
        call.enqueue(new Callback<MembersResponse>() {
            @Override
            public void onResponse(Call<MembersResponse> call, Response<MembersResponse> response) {
                Log.e("getMembers",new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    List<MembersResult> membersResultList = response.body().getResults();
                    setMembersAdapter(membersResultList);
                }else {
                    Toast.makeText(EventDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MembersResponse> call, Throwable t) {
                Log.e("getMembers",""+t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void setMembersAdapter(List<MembersResult> membersResultList){
        recyclerSponsors.setLayoutManager(new LinearLayoutManager(this));
        MembersAdapter adapter = new MembersAdapter(this,membersResultList);
        recyclerSponsors.setAdapter(adapter);

        adapter.setClickListener(new MembersAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String id,String title) {
                Log.e("title---->",""+title);
            }
        });
    }

    private void checkTicket(){
        MyProgressDialog.show(EventDetailsActivity.this,"Loading...");
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BuyTicketResponse> call = apiInterface.checkTicket("true",token,event_id);
        call.enqueue(new Callback<BuyTicketResponse>() {
            @Override
            public void onResponse(Call<BuyTicketResponse> call, Response<BuyTicketResponse> response) {
                Log.e("checkTicket",new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    showSelfieDialog();
                }else {
                    Toast.makeText(EventDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BuyTicketResponse> call, Throwable t) {
                Log.e("checkTicket",""+t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void showSelfieDialog(){
        new AlertDialog.Builder(EventDetailsActivity.this)
                .setTitle("Upload Selfie")
                .setMessage("Please upload your selfie to recognised you at the event. Thank you!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dexter.withActivity(EventDetailsActivity.this)
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

                    }
                })
                .show();
    }

    private void showImagePickerOptions() {
//        ImagePickerActivity.showImagePickerOptions(EventDetailsActivity.this, new ImagePickerActivity.PickerOptionListener() {
//            @Override
//            public void onTakeCameraSelected() {
//                launchCameraIntent();
//            }
//
//            @Override
//            public void onChooseGallerySelected() {
//               Log.e("onGallery","---->");
//            }
//        });

        ImagePickerActivity.showCameraOnly(EventDetailsActivity.this, new ImagePickerActivity.PickerCameraListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(EventDetailsActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailsActivity.this);
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
        Uri uri = Uri.fromParts("package", EventDetailsActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmapProfile = MediaStore.Images.Media.getBitmap(EventDetailsActivity.this.getContentResolver(), uri);

                    base64String = ImageUtil.convert(bitmapProfile);
                    Log.e("base64String","base64String: " + base64String);
//                    Bitmap convertBitmap = ImageUtil.convert(base64String);
//                    Log.e(TAG, "convertBitmap: " + convertBitmap);
                    // loading profile image from local cache
                    buyTicket(base64String);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void buyTicket(String photo){
        Log.e("photo--->",photo);
        MyProgressDialog.show(EventDetailsActivity.this,"Loading...");
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BuyTicketResponse> call = apiInterface.buyTicket("true",token,event_id,photo);
        call.enqueue(new Callback<BuyTicketResponse>() {
            @Override
            public void onResponse(Call<BuyTicketResponse> call, Response<BuyTicketResponse> response) {
                Log.e("buyTicket",new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    Toast.makeText(EventDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(EventDetailsActivity.this,MyTicketActivity.class));
                    finish();
                }else {
                    Toast.makeText(EventDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BuyTicketResponse> call, Throwable t) {
                Log.e("buyTicket",""+t);
                MyProgressDialog.dismiss();
            }
        });
    }

}
