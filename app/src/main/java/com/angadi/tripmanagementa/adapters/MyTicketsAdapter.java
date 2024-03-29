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
import com.angadi.tripmanagementa.models.AllEventsResult;
import com.angadi.tripmanagementa.models.MyTicketsResult;
import com.angadi.tripmanagementa.utils.Constants;
import com.bumptech.glide.Glide;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MyTicketsAdapter extends RecyclerView.Adapter<MyTicketsAdapter.MyViewHolder> {

    Context mContext;
    List<MyTicketsResult> resultList;
    TicketClickListener ticketClickListener;

    public MyTicketsAdapter(Context context, List<MyTicketsResult> resultList) {
        super();
        this.mContext = context;
        this.resultList = resultList;
    }

    public interface TicketClickListener {
        void onClick(View view, int position, String id);
    }

    public void setClickListener(TicketClickListener ticketClickListener) {
        this.ticketClickListener = ticketClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_events, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String name_str = capitalizeWord(resultList.get(position).getPeaName());
        holder.txt_title.setText(name_str);
        holder.txt_date.setText(resultList.get(position).getPeaDate());
        holder.txt_location.setText(resultList.get(position).getPeaVenue());
//        if (resultList.get(position).getPeaLogo().equalsIgnoreCase("NULL")) {
//            Glide.with(mContext).load(R.drawable.explore_event)
//                    .into(holder.imageView);
//        } else {
        holder.imageView.setImageURI(Constants.BASE_URL + resultList.get(position).getPeaLogo());
//        holder.imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
//        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ticketClickListener != null) {
                    ticketClickListener.onClick(view, position, resultList.get(position).getPeaId());
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
