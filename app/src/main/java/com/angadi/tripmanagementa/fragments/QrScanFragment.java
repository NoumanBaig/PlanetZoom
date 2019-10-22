package com.angadi.tripmanagementa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.HomeActivity;
import com.angadi.tripmanagementa.adapters.CountSectionAdapter;
import com.angadi.tripmanagementa.models.DashboardResponse;
import com.angadi.tripmanagementa.models.DashboardResult;
import com.angadi.tripmanagementa.models.QrScanResponse;
import com.angadi.tripmanagementa.rest.ApiClient;
import com.angadi.tripmanagementa.rest.ApiInterface;
import com.angadi.tripmanagementa.utils.Prefs;
import com.google.gson.Gson;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrScanFragment extends Fragment {

    @BindView(R.id.txt)
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scan,container,false);
        ButterKnife.bind(this,view);

   

        return view;
    }



}
