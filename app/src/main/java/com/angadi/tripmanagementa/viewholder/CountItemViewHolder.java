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

    public void render(String text, int color){
//        textView.setText(text);
        containerView.setBackgroundColor(color);
        showQrCode(text);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ScanResultActivity.class).putExtra("qr_id",text));
            }
        });
    }
    private void showQrCode(String str_qr_id){
        try {
            Bitmap bitmap = encodeAsBitmap(str_qr_id);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap encodeAsBitmap(String list) throws WriterException {
        Log.e("-----------------", String.valueOf(screenInches));

        try {
            Log.e("screenInches---->", String.valueOf(screenInches));
            if (screenInches <= 5.2) {
                Log.e("first", "first");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 600, 600, null);
            } else if (screenInches >= 5.21 && screenInches <= 5.3) {
                Log.e("second", "second");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);

            } else if (screenInches >= 5.31 && screenInches <= 5.5) {
                Log.e("second1", "second1");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);

            } else if (screenInches >= 5.6 && screenInches <= 5.99) {
                Log.e("third", "third");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);

            } else if (screenInches >= 6.1 && screenInches <= 6.5) {
                Log.e("Fourth", "Fourth");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 760, 760, null);

            } else {
                Log.e("else", "else");
                result = new MultiFormatWriter().encode(String.valueOf(list), BarcodeFormat.QR_CODE, 360, 360, null);
            }

        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

}