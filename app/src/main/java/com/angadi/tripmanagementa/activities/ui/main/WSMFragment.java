package com.angadi.tripmanagementa.activities.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.ScanResultActivity;
import com.angadi.tripmanagementa.adapters.SBMAdapter;
import com.angadi.tripmanagementa.fragments.ScanProfileDialogFragment;
import com.angadi.tripmanagementa.fragments.ScanResultDialogFragment;
import com.angadi.tripmanagementa.models.QRHisData;
import com.angadi.tripmanagementa.models.QRHisResult;
import com.angadi.tripmanagementa.models.QRHistoryResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.MyProgressDialog;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class WSMFragment extends Fragment implements ScanResultDialogFragment.MessageDialogListener, ScanProfileDialogFragment.ProfileDialogListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    @BindView(R.id.recyclerViewWSM)
    RecyclerView recyclerView;
    @BindView(R.id.layout_not_found)
    LinearLayout layout_not_found;
    String string;
//    public static WSMFragment newInstance(int index) {
//        WSMFragment fragment = new WSMFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_SECTION_NUMBER, index);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        pageViewModel.setIndex(index);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_wsm, container, false);
        ButterKnife.bind(this, root);
//        final TextView textView = root.findViewById(R.id.section_label);
        string = Prefs.with(getActivity()).getString("scan_history", "");
        if (string.equalsIgnoreCase("dashboard")) {
            getHistory();
        } else {
            getProfileHistory();
        }


        return root;
    }

    private void getHistory() {
        MyProgressDialog.show(getActivity(), "Loading...");
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<QRHistoryResponse> responseCall = apiInterface.getScanHistory("true", token, "1", "0");
        responseCall.enqueue(new Callback<QRHistoryResponse>() {
            @Override
            public void onResponse(Call<QRHistoryResponse> call, Response<QRHistoryResponse> response) {
                Log.e("wsm_history", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        layout_not_found.setVisibility(View.GONE);
                        List<QRHisResult> results = response.body().getResults();
                        List<QRHisData> dataList = results.get(0).getScanData();
                        setAdapter(dataList);
                    } else {
                        layout_not_found.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<QRHistoryResponse> call, Throwable t) {
                Log.e("wsm_history", "" + t);
                MyProgressDialog.dismiss();
                layout_not_found.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getProfileHistory() {
        MyProgressDialog.show(getActivity(), "Loading...");
        String token = Prefs.with(getActivity()).getString("token", "");
        Log.e("token", token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<QRHistoryResponse> responseCall = apiInterface.getProfileScanHistory("true", token, "1", "0");
        responseCall.enqueue(new Callback<QRHistoryResponse>() {
            @Override
            public void onResponse(Call<QRHistoryResponse> call, Response<QRHistoryResponse> response) {
                Log.e("wsm_history", new Gson().toJson(response));
                MyProgressDialog.dismiss();
                try {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        layout_not_found.setVisibility(View.GONE);
                        List<QRHisResult> results = response.body().getResults();
                        List<QRHisData> dataList = results.get(0).getScanData();
                        setAdapter(dataList);
                    } else {
                        layout_not_found.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<QRHistoryResponse> call, Throwable t) {
                Log.e("wsm_history", "" + t);
                MyProgressDialog.dismiss();
                layout_not_found.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setAdapter(List<QRHisData> dataList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SBMAdapter adapter = new SBMAdapter(getActivity(), dataList, string);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new SBMAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position, String id) {
                Log.e("id--->", id);
                byte[] data = new byte[0];
                try {
                    data = id.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                if (string.equalsIgnoreCase("dashboard")) {
                    onBizClick(base64);
                } else {
                    onProfileClick(base64);
                }
            }
        });
    }

    private void onProfileClick(String base64) {
        DialogFragment user_profile = ScanProfileDialogFragment.newInstance("User Profile", base64, "", "", "", this);
        user_profile.show(getActivity().getSupportFragmentManager(), "user_profile");
    }

    private void onBizClick(String base64) {
        DialogFragment scan_results = ScanResultDialogFragment.newInstance(getActivity(), "Scan Results", base64, "", "", "", this);
        scan_results.show(getActivity().getSupportFragmentManager(), "scan_results");
    }

    @Override
    public void onProfilePositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }
}