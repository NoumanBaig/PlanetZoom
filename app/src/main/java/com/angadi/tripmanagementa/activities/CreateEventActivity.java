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
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.EventsAdapter;
import com.angadi.tripmanagementa.adapters.MembersAdapter;
import com.angadi.tripmanagementa.models.AllEventsResponse;
import com.angadi.tripmanagementa.models.AllEventsResult;
import com.angadi.tripmanagementa.models.CreateEventResponse;
import com.angadi.tripmanagementa.models.CreateQrResponse;
import com.angadi.tripmanagementa.models.MembersResponse;
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.ImageUtil;
import com.angadi.tripmanagementa.utils.Prefs;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
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

public class CreateEventActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute;
    double latitude = 12.9716, longitude = 77.5946;
    public static final int REQUEST_IMAGE = 100;
    @BindView(R.id.img_logo)
    ImageView img_logo;
    String base64String;
    @BindView(R.id.loading_layout)
    View loadingIndicator;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_desc)
    EditText edt_desc;
    @BindView(R.id.edt_organisation)
    EditText edt_organisation;
    @BindView(R.id.edt_venue)
    EditText edt_venue;
    @BindView(R.id.edt_location)
    EditText edt_location;
    @BindView(R.id.edt_noOfTickets)
    EditText edt_noOfTickets;
    @BindView(R.id.edt_price)
    EditText edt_price;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.layout_add)
    LinearLayout layout_add;
    @BindView(R.id.layout_members)
    LinearLayout layout_members;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.recyclerMembers)
    RecyclerView recyclerMembers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getCurrentLocation();

        showHideLayouts();
    }

    private void showHideLayouts(){
        if (Constants.event_created){
            layout_add.setVisibility(View.VISIBLE);
            layout_members.setVisibility(View.VISIBLE);
            getMembers();
        }else {
            layout_add.setVisibility(View.GONE);
            layout_members.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        startActivity(new Intent(CreateEventActivity.this, ShowEventsActivity.class).putExtra("events","created"));
        finish();
    }

    private void scrollDown(){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(CreateEventActivity.this, new OnSuccessListener<Location>() {
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

    @OnClick({R.id.img_loc, R.id.btn_create, R.id.img_logo, R.id.txt_date, R.id.txt_time,R.id.btn_addDays,R.id.btn_addMembers})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_date:
                setDateWithPicker();
                break;
            case R.id.txt_time:
                setTimePicker();
                break;
            case R.id.btn_create:
                validate();
                break;
            case R.id.img_loc:
                startActivityForResult(new LocationPickerActivity.Builder()
                        .withLocation(latitude, longitude).withSatelliteViewHidden().shouldReturnOkOnBackPressed()
                        .withGooglePlacesEnabled()
                        .build(CreateEventActivity.this), 1);
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
            case R.id.btn_addDays:
                startActivity(new Intent(this,AddSubEventsActivity.class));
                break;
            case R.id.btn_addMembers:
                getMembers();
                break;

        }
    }

    private void setDateWithPicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        txt_date.setText("" + dayOfMonth + "-" + month + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    private void setTimePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                txt_time.setText(selectedHour + " : " + selectedMinute);
            }
        }, mHour, mMinute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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
        Intent intent = new Intent(CreateEventActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(CreateEventActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
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
                    edt_location.setText(address);
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
                    Log.e("base64String", "base64String: " + base64String);
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadProfile(String url) {
        Log.e("", "Image cache path: " + url);
        Picasso.get().load(url).into(img_logo);
    }

    private void validate() {
        if (edt_name.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
        } else if (edt_desc.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
        } else if (edt_organisation.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter organisation", Toast.LENGTH_SHORT).show();
        } else if (edt_venue.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter venue", Toast.LENGTH_SHORT).show();
        } else if (edt_location.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter location", Toast.LENGTH_SHORT).show();
        } else if (txt_date.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Date", Toast.LENGTH_SHORT).show();
        } else if (txt_time.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Time", Toast.LENGTH_SHORT).show();
        } else if (edt_noOfTickets.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter No of tickets", Toast.LENGTH_SHORT).show();
        } else if (edt_price.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter price", Toast.LENGTH_SHORT).show();
        } else {
            createEvent(edt_name.getText().toString(), edt_price.getText().toString(), edt_noOfTickets.getText().toString(),
                    edt_location.getText().toString(), edt_venue.getText().toString(), edt_desc.getText().toString(), txt_date.getText().toString(),
                    txt_time.getText().toString(), edt_organisation.getText().toString());
        }
    }

    private void createEvent(String name, String price, String tickets, String location, String venue, String desc,
                             String date, String time, String organisation) {
        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(CreateEventActivity.this).getString("token", "");
        Log.e("token", token);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CreateEventResponse> call = apiInterface.addEvents("true", token, name, price, tickets, location, venue, desc, date, time, organisation, "0", base64String);

        call.enqueue(new Callback<CreateEventResponse>() {
            @Override
            public void onResponse(Call<CreateEventResponse> call, Response<CreateEventResponse> response) {
                Log.e("getAllEvents", new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        Toast.makeText(CreateEventActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Prefs.with(CreateEventActivity.this).save("event_id", response.body().getNewEventId());
//                        goBack();
                        Constants.event_created=true;
                        scrollDown();
                        layout_add.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(CreateEventActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CreateEventResponse> call, Throwable t) {
                Log.e("getAllEvents", "" + t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void getMembers(){
        loadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MembersResponse> call = apiInterface.getMembers("true");
        call.enqueue(new Callback<MembersResponse>() {
            @Override
            public void onResponse(Call<MembersResponse> call, Response<MembersResponse> response) {
                Log.e("getMembers",new Gson().toJson(response));
                loadingIndicator.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    List<MembersResult> membersResultList = response.body().getResults();
                    layout_members.setVisibility(View.VISIBLE);
                    setMembersAdapter(membersResultList);
                    scrollDown();
                }else {
                    Toast.makeText(CreateEventActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MembersResponse> call, Throwable t) {
                Log.e("getMembers",""+t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void setMembersAdapter(List<MembersResult> membersResultList){
        recyclerMembers.setLayoutManager(new LinearLayoutManager(this));
        MembersAdapter adapter = new MembersAdapter(this,membersResultList);
        recyclerMembers.setAdapter(adapter);

        adapter.setClickListener(new MembersAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String id) {
                Log.e("id---->",""+id);
            }
        });
    }

}
