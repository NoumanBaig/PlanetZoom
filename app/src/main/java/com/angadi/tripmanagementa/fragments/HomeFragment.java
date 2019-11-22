package com.angadi.tripmanagementa.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.CreateEventActivity;
import com.angadi.tripmanagementa.activities.HomeActivity;
import com.angadi.tripmanagementa.activities.LoginActivity;
import com.angadi.tripmanagementa.models.AddUpdateResponse;
import com.angadi.tripmanagementa.models.AuthCheckResponse;
import com.angadi.tripmanagementa.models.GetUpdateResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ZXingScannerView.ResultHandler,
        ScanResultDialogFragment.MessageDialogListener, ScanEventDialogFragment.EventDialogListener, ScanProfileDialogFragment.ProfileDialogListener {

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
        mScannerView.setFocusable(true);
        mScannerView.setBorderColor(getActivity().getResources().getColor(R.color.colorAccent));

//        String event_id = Prefs.with(getActivity()).getString("live_message", "");
//        if (!event_id.equalsIgnoreCase("")) {
            getLiveEvent("10");
            authCheck();
//        }
//        else {
//            txt_live.setVisibility(View.GONE);
//        }
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
    void onFlashClick() {
        mFlash = !mFlash;
        mScannerView.setFlash(mFlash);
        if (mFlash) {
            fab_flash.setImageResource(R.drawable.ic_flash_on);
        } else {
            fab_flash.setImageResource(R.drawable.ic_flash_off);
        }
    }

    @OnClick(R.id.fab_gallery)
    void onGalleryClick() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Log.e("permission", "granted");
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1234);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Log.e("permission", "denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


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
                String qr_type = data.getQueryParameter("qr_type");
                String qr_user_id = data.getQueryParameter("qr_user_id");
                Log.e("qr_type ", "" + qr_type);
                Log.e("qr_user_id ", "" + qr_user_id);
                String new_qr_id = String.valueOf(rawResult);
//                if (Constants.BASE_URL.equalsIgnoreCase("https://test.planetzoom.app/")) {
//                    Log.e("dev", "---->");
//                    Log.e("result_length", "" + new_qr_id.length());
//                    int count  = countNumOfChars(new_qr_id);
//                    Log.e("result_count", "" + count);
//                    if (new_qr_id.length() > 47) {
//                        new_qr_id = new_qr_id.substring(new_qr_id.indexOf("/") + 25);
//                        new_qr_id = new_qr_id.substring(0, new_qr_id.indexOf("?"));
//                        showResultDialog(new_qr_id, qr_type, String.valueOf(rawResult), qr_user_id);
//                    } else {
//                        Toast.makeText(getActivity(), "Invalid Test QR", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Log.e("pro", "---->");
//                    Log.e("result_length", "" + new_qr_id.length());
//                    int count  = countNumOfChars(new_qr_id);
//                    Log.e("result_count", "" + count);
//                    if (new_qr_id.length() > 47) {
//                        Toast.makeText(getActivity(), "Invalid Live QR", Toast.LENGTH_SHORT).show();
//                    } else {
//                        new_qr_id = new_qr_id.substring(new_qr_id.indexOf("/") + 20);
//                        new_qr_id = new_qr_id.substring(0, new_qr_id.indexOf("?"));
//                        showResultDialog(new_qr_id, qr_type, String.valueOf(rawResult), qr_user_id);
//                    }
//                }
//                Log.e("result_length", "" + new_qr_id.length());
//                int count  = countNumOfChars(new_qr_id);
//                Log.e("result_count", "" + count);
                if (!new_qr_id.contains("planetzoom")) {
                    showAlert(new_qr_id);
                }
                Log.e("result_length", "" + new_qr_id.length());
                new_qr_id = new_qr_id.substring(new_qr_id.indexOf("/") + 20);
                new_qr_id = new_qr_id.substring(0, new_qr_id.indexOf("?"));
                Log.e("new_qr_id", new_qr_id);
                showResultDialog(new_qr_id, qr_type, String.valueOf(rawResult), qr_user_id);
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


    private void showAlert(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Looks like its not a PlanetZoom QR");
        alert.setMessage("Scanned Content:  "+message);
        alert.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                clearScanner();
            }
        });
        alert.show();
    }

    private void showResultDialog(String qr_code_id, String qr_code_type, String qr_url, String user_id) {
        if (qr_code_type.equalsIgnoreCase("event")) {
            DialogFragment event_ticket = ScanEventDialogFragment.newInstance("Event Ticket", qr_code_id, qr_code_type, qr_url, user_id, this);
            event_ticket.show(getActivity().getSupportFragmentManager(), "event_ticket");

        } else if (qr_code_type.equalsIgnoreCase("user")) {
            DialogFragment user_profile = ScanProfileDialogFragment.newInstance("User Profile", qr_code_id, qr_code_type, qr_url, user_id, this);
            user_profile.show(getActivity().getSupportFragmentManager(), "user_profile");

        } else {
            DialogFragment scan_results = ScanResultDialogFragment.newInstance(getActivity(), "Scan Results", qr_code_id, qr_code_type, qr_url, user_id, this);
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

    private void clearScanner() {
        mScannerView.resumeCameraPreview(this);
    }

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


    private void getLiveEvent(String event_id) {
//        MyProgressDialog.show(CreateEventActivity.this,"Loading...");
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        Log.e("event_id", event_id);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetUpdateResponse> call = apiInterface.getEventLiveUpdate("true", token, event_id);
        call.enqueue(new Callback<GetUpdateResponse>() {
            @Override
            public void onResponse(Call<GetUpdateResponse> call, Response<GetUpdateResponse> response) {
                Log.e("getLiveEvent", new Gson().toJson(response));
//                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        Log.e("success", "live----->");
                       // txt_live.setVisibility(View.VISIBLE);
                        txt_live.setText(response.body().getLiveMsg());
                        txt_live.setSelected(true);
                    } else {
                      //  txt_live.setVisibility(View.GONE);
                        Log.e("failure", "live----->");
                        txt_live.setText("Thank you for downloading PlanetZoom");
                        txt_live.setSelected(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetUpdateResponse> call, Throwable t) {
                Log.e("getLiveEvent", "" + t);
//                MyProgressDialog.dismiss();
            }
        });
    }

    private void authCheck() {
        String token = Prefs.with(getActivity()).getString("token", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AuthCheckResponse> call = apiInterface.authCheck("true", token);
        call.enqueue(new Callback<AuthCheckResponse>() {
            @Override
            public void onResponse(Call<AuthCheckResponse> call, Response<AuthCheckResponse> response) {
                Log.e("authCheck", new Gson().toJson(response));
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Log.e("success", response.body().getMessage());
                } else {
                    Log.e("failure", response.body().getMessage());
                    Prefs.with(getActivity()).remove();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }

            }

            @Override
            public void onFailure(Call<AuthCheckResponse> call, Throwable t) {
                Log.e("authCheck", "" + t);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1234) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    try {
                        Bitmap bMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), intent.getData());

                      //  String contents = null;

//                        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
//                        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
//                        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
//                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//                        Barcode data = new Barcode();

                        Frame frame = new Frame.Builder().setBitmap(bMap).build();
                        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getActivity())
                                .build();
                        if (barcodeDetector.isOperational()) {
                            SparseArray<Barcode> sparseArray = barcodeDetector.detect(frame);
                            if (sparseArray != null && sparseArray.size() > 0) {
                                for (int i = 0; i < sparseArray.size(); i++) {
                                    Log.e("LOG_TAG", "Value: " + sparseArray.valueAt(i).rawValue);
                                   // onObjectDetected(sparseArray.valueAt(i));
                                    Uri data = Uri.parse(sparseArray.valueAt(i).rawValue);
                                    if (data != null) {
                                        String qr_type = data.getQueryParameter("qr_type");
                                        String qr_user_id = data.getQueryParameter("qr_user_id");
                                        Log.e("qr_type ", "" + qr_type);
                                        Log.e("qr_user_id ", "" + qr_user_id);
                                        String new_qr_id = sparseArray.valueAt(i).rawValue;
                                        Log.e("result_length", "" + new_qr_id.length());
                                        Log.e("new_qr_id", new_qr_id);
                                        if (!new_qr_id.contains("planetzoom.app")) {
                                            showAlert(new_qr_id);
                                        }else {
                                            if (new_qr_id.contains("test")){
                                                Log.e("dev","environment-->");
                                            }else {
                                                new_qr_id = new_qr_id.substring(new_qr_id.indexOf("/") + 20);
                                                new_qr_id = new_qr_id.substring(0, new_qr_id.indexOf("?"));

                                                showResultDialog(new_qr_id, qr_type, sparseArray.valueAt(i).rawValue, qr_user_id);
                                            }

                                        }

                                    }
                                }
                            } else {
                                Log.e("LOG_TAG", "SparseArray null or empty");

                            }

                        } else {
                            Log.e("LOG_TAG", "Detector dependencies are not yet downloaded");
                        }

                    } catch (IOException e) {
                        Log.e("IOException","--->"+e);
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
