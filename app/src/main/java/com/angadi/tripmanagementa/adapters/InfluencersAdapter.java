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
import com.angadi.tripmanagementa.models.Influencers;
import com.angadi.tripmanagementa.models.Website;
import com.angadi.tripmanagementa.utils.Constants;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfluencersAdapter extends RecyclerView.Adapter<InfluencersAdapter.MyViewHolder> {

    Context mContext;
    List<Influencers> influencersList;
    ClickListener clickListener;

    public InfluencersAdapter(Context context, List<Influencers> influencersList) {
        super();
        this.mContext = context;
        this.influencersList = influencersList;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_influencers, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        holder.imageView.setImageURI(Constants.BASE_URL+influencersList.get(position).getImage());
        Glide.with(mContext).load(Constants.BASE_URL+influencersList.get(position).getImage()).override(100,100)
                .placeholder(R.drawable.ic_placeholder).into(holder.imageView);
        holder.textView.setText(influencersList.get(position).getName());
        holder.textView2.setText(influencersList.get(position).getDes());
    }

    @Override
    public int getItemCount() {
        return influencersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        LinearLayout layout;
        TextView textView,textView2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.linear);
            imageView = itemView.findViewById(R.id.img_logo);
            textView = itemView.findViewById(R.id.txt);
            textView2 = itemView.findViewById(R.id.txt2);

        }
    }
}
