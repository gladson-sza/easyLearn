package com.gmmp.easylearn.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gmmp.easylearn.R;

public class ViewDialog {

    private Activity activity;
    private Dialog dialog;

    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(String t, String d) {
        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading);
        TextView title = dialog.findViewById(R.id.title);
        TextView description = dialog.findViewById(R.id.description);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        title.setText(t);
        if(d.isEmpty()){
            description.setVisibility(View.GONE);
        }else{
            description.setVisibility(View.VISIBLE);
            description.setText(d);
        }
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        Glide.with(activity)
                .load(R.drawable.ic_loading)
                .placeholder(R.drawable.ic_loading)
                .centerCrop()
                .into(gifImageView);
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }

}