package com.org.zapayapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.org.zapayapp.R;
import java.io.ByteArrayOutputStream;

public class SignatureActivity extends AppCompatActivity {
    //https://github.com/gcacace/android-signaturepad
    private SignaturePad signatureView;
    private Button clear_button;
    private Button save_button;
    private ImageView closeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        inIt();
        inItAction();
    }

    private void inIt() {
        signatureView = findViewById(R.id.signatureView);
        clear_button = findViewById(R.id.clear_button);
        save_button = findViewById(R.id.save_button);
        closeIV = findViewById(R.id.closeIV);
    }

    private void inItAction() {
        // signatureView.setBackgroundResource(R.mipmap.splash_bg);
        signatureView.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
                save_button.setClickable(true);
                clear_button.setClickable(true);
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared

            }
        });

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clear();
                save_button.setClickable(false);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = signatureView.getSignatureBitmap();
                //Bitmap signatureBitmap = getBitmapFromView(signatureView);

                /*Intent intent = new Intent();
                  intent.putExtra("BitmapImage", signatureBitmap);
                  setResult(200, intent);
                  finish();*/

                Intent intent = new Intent();
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                intent.putExtra("byteArray", bs.toByteArray());
                setResult(200, intent);
                finish();
            }
        });
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}