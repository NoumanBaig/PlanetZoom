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
import com.angadi.tripmanagementa.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyTicketsAdapter extends RecyclerView.Adapter<MyTicketsAdapter.MyViewHolder> {

    Context mContext;
//    List<AllEventsResult> resultList;
    TicketClickListener ticketClickListener;

    public MyTicketsAdapter(Context context) {
        super();
        this.mContext = context;
//        this.resultList = resultList;
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

//        holder.txt_title.setText(resultList.get(position).getPeaName());
//        holder.txt_date.setText(resultList.get(position).getPeaDate());
//        holder.txt_location.setText(resultList.get(position).getPeaVenue());
//        if (resultList.get(position).getPea_logo().equalsIgnoreCase("NULL")) {
//            Picasso.get().load(R.drawable.organise_event)
//                    .into(holder.imageView);
//        } else {
//            Picasso.get().load(Constants.BASE_URL + resultList.get(position).getPea_logo()).into(holder.imageView);
//        }
//
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ticketClickListener != null) {
                    ticketClickListener.onClick(view, position,"1");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title, txt_date, txt_location;
        ImageView imageView;
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
}
