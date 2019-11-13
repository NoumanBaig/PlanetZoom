package com.angadi.tripmanagementa.fragments;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.HomeActivity;
import com.angadi.tripmanagementa.utils.Prefs;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeFragment extends Fragment {

    @BindView(R.id.txt)
    TextView textView;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome,container,false);
        ButterKnife.bind(this,view);

        textView.setText("Hi, "+ Prefs.with(getActivity()).getString("firstName",""));
        Prefs.with(getActivity()).save("login","true");

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return view;
    }

//    @OnClick(R.id.btn_getStarted)
//    public void onClick(View view){
//        startActivity(new Intent(getActivity(), HomeActivity.class));
//
//    }
}
