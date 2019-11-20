package com.angadi.tripmanagementa.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.CreateQrResponse;
import com.angadi.tripmanagementa.models.QrScanResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.ImageUtil;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class EditQRActivity extends AppCompatActivity {
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_desc)
    EditText edt_desc;
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
    @BindView(R.id.btn_update)
    Button btn_update;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_IMAGE_GALLERY = 101;
    @BindView(R.id.imageView)
    ImageView imageView;
    String base64String = "", base64StringImages, str_cat_id, str_sub_id, str_qr_id, str_id, str_url;
    LatLng latLng;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_qr);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update QR Code");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent().getExtras() != null) {
            str_id = getIntent().getStringExtra("str_id");
            str_url = getIntent().getStringExtra("str_url");
            Log.e("str_id", "" + str_id);
            Log.e("str_url", "" + str_url);
            getScanResult(str_id, str_url);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(EditQRActivity.this, ScanResultActivity.class)
                .putExtra("qr_id", str_id).putExtra("qr_url", str_url));
        finish();
        return true;
    }

    private void getScanResult(String id, String qr_url) {
        MyProgressDialog.show(EditQRActivity.this, "Loading...");
        String token = Prefs.with(EditQRActivity.this).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<QrScanResponse> responseCall = apiInterface.scanResult("true", id, token);
        responseCall.enqueue(new Callback<QrScanResponse>() {
            @Override
            public void onResponse(Call<QrScanResponse> call, Response<QrScanResponse> response) {
                Log.e("scan_res", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    displayTexts(response, qr_url, id);
                } else {
                    Toast.makeText(EditQRActivity.this, "Error while fetching data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QrScanResponse> call, Throwable t) {
                Log.e("scan_res", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void displayTexts(Response<QrScanResponse> response, String url, String id) {
        assert response.body() != null;

        if (!response.body().getQcaaName().equalsIgnoreCase("")) {
            edt_name.setText(response.body().getQcaaName());
        }
        if (!response.body().getQcaaDesc().equalsIgnoreCase("")) {
            edt_desc.setText(response.body().getQcaaDesc());
        }

        if (!response.body().getQcaaEmailId().equalsIgnoreCase("")) {
            edt_email.setText(response.body().getQcaaEmailId());
        }
        if (!response.body().getQcaaPlace().equalsIgnoreCase("")) {
            edt_adds.setText(response.body().getQcaaPlace());
            latLng = getLocationFromAddress(EditQRActivity.this, response.body().getQcaaPlace());
            Log.e("latLng-->", "" + latLng);
        }
        if (!response.body().getQcaaWebsite().equalsIgnoreCase("")) {
            edt_website.setText(response.body().getQcaaWebsite());
        }
        if (!response.body().getQcaaPhoneNo().equalsIgnoreCase("")) {
            edt_mobile.setText(response.body().getQcaaPhoneNo());
        }
        if (!response.body().getQcaaSocialWhatsapp().equalsIgnoreCase("")) {
            edt_whatsapp.setText(response.body().getQcaaSocialWhatsapp());
        }
        if (!response.body().getQcaaSocialFacebook().equalsIgnoreCase("")) {
            edt_facebook.setText(response.body().getQcaaSocialFacebook());
        }
        if (!response.body().getQcaaSocialYoutube().equalsIgnoreCase("")) {
            edt_youtube.setText(response.body().getQcaaSocialYoutube());
        }
        if (response.body().getQcaaProfileLogo().equalsIgnoreCase("NULL")) {
            imageView.setImageResource(R.drawable.ic_placeholder);
        }
//        else {
//            Glide.with(EditQRActivity.this).load(Constants.BASE_URL + response.body().getQcaaProfileLogo()).into(imageView);
//            // imageView.setImageURI(Constants.BASE_URL + response.body().getQcaaProfileLogo());
////            bitmap = ImageUtil.convert(Constants.BASE_URL +response.body().getQcaaProfileLogo());
////            imageView.setImageBitmap(bitmap);
//
//        }
        decodeId(id);
    }

    private void decodeId(String id) {
        byte[] tmp2 = Base64.decode(id, Base64.DEFAULT);
        try {
            str_qr_id = new String(tmp2, "UTF-8");
            Log.e("str_qr_id", str_qr_id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @OnClick({R.id.btn_update, R.id.img_loc, R.id.imageView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                validate(edt_name.getText().toString(), edt_email.getText().toString(), edt_mobile.getText().toString(), edt_whatsapp.getText().toString(),
                        edt_facebook.getText().toString(), edt_youtube.getText().toString(), edt_website.getText().toString(), edt_adds.getText().toString(),
                        edt_desc.getText().toString(), "on", str_qr_id);
                break;
            case R.id.img_loc:
                startActivityForResult(new LocationPickerActivity.Builder()
                        .withLocation(latLng).withSatelliteViewHidden().shouldReturnOkOnBackPressed()
                        .withGooglePlacesEnabled()
                        .build(EditQRActivity.this), 1);
                break;
            case R.id.imageView:
                Dexter.withActivity(this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions(1);
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


    private void showImagePickerOptions(int num) {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent(num);
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent(num);
            }
        });
    }

    private void launchCameraIntent(int num) {
        Intent intent = new Intent(EditQRActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        if (num == 1) {
            startActivityForResult(intent, REQUEST_IMAGE);
        } else {
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
        }
    }

    private void launchGalleryIntent(int num) {
        Intent intent = new Intent(EditQRActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        if (num == 1) {
            startActivityForResult(intent, REQUEST_IMAGE);
        } else {
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditQRActivity.this);
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
                    Log.e("base64String", "base64String: " + base64String);
                    Glide.with(EditQRActivity.this).load(uri.toString()).into(imageView);
//                    Bitmap convertBitmap = ImageUtil.convert(base64String);
//                    Log.e(TAG, "convertBitmap: " + convertBitmap);
                    // loading profile image from local cache
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        if (requestCode == REQUEST_IMAGE_GALLERY) {
//            if (resultCode == Activity.RESULT_OK) {
//                Uri uri = data.getParcelableExtra("path");
//                try {
//                    // You can update this bitmap to your server
//                    Bitmap bitmapProfile = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                    base64StringImages = ImageUtil.convert(bitmapProfile);
//                    Log.e("base64StringImages", "" + base64StringImages);
//                    uploadGallery(qr_code_id, base64StringImages);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }

    private void validate(String name, String email, String mobile, String whatsApp, String faceBook, String youTube, String website,
                          String address, String desc, String edit, String id) {
        if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
        } else if (base64String.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please update image", Toast.LENGTH_SHORT).show();
        } else {
            updateQRcode(name, email, mobile, whatsApp, faceBook, youTube, website, address, desc, edit, id);
        }

    }

    private void updateQRcode(String name, String email, String mobile, String whatsApp, String faceBook, String youTube, String website,
                              String address, String desc, String edit, String id) {
        MyProgressDialog.show(EditQRActivity.this, "Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CreateQrResponse> responseCall = apiInterface.edit_qr(Prefs.with(EditQRActivity.this).getString("token", "")
                , str_cat_id, str_sub_id, name, email, mobile, whatsApp, faceBook, youTube, website, address, base64String, desc, edit, id);
        responseCall.enqueue(new Callback<CreateQrResponse>() {
            @Override
            public void onResponse(Call<CreateQrResponse> call, Response<CreateQrResponse> response) {
                Log.e("updateQRcode", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(EditQRActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditQRActivity.this, ScanResultActivity.class)
                            .putExtra("qr_id", response.body().getQrCodeIdSecure()).putExtra("qr_url", response.body().getQrCodeIdSecureLink()));
                    finish();
//                    Intent intent = new Intent(EditQRActivity.this,ScanResultActivity.class);
//                    intent.putExtra("qr_id",response.body().getQrCodeIdSecure());
//                    intent.putExtra("qr_url",response.body().getQrCodeIdSecureLink());
//                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(EditQRActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateQrResponse> call, Throwable t) {
                Log.e("updateQRcode", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

}
