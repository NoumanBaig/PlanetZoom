package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.EventDetailsResponse;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MyTicketDetailsActivity extends AppCompatActivity {

    double screenInches;
    BitMatrix result;
    //    @BindView(R.id.loading_layout)
//    View loadingIndicator;
    //    @BindView(R.id.txt_name)
//    TextView txt_name;
    @BindView(R.id.txt_id)
    TextView txt_id;
    @BindView(R.id.txt_desc)
    TextView txt_desc;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_amount)
    TextView txt_amount;
    @BindView(R.id.txt_venue)
    TextView txt_venue;
        @BindView(R.id.layout)
        RelativeLayout view;
    @BindView(R.id.img)
    SimpleDraweeView imageView;
    @BindView(R.id.img_qr_code)
    ImageView img_qr_code;
    String event_id,event_name,event_desc;
    File imagePath;
    Bitmap bitmap, bitmapQrborder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_details);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("E Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            event_id = getIntent().getStringExtra("event_id");
            getTicketDetails(event_id);
        }

        getScreenResolution();

    }

    private void getScreenResolution(){
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

    private void getTicketDetails(String event_id) {
        MyProgressDialog.show(MyTicketDetailsActivity.this, "Loading...");
        String token = Prefs.with(MyTicketDetailsActivity.this).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<EventDetailsResponse> call = apiInterface.getEventDetails("true", token, event_id);
        call.enqueue(new Callback<EventDetailsResponse>() {
            @Override
            public void onResponse(Call<EventDetailsResponse> call, Response<EventDetailsResponse> response) {
                Log.e("getTicketDetails", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        txt_id.setText("Ticket ID: "+response.body().getPeaTickets());
                        event_name = response.body().getPeaName();
                        event_desc = response.body().getPeaDesc();
                        txt_desc.setText(event_desc);
//                        txt_address.setText(response.body().getPeaLocation());
                        String formatedDate = parseDate(response.body().getPeaDate());
                        txt_date.setText("Date: " + formatedDate);
                        txt_time.setText("Time: " + response.body().getPeaDateTime());
                        txt_venue.setText(response.body().getPeaVenue());
                        txt_amount.setText(response.body().getPeaPrice());

                        if (response.body().getPeaLogo().equalsIgnoreCase("NULL")) {
                            imageView.setImageResource(R.drawable.explore_event);
                        } else {
                            imageView.setImageURI(Uri.parse(Constants.BASE_URL + response.body().getPeaLogo()));
                        }
                        String bitmap_name = response.body().getPeaName()+" TICKET";
                        bitmapQrborder = writeTextOnDrawable(R.drawable.new_pro_frame, bitmap_name).getBitmap();
                        showQrCode(response.body().getPeaQrCodeIdSecureLink());

                    } else {
                        Toast.makeText(MyTicketDetailsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EventDetailsResponse> call, Throwable t) {
                Log.e("getTicketDetails", "" + t);
                MyProgressDialog.dismiss();
            }
        });

    }

    public String parseDate(String str_date) {
        String inputPattern = "dd-mm-yyyy";
        String outputPattern = "dd MMM, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(str_date);
            str = outputFormat.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return str;
    }

    private void showQrCode(String str_qr_id) {
        try {
            bitmap = encodeAsBitmap(str_qr_id);
            img_qr_code.setImageBitmap(mergeBitmaps(bitmap,bitmapQrborder));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap encodeAsBitmap(String list) throws WriterException {
        Log.e("-----------------", String.valueOf(screenInches));

        try {
            Log.e("screenInches---->", String.valueOf(screenInches));
            if(screenInches > 5.0 && screenInches < 5.5){
                Log.e("first", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 800, 800, null);
            }else if(screenInches > 5.5 && screenInches < 6.0){
                Log.e("second", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 1000, 1000, null);
            }else if (screenInches > 6.0){
                Log.e("third", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 1200, 1200, null);
            }else if(screenInches < 5.0 && screenInches > 4.0){
                Log.e("fourth", "--->");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 700, 700, null);
            }
            else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ticket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share_ticket) {
            //  startActivity(new Intent(HomeActivity.this,CreateQrActivity.class));
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                               takeSS(view);
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Log.e("permission", "denied");
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();


        }

        return super.onOptionsItemSelected(item);
    }

    private void takeSS(View v){
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";

            // create bitmap screen capture
            v.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //setting screenshot in imageview
            String filePath = imageFile.getPath();

            Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            //ivpl.setImageBitmap(ssbitmap);
            //sharePath = filePath;
            share(ssbitmap);

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void share(Bitmap mBitmap){

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "PlanetZoom Event");
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
        startActivity(Intent.createChooser(share, "Share Ticket"));
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
}