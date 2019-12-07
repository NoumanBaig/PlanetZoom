package com.angadi.tripmanagementa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.adapters.GridlayoutAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ECardFragment extends Fragment {

    @BindView(R.id.recyclerGrid)
    RecyclerView recyclerView;
    ArrayList<Integer> images_arr;
    ArrayList<String> names_arr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e_card, container, false);
        ButterKnife.bind(this, view);
        setGridAdapter();
        return view;
    }

    private void setGridAdapter() {
        images_arr = new ArrayList<Integer>();
        images_arr.add(R.drawable.ic_favorite);
        images_arr.add(R.drawable.ic_id_card);
        images_arr.add(R.drawable.ic_id_card);
        images_arr.add(R.drawable.ic_settings);

        names_arr = new ArrayList<String>();
        names_arr.add("Favorites");
        names_arr.add("My Card");
        names_arr.add("All E - Cards");
        names_arr.add("Settings");

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        GridlayoutAdapter adapter = new GridlayoutAdapter(getActivity(), images_arr, names_arr);
        recyclerView.setAdapter(adapter);
    }

}
