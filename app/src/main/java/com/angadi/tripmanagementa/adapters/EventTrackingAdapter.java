package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.SubEventResult;
import com.angadi.tripmanagementa.models.TrackData;

import java.util.List;

public class EventTrackingAdapter extends RecyclerView.Adapter<EventTrackingAdapter.CategoryHolder> {

    Context mContext;
    List<TrackData> resultList;
    ClickListener clickListener;
    DeleteClickListener deleteClickListener;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public EventTrackingAdapter(Context mContext, List<TrackData> resultList) {
        this.mContext = mContext;
        this.resultList = resultList;
    }

    public interface ClickListener {
        void onClick(View view, int position, String id, String title, String desc);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface DeleteClickListener {
        void onClick(View view, int position, String id);
    }

    public void setClickListener(DeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_event_tracking, parent, false);
        return new EventTrackingAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        if (resultList.get(position).getMtaaTrackPlace().equalsIgnoreCase("Exit 1") ||
                resultList.get(position).getMtaaTrackPlace().equalsIgnoreCase("Exit 2")){
            holder.textView.setText(resultList.get(position).getMtaaTrackPlace());
            holder.textView.setBackgroundColor(mContext.getResources().getColor(R.color.dark_green));
        }else {
            holder.textView.setText(resultList.get(position).getMtaaTrackPlace());
            holder.textView.setBackgroundColor(mContext.getResources().getColor(R.color.demo_day_green));
        }

        holder.txt_time.setText(resultList.get(position).getMtaa_time_ago());

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {

        TextView textView,txt_time;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
//            checkBox = itemView.findViewById(R.id.checkbox);
            textView = itemView.findViewById(R.id.txt);
            txt_time = itemView.findViewById(R.id.txt_time);
        }
    }
}
