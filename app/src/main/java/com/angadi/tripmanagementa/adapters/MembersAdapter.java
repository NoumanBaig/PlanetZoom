package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.MembersResult;
import com.angadi.tripmanagementa.models.SubResults;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.CategoryHolder> {

    Context mContext;
    List<MembersResult> resultList;
    ClickListener clickListener;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public MembersAdapter(Context mContext, List<MembersResult> resultList) {
        this.mContext = mContext;
        this.resultList = resultList;
    }

    public interface ClickListener {
        void onClick(View view, int position, String id,String title);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_members, parent, false);
        return new MembersAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.textView.setText(resultList.get(position).getEulaaName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(holder.linearLayout, position, resultList.get(position).getEulaaId(),
                            resultList.get(position).getEulaaName());
                }
            }
        });

//        holder.checkBox.setChecked(resultList.get(position).getSelected());
//        holder.checkBox.setTag(new Integer(position));
//        //for default check in first item
//        if (position == 0 && resultList.get(0).getSelected() && holder.checkBox.isChecked()) {
//            lastChecked = holder.checkBox;
//            lastCheckedPos = 0;
//        }
//        holder.checkBox.setTag(position);
//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                int clickedPos = ((Integer) cb.getTag()).intValue();
//               // Toast.makeText(mContext, resultList.get(clickedPos).getSubCatName() + " clicked!", Toast.LENGTH_SHORT).show();
//                if (cb.isChecked()) {
//                    if (lastChecked != null) {
//                        lastChecked.setChecked(false);
//                        resultList.get(lastCheckedPos).setSelected(false);
//                    }
//                    lastChecked = cb;
//                    lastCheckedPos = clickedPos;
//                } else
//                    lastChecked = null;
//                resultList.get(clickedPos).setSelected(cb.isChecked());
//
//                if (clickListener != null) {
//                    clickListener.onClick(holder.linearLayout,position,resultList.get(position).getEulaaId());
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
        LinearLayout linearLayout;
        CheckBox checkBox;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
//            checkBox = itemView.findViewById(R.id.checkbox);
            textView = itemView.findViewById(R.id.txt);
            linearLayout = itemView.findViewById(R.id.linear);
        }
    }
}
