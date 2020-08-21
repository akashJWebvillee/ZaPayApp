package com.org.zapayapp.alert_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.org.zapayapp.R;
import com.org.zapayapp.utils.CommonMethods;

public class AlertForcePopup extends DialogFragment {
    private AlertForceCallback mListener;
    private Context context;
    private boolean isAddress;
    private String header = "", textOk = "", textCancel = "", from = "";
    private TextView okTV, cancelTV, textHeader;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.getDialog() != null) {
            this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (this.getDialog().getWindow() != null)
                this.getDialog().getWindow().setBackgroundDrawable(CommonMethods.getDrawableWrapper(getActivity(), android.R.color.transparent));
        }

        View view = inflater.inflate(R.layout.alert_dialog, container);

        Bundle mArgs = getArguments();
        if (mArgs != null) {
            header = mArgs.getString("header");
            textOk = mArgs.getString("textOk");
            textCancel = mArgs.getString("textCancel");
            from = mArgs.getString("from");
            isAddress = mArgs.getBoolean("isAddress",false);
        }

        okTV = view.findViewById(R.id.okTV);
        okTV.setOnClickListener(this::onClick);
        cancelTV = view.findViewById(R.id.cancelTV);
        cancelTV.setOnClickListener(this::onClick);
        textHeader = view.findViewById(R.id.textHeader);

        okTV.setVisibility(View.GONE);
        cancelTV.setVisibility(View.GONE);
        textHeader.setVisibility(View.GONE);

        if (header != null && header.length() > 0) {
            textHeader.setVisibility(View.VISIBLE);
            textHeader.setText(header);
        }
        if (textOk != null && textOk.length() > 0) {
            okTV.setVisibility(View.VISIBLE);
            okTV.setText(textOk);
        }
        if (textCancel != null && textCancel.length() > 0) {
            cancelTV.setVisibility(View.VISIBLE);
            cancelTV.setText(textCancel);
        }
        return view;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            try {
                this.mListener = (AlertForceCallback) a;
            } catch (final ClassCastException e) {
                e.printStackTrace();
                throw new ClassCastException(a.toString() + " must implement OnCompleteListener");
            }
        }
    }

    public void onClick(View v) {
        if (v.equals(okTV)) {
            mListener.onForceCallback(from,isAddress);
            this.dismiss();
        } else if (v.equals(cancelTV)) {
            this.dismiss();
        }
    }

    @Override
    public void onResume() {
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            int w = CommonMethods.getScreenWidth() - 200;
            dialog.getWindow().setLayout(w, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        super.onResume();
    }

    /**
     * The interface Alert force callback.
     */
    public interface AlertForceCallback {
        /**
         * On force callback.
         *
         * @param from the value
         */
        void onForceCallback(String from, boolean isAddress);
    }
}
