package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.ProfileStatusResponse;
import com.angadi.tripmanagementa.models.QrScanResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ScanResultActivity extends AppCompatActivity {


    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.img_qr_code)
    ImageView img_qr_code;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_desc)
    TextView txt_desc;
    @BindView(R.id.txt_cat)
    TextView txt_cat;
    @BindView(R.id.txt_mobile)
    TextView txt_mobile;
    @BindView(R.id.txt_email)
    TextView txt_email;
    @BindView(R.id.txt_website)
    TextView txt_website;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.txt_fav)
    TextView txt_fav;
    @BindView(R.id.txt_like)
    TextView txt_like;
    @BindView(R.id.txt_dislike)
    TextView txt_dislike;
    @BindView(R.id.txt_ratings)
    TextView txt_ratings;
    @BindView(R.id.txt_ratings_avg)
    TextView txt_ratings_avg;
    double screenInches;
    BitMatrix result;
    private String str_location, str_mobile, str_email, str_website, str_whatsApp, str_facebook, str_youtube, str_id, str_url,
            str_instagram, str_linkedin, str_name;
    @BindView(R.id.layout_shareQR)
    LinearLayout layout_shareQR;
    @BindView(R.id.layout_save)
    LinearLayout layout_save;
    @BindView(R.id.layout_shareAndSave)
    LinearLayout layout_shareAndSave;
    Bitmap bitmap, bitmapQrborder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Qr Code Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            str_id = getIntent().getStringExtra("qr_id");
            str_url = getIntent().getStringExtra("qr_url");
            Log.e("str_id", "" + str_id);
            Log.e("str_url", "" + str_url);
            getScanResult(str_id, str_url);
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

    private void getScanResult(String id, String qr_url) {
        MyProgressDialog.show(ScanResultActivity.this, "Loading...");
        String token = Prefs.with(ScanResultActivity.this).getString("token", "");
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
                    Toast.makeText(ScanResultActivity.this, "Error while fetching data", Toast.LENGTH_SHORT).show();
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
            txt_name.setText(response.body().getQcaaName());
        }
        if (!response.body().getQcaaDesc().equalsIgnoreCase("")) {
            txt_desc.setText(response.body().getQcaaDesc());
        }
        if (!response.body().getQcaaCat().equalsIgnoreCase("")) {
            txt_cat.setText(response.body().getQcaaCat() + ", " + response.body().getQcaaSubCat());
        }
        if (!response.body().getQcaaEmailId().equalsIgnoreCase("")) {
            txt_email.setText(response.body().getQcaaEmailId());
        }
        if (!response.body().getQcaaPlace().equalsIgnoreCase("")) {
            txt_address.setText(response.body().getQcaaPlace());
        }
        if (!response.body().getQcaaWebsite().equalsIgnoreCase("")) {
            txt_website.setText(response.body().getQcaaWebsite());
        }
        if (!response.body().getQcaaPhoneNo().equalsIgnoreCase("")) {
            txt_mobile.setText(response.body().getQcaaPhoneNo());
        }

        if (response.body().getQcaaProfileLogo().equalsIgnoreCase("NULL")) {
            imageView.setImageResource(R.drawable.ic_placeholder);
        } else {
            Glide.with(ScanResultActivity.this).load(Constants.BASE_URL + response.body().getQcaaProfileLogo()).into(imageView);
            // imageView.setImageURI(Constants.BASE_URL + response.body().getQcaaProfileLogo());

        }
        str_mobile = response.body().getQcaaPhoneNo();
        str_location = response.body().getQcaaPlace();
        str_email = response.body().getQcaaEmailId();
        str_website = response.body().getQcaaWebsite();
        str_whatsApp = response.body().getQcaaSocialWhatsapp();
//        str_instagram = response.body().getUraInstagram();
        str_facebook = response.body().getQcaaSocialFacebook();
        str_youtube = response.body().getQcaaSocialYoutube();
