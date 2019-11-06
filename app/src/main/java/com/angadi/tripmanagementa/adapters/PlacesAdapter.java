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
import com.angadi.tripmanagementa.models.PlacesResult;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {

    Context mContext;
    List<PlacesResult> resultList;
    PlacesClickListener placesClickListener;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public PlacesAdapter(Context context, List<PlacesResult> resultList) {
        super();
        this.mContext = context;
        this.resultList = resultList;
    }

    public interface PlacesClickListener {
        void onClick(View view, int position, String id,String uid);
    }

    public void setClickListener(PlacesClickListener placesClickListener) {
        this.placesClickListener = placesClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_places, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_title.setText(resultList.get(position).getEtlaaPlaces());

//      holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (placesClickListener != null) {
//                    placesClickListener.onClick(holder.layout, position,resultList.get(position).getEtlaaId());
//                }
//            }
//        });

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

                if (placesClickListener != null) {
                    placesClickListener.onClick(holder.layout, position,resultList.get(position).getEtlaaId(),resultList.get(position).getEtlaaUid());
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
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
            layout = itemView.findViewById(R.id.linear);
            txt_title = itemView.findViewById(R.id.txt);

        }
    }
}
