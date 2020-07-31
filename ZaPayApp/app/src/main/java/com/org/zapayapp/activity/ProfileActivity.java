package com.org.zapayapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.org.zapayapp.R;
import com.org.zapayapp.dialogs.ChangePassDialogActivity;
import com.org.zapayapp.dialogs.EditProfileDialogActivity;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private TextView editProfileTV,changePasswordTV,profileTxtName,profileTxtEmail,profileTxtMobile,profileTxtAddress;
    private Intent intent;

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
        profileTxtName = findViewById(R.id.profileTxtName);
        profileTxtEmail = findViewById(R.id.profileTxtEmail);
        profileTxtMobile = findViewById(R.id.profileTxtMobile);
        profileTxtAddress = findViewById(R.id.profileTxtAddress);
    }

    private void initAction() {
        changePasswordTV.setOnClickListener(this);
        editProfileTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePasswordTV:
                intent = new Intent(ProfileActivity.this, ChangePassDialogActivity.class);
                startActivity(intent);
                break;
            case R.id.editProfileTV:
                intent = new Intent(ProfileActivity.this, EditProfileDialogActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentScreen(MY_PROFILE);
    }
}
