package com.angadi.tripmanagementa.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.angadi.tripmanagementa.R;


/**
 * Created by nouman.
 */
public class AlertDialogBox {
    Context mCon;
    public static void showAlert(Context activity, String message)
    {

        final Dialog alertDialog=new Dialog(activity,android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_dialog);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView title=(TextView)alertDialog.findViewById(R.id.dialog_txt);
        TextView msg=(TextView)alertDialog.findViewById(R.id.dialog_msg);
        Button ok=(Button)alertDialog.findViewById(R.id.dialog_btn);
        title.setTextColor(activity.getResources().getColor(R.color.red));
        title.setText("Alert!");
        msg.setText(message);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public static void showSuccessAlert(final Context activity, String message)
    {
        final Dialog alertDialog=new Dialog(activity, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_dialog);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView title=(TextView)alertDialog.findViewById(R.id.dialog_txt);
        TextView msg=(TextView)alertDialog.findViewById(R.id.dialog_msg);
        Button ok=(Button)alertDialog.findViewById(R.id.dialog_btn);
        title.setTextColor(activity.getResources().getColor(R.color.green));
        title.setText("Success!");
        msg.setText(message);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
