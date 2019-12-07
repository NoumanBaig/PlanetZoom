package com.angadi.tripmanagementa.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.circlenavigation.CircleItem;
import com.angadi.tripmanagementa.circlenavigation.CircleNavigationView;
import com.angadi.tripmanagementa.circlenavigation.CircleOnClickListener;
import com.angadi.tripmanagementa.fragments.ECardFragment;
import com.angadi.tripmanagementa.fragments.HomeFragment;
import com.angadi.tripmanagementa.fragments.EventsFragment;
import com.angadi.tripmanagementa.fragments.ProfileFragment;
import com.angadi.tripmanagementa.fragments.DashboardFragment;
import com.angadi.tripmanagementa.fragments.OffersFragment;
import com.angadi.tripmanagementa.fragments.ScanResultDialogFragment;
import com.angadi.tripmanagementa.models.AuthCheckResponse;
import com.angadi.tripmanagementa.models.CheckAdminResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Prefs;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.gson.Gson;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements ScanResultDialogFragment.MessageDialogListener,
        OnSuccessListener<AppUpdateInfo> {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    @BindView(R.id.img_toolbar)
    ImageView img_toolbar;
    private AppUpdateManager appUpdateManager;
    private boolean mNeedsFlexibleUpdate;
    public static final int REQUEST_CODE = 1234;
    boolean doubleBackToExitPressedOnce = false;
    CircleNavigationView mCircleNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        appUpdateManager = AppUpdateManagerFactory.create(HomeActivity.this);
        setVersionText();
        mNeedsFlexibleUpdate = false;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCircleNavigationView = (CircleNavigationView) findViewById(R.id.navigation);
        mCircleNavigationView.initWithSaveInstanceState(savedInstanceState);
        mCircleNavigationView.setCentreButtonSelectable(true);
        cameraPermission();
        mCircleNavigationView.addCircleItem(new CircleItem("Offers", R.drawable.bargains));
        mCircleNavigationView.addCircleItem(new CircleItem("Events", R.drawable.event));
        mCircleNavigationView.addCircleItem(new CircleItem("Dashboard", R.drawable.dashboard));
        mCircleNavigationView.addCircleItem(new CircleItem("E-Card", R.drawable.ic_id_card));
        Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment, "home");
        //checking whether user is a organiser or not
        checkAdmin();
        mCircleNavigationView.setCircleOnClickListener(new CircleOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
                HomeFragment homeFragment = new HomeFragment();
                loadFragment(homeFragment, "home");
//                Toast.makeText(HomeActivity.this, "Center Item Click", Toast.LENGTH_SHORT).show();
//                mCircleNavigationView.showBadgeAtIndex(2, 80, getResources().getColor(R.color.colorAccent)
//                        , 16, getResources().getColor(R.color.colorPrimary));

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
//
                if (itemIndex == 0) {
                    Log.e("Zero", "---->");
                    Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
                    OffersFragment offersFragment = new OffersFragment();
                    loadFragment(offersFragment, "offers");
                } else if (itemIndex == 1) {
                    Log.e("One", "---->");
                    Glide.with(HomeActivity.this).load(R.drawable.planet_event).into(img_toolbar);
                    EventsFragment eventsFragment = new EventsFragment();
                    loadFragment(eventsFragment, "events");
                } else if (itemIndex == 2) {
                    Log.e("Two", "---->");
                    Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    loadFragment(dashboardFragment, "dashboard");
                } else if (itemIndex == 3) {
                    Log.e("Three", "---->");
                    Glide.with(HomeActivity.this).load(R.drawable.planet_zoom_white).into(img_toolbar);
//                    ProfileFragment profileFragment = new ProfileFragment();
//                    loadFragment(profileFragment, "profile");
                    ECardFragment eCardFragment = new ECardFragment();
                    loadFragment(eCardFragment, "e_card");
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
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(this);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        if (data != null) {
            Log.e("data", "" + data);
            String qr_code_id = data.getQueryParameter("qr_code_id");
            String qr_type = data.getQueryParameter("qr_type");
            String qr_user_id = data.getQueryParameter("qr_user_id");

            Log.e("qr_code_id ", "" + qr_code_id);
            Log.e("qr_type ", "" + qr_type);
            Log.e("qr_user_id ", "" + qr_user_id);
            showResultDialog(qr_code_id, qr_type, String.valueOf(data), qr_user_id);
        }
    }

    public void showResultDialog(String qr_code_id, String qr_code_type, String qr_url, String user_id) {
        DialogFragment newFragment2 = ScanResultDialogFragment.newInstance(HomeActivity.this, "Scan Results", qr_code_id, qr_code_type, qr_url, user_id, this);
        newFragment2.show(HomeActivity.this.getSupportFragmentManager(), "scan_results");
//        ScanResultDialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
//        fragment.show(getActivity().getSupportFragmentManager(), "scan_results");
    }


    private void loadFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Camera--->", "PERMISSION_GRANTED");
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
            startActivity(new Intent(HomeActivity.this, CreateQrActivity.class));
        } else if (id == R.id.search) {
            Toast.makeText(this, "Search clicked...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    private void checkAdmin() {
//        loadingIndicator.setVisibility(View.VISIBLE);
        String token = Prefs.with(HomeActivity.this).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CheckAdminResponse> call = apiInterface.checkAdmin("true", token);
        call.enqueue(new Callback<CheckAdminResponse>() {
            @Override
            public void onResponse(Call<CheckAdminResponse> call, Response<CheckAdminResponse> response) {
                Log.e("checkAdmin", new Gson().toJson(response));
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    if (response.body().getStatusAdmin().equalsIgnoreCase("ADMIN")) {
                        Prefs.with(HomeActivity.this).save("organiser", "true");
                    } else {
                        Prefs.with(HomeActivity.this).save("organiser", "false");
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


    @Override
    public void onSuccess(AppUpdateInfo appUpdateInfo) {
        if (appUpdateInfo.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
            Log.e("UPDATE_IN_PROGRESS", "onSuccess");
            // If an in-app update is already running, resume the update.
            startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
        } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
            Log.e("DOWNLOADED", "onSuccess");
            // If the update is downloaded but not installed,
            // notify the user to complete the update.
            popupSnackbarForCompleteUpdate();
        } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            Log.e("UPDATE_AVAILABLE", "onSuccess");
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
            } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                mNeedsFlexibleUpdate = true;
                showFlexibleUpdateNotification();
            }
        }
    }

    private void setVersionText() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {

        }
        Log.e("mVersionNumber", "" + packageInfo.versionName);
//        mVersionNumber.setText(packageInfo.versionName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e(MainActivity.class.getSimpleName(), "Update flow completed! Result code: " + resultCode);
            } else {
                Log.e(MainActivity.class.getSimpleName(), "Update flow failed! Result code: " + resultCode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startUpdate(final AppUpdateInfo appUpdateInfo, final int appUpdateType) {
        final Activity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                            appUpdateType,
                            activity,
                            REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.container),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    private void showFlexibleUpdateNotification() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.container),
                        "An update is available and accessible in More.",
                        Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        HomeFragment home = new HomeFragment();
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("home");
        Fragment dashboardFragment = getSupportFragmentManager().findFragmentByTag("dashboard");
        Fragment eventsFragment = getSupportFragmentManager().findFragmentByTag("events");
        Fragment profileFragment = getSupportFragmentManager().findFragmentByTag("e_card");
        Fragment offersFragment = getSupportFragmentManager().findFragmentByTag("offers");
        if (homeFragment != null && homeFragment.isVisible()) {
            Log.e("home", "--->");
            doubleTapExit();
        } else if (dashboardFragment != null && dashboardFragment.isVisible()) {
            Log.e("dashboard", "--->");
            loadFragment(home, "home");
            mCircleNavigationView.setCentreButtonSelectable(true);
            mCircleNavigationView.updateSpaceItems(-1);
        } else if (eventsFragment != null && eventsFragment.isVisible()) {
            Log.e("events", "--->");
            loadFragment(home, "home");
            mCircleNavigationView.setCentreButtonSelectable(true);
            mCircleNavigationView.updateSpaceItems(-1);
        } else if (profileFragment != null && profileFragment.isVisible()) {
            Log.e("e_card", "--->");
            loadFragment(home, "home");
            mCircleNavigationView.setCentreButtonSelectable(true);
            mCircleNavigationView.updateSpaceItems(-1);
        } else if (offersFragment != null && offersFragment.isVisible()) {
            Log.e("offers", "--->");
            loadFragment(home, "home");
            mCircleNavigationView.setCentreButtonSelectable(true);
            mCircleNavigationView.updateSpaceItems(-1);
        }
    }

    private void doubleTapExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
