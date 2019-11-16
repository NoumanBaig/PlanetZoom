package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.models.ProfileGallery;
import com.angadi.tripmanagementa.utils.Constants;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    Context mContext;
    String string;
    ClickListener clickListener;
    DeleteListener deleteListener;
    List<ProfileGallery> galleryList;

    public GalleryAdapter(Context context, List<ProfileGallery> galleryList, String string) {
        super();
        this.mContext = context;
        this.galleryList = galleryList;
        this.string = string;
    }

    public interface ClickListener {
        void onClick(View view, int position, String url);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface DeleteListener {
        void onDelete(View view, int position, String img_id);
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_profile_gallery, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (string.equalsIgnoreCase("scan")) {
            holder.img_delete.setVisibility(View.GONE);
        } else {
            holder.img_delete.setVisibility(View.VISIBLE);
        }

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteListener != null) {
                    deleteListener.onDelete(view, position, galleryList.get(position).getPiaaId());
                }
            }
        });

        holder.imageView.setImageURI(Constants.BASE_URL + galleryList.get(position).getPiaaImage());

    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView imageView;
        LinearLayout layout;
        ImageView img_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.linear);
            imageView = itemView.findViewById(R.id.img);
            img_delete = itemView.findViewById(R.id.img_delete);

        }
    }
}
