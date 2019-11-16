package com.angadi.tripmanagementa.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.ProfileResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubEventDetailsDialogFragment extends DialogFragment {

    public static final String TAG = "details_dialog";

    private Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edt_search;
    View loading;
    String title, desc, qr_code_type, token, qr_url, user_id,demo;
    private DetailsDialogListener mListener;
    View view;
    int position;
    @BindView(R.id.txt)
    TextView textView;
    @BindView(R.id.layout_desc)
    NestedScrollView layout_desc;
    @BindView(R.id.layout_image)
    LinearLayout layout_image;
    @BindView(R.id.img)
            ImageView imageView;

    double screenInches;
    BitMatrix result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    public static SubEventDetailsDialogFragment newInstance(String title, String desc, String demo,Integer position,DetailsDialogListener listener) {
        SubEventDetailsDialogFragment dialogFragment = new SubEventDetailsDialogFragment();
        dialogFragment.title = title;
        dialogFragment.desc = desc;
        dialogFragment.demo = demo;
        dialogFragment.position = position;
        dialogFragment.mListener = listener;
        return dialogFragment;
    }


    public interface DialogListener {
        void updateResult(View view, int position, String id, String name, Integer cost, Integer tax);
    }

    public interface DetailsDialogListener {
        public void onProfilePositiveClick(DialogFragment dialog);
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
          //  MyProgressDialog.show(getActivity(), "Loading...");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.subevent_details_fragment, container, false);
        ButterKnife.bind(this, view);
        toolbar = view.findViewById(R.id.toolbar);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        toolbar.setNavigationOnClickListener(v -> dismiss());
       // MyProgressDialog.dismiss();
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        try {
            token = Prefs.with(getActivity()).getString("token", "");
            Log.e("token", token);
            Log.e("title", title);
            Log.e("demo", demo);
            Log.e("position", ""+position);
            if (demo.equalsIgnoreCase("DEMO DAY 2019")){
               layout_image.setVisibility(View.VISIBLE);
               layout_desc.setVisibility(View.GONE);
               if (position==0){
                   imageView.setImageResource(R.drawable.demo_day_one);
               }else {
                   imageView.setImageResource(R.drawable.demo_day_two);
               }

            }else {
                layout_image.setVisibility(View.GONE);
                layout_desc.setVisibility(View.VISIBLE);
                textView.setText(""+desc);
            }


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
                if (mListener != null) {
                    mListener.onProfilePositiveClick(SubEventDetailsDialogFragment.this);
                }
                dismiss();
            }
        });


    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onProfilePositiveClick(SubEventDetailsDialogFragment.this);
        }
        dialog.dismiss();
    }

}
