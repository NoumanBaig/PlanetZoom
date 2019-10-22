package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.Result;
import com.angadi.tripmanagementa.models.SubResults;
import com.angadi.tripmanagementa.utils.Constants;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CategoryHolder> {

    Context mContext;
    List<SubResults> resultList;
    SubCategoryClickListener categoryClickListener;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public SubCategoryAdapter(Context mContext, List<SubResults> resultList) {
        this.mContext = mContext;
        this.resultList = resultList;
    }

    public interface SubCategoryClickListener {
        void onClick(View view, int position, String id, String sub_id, Boolean selected, String cat_name, String image);
    }

    public void setClickListener(SubCategoryClickListener categoryClickListener) {
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_sub_categories, parent, false);
        return new SubCategoryAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.textView.setText(resultList.get(position).getSubCatName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryClickListener != null) {
                    categoryClickListener.onClick(holder.linearLayout, position, resultList.get(position).getMainCatId()
                            , resultList.get(position).getSubCatId(), resultList.get(position).getSelected(), resultList.get(position).getSubCatName(), resultList.get(position).getCaaImg());
                }
            }
        });

        holder.checkBox.setChecked(resultList.get(position).getSelected());
        holder.checkBox.setTag(new Integer(position));
        //for default check in first item
        if (position == 0 && resultList.get(0).getSelected() && holder.checkBox.isChecked()) {
            lastChecked = holder.checkBox;
            lastCheckedPos = 0;
        }
        holder.checkBox.setTag(position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int clickedPos = ((Integer) cb.getTag()).intValue();
               // Toast.makeText(mContext, resultList.get(clickedPos).getSubCatName() + " clicked!", Toast.LENGTH_SHORT).show();
                if (cb.isChecked()) {
                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        resultList.get(lastCheckedPos).setSelected(false);
                    }
                    lastChecked = cb;
                    lastCheckedPos = clickedPos;
                } else
                    lastChecked = null;
                resultList.get(clickedPos).setSelected(cb.isChecked());

                if (categoryClickListener != null) {
                    categoryClickListener.onClick(holder.linearLayout,position,resultList.get(position).getMainCatId(),
                            resultList.get(position).getSubCatId(),resultList.get(position).getSelected(),resultList.get(position).getSubCatName(),resultList.get(position).getCaaImg());
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
        CheckBox checkBox;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            textView = itemView.findViewById(R.id.txt);
            linearLayout = itemView.findViewById(R.id.linear);
        }
    }
}
