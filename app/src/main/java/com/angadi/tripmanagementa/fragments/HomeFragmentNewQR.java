package com.angadi.tripmanagementa.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.utils.FlashlightProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class HomeFragmentNewQR extends Fragment{

    //    @BindView(R.id.flash)
//    ImageView flash;
    private static final String FLASH_STATE = "FLASH_STATE";
//    private ZXingScannerView mScannerView;
    private boolean mFlash = false;
//    @BindView(R.id.bcode)
//    BarcodeView barcodeView;
//    @BindView(R.id.fab_flash_on)
    FloatingActionButton fab_flash_on;
    @BindView(R.id.fab_flash_off)
    FloatingActionButton fab_flash_off;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_new_qr, container, false);
        ButterKnife.bind(this, view);

//        ViewGroup contentFrame = (ViewGroup) view.findViewById(R.id.content_frame);
//        mScannerView = new ZXingScannerView(getActivity());
//        contentFrame.addView(mScannerView);
//        mScannerView.setBorderColor(getActivity().getResources().getColor(R.color.colorAccent));

//        startInit();

        return view;
    }

//    private void startInit(){
//        List<BarcodeFormat> barcodeFormats = new ArrayList<>();
//        barcodeFormats.add(BarcodeFormat.QR_CODE);
//        barcodeFormats.add(BarcodeFormat.AZTEC);
//        barcodeView.setFormats(barcodeFormats);
//        barcodeView.setBarcodeResultListener(this);
//        barcodeView.bindToLifecycle(this);
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(FLASH_STATE, mFlash);
//    }

//    @Override
//    public void handleResult(Result rawResult) {
//        Log.e("rawResult", "" + rawResult);
//        try {
//            Uri data = Uri.parse(rawResult.getText());
//            if (data != null) {
//    //            rawResult: https://planetzoom.app/qr/MTQ=?qr_type=profile
//                String new_qr_id = String.valueOf(rawResult);
//                new_qr_id = new_qr_id.substring(new_qr_id.indexOf("/") + 20);
//                new_qr_id = new_qr_id.substring(0, new_qr_id.indexOf("?"));
//                Log.e("new_qr_id", new_qr_id);
//
//                String qr_type = data.getQueryParameter("qr_type");
//                String qr_user_id = data.getQueryParameter("qr_user_id");
//                Log.e("qr_type ", "" + qr_type);
//                Log.e("qr_user_id ", "" + qr_user_id);
//
//                showResultDialog(new_qr_id, qr_type, String.valueOf(data), qr_user_id);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // Note:
//        // * Wait 2 seconds to resume the preview.
//        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
//        // * I don't know why this is the case but I don't have the time to figure out.
////        Handler handler = new Handler();
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                mScannerView.resumeCameraPreview((ZXingScannerView.ResultHandler) getActivity());
////            }
////        }, 2000);
//    }

//    private void showResultDialog(String qr_code_id, String qr_code_type, String qr_url, String user_id) {
//        if (qr_code_type.equalsIgnoreCase("event")){
//            DialogFragment event_ticket = ScanEventDialogFragment.newInstance("Event Ticket", qr_code_id, qr_code_type, qr_url, user_id, this);
//            event_ticket.show(getActivity().getSupportFragmentManager(), "event_ticket");
//
//        }else if (qr_code_type.equalsIgnoreCase("user")){
//            DialogFragment user_profile = ScanProfileDialogFragment.newInstance("User Profile", qr_code_id, qr_code_type, qr_url, user_id, this);
//            user_profile.show(getActivity().getSupportFragmentManager(), "user_profile");
//
//        }else {
//            DialogFragment scan_results = ScanResultDialogFragment.newInstance(getActivity(),"Scan Results", qr_code_id, qr_code_type, qr_url, user_id, this);
//            scan_results.show(getActivity().getSupportFragmentManager(), "scan_results");
//
//        }
////        ScanResultDialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
////        fragment.show(getActivity().getSupportFragmentManager(), "scan_results");
//    }

//    @OnClick(R.id.flash)
//    public void toggleFlash(View v) {
//        mFlash = !mFlash;
//        mScannerView.setFlash(mFlash);
//    }

//    @Override
//    public void onDialogPositiveClick(ScanResultDialogFragment dialog) {
//        // Resume the camera
//        mScannerView.resumeCameraPreview(this);
//    }


//    @Override
//    public void onDialogPositiveClick(DialogFragment dialog) {
//      startInit();
//    }
//
//    @Override
//    public void onEventPositiveClick(DialogFragment dialog) {
//        startInit();
//    }
//
//    @Override
//    public void onProfilePositiveClick(DialogFragment dialog) {
//        startInit();
//    }
//
//    @Override
//    public boolean onBarcodeResult(@NotNull Result result) {
//        barcodeView.unbind();
//        Log.e("rawResult", "" + result);
//        try {
//            Uri data = Uri.parse(result.getText());
//            if (data != null) {
//                //            rawResult: https://planetzoom.app/qr/MTQ=?qr_type=profile
//                String new_qr_id = String.valueOf(result);
//                new_qr_id = new_qr_id.substring(new_qr_id.indexOf("/") + 20);
//                new_qr_id = new_qr_id.substring(0, new_qr_id.indexOf("?"));
//                Log.e("new_qr_id", new_qr_id);
//
//                String qr_type = data.getQueryParameter("qr_type");
//                String qr_user_id = data.getQueryParameter("qr_user_id");
//                Log.e("qr_type ", "" + qr_type);
//                Log.e("qr_user_id ", "" + qr_user_id);
//
//                showResultDialog(new_qr_id, qr_type, String.valueOf(data), qr_user_id);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public void onBarcodeScanCancelled() {
//
//    }

//    @Override
//    public void onDialogPositiveClick(DialogFragment dialog) {
//        mScannerView.resumeCameraPreview(this);
//    }

    @OnClick({R.id.fab_flash_on,R.id.fab_flash_off,R.id.fab_gallery})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fab_flash_on:
                fab_flash_on.setVisibility(View.GONE);
                fab_flash_off.setVisibility(View.VISIBLE);
                FlashlightProvider flashlightProvider = new FlashlightProvider(getActivity());
                flashlightProvider.turnFlashlightOn();
                break;
            case R.id.fab_flash_off:
                fab_flash_off.setVisibility(View.GONE);
                fab_flash_on.setVisibility(View.VISIBLE);
                FlashlightProvider flashlightProvider2 = new FlashlightProvider(getActivity());
                flashlightProvider2.turnFlashlightOff();
                break;
            case R.id.fab_gallery:
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1234);
                break;
        }
    }
}
