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
import com.angadi.tripmanagementa.models.ShowMembersResult;
import com.angadi.tripmanagementa.models.Website;
import com.angadi.tripmanagementa.utils.Constants;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.MyViewHolder> {

    Context mContext;
    List<Website> websiteList;
    ClickListener clickListener;

    public SponsorsAdapter(Context context,List<Website> websiteList) {
        super();
        this.mContext = context;
        this.websiteList = websiteList;
    }

    public interface ClickListener {
        void onClick(View view, int position, String url);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_sponsors, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

      holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(view, position,websiteList.get(position).getWeb());
                }
            }
        });

        holder.imageView.setImageURI(Constants.BASE_URL+websiteList.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return websiteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView imageView;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.linear);
            imageView = itemView.findViewById(R.id.img_logo);

        }
    }
}
