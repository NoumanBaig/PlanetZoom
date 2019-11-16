package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.NetworkInformation;
import com.angadi.tripmanagementa.utils.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.img_logo)
    ImageView img_logo;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.txt)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        showVersion();
        if (NetworkInformation.isConnected(SplashActivity.this)){
            no_internet.setVisibility(View.GONE);
            img_logo.setVisibility(View.VISIBLE);
            runTimer();
        }else {
            no_internet.setVisibility(View.VISIBLE);
            img_logo.setVisibility(View.GONE);
        }

    }

    private void runTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Prefs.with(SplashActivity.this).getString("login","").equalsIgnoreCase("true")) {
                    Log.e("--->","prefs true");
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Log.e("--->","prefs false");
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                Pair<View, String> p1 = Pair.create((View) img_logo, "logo");
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(SplashActivity.this, p1);
//                startActivity(intent,options.toBundle());
//                finish();
            }
        }, 2000);
    }

    private void showVersion(){
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            if (Constants.BASE_URL.equalsIgnoreCase("https://test.planetzoom.app/")){
                textView.setText("Version: dev");
            }else {
                textView.setText("Version: "+version);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
