package com.org.zapayapp.webservices;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;

import com.org.zapayapp.R;
import com.org.zapayapp.utils.CommonMethods;

public class ProgressBarHandler {

    private ProgressBar mProgressBar;
    private RelativeLayout rl;

    /**
     * Instantiates a new Progress bar handler.
     *
     * @param context the context
     */
    public ProgressBarHandler(Context context) {
        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        //mProgressBar.setIndeterminate(true);
        setButtonTint(mProgressBar, ColorStateList.valueOf(CommonMethods.getColorWrapper(context, R.color.colorPrimaryDark)));
        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rl = new RelativeLayout(context);
        rl.setBackgroundColor(CommonMethods.getColorWrapper(context, android.R.color.transparent));
        rl.setClickable(true);
        rl.setFocusable(true);
        rl.setGravity(Gravity.CENTER);
        rl.addView(mProgressBar);
        layout.addView(rl, params);
        hide();
    }

    private void setButtonTint(ProgressBar button, ColorStateList tint) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            button.setBackgroundTintList(tint);
        } else {
            ViewCompat.setBackgroundTintList(button, tint);
        }
    }

    public void show() {
        rl.setVisibility(View.VISIBLE);
    }

    public void hide() {
        rl.setVisibility(View.INVISIBLE);
    }
}
