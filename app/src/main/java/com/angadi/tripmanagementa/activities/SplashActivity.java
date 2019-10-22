package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.utils.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

//    @BindView(R.id.img_logo)
//    ImageView img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        runTimer();
    }

    private void runTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Prefs.with(SplashActivity.this).getString("login","").equalsIgnoreCase("true")) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
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

}
