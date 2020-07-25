package com.org.zapayapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.org.zapayapp.R;
import com.org.zapayapp.dialogs.ChangePassDialogActivity;
import com.org.zapayapp.dialogs.EditProfileDialogActivity;

public class ProfileActivity extends BaseActivity {

    private TextView editProfileTV;
    private TextView changePasswordTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        changePasswordTV = findViewById(R.id.changePasswordTV);
        editProfileTV = findViewById(R.id.editProfileTV);
    }

    private void initAction() {
        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChangePassDialogActivity.class);
                startActivity(intent);
            }
        });
        editProfileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileDialogActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentScreen(MY_PROFILE);
    }
}
