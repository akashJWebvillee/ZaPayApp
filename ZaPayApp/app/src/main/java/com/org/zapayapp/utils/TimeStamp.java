package com.org.zapayapp.utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeStamp {
    public static String timeFun(String epochTime) {
        long unix_seconds = Long.parseLong(epochTime);
        unix_seconds =unix_seconds*1000;
        //unix_seconds =unix_seconds;
       // String format = "dd/MM/yyyy HH:mm a";
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        String java_date=sdf.format(new Date(unix_seconds));
         //String java_date=sdf.format(new Date(unix_seconds));


        /* Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(unix_seconds));
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minut = cal.get(Calendar.MINUTE);
       String timeWithAm_Pm=updateTime(hours,minut);*/


       return java_date;
    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        return aTime;
    }


    public static String setTimeFormate(String inputDate) {

       /*
        String dateString = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            Date date = simpleDateFormat.parse(inputDate);

            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;*/


         //2020-08-04T06:54:38.208Z
        String dateStr = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(inputDate);//You will get date object relative to server/client timezone wherever it is parsed

            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH); //If you need time just put specific format for time like 'HH:mm:ss'
            formatter.setTimeZone(TimeZone.getDefault());
            dateStr = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;

    }

    public static String epochToDateTimeAppFormat(long time) {
        //String format = "dd/MM/yyyy HH:mm a";
        String format = "MMM dd, yyyy 'at' hh:mm a";
        //String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(time * 1000)).replace("am", "AM").replace("pm", "PM");
    }
}
