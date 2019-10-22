package com.angadi.tripmanagementa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.angadi.tripmanagementa.R;
import com.google.zxing.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class HomeFragment extends Fragment implements ZXingScannerView.ResultHandler,MessageDialogFragment.MessageDialogListener{

//    @BindView(R.id.flash)
//    ImageView flash;
    private static final String FLASH_STATE = "FLASH_STATE";
    private ZXingScannerView mScannerView;
    private boolean mFlash;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        ViewGroup contentFrame = (ViewGroup) view.findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(getActivity());
        contentFrame.addView(mScannerView);
        mScannerView.setBorderColor(getActivity().getResources().getColor(R.color.colorAccent));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        // You can optionally set aspect ratio tolerance level
        // that is used in calculating the optimal Camera preview size
       // mScannerView.setAspectTolerance(0.2f);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
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
        showMessageDialog("Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString());

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

    public void showMessageDialog(String message) {
        DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
        fragment.show(getActivity().getSupportFragmentManager(), "scan_results");
    }

//    @OnClick(R.id.flash)
//    public void toggleFlash(View v) {
//        mFlash = !mFlash;
//        mScannerView.setFlash(mFlash);
//    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }
}
