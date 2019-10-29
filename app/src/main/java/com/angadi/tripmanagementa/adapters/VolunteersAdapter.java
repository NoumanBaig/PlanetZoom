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
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.models.PlacesResult;
import com.angadi.tripmanagementa.models.ShowMembersResult;

import java.util.List;

public class VolunteersAdapter extends RecyclerView.Adapter<VolunteersAdapter.MyViewHolder> {

    Context mContext;
    List<ShowMembersResult> resultList;
    ClickListener clickListener;

    public VolunteersAdapter(Context context, List<ShowMembersResult> resultList) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_volunteers, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_title.setText(resultList.get(position).getEavqaFname());

      holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(view, position,"1");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.linear);
            txt_title = itemView.findViewById(R.id.txt);

        }
    }
}
