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
import com.angadi.tripmanagementa.models.ReportsTrackData;
import com.angadi.tripmanagementa.models.ShowMembersResult;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {

    Context mContext;
    List<ReportsTrackData> resultList;
    ClickListener clickListener;

    public ReportsAdapter(Context context, List<ReportsTrackData> resultList) {
        super();
        this.mContext = context;
        this.resultList = resultList;
    }

    public interface ClickListener {
        void onClick(View view, int position, String id);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_reports, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

      holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(view, position,resultList.get(position).getMtaaUid());
                }
            }
        });

      holder.txt_date.setText("Date: "+resultList.get(position).getMtaaDate());
      holder.txt_name.setText("Name: "+resultList.get(position).getMtaaUserName());
      holder.txt_scName.setText("Scanner Name: "+resultList.get(position).getMtaaVolunteerName());
      holder.txt_loginId.setText("Login ID: "+resultList.get(position).getMtaaUserLoginId());
      holder.txt_place.setText("Scanned Place: "+resultList.get(position).getMtaaTrackPlace());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_date,txt_place,txt_loginId,txt_scName,txt_name;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.linear);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_scName = itemView.findViewById(R.id.txt_scName);
            txt_loginId = itemView.findViewById(R.id.txt_loginId);
            txt_place = itemView.findViewById(R.id.txt_place);
            txt_date = itemView.findViewById(R.id.txt_date);

        }
    }
}
