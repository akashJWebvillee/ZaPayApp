package com.org.zapayapp.dialogs;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.org.zapayapp.R;
import com.org.zapayapp.utils.CommonMethods;

public class ChangeBankDialogActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView saveTV;
    private ImageView closeTV;
    private EditText accountNumberTV,routNumberTV;
    private String header = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(CommonMethods.getDrawableWrapper(this, android.R.color.transparent));
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bank_account_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getIntentValues();
        init();
        initAction();
    }

    private void getIntentValues() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.getStringExtra("header") != null) {
                    header = intent.getStringExtra("header");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        saveTV = findViewById(R.id.saveTV);
        accountNumberTV = findViewById(R.id.accountNumberTV);
        routNumberTV = findViewById(R.id.routNumberTV);
        closeTV = findViewById(R.id.closeTV);
    }

    private void initAction() {
        saveTV.setOnClickListener(this);
        closeTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(saveTV)) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }else if (v.equals(closeTV)){
            finish();
        }
    }
}
