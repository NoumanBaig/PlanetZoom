package com.angadi.tripmanagementa.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.CreateQrThreeActivity;
import com.angadi.tripmanagementa.activities.ScanResultActivity;
import com.angadi.tripmanagementa.utils.Constants;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class CountItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.container)
    View containerView;
    double screenInches;
    BitMatrix result;
    Context context;

    public CountItemViewHolder(Context context,View itemView) {
        super(itemView);
        this.context=context;
        ButterKnife.bind(this, itemView);
    }

    public void render(String qr_id,String qr_url, int color){
//        textView.setText(text);
        containerView.setBackgroundColor(color);
        //showQrCode(text);
        Glide.with(context).load(R.drawable.qr_image).override(200,200).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ScanResultActivity.class).putExtra("qr_id",qr_id).putExtra("qr_url",qr_url));
            }
        });
    }
}