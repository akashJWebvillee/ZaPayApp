package com.org.zapayapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormat {


    public static long getEpochFromDate(String date) {
     /*   long epochTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(today));
        try {
            Date date1 = formatter.parse(formatter.format(calendar.getTime()));
            assert date1 != null;
            epochTime = date1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return epochTime;*/

        long epochTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            Date date1 = formatter.parse(date);
            assert date1 != null;
            epochTime = date1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return epochTime;
    }


    public static String getDateFromEpoch(String epochTime) {
        // long unix_seconds = Long.parseLong(epochTime);
        long unix_seconds = Long.parseLong(epochTime);
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        //String ss=sdf.format(new Date(time * 1000));
        //String java_date = sdf.format(new Date(unix_seconds));
        String java_date = sdf.format(new Date(unix_seconds * 1000));

        return java_date;
    }


}
