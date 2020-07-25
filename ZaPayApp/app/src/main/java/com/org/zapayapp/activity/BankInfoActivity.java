package com.org.zapayapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.org.zapayapp.R;
import com.org.zapayapp.dialogs.ChangeBankDialogActivity;

public class BankInfoActivity extends BaseActivity {

    private TextView changeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);
        init();
        initAction();
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    private void init() {
        changeTV = findViewById(R.id.changeTV);
    }

    private void initAction() {
        changeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  bankAccountChangeDialog();
                Intent intent = new Intent(BankInfoActivity.this, ChangeBankDialogActivity.class);
                startActivity(intent);
            }
        });
    }

    private void bankAccountChangeDialog() {
        final Dialog dialog = new Dialog(BankInfoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bank_account_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        TextView saveTV = dialog.findViewById(R.id.saveTV);
        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
