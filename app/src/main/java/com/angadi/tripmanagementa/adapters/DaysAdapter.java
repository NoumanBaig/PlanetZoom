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
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.models.SubEventResult;

import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.CategoryHolder> {

    Context mContext;
    List<SubEventResult> resultList;
    ClickListener clickListener;
    DeleteClickListener deleteClickListener;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public DaysAdapter(Context mContext,List<SubEventResult> resultList) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_days, parent, false);
        return new DaysAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.textView.setText(resultList.get(position).getEsaaSubTitle());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(holder.linearLayout, position, resultList.get(position).getEsaaId(),
                            resultList.get(position).getEsaaSubTitle(),resultList.get(position).getEsaaDesc());
                }
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteClickListener != null) {
                    deleteClickListener.onClick(holder.img_delete, position, resultList.get(position).getEsaaId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {

        TextView textView;
        LinearLayout linearLayout;
        ImageView img_delete;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
//            checkBox = itemView.findViewById(R.id.checkbox);
            textView = itemView.findViewById(R.id.txt);
            linearLayout = itemView.findViewById(R.id.linear);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