//        str_linkedin = response.body().getUraLinkedin();
        str_name = response.body().getQcaaName();

        String bitmap_name = response.body().getQcaaName() + " BIZ QR";
        bitmapQrborder = writeTextOnDrawable(R.drawable.new_pro_frame, bitmap_name).getBitmap();

        if (!url.equalsIgnoreCase("")) {
            showQrCode(url);
        } else {
            img_qr_code.setVisibility(View.GONE);
            layout_shareAndSave.setVisibility(View.GONE);
        }

        decodeId(id);
    }

    private void decodeId(String id) {
        Log.e("id---->", id);
        byte[] tmp = Base64.decode(id, Base64.DEFAULT);
        try {
            String str_profile_id = new String(tmp, "UTF-8");
            Log.e("str_profile_id", "" + str_profile_id);
            getStatus(str_profile_id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    private void getStatus(String pro_id) {
        MyProgressDialog.show(ScanResultActivity.this, "Loading...");
        String token = Prefs.with(ScanResultActivity.this).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileStatusResponse> call = apiInterface.qrCodeStatus("true", token, pro_id);
        call.enqueue(new Callback<ProfileStatusResponse>() {
            @Override
            public void onResponse(Call<ProfileStatusResponse> call, Response<ProfileStatusResponse> response) {
                Log.e("getStatus", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        setFavsAndRatings(response);
                    } else {
                        Toast.makeText(ScanResultActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProfileStatusResponse> call, Throwable t) {
                Log.e("getStatus", "" + t);
                MyProgressDialog.dismiss();
            }
        });

    }

    private void setFavsAndRatings(Response<ProfileStatusResponse> response) {
        assert response.body() != null;

        txt_like.setText(response.body().getLikeCnt() + " Likes");
        txt_dislike.setText(response.body().getDislikeCnt() + " Dislikes");
        txt_fav.setText(response.body().getFavCnt() + " Favorites");
        txt_ratings.setText(response.body().getRateCnt() + " Ratings");
        if (response.body().getRateAvg().equalsIgnoreCase("")) {
            txt_ratings_avg.setText("0");
        } else {
            txt_ratings_avg.setText(response.body().getRateAvg());
        }

    }


    @OnClick({R.id.layout_fav, R.id.layout_like, R.id.layout_dislike, R.id.layout_share, R.id.layout_ratings, R.id.layout_mobile,
            R.id.layout_email, R.id.layout_website, R.id.layout_location, R.id.layout_whatsApp, R.id.layout_facebook, R.id.layout_youtube,
            R.id.layout_instagram, R.id.layout_linkedIn, R.id.layout_shareQR, R.id.layout_save, R.id.btn_reach_us})
    public void onLayoutClick(View view) {
        switch (view.getId()) {
            case R.id.layout_fav:
                break;
            case R.id.layout_like:
                break;
            case R.id.layout_dislike:
                break;
            case R.id.layout_share:
                break;
            case R.id.layout_ratings:
                break;
            case R.id.layout_mobile:
                if (!str_mobile.equalsIgnoreCase("")) {
                    callPhoneNumber(str_mobile);
                }
                break;
            case R.id.layout_email:
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + str_email)));
                break;
            case R.id.layout_website:
                openUri(str_website);
                break;
            case R.id.layout_location:
                if (!str_location.equalsIgnoreCase("")) {
                    setLocation(str_location);
                }
                break;
            case R.id.layout_whatsApp:
                if (!str_whatsApp.equalsIgnoreCase("")) {
                    whatsApp(str_whatsApp);
                }
                break;
            case R.id.layout_facebook:
                openUri(str_facebook);
                break;
            case R.id.layout_youtube:
                openUri(str_youtube);
                break;
            case R.id.layout_instagram:
                openUri(str_instagram);
                break;
            case R.id.layout_linkedIn:
                openUri(str_linkedin);
                break;
            case R.id.layout_shareQR:
                shareQr();
                break;
            case R.id.layout_save:
                saveQrToGallery();
                break;
            case R.id.btn_reach_us:
                if (!str_email.equalsIgnoreCase("")) {
                    sendReachUsMail(str_email, str_name);
                } else {
                    Toast.makeText(ScanResultActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void callPhoneNumber(String phoneNumber) {
        Dexter.withActivity(ScanResultActivity.this)
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
    }

    private void setLocation(String address) {
        String map = "http://maps.google.co.in/maps?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
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
            Toast.makeText(ScanResultActivity.this, "WhatsApp is not installed in this device", Toast.LENGTH_SHORT).show();
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

    private void openUri(String uri) {
//        String uri = WebsiteFromList;
        if (uri != "") {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + uri));
            startActivity(intent);
        }
    }

    private void showQrCode(String str_qr_id) {
        img_qr_code.setVisibility(View.VISIBLE);
        layout_shareAndSave.setVisibility(View.VISIBLE);
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
                if (screenInches < 5.3) {
                    Log.e("first", "5.3-->");
                    result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 1250, 1250, null);
                } else {
                    Log.e("first", "--->");
                    result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 800, 800, null);
                }
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

    private void saveQrToGallery() {
        Dexter.withActivity(ScanResultActivity.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (bitmap != null) {
                                Log.e("Bitmapforboreder", String.valueOf(bitmapQrborder));
                                String Filepath = getImageUri(ScanResultActivity.this, mergeBitmaps(bitmap, bitmapQrborder)).getPath();
                                if (Filepath != null) {
                                    addImageToGallery(Filepath, ScanResultActivity.this);
                                }

                            }

                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Log.e("Permission", "denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void shareQr() {
        Dexter.withActivity(ScanResultActivity.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Bitmap bitmap_share = mergeBitmaps(bitmap, bitmapQrborder);
                            shareQrCodeImage(bitmap_share);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Log.e("Permission", "denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void shareQrCodeImage(Bitmap mBitmap) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "PlanetZoom Profile");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);


        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Profile"));
    }

    //To get Uri from Bitmap
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, null, null);
        return Uri.parse(path);
    }

    //Adding generated QR into Gallery
    public void addImageToGallery(final String filePath, final Context context) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Toast.makeText(ScanResultActivity.this, "QR Code has been saved to Gallery", Toast.LENGTH_SHORT).show();
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

    private void sendReachUsMail(String str_email, String fName) {
        String[] recipients = str_email.split(",");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, fName);
        intent.putExtra(Intent.EXTRA_TEXT, "Tried to reach you");

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {
            if (str_url.equalsIgnoreCase("")) {
                Toast.makeText(this, "Sorry! You don't have access to edit", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(ScanResultActivity.this, EditQRActivity.class)
                        .putExtra("str_id", str_id).putExtra("str_url", str_url));
                finish();
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
