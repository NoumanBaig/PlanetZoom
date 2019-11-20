package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.Influencers;
import com.angadi.tripmanagementa.models.Stalls;
import com.angadi.tripmanagementa.utils.Constants;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class StallsAdapter extends RecyclerView.Adapter<StallsAdapter.MyViewHolder> {

    Context mContext;
    List<Stalls> stallsList;
    ClickListener clickListener;

    public StallsAdapter(Context context, List<Stalls> stallsList) {
        super();
        this.mContext = context;
        this.stallsList = stallsList;
    }

    public interface ClickListener {
        void onClick(View view, int position, String url);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_stalls, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        holder.imageView.setImageURI(Constants.BASE_URL+stallsList.get(position).getImage());
        Glide.with(mContext).load(Constants.BASE_URL+stallsList.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(stallsList.get(position).getName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(view, position,stallsList.get(position).getWeb());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return stallsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        LinearLayout layout;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.linear);
            imageView = itemView.findViewById(R.id.img_logo);
            textView = itemView.findViewById(R.id.txt);

        }
    }
}
