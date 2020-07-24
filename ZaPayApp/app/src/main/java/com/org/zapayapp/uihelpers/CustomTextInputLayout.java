package com.org.zapayapp.uihelpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.org.zapayapp.R;

public class CustomTextInputLayout extends TextInputLayout {

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        super.setErrorEnabled(enabled);
        if (!enabled) {
            return;
        }
        try {
            TextView errorView = this.findViewById(R.id.textinput_error);
            FrameLayout errorViewParent = (FrameLayout) errorView.getParent();
            errorViewParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            errorView.setGravity(Gravity.END);
        } catch (Exception e) {
            // At least log what went wrong
            e.printStackTrace();
        }
    }

    public static Spannable setErrorMessage(Context context, String message) {
        Spannable span = new SpannableString("  " + message);
        Drawable android = context.getResources().getDrawable(R.mipmap.ic_error);
        android.setBounds(0, 0, 32, 32);
        CenteredImageSpan image = new CenteredImageSpan(android);
        span.setSpan(image, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return span;
    }
}
