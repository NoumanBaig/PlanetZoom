package com.angadi.tripmanagementa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.utils.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.txt)
    TextView textView;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        textView.setText("Hi, "+ Prefs.with(WelcomeActivity.this).getString("firstName",""));
        Prefs.with(WelcomeActivity.this).save("login","true");
    }
    @OnClick(R.id.btn_getStarted)
    public void onClick(View view){
        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        finish();

    }
}
