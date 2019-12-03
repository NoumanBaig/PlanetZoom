package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.Stalls;
import com.angadi.tripmanagementa.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventDashboardAdapter extends RecyclerView.Adapter<EventDashboardAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<String> arrayList,arrayListNames;
    ClickListener clickListener;
    List<String> colors;

    public EventDashboardAdapter(Context context, ArrayList<String> arrayList,ArrayList<String> arrayListNames) {
        super();
        this.mContext = context;
        this.arrayList = arrayList;
        this.arrayListNames = arrayListNames;
    }

    public interface ClickListener {
        void onClick(View view, int position, String name);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_event_dashboard, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Random r = new Random();
        int red=r.nextInt(255 - 0 + 1)+0;
        int green=r.nextInt(255 - 0 + 1)+0;
        int blue=r.nextInt(255 - 0 + 1)+0;

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.RECTANGLE);
        draw.setColor(Color.rgb(red,green,blue));
        holder.layout.setBackground(draw);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(view, position, arrayListNames.get(position));
                }
            }
        });

        holder.textView.setText(arrayListNames.get(position)+" "+arrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView textView, textView2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            textView = itemView.findViewById(R.id.txt);
            textView2 = itemView.findViewById(R.id.txt2);

        }
    }
}
