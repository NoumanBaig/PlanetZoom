package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.AllECardsActivity;
import com.angadi.tripmanagementa.activities.FavoritesActivity;
import com.angadi.tripmanagementa.activities.ProfileActivity;
import com.angadi.tripmanagementa.activities.SettingsActivity;
import com.angadi.tripmanagementa.models.Influencers;
import com.angadi.tripmanagementa.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GridlayoutAdapter extends RecyclerView.Adapter<GridlayoutAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Integer> images;
    ArrayList<String> names;
    ClickListener clickListener;
    int row_index = -1;

    public GridlayoutAdapter(Context context, ArrayList<Integer> images, ArrayList<String> names) {
        super();
        this.mContext = context;
        this.images = images;
        this.names = names;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_grid_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mContext).load(images.get(position)).into(holder.imageView);
        holder.textView.setText(names.get(position));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
                if (position==0){
                    mContext.startActivity(new Intent(mContext, FavoritesActivity.class));
                }else if (position==1){
                    mContext.startActivity(new Intent(mContext, ProfileActivity.class));
                }else if (position==2){
                    mContext.startActivity(new Intent(mContext, AllECardsActivity.class));
                }else if (position==3){
                    mContext.startActivity(new Intent(mContext, SettingsActivity.class));
                }
            }
        });

        if (row_index == position) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.black));
        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView textView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.linear);
            textView = itemView.findViewById(R.id.txt);
            imageView = itemView.findViewById(R.id.img);
        }
    }
}
