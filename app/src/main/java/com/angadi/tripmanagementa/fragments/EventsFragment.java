package com.angadi.tripmanagementa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.ShowEventsActivity;
import com.angadi.tripmanagementa.utils.Constants;
import com.angadi.tripmanagementa.utils.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

//    @BindView(R.id.loading_layout)
//    View loadingIndicator;
    @BindView(R.id.layout_both)
    LinearLayout layout_both;
    @BindView(R.id.layout_one)
    LinearLayout layout_one;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, view);

        if (Prefs.with(getActivity()).getString("organiser","").equalsIgnoreCase("true")){
            layout_both.setVisibility(View.VISIBLE);
            layout_one.setVisibility(View.GONE);
        }else {
            layout_both.setVisibility(View.GONE);
            layout_one.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @OnClick({R.id.layout_explore,R.id.layout_organise,R.id.layout_explorer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_explore:
                startActivity(new Intent(getActivity(), ShowEventsActivity.class).putExtra("events","explorer"));
                break;
            case R.id.layout_organise:
                startActivity(new Intent(getActivity(), ShowEventsActivity.class).putExtra("events","organiser"));
                break;
            case R.id.layout_explorer:
                startActivity(new Intent(getActivity(), ShowEventsActivity.class).putExtra("events","explorer"));
                break;
            default:
                break;
        }
    }

}
