package com.angadi.tripmanagementa.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.DaysAdapter;
import com.angadi.tripmanagementa.adapters.MembersAdapter;
import com.angadi.tripmanagementa.adapters.SliderAdapter;
import com.angadi.tripmanagementa.adapters.SponsorsAdapter;
import com.angadi.tripmanagementa.adapters.SubEventsAdapter;
import com.angadi.tripmanagementa.fragments.ScanEventDialogFragment;
import com.angadi.tripmanagementa.fragments.SubEventDetailsDialogFragment;
import com.angadi.tripmanagementa.models.AllEventsResponse;
import com.angadi.tripmanagementa.models.AllEventsResult;
import com.angadi.tripmanagementa.models.BuyTicketResponse;
import com.angadi.tripmanagementa.models.EventDetailsResponse;
import com.angadi.tripmanagementa.models.MembersResponse;
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.models.PeaGallery;
import com.angadi.tripmanagementa.models.ShowSubEventResponse;
import com.angadi.tripmanagementa.models.SponsorsLogoResponse;
import com.angadi.tripmanagementa.models.SubEventResult;
import com.angadi.tripmanagementa.models.Website;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.ImageUtil;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class EventDetailsActivity extends AppCompatActivity implements SubEventDetailsDialogFragment.DetailsDialogListener, SubEventDetailsDialogFragment.DialogListener {

    //    @BindView(R.id.loading_layout)
//    View loadingIndicator;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_desc)
    TextView txt_desc;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_price)
    TextView txt_price;
    @BindView(R.id.txt_venue)
    TextView txt_venue;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.img_logo)
    ImageView img_logo;
    @BindView(R.id.recyclerDates)
    RecyclerView recyclerDates;
    @BindView(R.id.recyclerSponsors)
    RecyclerView recyclerSponsors;
    String event_id, encoded = "no";
    public static final int REQUEST_IMAGE = 100;
    String base64String;
    @BindView(R.id.imageSlider)
    SliderView sliderView;
    ArrayList<Integer> slider_images;
    String str_location, str_contact, str_emergency, selected_category;
    @BindView(R.id.layout_stalls)
    LinearLayout layout_stalls;
    @BindView(R.id.layout_location)
    LinearLayout layout_location;
    @BindView(R.id.layout_floor)
    LinearLayout layout_floor;
    @BindView(R.id.layout_contact)
    LinearLayout layout_contact;
    @BindView(R.id.layout_chat)
    LinearLayout layout_chat;
    @BindView(R.id.layout_emergency)
    LinearLayout layout_emergency;
    @BindView(R.id.img_qr_code)
    ImageView img_qr_code;
    double screenInches;
    BitMatrix result;
    Bitmap bitmap, bitmapQrborder;
    @BindView(R.id.layout_sponsors)
    LinearLayout layout_sponsors;
    String[] categories = {"Select Category", "VIP", "Pioneer", "Attendee", "Influencer", "Partner","Core Team","Organizing Team"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            event_id = getIntent().getStringExtra("event_id");
            encoded = getIntent().getStringExtra("encoded");
            Log.e("event_id", "" + event_id);
            Log.e("encoded", "" + encoded);
            assert encoded != null;
            if (encoded.equalsIgnoreCase("yes")) {
                String scan = null;
                byte[] tmp = Base64.decode(event_id, Base64.DEFAULT);
                try {
                    scan = new String(tmp, "UTF-8");
                    Log.e("scan", "" + scan);
                    getEventDetails(scan);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                getEventDetails(event_id);
                Log.e("event_id", "" + event_id);
            }


        }
        getScreenResolution();

    }

    private void getScreenResolution() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        screenInches = Math.sqrt(x + y);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btn_buy)
    public void onClick(View view) {
        checkTicket();
    }

    private void getEventDetails(String event_id) {
        MyProgressDialog.show(EventDetailsActivity.this, "Loading...");
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
                        txt_time.setText(response.body().getPeaDateTime());
                        txt_venue.setText(response.body().getPeaVenue());
                        txt_price.setText(response.body().getPeaPrice());
                        str_location = response.body().getPeaLocation();
                        str_contact = response.body().getPea_contact_no();
                        str_emergency = response.body().getPea_emergency_no();
                        String name_str = capitalizeWord(response.body().getPeaName());
                        String bitmap_name = name_str + " EVENT";
                        bitmapQrborder = writeTextOnDrawable(R.drawable.new_pro_frame, bitmap_name).getBitmap();
                        showQrCode(response.body().getPeaQrCodeIdSecureLink());
                        List<PeaGallery> galleryList = response.body().getPeaGallerys();

//                        img_logo.setImageURI(Constants.BASE_URL + response.body().getPeaLogo());
                        Glide.with(EventDetailsActivity.this).load(Constants.BASE_URL + response.body().getPeaLogo())
                                .placeholder(R.drawable.ic_placeholder).into(img_logo);
                        if (galleryList.size() != 0) {
                            sliderView.setVisibility(View.VISIBLE);
                            imageSlider(galleryList);
                        } else {
                            sliderView.setVisibility(View.GONE);
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

    private void setClickLinks(String contact_num, String emergency_num, String location) {

    }

    @OnClick({R.id.layout_stalls, R.id.layout_location, R.id.layout_floor, R.id.layout_contact, R.id.layout_chat,
            R.id.layout_emergency, R.id.layout_influencers})
    public void onClickLayouts(View view) {
        switch (view.getId()) {
            case R.id.layout_stalls:
                if (txt_name.getText().toString().equalsIgnoreCase("DEMO DAY 2019")) {
                    startActivity(new Intent(EventDetailsActivity.this, StallsActivity.class));
                }
                break;
            case R.id.layout_influencers:
                if (txt_name.getText().toString().equalsIgnoreCase("DEMO DAY 2019")) {
                    startActivity(new Intent(EventDetailsActivity.this, InfluencersActivity.class));
                }
                break;
            case R.id.layout_location:
                if (!str_location.equalsIgnoreCase("")) {
                    setLocation(str_location);
                }
                break;
            case R.id.layout_floor:
                break;
            case R.id.layout_contact:
                if (!str_contact.equalsIgnoreCase("")) {
                    callPhoneNumber(str_contact);
                }
                break;
            case R.id.layout_chat:
                if (!str_contact.equalsIgnoreCase("")) {
                    whatsApp(str_contact);
                }
                break;
            case R.id.layout_emergency:
                if (!str_emergency.equalsIgnoreCase("")) {
                    callPhoneNumber(str_emergency);
                }
                break;
        }
    }

    private void imageSlider(List<PeaGallery> galleryList) {
//        slider_images = new ArrayList<>();
//        slider_images.add(R.drawable.explore_event);
//        slider_images.add(R.drawable.organise_event);
//        slider_images.add(R.drawable.planet_zoom);
//        slider_images.add(R.drawable.planet_event);
        final SliderAdapter adapter = new SliderAdapter(EventDetailsActivity.this, galleryList);
        //adapter.setCount(3);
        sliderView.setSliderAdapter(adapter);

//        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
//        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        sliderView.setIndicatorSelectedColor(Color.WHITE);
//        sliderView.setIndicatorUnselectedColor(Color.GRAY);
//        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });
    }


    private void getSubEvents(String id) {
//        MyProgressDialog.show(EventDetailsActivity.this,"Loading...");
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShowSubEventResponse> call = apiInterface.showSubEvent("show", token, id);
        call.enqueue(new Callback<ShowSubEventResponse>() {
            @Override
            public void onResponse(Call<ShowSubEventResponse> call, Response<ShowSubEventResponse> response) {
                Log.e("getSubEvents", new Gson().toJson(response));
//                MyProgressDialog.dismiss();
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
//                MyProgressDialog.dismiss();
            }
        });

    }

    private void setAdapter(List<SubEventResult> resultList) {
        recyclerDates.setLayoutManager(new LinearLayoutManager(this));
        SubEventsAdapter subEventsAdapter = new SubEventsAdapter(this, resultList);
        recyclerDates.setAdapter(subEventsAdapter);

        subEventsAdapter.setClickListener(new SubEventsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String id, String title, String desc) {
                String demo = txt_name.getText().toString();
                Log.e("demo--->", "" + demo);
                DialogFragment event_ticket = SubEventDetailsDialogFragment.newInstance(title, desc, demo, position, EventDetailsActivity.this);
                event_ticket.show(getSupportFragmentManager(), "event_sub");
            }
        });

        if (txt_name.getText().toString().equalsIgnoreCase("DEMO DAY 2019")) {
            getSponsorsLogos();
        }
    }

    private void getSponsorsLogos() {
//        MyProgressDialog.show(EventDetailsActivity.this,"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SponsorsLogoResponse> call = apiInterface.getSponsorsLogos("true");
        call.enqueue(new Callback<SponsorsLogoResponse>() {
            @Override
            public void onResponse(Call<SponsorsLogoResponse> call, Response<SponsorsLogoResponse> response) {
                Log.e("getMembers", new Gson().toJson(response));
                layout_sponsors.setVisibility(View.VISIBLE);
//                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    List<Website> websiteList = response.body().getWebsite();
                    setSponsorsAdapter(websiteList);
                } else {
                    Toast.makeText(EventDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SponsorsLogoResponse> call, Throwable t) {
                Log.e("getMembers", "" + t);
//                MyProgressDialog.dismiss();
                layout_sponsors.setVisibility(View.GONE);
            }
        });
    }

    private void setSponsorsAdapter(List<Website> websiteList) {
        recyclerSponsors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SponsorsAdapter adapter = new SponsorsAdapter(EventDetailsActivity.this, websiteList);
        recyclerSponsors.setAdapter(adapter);
//
        adapter.setClickListener(new SponsorsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String url) {
                openUri(url);
            }
        });
    }

    private void openUri(String uri) {
        if (!uri.equals("")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }

    private void checkTicket() {
        MyProgressDialog.show(EventDetailsActivity.this, "Loading...");
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BuyTicketResponse> call = apiInterface.checkTicket("true", token, event_id);
        call.enqueue(new Callback<BuyTicketResponse>() {
            @Override
            public void onResponse(Call<BuyTicketResponse> call, Response<BuyTicketResponse> response) {
                Log.e("checkTicket", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    showSelfieDialog();
//                    Toast.makeText(EventDetailsActivity.this, "Please purchase the Ticket in Demo day website", Toast.LENGTH_LONG).show();
                } else {
//                    startActivity(new Intent(EventDetailsActivity.this, MyTicketDetailsActivity.class)
//                            .putExtra("event_id",event_id));
                    Toast.makeText(EventDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BuyTicketResponse> call, Throwable t) {
                Log.e("checkTicket", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void showSelfieDialog() {
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
                    Log.e("base64String", "base64String: " + base64String);
//                    Bitmap convertBitmap = ImageUtil.convert(base64String);
//                    Log.e(TAG, "convertBitmap: " + convertBitmap);
                    // loading profile image from local cache
//                    buyTicket(base64String);
                    showCategoriesDialog(base64String);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void showCategoriesDialog(String photo) {
        final Dialog alertDialog = new Dialog(EventDetailsActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_ticket_category);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Spinner spinner = alertDialog.findViewById(R.id.spinner);
        Button button = alertDialog.findViewById(R.id.button);
        EditText editText = alertDialog.findViewById(R.id.editText);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventDetailsActivity.this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("onItemSelected", "" + adapterView.getSelectedItem().toString());
                selected_category = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edt_str = editText.getText().toString();
                if (selected_category.equalsIgnoreCase("Select Category")) {
                    Toast.makeText(EventDetailsActivity.this, "Please select ticket category", Toast.LENGTH_SHORT).show();
                } else if (edt_str.equalsIgnoreCase("")) {
                    Toast.makeText(EventDetailsActivity.this, "Please type no no Tickets", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    Log.e("onClick--->", edt_str);
                    Log.e("selected_category", selected_category);
                    buyTicket(photo,selected_category,edt_str);
                }
            }
        });
        alertDialog.show();
    }


    private void buyTicket(String photo,String category,String count) {
        Log.e("photo--->", photo);
        MyProgressDialog.show(EventDetailsActivity.this, "Loading...");
        String token = Prefs.with(EventDetailsActivity.this).getString("token", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BuyTicketResponse> call = apiInterface.buyTicket("true", token, event_id, photo,category,count);
        call.enqueue(new Callback<BuyTicketResponse>() {
            @Override
            public void onResponse(Call<BuyTicketResponse> call, Response<BuyTicketResponse> response) {
                Log.e("buyTicket", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(EventDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(EventDetailsActivity.this, MyTicketActivity.class));
                    finish();
                } else {
                    Toast.makeText(EventDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BuyTicketResponse> call, Throwable t) {
                Log.e("buyTicket", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }

    private void callPhoneNumber(String phoneNumber) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
//                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel:", phoneNumber, null));
//                            startActivity(intent);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
//        if (Build.VERSION.SDK_INT > 22) {
//            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    Activity#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//                return;
//            }
//            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel:", phoneNumber, null));
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel:", phoneNumber, null));
//            startActivity(intent);
//        }

    }

    private void setLocation(String address) {
        String map = "http://maps.google.co.in/maps?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
    }

    public static Intent viewOnMap(String address) {
        return new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format("geo:0,0?q=%s",
                        URLEncoder.encode(address))));
    }

    private void whatsApp(String number) {
        PackageManager pm = getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);

        if (isInstalled) {
            // Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            // startActivity(launchIntent);


            Intent i = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + URLEncoder.encode("", "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(pm) != null) {
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(EventDetailsActivity.this, "WhatsApp is not installed in this device", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {

        boolean found = true;

        try {

            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = false;
        }

        return found;
    }


    @Override
    public void updateResult(View view, int position, String id, String name, Integer cost, Integer tax) {

    }

    @Override
    public void onProfilePositiveClick(DialogFragment dialog) {

    }

    private void showQrCode(String str_qr_id) {
        try {
            bitmap = encodeAsBitmap(str_qr_id);
            img_qr_code.setImageBitmap(mergeBitmaps(bitmap, bitmapQrborder));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap encodeAsBitmap(String list) throws WriterException {
        Log.e("-----------------", String.valueOf(screenInches));

        try {
            Log.e("screenInches---->", String.valueOf(screenInches));
            if (screenInches > 5.0 && screenInches < 5.5) {
                Log.e("first", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 800, 800, null);
            } else if (screenInches > 5.5 && screenInches < 6.0) {
                Log.e("second", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 1000, 1000, null);
            } else if (screenInches > 6.0) {
                Log.e("third", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 1200, 1200, null);
            } else if (screenInches < 5.0 && screenInches > 4.0) {
                Log.e("fourth", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 700, 700, null);
            } else {
                Log.e("else", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 800, 800, null);
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

    private BitmapDrawable writeTextOnDrawable(int drawableId, String text) {
        Typeface montserrat_bold = Typeface.createFromAsset(getAssets(), "fonts/Bold.OTF");

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTypeface(montserrat_bold);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(18 * getResources().getDisplayMetrics().density);
        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, 200, 80, paint);
        return new BitmapDrawable(getResources(), bm);
    }


    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Log.e("bitmap_height", "" + height);
        Log.e("bitmap_width", "" + width);

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }

    private String capitalizeWord(String string) {
        String[] words = string.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder();
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterfirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }

}
