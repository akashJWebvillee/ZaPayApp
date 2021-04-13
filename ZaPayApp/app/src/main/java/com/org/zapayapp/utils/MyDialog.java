package com.org.zapayapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.zapayapp.R;
import com.org.zapayapp.adapters.ViewAllDateAdapter;
import com.org.zapayapp.model.DateModel;

import java.util.ArrayList;
import java.util.Objects;

public class MyDialog {

    public static void viewAllDateFunc(Context context, ArrayList<String> dateList) {
        Dialog viewAllDateDialog = new Dialog(context);
        viewAllDateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewAllDateDialog.setCancelable(true);
        viewAllDateDialog.setCanceledOnTouchOutside(true);

        viewAllDateDialog.setContentView(R.layout.view_all_date);
        Objects.requireNonNull(viewAllDateDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        viewAllDateDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        RecyclerView dateRecView = viewAllDateDialog.findViewById(R.id.dateRecView);

        dateRecView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        dateRecView.setItemAnimator(new DefaultItemAnimator());
        ViewAllDateAdapter allDateAdapter = new ViewAllDateAdapter(context, dateList);
        dateRecView.setAdapter(allDateAdapter);
        viewAllDateDialog.show();
    }
}
