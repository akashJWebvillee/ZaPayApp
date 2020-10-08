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

public class SimpleAlertFragment extends DialogFragment {

    private AlertSimpleCallback mListener;
    private Context context;
    private String header = "", textOk = "", textCancel = "",from ="";
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
            from = mArgs.getString("from","");
        }

        okTV = view.findViewById(R.id.okTV);
        okTV.setOnClickListener(this::onClick);
        cancelTV = view.findViewById(R.id.cancelTV);
        cancelTV.setVisibility(View.GONE);
        textHeader = view.findViewById(R.id.textHeader);

        if (header != null && header.length() > 0) {
            textHeader.setText(header);
        }
        if (textOk != null && textOk.length() > 0) {
            okTV.setText(textOk);
        }
        if (textCancel != null && textCancel.length() > 0) {
            cancelTV.setText(textCancel);
        }
        return view;
    }

    public void onClick(View v) {
        if (v.equals(okTV)) {
            mListener.onSimpleCallback(from);
            this.dismiss();
        }else if (v.equals(textCancel)){
            dismiss();
        }
    }

    @Override
    public void onResume() {
        Dialog dialog = getDialog();
        assert dialog != null;
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            int w = CommonMethods.getScreenWidth() - 150;
            dialog.getWindow().setLayout(w, ViewGroup.LayoutParams.WRAP_CONTENT);
           // dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        super.onResume();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            try {
                this.mListener = (AlertSimpleCallback) a;
            } catch (final ClassCastException e) {
                e.printStackTrace();
                throw new ClassCastException(a.toString() + " must implement OnCompleteListener");
            }
        }
    }

    public interface AlertSimpleCallback {
        void onSimpleCallback(String from);
    }
}
