package com.angadi.tripmanagementa.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ProgressBar;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private String str_location,str_mobile,str_email,str_website,str_whatsApp,str_facebook,str_youtube,str_instagram,str_linkedin;
    String title,qr_code_id,qr_code_type,token,qr_url,user_id;
    private MessageDialogListener mListener;
    View view;
    @BindView(R.id.imageView)
    SimpleDraweeView imageView;
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
    @BindView(R.id.progress)
    ProgressBar progressBar;
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
//            MyProgressDialog.show(mContext,"Loading...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.scan_dialog_fragment, container, false);
        ButterKnife.bind(this, view);
        toolbar = view.findViewById(R.id.toolbar);

        progressBar.setVisibility(View.VISIBLE);
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
                progressBar.setVisibility(View.GONE);
                if (response.body().getStatus().equalsIgnoreCase("success")){
                   displayTexts(response);
                }else {
                     Toast.makeText(mContext, "Error while fetching data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QrScanResponse> call, Throwable t) {
                Log.e("scan_res", ""+t);
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void displayTexts(Response<QrScanResponse> response) {
        assert response.body() != null;

        if (!response.body().getQcaaName().equalsIgnoreCase("")) {
            txt_name.setText(response.body().getQcaaName());
        }
        if (!response.body().getQcaaCat().equalsIgnoreCase("")) {
            txt_cat.setText(response.body().getQcaaCat()+", "+response.body().getQcaaSubCat());
        }
        if (!response.body().getQcaaEmailId().equalsIgnoreCase("")) {
            txt_email.setText(response.body().getQcaaEmailId());
        }
        if (!response.body().getQcaaPlace().equalsIgnoreCase("")) {
            txt_address.setText(response.body().getQcaaPlace());
        }
        if (!response.body().getQcaaWebsite().equalsIgnoreCase("")) {
            txt_website.setText(response.body().getQcaaWebsite());
        }
        if (!response.body().getQcaaPhoneNo().equalsIgnoreCase("")) {
            txt_mobile.setText(response.body().getQcaaPhoneNo());
        }

        if (response.body().getQcaaProfileLogo().equalsIgnoreCase("NULL")){
            imageView.setImageResource(R.drawable.ic_placeholder);
        }
        else {
            imageView.setImageURI(Constants.BASE_URL + response.body().getQcaaProfileLogo());

        }
        str_mobile = response.body().getQcaaPhoneNo();
        str_location = response.body().getQcaaPlace();
        str_email = response.body().getQcaaEmailId();
        str_website = response.body().getQcaaWebsite();
        str_whatsApp = response.body().getQcaaSocialWhatsapp();
//        str_instagram = response.body().getUraInstagram();
        str_facebook = response.body().getQcaaSocialFacebook();
        str_youtube = response.body().getQcaaSocialYoutube();
//        str_linkedin = response.body().getUraLinkedin();

    }


    @OnClick({R.id.layout_fav, R.id.layout_like, R.id.layout_dislike, R.id.layout_share, R.id.layout_ratings, R.id.layout_mobile,
            R.id.layout_email, R.id.layout_website, R.id.layout_location, R.id.layout_whatsApp, R.id.layout_facebook, R.id.layout_youtube,
            R.id.layout_instagram, R.id.layout_linkedIn})
    public void onLayoutClick(View view) {
        switch (view.getId()) {
            case R.id.layout_fav:
                break;
            case R.id.layout_like:
                break;
            case R.id.layout_dislike:
                break;
            case R.id.layout_share:
                break;
            case R.id.layout_ratings:
                break;
            case R.id.layout_mobile:
                if (!str_mobile.equalsIgnoreCase("")){
                    callPhoneNumber(str_mobile);
                }
                break;
            case R.id.layout_email:
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + str_email)));
                break;
            case R.id.layout_website:
                openUri(str_website);
                break;
            case R.id.layout_location:
                if (!str_location.equalsIgnoreCase("")){
                    setLocation(str_location);
                }
                break;
            case R.id.layout_whatsApp:
                if (!str_whatsApp.equalsIgnoreCase("")){
                    whatsApp(str_whatsApp);
                }
                break;
            case R.id.layout_facebook:
                openUri(str_facebook);
                break;
            case R.id.layout_youtube:
                openUri(str_youtube);
                break;
            case R.id.layout_instagram:
                openUri(str_instagram);
                break;
            case R.id.layout_linkedIn:
                openUri(str_linkedin);
                break;
            default:
                break;
        }
    }

    private void callPhoneNumber(String phoneNumber) {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:"+phoneNumber));
                            startActivity(intent);
//                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel:", phoneNumber, null));
//                            startActivity(intent);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void setLocation(String address) {
        String map = "http://maps.google.co.in/maps?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
    }

    private void whatsApp(String number) {
        PackageManager pm = getActivity().getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);

        if (isInstalled) {
            // Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            // startActivity(launchIntent);


            Intent i = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + URLEncoder.encode("", "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(pm) != null) {
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity(), "WhatsApp is not installed in this device", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {

        boolean found = true;

        try {

            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = false;
        }

        return found;
    }

    private void openUri(String uri){
//        String uri = WebsiteFromList;
        if (uri != "") {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+uri));
            startActivity(intent);
        }
    }
}
