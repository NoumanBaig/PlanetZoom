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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.LoginActivity;
import com.angadi.tripmanagementa.adapters.DashboardDialogAdapter;
import com.angadi.tripmanagementa.adapters.EventDashboardAdapter;
import com.angadi.tripmanagementa.models.AdminResponse;
import com.angadi.tripmanagementa.models.CheckAdminResponse;
import com.angadi.tripmanagementa.models.EventVisitorResult;
import com.angadi.tripmanagementa.models.EventVisitorSingleRes;
import com.angadi.tripmanagementa.models.LogoutResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardDialogFragment extends DialogFragment {

    public static final String TAG = "settings_dialog";
    private Toolbar toolbar;
    String title, day, qr_code_type, token, qr_url;
    private MessageDialogListener mListener;
    View view;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.txt_count)
    TextView txt_count;

//    @BindView(R.id.loading_layout)
//    View loadingIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    public static DashboardDialogFragment newInstance(String title, String day,MessageDialogListener listener) {
        DashboardDialogFragment dialogFragment = new DashboardDialogFragment();
        dialogFragment.title = title;
        dialogFragment.day = day;
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
            MyProgressDialog.show(getActivity(),"Loading...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.dashboard_dialog_fragment, container, false);
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
        getResponse(day,title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDialogPositiveClick(DashboardDialogFragment.this);
                }
                dismiss();
            }
        });

    }



    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onDialogPositiveClick(DashboardDialogFragment.this);
        }
        dialog.dismiss();
    }


    private void getResponse(String day,String category) {
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<EventVisitorSingleRes> responseCall = apiInterface.getVisitor(day,category);
        responseCall.enqueue(new Callback<EventVisitorSingleRes>() {
            @Override
            public void onResponse(Call<EventVisitorSingleRes> call, Response<EventVisitorSingleRes> response) {
                Log.e("setAdmin_res", new Gson().toJson(response));
               MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        txt_count.setText("Total Count = "+response.body().getTotalCount());
                        List<EventVisitorResult> resultList = response.body().getResults();
                        setAdapter(resultList);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EventVisitorSingleRes> call, Throwable t) {
                Log.e("setAdmin_res", "" + t);
               MyProgressDialog.dismiss();
            }
        });

    }

    private void setAdapter(List<EventVisitorResult> resultList){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DashboardDialogAdapter adapter = new DashboardDialogAdapter(getActivity(),resultList);
        recyclerView.setAdapter(adapter);

    }
}
