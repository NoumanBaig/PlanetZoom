package com.angadi.tripmanagementa.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.ProfileResponse;
import com.angadi.tripmanagementa.models.QrScanResponse;
import com.angadi.tripmanagementa.models.ScanEventQrResponse;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ScanResultDialogFragment extends DialogFragment {

    public static final String TAG = "example_dialog";

    private Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edt_search;
    View loading;
    String title,qr_code_id,qr_code_type,token,qr_url,user_id;
    private MessageDialogListener mListener;
    View view;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.img_qr_code)
    ImageView img_qr_code;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_cat)
    TextView txt_cat;
    @BindView(R.id.txt_mobile)
    TextView txt_mobile;
    @BindView(R.id.txt_email)
    TextView txt_email;
    @BindView(R.id.txt_website)
    TextView txt_website;
    @BindView(R.id.txt_address)
    TextView txt_address;
    double screenInches;
    BitMatrix result;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(androidx.fragment.app.DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    public static ScanResultDialogFragment newInstance(Context context,String title, String qr_code_id,String qr_code_type,String qr_url,String user_id, MessageDialogListener listener) {
        ScanResultDialogFragment dialogFragment = new ScanResultDialogFragment();
        dialogFragment.mContext=context;
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

    public interface MessageDialogListener {
        public void onDialogPositiveClick(androidx.fragment.app.DialogFragment dialog);
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
            MyProgressDialog.show(mContext,"Loading...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.scan_dialog_fragment, container, false);
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
        try {
            token = Prefs.with(mContext).getString("token","");
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
                    mListener.onDialogPositiveClick(ScanResultDialogFragment.this);
                }
                dismiss();
            }
        });

        getScanResult(qr_code_id);

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mListener != null) {
            mListener.onDialogPositiveClick(ScanResultDialogFragment.this);
        }
        dialog.dismiss();
    }

    private void getScanResult(String id){

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<QrScanResponse> responseCall = apiInterface.scanResult("true",id,token);
        responseCall.enqueue(new Callback<QrScanResponse>() {
            @Override
            public void onResponse(Call<QrScanResponse> call, Response<QrScanResponse> response) {
                Log.e("scan_res", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    String name = response.body().getQcaaName();
                    String category = response.body().getQcaaCat();
                    String subCat = response.body().getQcaaSubCat();
                    String emailId = response.body().getQcaaEmailId();
                    String phoneNo = response.body().getQcaaPhoneNo();
                    String website = response.body().getQcaaWebsite();
                    String place = response.body().getQcaaPlace();
                    String logo = response.body().getQcaaProfileLogo();

                    try {
                        Glide.with(getActivity()).load(Constants.BASE_URL+logo).into(imageView);
                        txt_name.setText(name);
                        txt_cat.setText(category+", "+subCat);
                        txt_email.setText(emailId);
                        txt_mobile.setText(phoneNo);
                        txt_website.setText(website);
                        txt_address.setText(place);
                        //showQrCode(qr_url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                     Toast.makeText(mContext, "Error while fetching data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QrScanResponse> call, Throwable t) {
                Log.e("scan_res", ""+t);
               MyProgressDialog.dismiss();
            }
        });
    }





}
