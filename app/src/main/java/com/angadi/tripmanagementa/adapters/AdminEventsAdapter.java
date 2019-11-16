package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.AdminEventResult;
import com.angadi.tripmanagementa.models.AllEventsResult;
import com.angadi.tripmanagementa.utils.Constants;
import com.bumptech.glide.Glide;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AdminEventsAdapter extends RecyclerView.Adapter<AdminEventsAdapter.MyViewHolder> {

    Context mContext;
    List<AdminEventResult> resultList;
    AdminEventClickListener adminEventClickListener;

    public AdminEventsAdapter(Context context, List<AdminEventResult> resultList) {
        super();
        this.mContext = context;
        this.resultList = resultList;
    }

    public interface AdminEventClickListener {
        void onClick(View view, int position, String id);
    }

    public void setClickListener(AdminEventClickListener adminEventClickListener) {
        this.adminEventClickListener = adminEventClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_admin_events, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        substring(0, 1).toUpperCase() + name.substring(1)
        String name_str = capitalizeWord(resultList.get(position).getPeaName());
        holder.txt_title.setText(name_str);
        holder.txt_date.setText(resultList.get(position).getPeaDate());
        holder.txt_location.setText(resultList.get(position).getPeaVenue());
//        if (resultList.get(position).getPea_logo().equalsIgnoreCase("NULL")) {
//            Glide.with(mContext).load(R.drawable.organise_event)
//                    .into(holder.imageView);
//        } else {
//            Glide.with(mContext).load(Constants.BASE_URL + resultList.get(position).getPea_logo()).into(holder.imageView);
//        }
//        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.imageView.setImageURI(Constants.BASE_URL + resultList.get(position).getPea_logo());

//        holder.imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminEventClickListener != null) {
                    adminEventClickListener.onClick(view, position, resultList.get(position).getPeaId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title, txt_date, txt_location;
        SimpleDraweeView imageView;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_location = itemView.findViewById(R.id.txt_location);
            imageView = itemView.findViewById(R.id.img);
        }
    }

    private String capitalizeWord(String string){
        String[] words = string.split("\\s");
        StringBuilder capitalizeWord= new StringBuilder();
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterfirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }
}
