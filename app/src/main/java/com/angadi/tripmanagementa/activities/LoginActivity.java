package com.angadi.tripmanagementa.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ChangeBounds;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.fragments.WelcomeFragment;
import com.angadi.tripmanagementa.models.LoginResponse;
import com.angadi.tripmanagementa.models.RegisterResponse;
import com.angadi.tripmanagementa.models.VerifyOtp;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.HideKeyboard;
import com.angadi.tripmanagementa.utils.Prefs;
import com.angadi.tripmanagementa.utils.ShowSnackBar;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //    @BindView(R.id.topText)
//    TextView topText;
//    @BindView(R.id.textView)
//    TextView textView;
//    @BindView(R.id.layout_first)
//    LinearLayout layout_first;
//    @BindView(R.id.layout_second)
//    LinearLayout layout_second;
//    @BindView(R.id.layout_terms)
//    LinearLayout layout_terms;
    @BindView(R.id.container)
    ConstraintLayout container;
    //    @BindView(R.id.username)
//    EditText username;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.verify_layout)
    LinearLayout verify_layout;
    @BindView(R.id.layout_resend)
    LinearLayout layout_resend;
    @BindView(R.id.layout_loading)
    LinearLayout layout_loading;
    @BindView(R.id.layout_firstName)
    LinearLayout layout_firstName;
    @BindView(R.id.img_close)
    ImageView img_close;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.avi2)
    AVLoadingIndicatorView avi2;
    boolean show;
    int time;
    String str_otp,str_device_token,str_fname;
    @BindView(R.id.pinView)
    PinView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseApp.initializeApp(LoginActivity.this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ViewGroup) findViewById(R.id.container)).getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
        }
        getDeviceToken();
        str_device_token = "hdkajskajsajkajsksjasajksaksjakjsdhhdhd";

        // avi.smoothToShow();

    }

    private void getDeviceToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new Instance ID token
                        str_device_token = task.getResult().getToken();
                        Log.e("str_device_token",str_device_token);
                    }
                });

    }

    @OnClick({R.id.btn_next, R.id.img_close, R.id.txt_resend,R.id.btn_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (layout_firstName.getVisibility() == View.VISIBLE) {
                    if (edt_name.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(this, "Please enter First Name", Toast.LENGTH_SHORT).show();
                    } else {
                        callSignup(edt_name.getText().toString(), edt_mobile.getText().toString());
                    }
                } else {
                    onNext();
                }
                break;
            case R.id.img_close:
                toggle();
                // resetting time when close
                time = 10;
                break;
            case R.id.txt_resend:
                setTimer();
                break;
            case R.id.btn_verify:
                onVerify();
                break;
            default:
                break;

        }

    }

    private void onNext() {
        String username = edt_mobile.getText().toString();

        if (!TextUtils.isEmpty(username)) {
            if (username.length() >= 10 && isValidMobile(username)) {
                callLogin(username);
            } else {
                if (isValidMail(username)) {
                    callLogin(username);
                } else {
                    Toast.makeText(this, "Please enter valid " + getResources().getString(R.string.mobile_email), Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            Toast.makeText(this, "Please enter valid " + getResources().getString(R.string.mobile_email), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    private void onVerify() {
        String OTP = pinView.getText().toString();
        if (OTP.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
        } else {
            verifyOtpCall(edt_mobile.getText().toString(),OTP,str_device_token);
//            pinView.setLineColor(getResources().getColor(R.color.red));
//            textView.setText("Incorrect OTP");
//            textView.setTextColor(getResources().getColor(R.color.red));
        }
    }


    private void toggle() {
        HideKeyboard.hide(LoginActivity.this, container);
        View redLayout = findViewById(R.id.verify_layout);
        ViewGroup parent = findViewById(R.id.container);

        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(500);
        transition.addTarget(R.id.verify_layout);

        TransitionManager.beginDelayedTransition(parent, transition);
        if (redLayout.getVisibility() == View.VISIBLE) {
            redLayout.setVisibility(View.GONE);
            img_close.setVisibility(View.GONE);
        } else {
            redLayout.setVisibility(View.VISIBLE);
            img_close.setVisibility(View.VISIBLE);
            setTimer();
        }

    }

    private void setTimer() {
        layout_loading.setVisibility(View.VISIBLE);
        layout_resend.setVisibility(View.GONE);
        time = 10;
        TextView textTimer = (TextView) findViewById(R.id.txt_timer);
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                textTimer.setText("0:" + checkDigit(time));
                time--;
            }

            public void onFinish() {
                layout_loading.setVisibility(View.GONE);
                layout_resend.setVisibility(View.VISIBLE);
                // textTimer.setText("try again");
            }
        }.start();
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void callLogin(String username) {
        avi.smoothToShow();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> loginResponseCall = apiInterface.login(username);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e("callLogin_res", new Gson().toJson(response));
                avi.smoothToHide();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        if (response.body().getStatusLogin().equalsIgnoreCase("creating_new")) {
                            layout_firstName.setVisibility(View.VISIBLE);
                        } else {
                            layout_firstName.setVisibility(View.GONE);
                            str_otp = String.valueOf(response.body().getRand());
                            str_fname = String.valueOf(response.body().getFname());
                            String str_uid = String.valueOf(response.body().getUID());
                            Prefs.with(LoginActivity.this).save("firstName",str_fname);
                            Prefs.with(LoginActivity.this).save("str_uid",str_uid);
                            toggle();
                            pinView.setText(str_otp);
                        }

                    } else {
                        Log.e("else", "----->");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("login_failure", "" + t);
                avi.smoothToHide();
            }
        });
    }

    private void callSignup(String firstName, String username) {
        avi.smoothToShow();
        Prefs.with(LoginActivity.this).save("firstName",firstName);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterResponse> responseCall = apiInterface.register(firstName, username);
        responseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.e("callSignup_res", new Gson().toJson(response));
                avi.smoothToHide();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        str_otp = String.valueOf(response.body().getRand());
                        toggle();
                        pinView.setText(str_otp);
                    } else {
                        Log.e("else", "----->");
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("signup_failure", "" + t);
                avi.smoothToHide();
            }
        });
    }

    private void verifyOtpCall(String username,String otp, String token){
        avi2.smoothToShow();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<VerifyOtp> otpCall = apiInterface.verify_otp(username,otp,token);
        otpCall.enqueue(new Callback<VerifyOtp>() {
            @Override
            public void onResponse(Call<VerifyOtp> call, Response<VerifyOtp> response) {
                Log.e("verifyOtp_res", new Gson().toJson(response));
                avi2.smoothToHide();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        String token = response.body().getToken();
                        String user_id = response.body().getUser_id();
                        Prefs.with(LoginActivity.this).save("token",token);
                        Prefs.with(LoginActivity.this).save("user_id",user_id);

                        WelcomeFragment welcomeFragment = new WelcomeFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.container, welcomeFragment);
                        fragmentTransaction.commit();
                    } else {
                        Log.e("else", "----->");
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyOtp> call, Throwable t) {
                avi2.smoothToHide();
                Log.e("otp_failure", "" + t);
            }
        });
    }
}
