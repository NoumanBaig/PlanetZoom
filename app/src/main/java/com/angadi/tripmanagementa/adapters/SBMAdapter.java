package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.QRHisData;
import com.angadi.tripmanagementa.models.ReportsTrackData;

import java.util.List;

public class SBMAdapter extends RecyclerView.Adapter<SBMAdapter.MyViewHolder> {

    Context mContext;
    List<QRHisData> resultList;
    ClickListener clickListener;
    String string;

    public SBMAdapter(Context context, List<QRHisData> resultList,String string) {
        super();
        this.mContext = context;
        this.resultList = resultList;
        this.string = string;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_sbm, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (string.equalsIgnoreCase("dashboard")){
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onClick(view, position,resultList.get(position).getSraaQrId());
                    }
                }
            });

            holder.txt_date.setText("Date: "+resultList.get(position).getSraaDate());
            holder.txt_name.setText(resultList.get(position).getSraaQrCodeName());
            holder.txt_cat.setText("Category: "+resultList.get(position).getSraaQrCodeCat());
        }else {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onClick(view, position,resultList.get(position).getSraaQrId());
                    }
                }
            });

            holder.txt_date.setText("Date: "+resultList.get(position).getSraaDate());
            holder.txt_name.setText(resultList.get(position).getSraaFirstName());
            holder.txt_cat.setText("Login ID: "+resultList.get(position).getSraaLoginId());
        }

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_date,txt_cat,txt_name;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.linear);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_cat = itemView.findViewById(R.id.txt_cat);
            txt_date = itemView.findViewById(R.id.txt_date);

        }
    }
}
