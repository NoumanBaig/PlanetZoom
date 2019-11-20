package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.fragments.HomeFragment;
import com.angadi.tripmanagementa.fragments.EventsFragment;
import com.angadi.tripmanagementa.fragments.ProfileFragment;
import com.angadi.tripmanagementa.fragments.DashboardFragment;
import com.angadi.tripmanagementa.fragments.OffersFragment;
import com.angadi.tripmanagementa.fragments.ScanResultDialogFragment;
import com.angadi.tripmanagementa.models.CheckAdminResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Prefs;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.itparsa.circlenavigation.CircleItem;
import com.itparsa.circlenavigation.CircleNavigationView;
import com.itparsa.circlenavigation.CircleOnClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements ScanResultDialogFragment.MessageDialogListener{

    private static final int ZXING_CAMERA_PERMISSION = 1;
    @BindView(R.id.img_toolbar)
    ImageView img_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CircleNavigationView mCircleNavigationView;
        mCircleNavigationView = (CircleNavigationView) findViewById(R.id.navigation);
        mCircleNavigationView.initWithSaveInstanceState(savedInstanceState);
        mCircleNavigationView.setCentreButtonSelectable(true);
        cameraPermission();
        mCircleNavigationView.addCircleItem(new CircleItem("Offers", R.drawable.bargains));
        mCircleNavigationView.addCircleItem(new CircleItem("Events", R.drawable.event));
        mCircleNavigationView.addCircleItem(new CircleItem("Dashboard", R.drawable.dashboard));
        mCircleNavigationView.addCircleItem(new CircleItem("Profile", R.drawable.user));
        Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment);
        //checking whether user is a organiser or not
        checkAdmin();
        mCircleNavigationView.setCircleOnClickListener(new CircleOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
                HomeFragment homeFragment = new HomeFragment();
                loadFragment(homeFragment);
//                Toast.makeText(HomeActivity.this, "Center Item Click", Toast.LENGTH_SHORT).show();
//                mCircleNavigationView.showBadgeAtIndex(2, 80, getResources().getColor(R.color.colorAccent)
//                        , 16, getResources().getColor(R.color.colorPrimary));

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
//
               if (itemIndex==0){
                   Log.e("Zero","---->");
                   Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
                   OffersFragment zeroFragment = new OffersFragment();
                   loadFragment(zeroFragment);
               }else if (itemIndex==1){
                   Log.e("One","---->");
                   Glide.with(HomeActivity.this).load(R.drawable.planet_event).into(img_toolbar);
                   EventsFragment oneFragment = new EventsFragment();
                   loadFragment(oneFragment);
               }
               else if (itemIndex==2){
                   Log.e("Two","---->");
                   Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
                   DashboardFragment zeroFragment = new DashboardFragment();
                   loadFragment(zeroFragment);
               }
               else if (itemIndex==3){
                   Log.e("Three","---->");
                   Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
                   ProfileFragment zeroFragment = new ProfileFragment();
                   loadFragment(zeroFragment);
               }
//                if (itemIndex == 2)
//                    mCircleNavigationView.hideBadgeAtIndex(2);
//
//                if (itemIndex == 0)
//                    mCircleNavigationView.showBadgeAtIndexWithoutText(3, 8, getResources().getColor(R.color.colorAccent));
//
//                if (itemIndex == 3)
//                    mCircleNavigationView.hideBadgeAtIndex(3);

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        if (data != null) {
            Log.e("data",""+data);
            String qr_code_id = data.getQueryParameter("qr_code_id");
            String qr_type = data.getQueryParameter("qr_type");
            String qr_user_id = data.getQueryParameter("qr_user_id");

            Log.e("qr_code_id ",""+qr_code_id);
            Log.e("qr_type ",""+qr_type);
            Log.e("qr_user_id ",""+qr_user_id);
           showResultDialog(qr_code_id,qr_type, String.valueOf(data),qr_user_id);
        }
    }

    public void showResultDialog(String qr_code_id,String qr_code_type,String qr_url,String user_id) {
        DialogFragment newFragment2 = ScanResultDialogFragment.newInstance(HomeActivity.this,"Scan Results", qr_code_id,qr_code_type,qr_url,user_id,this);
        newFragment2.show(HomeActivity.this.getSupportFragmentManager(), "scan_results");
//        ScanResultDialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
//        fragment.show(getActivity().getSupportFragmentManager(), "scan_results");
    }


    private void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void cameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Camera--->","PERMISSION_GRANTED");
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.create_qr) {
           startActivity(new Intent(HomeActivity.this,CreateQrActivity.class));
        }else if (id == R.id.search){
            Toast.makeText(this, "Search clicked...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    private void checkAdmin() {
//        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(HomeActivity.this).getString("token","");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CheckAdminResponse> call = apiInterface.checkAdmin("true",token);
        call.enqueue(new Callback<CheckAdminResponse>() {
            @Override
            public void onResponse(Call<CheckAdminResponse> call, Response<CheckAdminResponse> response) {
                Log.e("checkAdmin", new Gson().toJson(response));
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                  if (response.body().getStatusAdmin().equalsIgnoreCase("ADMIN")){
                      Prefs.with(HomeActivity.this).save("organiser","true");
                  }else {
                      Prefs.with(HomeActivity.this).save("organiser","false");
                  }
                } else {
                    Log.e("checkAdmin", "" + response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<CheckAdminResponse> call, Throwable t) {
                Log.e("checkAdmin", "" + t);

            }
        });
    }

}
