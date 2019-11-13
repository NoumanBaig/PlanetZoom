package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.CreateQrActivity;
import com.angadi.tripmanagementa.models.Result;
import com.angadi.tripmanagementa.utils.Constants;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    Context mContext;
    List<Result> resultList;
    CategoryClickListener categoryClickListener;
    public CategoryAdapter(Context mContext,List<Result> resultList) {
        this.mContext=mContext;
        this.resultList=resultList;
    }

    public interface CategoryClickListener {
        void onClick(View view, int position,String id, String cat_name, String image);
    }

    public void setClickListener(CategoryClickListener categoryClickListener) {
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_categories,parent,false);
        return new CategoryAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.textView.setText(resultList.get(position).getCaaName());
        holder.imageView.setImageURI(Constants.BASE_URL+resultList.get(position).getCaaImg());
        Log.e("Image--->",""+Constants.BASE_URL+resultList.get(position).getCaaImg());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryClickListener != null) {
                    categoryClickListener.onClick(holder.cardView,position,resultList.get(position).getCaaId(),
                            resultList.get(position).getCaaName(),resultList.get(position).getCaaImg());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView imageView;
        TextView textView;
        CardView cardView;


        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img);
            textView = itemView.findViewById(R.id.txt);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
