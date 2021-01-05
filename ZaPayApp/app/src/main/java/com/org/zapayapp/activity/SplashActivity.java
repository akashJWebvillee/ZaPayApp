package com.org.zapayapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.org.zapayapp.R;
import com.org.zapayapp.utils.Const;
import com.org.zapayapp.utils.SharedPref;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID)!=null&&SharedPref.getPrefsHelper().getPref(Const.Var.USER_ID).toString().length()>0){
                    //startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    if (SharedPref.getPrefsHelper().getPref(Const.Var.PIN)!=null&&SharedPref.getPrefsHelper().getPref(Const.Var.PIN).toString().length()>0){
                        Intent intent=new Intent(SplashActivity.this, SetPinActivity.class);
                        intent.putExtra("forWhat",getString(R.string.check_pin));
                        startActivity(intent);
                    }else {
                        Intent intent=new Intent(SplashActivity.this, SetPinActivity.class);
                        intent.putExtra("forWhat",getString(R.string.set_new_pin));
                        startActivity(intent);
                    }
                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}
