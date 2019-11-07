package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.CreateEventActivity;
import com.angadi.tripmanagementa.models.PeaGallery;
import com.angadi.tripmanagementa.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ImageHolder> {

    private ArrayList imageArray;
    private List<PeaGallery> galleryList;
    private Context context;
    private ImageClickListener listClickListener;
    private CloseImageClickListener closeClickListener;
    String whichImage;

    public AddImageAdapter(Context context, ArrayList imageArray,String whichImage) {
        this.context = context;
        this.imageArray = imageArray;
        this.whichImage = whichImage;
    }
    public AddImageAdapter(Context context,  List<PeaGallery> galleryList,String whichImage) {
        this.context = context;
        this.galleryList = galleryList;
        this.whichImage = whichImage;
    }

    public interface ImageClickListener {
        void onClick(View view, int position, int id);
    }

    public interface CloseImageClickListener {
        void onClick(View view, int position, int id);
    }

    public void setClickListener(ImageClickListener listClickListener) {
        this.listClickListener = listClickListener;
    }
    public void setCloseClickListener(CloseImageClickListener closeClickListener) {
        this.closeClickListener = closeClickListener;
    }


    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_image_layout, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
        ImageHolder imageHolder = new ImageHolder(v);
        return imageHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageHolder holder, int position) {
        if (whichImage.equalsIgnoreCase("bitmap")){
            holder.closeImageIv.setVisibility(View.VISIBLE);
            holder.addImageIv.setImageBitmap((Bitmap) imageArray.get(position));
            holder.addImageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listClickListener != null) {
                        listClickListener.onClick(holder.addImageIv, holder.getAdapterPosition(), 1);
                    }
                }
            });

            holder.closeImageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (closeClickListener != null) {
                        closeClickListener.onClick(holder.closeImageIv, holder.getAdapterPosition(), 0);
                    }
                }
            });

            if (position == imageArray.size() - 1) {
                holder.closeImageIv.setVisibility(View.INVISIBLE);
            } else {
                holder.closeImageIv.setVisibility(View.VISIBLE);
            }
        }else {
            holder.closeImageIv.setVisibility(View.GONE);
            Glide.with(context).load(Constants.BASE_URL+galleryList.get(position).getEiaaImage()).into(holder.addImageIv);
        }

    }

    @Override
    public int getItemCount() {
        if (whichImage.equalsIgnoreCase("bitmap")){
            return imageArray.size();
        }else {
            return galleryList.size();
        }

    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView addImageIv, closeImageIv;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            addImageIv = itemView.findViewById(R.id.add_image);
            closeImageIv = itemView.findViewById(R.id.close_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
