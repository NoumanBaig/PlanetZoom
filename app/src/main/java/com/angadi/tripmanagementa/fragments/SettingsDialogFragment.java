package com.angadi.tripmanagementa.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.HomeActivity;
import com.angadi.tripmanagementa.activities.LoginActivity;
import com.angadi.tripmanagementa.activities.ShowEventsActivity;
import com.angadi.tripmanagementa.models.AdminResponse;
import com.angadi.tripmanagementa.models.CheckAdminResponse;
import com.angadi.tripmanagementa.models.LogoutResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsDialogFragment extends DialogFragment {

    public static final String TAG = "settings_dialog";

    private Toolbar toolbar;

    String title, qr_code_id, qr_code_type, token, qr_url;
    private MessageDialogListener mListener;
    View view;
    @BindView(R.id.event_switch)
    Switch event_switch;
//    @BindView(R.id.loading_layout)
//    View loadingIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    public static SettingsDialogFragment newInstance(String title, MessageDialogListener listener) {
        SettingsDialogFragment dialogFragment = new SettingsDialogFragment();
        dialogFragment.title = title;
        dialogFragment.mListener = listener;
        return dialogFragment;
    }


    public interface DialogListener {
        void updateResult(View view, int position, String id, String name, Integer cost, Integer tax);
    }

    public interface MessageDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            this.mListener = (OnCompleteListener)activity;
//        }
//        catch (final ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.settings_dialog_fragment, container, false);
        ButterKnife.bind(this, view);
        toolbar = view.findViewById(R.id.toolbar);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        //  Toast.makeText(getActivity(), ""+qr_code_id, Toast.LENGTH_SHORT).show();
//        toolbar.inflateMenu(R.menu.example_dialog);
//        toolbar.setOnMenuItemClickListener(item -> {
//            dismiss();
//            return true;
//        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDialogPositiveClick(SettingsDialogFragment.this);
                }
                dismiss();
            }
        });
        setEvent_switch();


    }

    private void setEvent_switch() {
        if (Prefs.with(getActivity()).getString("organiser", "").equalsIgnoreCase("true")) {
            event_switch.setChecked(true);
        } else {
            event_switch.setChecked(false);
        }


        event_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.e("checked", "switch-->");
                    setAdmin("enable");
                } else {
                    Log.e("unchecked", "switch-->");
                    setAdmin("disable");
                }
            }
        });
    }

    @OnClick(R.id.btn_logout)
    public void onLogout(View view) {
        logout();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onDialogPositiveClick(SettingsDialogFragment.this);
        }
        dialog.dismiss();
    }


    private void setAdmin(String enable) {
        MyProgressDialog.show(getActivity(),"Loading...");
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AdminResponse> responseCall = apiInterface.setAdmin("true", token, enable);
        responseCall.enqueue(new Callback<AdminResponse>() {
            @Override
            public void onResponse(Call<AdminResponse> call, Response<AdminResponse> response) {
                Log.e("setAdmin_res", new Gson().toJson(response));
               MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                       checkAdmin();
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AdminResponse> call, Throwable t) {
                Log.e("setAdmin_res", "" + t);
               MyProgressDialog.dismiss();
            }
        });

    }

    private void checkAdmin() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CheckAdminResponse> call = apiInterface.checkAdmin("true",token);
        call.enqueue(new Callback<CheckAdminResponse>() {
            @Override
            public void onResponse(Call<CheckAdminResponse> call, Response<CheckAdminResponse> response) {
                Log.e("checkAdmin", new Gson().toJson(response));
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    if (response.body().getStatusAdmin().equalsIgnoreCase("ADMIN")){
                        Prefs.with(getActivity()).save("organiser","true");
                    }else {
                        Prefs.with(getActivity()).save("organiser","false");
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



    private void logout() {
        MyProgressDialog.show(getActivity(),"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LogoutResponse> responseCall = apiInterface.logout(token);
        responseCall.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                Log.e("logout", new Gson().toJson(response));
               MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Prefs.with(getActivity()).remove();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Log.e("logout", "" + t);
                MyProgressDialog.dismiss();
            }
        });
    }


}
