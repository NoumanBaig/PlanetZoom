package com.angadi.tripmanagementa.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountFooterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView textView;

    public CountFooterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void render(String text){
        textView.setText(text);
    }
}