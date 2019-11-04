package com.angadi.tripmanagementa.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.ImagePickerActivity;
import com.angadi.tripmanagementa.activities.LoginActivity;
import com.angadi.tripmanagementa.models.EditProfileResponse;
import com.angadi.tripmanagementa.models.LogoutResponse;
import com.angadi.tripmanagementa.models.ProfileResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.ImageUtil;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer;
import com.github.sumimakito.awesomeqr.RenderResult;
import com.github.sumimakito.awesomeqr.option.RenderOption;
import com.github.sumimakito.awesomeqr.option.color.Color;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ProfileFragment extends Fragment implements SettingsDialogFragment.MessageDialogListener {

    @BindView(R.id.img_edit)
    ImageView img_edit;
    @BindView(R.id.img)
    ImageView imageView;
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
//    @BindView(R.id.loading_layout)
//    View loadingIndicator;
    @BindView(R.id.img_qr_code)
    ImageView img_qr_code;
    public static final int REQUEST_IMAGE = 100;
    String base64String;
    double screenInches;
    BitMatrix result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);


        getProfile();
        return view;
    }

    private void getProfile() {
        MyProgressDialog.show(getActivity(),"Loading...");
        String token = Prefs.with(getActivity()).getString("token", "");
        String uid = Prefs.with(getActivity()).getString("UID", "1234");
        Log.e("token", token);
//        Log.e("uid", uid);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileResponse> responseCall = apiInterface.getProfile("true", token);
        responseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                Log.e("profile_res", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        displayTexts(response);
                    } else {
                        Toast.makeText(getActivity(), response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("profile_res", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void displayTexts(Response<ProfileResponse> response) {
        assert response.body() != null;
        edt_name.setText(response.body().getUraFname());
        edt_about.setText(response.body().getUraAbout());
        edt_address.setText(response.body().getUraAddress());
        edt_company.setText(response.body().getUraCompanyName());
        edt_designation.setText(response.body().getUraDesignation());
        edt_website.setText(response.body().getUraWebsite());
        edt_busi_phone.setText(response.body().getUraBizPhone());
        edt_facebook.setText(response.body().getUraFacebook());
        edt_whatsapp.setText(response.body().getUraWhatsapp());
        edt_linkedin.setText(response.body().getUraLinkedin());
        edt_youtube.setText(response.body().getUraYoutube());
        edt_insta.setText(response.body().getUraInstagram());

        Log.e("getUraImg","--->"+response.body().getUraImg());
        if (response.body().getUraImg().equalsIgnoreCase("NULL")){
            Picasso.get().load(R.drawable.ic_account_circle).error(R.drawable.ic_account_circle).into(imageView);
        }
        else {
            Picasso.get().load(Constants.BASE_URL+response.body().getUraImg()).into(imageView);

        }
        showQrCode(response.body().getUraCodeIdSecureLink());
    }


    @OnClick({R.id.btn_update,R.id.img_edit,R.id.img_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                editProfile(edt_name.getText().toString(),"",edt_about.getText().toString(),edt_address.getText().toString(),
                        edt_company.getText().toString(),edt_designation.getText().toString(),edt_website.getText().toString(),edt_busi_phone.getText().toString(),
                        "",edt_facebook.getText().toString(),edt_whatsapp.getText().toString(),edt_linkedin.getText().toString(),edt_youtube.getText().toString(),
                        edt_insta.getText().toString(),base64String);
                break;
            case R.id.img_edit:
                Dexter.withActivity(getActivity())
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
            case R.id.img_settings:
                DialogFragment dialogFragment = SettingsDialogFragment.newInstance("Settings",this);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "scan_results");
                break;
        }
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
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
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

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

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
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
                    Bitmap bitmapProfile = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

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
        Picasso.get().load(url).into(imageView);
    }


    private void editProfile(String fname, String lname, String about, String address, String company, String designation, String website,
                             String biz_phone, String biz_email, String facebook, String whatsapp, String linkedin, String youtube, String instagran, String photo) {
        MyProgressDialog.show(getActivity(),"Loading...");
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EditProfileResponse> responseCall = apiInterface.editProfile("true",token,fname,lname,about,address,company,designation,website,
                biz_phone,biz_email,facebook,whatsapp,linkedin,youtube,instagran,photo);
        responseCall.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                Log.e("logout", new Gson().toJson(response));
                MyProgressDialog.dismiss();
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
                MyProgressDialog.dismiss();
            }
        });

    }



    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }


    private void showQrCode(String str_qr_id){
        try {
            Bitmap bitmap = encodeAsBitmap(str_qr_id);
            img_qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap encodeAsBitmap(String list) throws WriterException {
        Log.e("-----------------", String.valueOf(screenInches));

        try {
            Log.e("screenInches---->", String.valueOf(screenInches));
            if (screenInches <= 5.2) {
                Log.e("first", "first");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 600, 600, null);
            } else if (screenInches >= 5.21 && screenInches <= 5.3) {
                Log.e("second", "second");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);

            } else if (screenInches >= 5.31 && screenInches <= 5.5) {
                Log.e("second1", "second1");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);

            } else if (screenInches >= 5.6 && screenInches <= 5.99) {
                Log.e("third", "third");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);

            } else if (screenInches >= 6.1 && screenInches <= 6.5) {
                Log.e("Fourth", "Fourth");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 760, 760, null);

            } else {
                Log.e("else", "else");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);
            }

        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
