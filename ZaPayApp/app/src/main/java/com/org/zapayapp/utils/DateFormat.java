package com.org.zapayapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormat {
    public static long getEpochFromDate(String date) {
        long epochTime = 0;
      //  SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date1 = formatter.parse(date);
            assert date1 != null;
            epochTime = date1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return epochTime/1000;
    }


    public static String getDateFromEpoch(String epochTime) {
        // long unix_seconds = Long.parseLong(epochTime);
        long unix_seconds = Long.parseLong(epochTime);
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        //String ss=sdf.format(new Date(time * 1000));
       // String java_date = sdf.format(new Date(unix_seconds));
        String java_date = sdf.format(new Date(unix_seconds * 1000));

        return java_date;
    }



    public static String dateFormatConvert(String dateData) {
        String inputPattern = "yyyy-mm-dd";
        String outputPattern = "mm/dd/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateData);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String dateFormatConvert11(String dateData) {
        String inputPattern = "dd/mm/yyyy";
        String outputPattern = "mm/dd/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateData);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


}
