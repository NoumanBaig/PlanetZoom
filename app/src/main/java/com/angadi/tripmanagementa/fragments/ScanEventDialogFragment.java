package com.angadi.tripmanagementa.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.EventDetailsActivity;
import com.angadi.tripmanagementa.adapters.EventTrackingAdapter;
import com.angadi.tripmanagementa.models.EventTrackResponse;
import com.angadi.tripmanagementa.models.ProfileResponse;
import com.angadi.tripmanagementa.models.QrScanResponse;
import com.angadi.tripmanagementa.models.ScanEventQrResponse;
import com.angadi.tripmanagementa.models.TrackData;
import com.angadi.tripmanagementa.models.TrackResult;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.schibstedspain.leku.tracker.TrackEvents;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ScanEventDialogFragment extends DialogFragment {

    public static final String TAG = "example_dialog";

    private Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edt_search;
    View loading;
    String title,qr_code_id,qr_code_type,token,qr_url,user_id;
    private EventDialogListener mListener;
    View view;
    double screenInches;
    BitMatrix result;
    @BindView(R.id.img)
    ImageView imageView;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_login_name)
    TextView txt_login_name;
    @BindView(R.id.txt_login_id)
    TextView txt_login_id;
    @BindView(R.id.txt_id)
    TextView txt_id;
    @BindView(R.id.txt_count)
    TextView txt_count;
    @BindView(R.id.txt_category)
    TextView txt_category;
    @BindView(R.id.txt_venue)
    TextView txt_venue;
    @BindView(R.id.txt_location)
    TextView txt_location;
    @BindView(R.id.txt_amount)
    TextView txt_amount;
    @BindView(R.id.progress)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.layout_main)
    LinearLayout layout_main;
    @BindView(R.id.layout_loading)
    LinearLayout layout_loading;
//    @BindView(R.id.layout_ticket)
//    LinearLayout layout_ticket;
//    @BindView(R.id.layout_animation)
//    LinearLayout layout_animation;
    @BindView(R.id.recyclerTracking)
    RecyclerView recyclerTracking;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    public static ScanEventDialogFragment newInstance(String title, String qr_code_id, String qr_code_type, String qr_url, String user_id, EventDialogListener listener) {
        ScanEventDialogFragment dialogFragment = new ScanEventDialogFragment();
        dialogFragment.title=title;
        dialogFragment.qr_code_id=qr_code_id;
        dialogFragment.qr_code_type=qr_code_type;
        dialogFragment.qr_url=qr_url;
        dialogFragment.user_id=user_id;
        dialogFragment.mListener=listener;
        return dialogFragment;
    }



    public interface DialogListener {
        void updateResult(View view, int position, String id, String name, Integer cost, Integer tax);
    }

    public interface EventDialogListener {
        public void onEventPositiveClick(DialogFragment dialog);
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
//            MyProgressDialog.show(getActivity(), "Loading...");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.scan_event_fragment, container, false);
        ButterKnife.bind(this, view);
        toolbar = view.findViewById(R.id.toolbar);
        progressBar.setVisibility(View.VISIBLE);
        layout_loading.setVisibility(View.VISIBLE);
        layout_main.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        try {
            token = Prefs.with(getActivity()).getString("token","");
            Log.e("token",token);
            Log.e("user_id",user_id);
            Log.e("qr_code_id",qr_code_id);
            Log.e("qr_code_type",qr_code_type);
            Log.e("qr_url",qr_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  Toast.makeText(getActivity(), ""+qr_code_id, Toast.LENGTH_SHORT).show();
//        toolbar.inflateMenu(R.menu.example_dialog);
//        toolbar.setOnMenuItemClickListener(item -> {
//            dismiss();
//            return true;
//        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onEventPositiveClick(ScanEventDialogFragment.this);
                }
                dismiss();
            }
        });

        getScanEventResult(user_id,qr_code_id);

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mListener != null) {
            mListener.onEventPositiveClick(ScanEventDialogFragment.this);
        }
        dialog.dismiss();
    }


    private void getScanEventResult(String user_id,String scan_id){
//        MyProgressDialog.show(getActivity(),"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ScanEventQrResponse> call = apiInterface.scanEventQr("true",token,user_id,scan_id);
        call.enqueue(new Callback<ScanEventQrResponse>() {
            @Override
            public void onResponse(Call<ScanEventQrResponse> call, Response<ScanEventQrResponse> response) {
                Log.e("getScanEventResult", new Gson().toJson(response));
                progressBar.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    layout_loading.setVisibility(View.GONE);
                    layout_main.setVisibility(View.VISIBLE);
                    String name_str = capitalizeWord(response.body().getPeaName());
                    txt_name.setText(name_str);
                    txt_id.setText("Ticket ID: SCDD"+response.body().getTickets_id());
                    txt_login_name.setText("Name: "+response.body().getUra_fname());
                    txt_login_id.setText("PZ Login ID: "+response.body().getUra_login_id());
                    txt_count.setText(response.body().getTickets_count());
                    txt_category.setText(response.body().getTickets_category());
                    txt_date.setText(response.body().getPeaDate());
                    txt_venue.setText(response.body().getPeaVenue());
                    txt_location.setText(response.body().getPeaLocation());
                    txt_amount.setText(response.body().getPeaPrice());
                  //  Glide.with(getActivity()).load(Constants.BASE_URL+response.body().getPea_ticket_selfi()).into(imageView);
                    getTracking(user_id,scan_id);
                    if (!response.body().getMessage().equalsIgnoreCase("Done")){
                        showAlert(response.body().getMessage());
                    }

                }else {
//                    layout_ticket.setVisibility(View.GONE);
//                    layout_animation.setVisibility(View.GONE);
                    startActivity(new Intent(getActivity(), EventDetailsActivity.class).putExtra("event_id",scan_id).putExtra("encoded","yes"));
                    dismiss();
//                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ScanEventQrResponse> call, Throwable t) {
                Log.e("getScanEventExp", ""+t);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private String capitalizeWord(String string){
        String[] words = string.split("\\s");
        StringBuilder capitalizeWord= new StringBuilder();
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterfirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }


    private void getTracking(String user_id,String scan_id){
        String user = null;
        String scan = null;
        byte[] tmp = Base64.decode(user_id, Base64.DEFAULT);
        byte[] tmp2 = Base64.decode(scan_id, Base64.DEFAULT);
        try {
            user = new String(tmp, "UTF-8");
            scan = new String(tmp2, "UTF-8");
            Log.e("user",""+user);
            Log.e("scan",""+scan);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        MyProgressDialog.show(getActivity(),"Loading...");
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EventTrackResponse> call = apiInterface.eventTracking("true",token,user,scan);
        call.enqueue(new Callback<EventTrackResponse>() {
            @Override
            public void onResponse(Call<EventTrackResponse> call, Response<EventTrackResponse> response) {
                Log.e("getTracking", new Gson().toJson(response));
                progressBar.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    List<TrackResult> trackResultList = response.body().getResults();
                    List<TrackData> dataList = null;
                    for (int i=0; i<trackResultList.size(); i++){
                        dataList = trackResultList.get(i).getTrackData();
                    }
                    
                    EventTrackingAdapter adapter = new EventTrackingAdapter(getActivity(),dataList);
                    recyclerTracking.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerTracking.setAdapter(adapter);
//                    scrollDown();
                }else {
                    Toast.makeText(getActivity(), ""+response.body().getStatus(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EventTrackResponse> call, Throwable t) {
                Log.e("getTracking", ""+t);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void showAlert(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Scan Alert!");
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    private void scrollDown() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

}
