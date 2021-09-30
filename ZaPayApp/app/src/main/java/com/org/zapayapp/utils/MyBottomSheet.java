package com.org.zapayapp.utils;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.org.zapayapp.R;
import com.org.zapayapp.databinding.WalletFilterSheetDialogBinding;
import java.util.Objects;

public class MyBottomSheet {
    public BottomSheetDialog bottomSheetDialog;
    private BottomSheetListener bottomSheetListener;
    private String forwhat = "", from_date = "", to_date = "", selected_date = "";
    private boolean clearAll = false;
    private int filterType = 0;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public interface BottomSheetListener {
        void onSheetClick(String forwhat, String response);
    }



    public MyBottomSheet() {

    }


   /* public void rattingReviewBottomSheet(Context context, BottomSheetListener bottomSheetListener, String forwhat) {
        this.bottomSheetListener = bottomSheetListener;
        this.forwhat = forwhat;
        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.rating_sheet_dialog, null);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetDialogAnimation;

        ImageView closeIV = bottomSheetView.findViewById(R.id.closeIV);
        TextView okayTV = bottomSheetView.findViewById(R.id.okayTV);
        CustomRatingBar delivery = bottomSheetView.findViewById(R.id.deliveryRatingBar);
        CustomRatingBar restaurant = bottomSheetView.findViewById(R.id.restaurantRatingBar);
        EditText editText = bottomSheetView.findViewById(R.id.writeReviewET);


        closeIV.setOnClickListener(view -> {
            CommonMethods.preventTwoClick(view);
            bottomSheetDialog.dismiss();
        });

        okayTV.setOnClickListener(view -> {
            CommonMethods.preventTwoClick(view);
            JSONObject object = new JSONObject();
            try {
                object.put("ratting", restaurant.getScore());
                object.put("review", editText.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bottomSheetListener.onSheetClick(forwhat, object.toString());
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }*/





    public void walletFilterBottomSheet(Context context, BottomSheetListener bottomSheetListener, String forwhat) {
        this.bottomSheetListener = bottomSheetListener;
        this.forwhat = forwhat;
        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        WalletFilterSheetDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.wallet_filter_sheet_dialog, null, false);
       // View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.outstanding_amount_sheet_dialog, null);

        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetDialogAnimation;

        binding.closeTV.setOnClickListener(view -> {
           // CommonMethods.preventTwoClick(view);
            bottomSheetDialog.dismiss();
        });

        binding.clearAllTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });



        binding.applyTV.setOnClickListener(view -> {
            if (filterType>0){
                //CommonMethods.preventTwoClick(view);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("FILTER_TYPE", filterType);

                bottomSheetListener.onSheetClick(forwhat, jsonObject.toString());
                bottomSheetDialog.dismiss();
            }else {
                Toast.makeText(context,"Please select at least one option",Toast.LENGTH_SHORT).show();
            }
        });


        binding.LL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = makeSelectable(1,binding);
            }
        });

        binding.LL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType =makeSelectable(2,binding);
            }
        });

        binding.LL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType =makeSelectable(3,binding);
            }
        });




        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.show();
    }

    private int makeSelectable(int a, WalletFilterSheetDialogBinding binding){
        binding.imageView1.setImageResource(R.drawable.checkbox_unselect);
        binding.imageView2.setImageResource(R.drawable.checkbox_unselect);
        binding.imageView3.setImageResource(R.drawable.checkbox_unselect);

        if (a==1){
            binding.imageView1.setImageResource(R.mipmap.ic_check_select);
        }else if (a==2){
            binding.imageView2.setImageResource(R.mipmap.ic_check_select);
        }else if (a==3){
            binding.imageView3.setImageResource(R.mipmap.ic_check_select);
        }

        return a;
    }
}