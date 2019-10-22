package com.angadi.tripmanagementa.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.angadi.tripmanagementa.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CountHeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView textView;

    public CountHeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void render(String text){
        textView.setText(text);
    }
}