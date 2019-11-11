package com.angadi.tripmanagementa.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.angadi.tripmanagementa.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class HomeFragment extends Fragment implements ZXingScannerView.ResultHandler,
        ScanResultDialogFragment.MessageDialogListener,ScanEventDialogFragment.EventDialogListener,ScanProfileDialogFragment.ProfileDialogListener {

    //    @BindView(R.id.flash)
//    ImageView flash;
    private static final String FLASH_STATE = "FLASH_STATE";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    @BindView(R.id.fab_flash)
    FloatingActionButton fab_flash;
    @BindView(R.id.txt_live)
    TextView txt_live;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        ViewGroup contentFrame = (ViewGroup) view.findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(getActivity());
        contentFrame.addView(mScannerView);
        mScannerView.setBorderColor(getActivity().getResources().getColor(R.color.colorAccent));
        txt_live.setText(getResources().getString(R.string.live_msg));
        txt_live.setSelected(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        // You can optionally set aspect ratio tolerance level
        // that is used in calculating the optimal Camera preview size
        mScannerView.startCamera();
      //  mScannerView.setFlash(mFlash);


    }

    @OnClick(R.id.fab_flash)
    void onFlashClick(){
        mFlash = !mFlash;
        mScannerView.setFlash(mFlash);
        if (mFlash){
            fab_flash.setImageResource(R.drawable.ic_flash_on);
        }else {
            fab_flash.setImageResource(R.drawable.ic_flash_off);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.e("rawResult", "" + rawResult);
        try {
            Uri data = Uri.parse(rawResult.getText());
            if (data != null) {
    //            rawResult: https://planetzoom.app/qr/MTQ=?qr_type=profile
                String new_qr_id = String.valueOf(rawResult);
                new_qr_id = new_qr_id.substring(new_qr_id.indexOf("/") + 20);
                new_qr_id = new_qr_id.substring(0, new_qr_id.indexOf("?"));
                Log.e("new_qr_id", new_qr_id);

                String qr_type = data.getQueryParameter("qr_type");
                String qr_user_id = data.getQueryParameter("qr_user_id");
                Log.e("qr_type ", "" + qr_type);
                Log.e("qr_user_id ", "" + qr_user_id);

                showResultDialog(new_qr_id, qr_type, String.valueOf(data), qr_user_id);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.resumeCameraPreview((ZXingScannerView.ResultHandler) getActivity());
//            }
//        }, 2000);
    }

    private void showResultDialog(String qr_code_id, String qr_code_type, String qr_url, String user_id) {
        if (qr_code_type.equalsIgnoreCase("event")){
            DialogFragment event_ticket = ScanEventDialogFragment.newInstance("Event Ticket", qr_code_id, qr_code_type, qr_url, user_id, this);
            event_ticket.show(getActivity().getSupportFragmentManager(), "event_ticket");

        }else if (qr_code_type.equalsIgnoreCase("user")){
            DialogFragment user_profile = ScanProfileDialogFragment.newInstance("User Profile", qr_code_id, qr_code_type, qr_url, user_id, this);
            user_profile.show(getActivity().getSupportFragmentManager(), "user_profile");

        }else {
            DialogFragment scan_results = ScanResultDialogFragment.newInstance(getActivity(),"Scan Results", qr_code_id, qr_code_type, qr_url, user_id, this);
            scan_results.show(getActivity().getSupportFragmentManager(), "scan_results");

        }
//        ScanResultDialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
//        fragment.show(getActivity().getSupportFragmentManager(), "scan_results");
    }

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


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onEventPositiveClick(DialogFragment dialog) {
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onProfilePositiveClick(DialogFragment dialog) {
        mScannerView.resumeCameraPreview(this);
    }

//    @Override
//    public void onDialogPositiveClick(DialogFragment dialog) {
//        mScannerView.resumeCameraPreview(this);
//    }
}
