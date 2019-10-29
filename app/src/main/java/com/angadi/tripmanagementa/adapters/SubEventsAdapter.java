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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.SubEventResult;

import java.util.List;

public class SubEventsAdapter extends RecyclerView.Adapter<SubEventsAdapter.CategoryHolder> {

    Context mContext;
    List<SubEventResult> resultList;
    ClickListener clickListener;
//    DeleteClickListener deleteClickListener;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public SubEventsAdapter(Context mContext, List<SubEventResult> resultList) {
        this.mContext = mContext;
        this.resultList = resultList;
    }

    public interface ClickListener {
        void onClick(View view, int position, String id, String title, String desc);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

//    public interface DeleteClickListener {
//        void onClick(View view, int position, String id);
//    }
//
//    public void setClickListener(DeleteClickListener deleteClickListener) {
//        this.deleteClickListener = deleteClickListener;
//    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_sub_events, parent, false);
        return new SubEventsAdapter.CategoryHolder(view);
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

//        holder.layout_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (clickListener != null) {
//                    clickListener.onClick(holder.linearLayout, position, resultList.get(position).getEsaaId(),
//                            resultList.get(position).getEsaaSubTitle(),resultList.get(position).getEsaaDesc());
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CardView linearLayout;
//        LinearLayout layout_view;
//        ImageView img_delete;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
//            checkBox = itemView.findViewById(R.id.checkbox);
            textView = itemView.findViewById(R.id.txt);
            linearLayout = itemView.findViewById(R.id.linear);
//            layout_view = itemView.findViewById(R.id.layout_view);
//            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
