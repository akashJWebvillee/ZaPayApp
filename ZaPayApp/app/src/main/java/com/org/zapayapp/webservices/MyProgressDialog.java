package com.org.zapayapp.webservices;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.zapayapp.R;


public class MyProgressDialog {
    private static MyProgressDialog mInstance;
    private Dialog dialog;

    public static synchronized MyProgressDialog getInstance() {
        if (mInstance == null) {
            mInstance = new MyProgressDialog();
        }
        return mInstance;
    }

    public void show(Context context) {
        if (dialog != null && dialog.isShowing() || ((AppCompatActivity) context).isFinishing()) {
            return;
        }

        dialog = new Dialog(context, R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_progress_dialog);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(R.color.loaderpBg60Alpha);

        final ImageView loaderImg = dialog.findViewById(R.id.loaderImageView);
        loaderImg.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        Glide.with(context).load(R.raw.loader_img).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(loaderImg);


      //  dialog.setContentView(R.layout.custom_progress_bar);
        //dialog.setCancelable(false); //back button is not work .......
        //dialog.setCancelable(true);
        //dialog.setCanceledOnTouchOutside(false);
        if (!((AppCompatActivity) context).isFinishing())
            dialog.show();
    }

    public void dismiss() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShown() {
        if (dialog != null && dialog.isShowing()) {
            dialog.show();
        }
        return false;
    }
}
