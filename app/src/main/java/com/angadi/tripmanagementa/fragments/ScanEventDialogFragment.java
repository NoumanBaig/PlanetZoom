package com.angadi.tripmanagementa.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;

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
    @BindView(R.id.txt_venue)
    TextView txt_venue;
    @BindView(R.id.txt_location)
    TextView txt_location;
    @BindView(R.id.txt_amount)
    TextView txt_amount;

    @BindView(R.id.layout_ticket)
    LinearLayout layout_ticket;
    @BindView(R.id.layout_animation)
    LinearLayout layout_animation;


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
            MyProgressDialog.show(getActivity(), "Loading...");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.scan_event_fragment, container, false);
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
        MyProgressDialog.show(getActivity(),"Loading...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ScanEventQrResponse> call = apiInterface.scanEventQr("true",token,user_id,scan_id);
        call.enqueue(new Callback<ScanEventQrResponse>() {
            @Override
            public void onResponse(Call<ScanEventQrResponse> call, Response<ScanEventQrResponse> response) {
                Log.e("getScanEventResult", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    layout_ticket.setVisibility(View.VISIBLE);
                    layout_animation.setVisibility(View.GONE);
                    txt_name.setText(response.body().getPeaName());
                    txt_date.setText(response.body().getPeaDate());
                    txt_venue.setText(response.body().getPeaVenue());
                    txt_location.setText(response.body().getPeaLocation());
                    txt_amount.setText(response.body().getPeaPrice());
                    Picasso.get().load(Constants.BASE_URL+response.body().getPea_ticket_selfi()).into(imageView);
                }else {
                    layout_ticket.setVisibility(View.GONE);
                    layout_animation.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ScanEventQrResponse> call, Throwable t) {
                Log.e("getScanEventExp", ""+t);
               MyProgressDialog.dismiss();
            }
        });
    }

    private void showQrCode(String str_qr_id){
        try {
            Bitmap bitmap = encodeAsBitmap(str_qr_id);
           // img_qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap encodeAsBitmap(String list) throws WriterException {
        Log.e("-----------------", String.valueOf(screenInches));

        try {
            Log.e("screenInches---->", String.valueOf(screenInches));
            if (screenInches <= 5.2) {
                Log.e("first", "first");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 600, 600, null);
            } else if (screenInches >= 5.21 && screenInches <= 5.3) {
                Log.e("second", "second");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);

            } else if (screenInches >= 5.31 && screenInches <= 5.5) {
                Log.e("second1", "second1");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);

            } else if (screenInches >= 5.6 && screenInches <= 5.99) {
                Log.e("third", "third");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);

            } else if (screenInches >= 6.1 && screenInches <= 6.5) {
                Log.e("Fourth", "Fourth");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 760, 760, null);

            } else {
                Log.e("else", "else");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);
            }

        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

}
