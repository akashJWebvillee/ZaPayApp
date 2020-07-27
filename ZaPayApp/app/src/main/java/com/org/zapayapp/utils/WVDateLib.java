package com.org.zapayapp.utils;

import android.content.Context;
import android.net.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The type Wv date lib.
 */
public class WVDateLib {

    /**
     * Separators are used as literal while creating formatted date string.
     */
    public final char SEPARATOR_1 = '/';
    /**
     * The constant SEPARATOR_2.
     */
    public final char SEPARATOR_2 = '.';
    /**
     * The constant SEPARATOR_3.
     */
    public final char SEPARATOR_3 = ':';
    /**
     * The constant SEPARATOR_4.
     */
    public final char SEPARATOR_4 = '-';
    /**
     * The constant SEPARATOR_5.
     */
    public final char SEPARATOR_5 = ' ';

    /**
     * The constant MINUTES_IN_AN_HOUR.
     */
    public final int MINUTES_IN_AN_HOUR = 60;
    /**
     * The constant SECONDS_IN_A_MINUTE.
     */
    public final int SECONDS_IN_A_MINUTE = 60;
    /**
     * The constant SECONDS_IN_A_HOUR.
     */
    public final int SECONDS_IN_A_HOUR = 60 * 60;
    /**
     * The constant MILI SECONDS_IN_A_HOUR.
     */
    public final int MILI_SECONDS_IN_A_HOUR = 60 * 60 * 1000;

    /**
     * The constant dateFormat.
     */
    private DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
    /**
     * The constant currentDateFormat.
     */
    private DateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    /**
     * The constant serverDateFormat.
     */
    private DateFormat serverDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
    /**
     * The constant dateWithoutYear.
     */
    private DateFormat dateWithoutYear = new SimpleDateFormat("d-MM-yy", Locale.ENGLISH);
    /**
     * The constant timeFormat.
     */
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    /**
     * The Context.
     */
    private Context context;

    /**
     * Instantiates a new Wv date lib.
     *
     * @param context the context
     */
    public WVDateLib(Context context) {
        this.context = context;
    }

    /**
     * Mili sec to date string.
     *
     * @param milliseconds the miliseconds
     * @return the string
     */
    public String milliSecToDate(long milliseconds) {
        //SimpleDateFormat formatter = new SimpleDateFormat(serverDateFormat, Locale.ENGLISH);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return serverDateFormat.format(calendar.getTime());
    }

    /**
     * Gets current date.
     *
     * @return the current date
     */
    public String getCurrentDate() {
        Date today = Calendar.getInstance().getTime();
        return currentDateFormat.format(today);
    }

    /**
     * Gets current date.
     *
     * @return the current date
     */
    public String getUserSelectedDate() {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        return tz.getID();
    }

    /**
     * Convert date string.
     *
     * @param date_of_birth the date of birth
     * @return the string
     */
    public String convertDate(String date_of_birth) {
        String dateStr = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            Date date = dateFormat.parse(date_of_birth);//You will get date object relative to server/client timezone wherever it is parsed
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH); //If you need time just put specific format for time like 'HH:mm:ss'
            dateStr = formatter.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * Server format string.
     *
     * @param date_of_birth the date of birth
     * @return the string
     */
    public String serverFormat(String date_of_birth) {
        String dateStr = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date = dateFormat.parse(date_of_birth);//You will get date object relative to server/client timezone wherever it is parsed
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); //If you need time just put specific format for time like 'HH:mm:ss'
            dateStr = formatter.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * Local format string.
     *
     * @param date_of_birth the date of birth
     * @return the string
     */
    public String localFormat(String date_of_birth) {
        String dateStr = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = dateFormat.parse(date_of_birth);//You will get date object relative to server/client timezone wherever it is parsed
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH); //If you need time just put specific format for time like 'HH:mm:ss'
            dateStr = formatter.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * Gets timestamp of date.
     *
     * @param yr    as year
     * @param month as month
     * @param day   as day
     * @param hr    as hours
     * @param min   as minute
     * @param sec   as seconds
     * @return time stamp value in long
     */
    public long getTimestampOfDate(int yr, int month, int day, int hr, int min, int sec) {
        int mm = month - 1;// coz in calender month count starts from 0
        Calendar calendar = new GregorianCalendar(yr, mm, day, hr, min, sec);
        return calendar.getTimeInMillis();
    }

    /**
     * Gets age.
     *
     * @param dobString the dob string
     * @return the age
     */
    public int getAge(String dobString) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            date = sdf.parse(dobString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        if (age == -1) return 0;

        return age;
    }

    /**
     * Gets age formatt.
     *
     * @param dobString  the dob string
     * @param dateFormat the date format
     * @return the age formatt
     */
    public int getAgeFormatt(String dobString, String dateFormat) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        try {
            date = sdf.parse(dobString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date == null) return 0;
        Calendar dob = Calendar.getInstance();
        dob.setTime(date);
        int day = dob.get(Calendar.DAY_OF_MONTH);
        int month = dob.get(Calendar.MONTH);
        int year = dob.get(Calendar.YEAR);

        return getAge(year + "-" + (month + 1) + "-" + day);
    }

    /**
     * Gets current time.
     *
     * @return Ex. : 19:05:33
     */
    public String getCurrentTimeToShow() {
        String time;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        int second = mcurrentTime.get(Calendar.SECOND);
        String hrs;
        String mins;
        if (hour < 10)
            hrs = "0" + hour;
        else
            hrs = String.valueOf(hour);

        if (minute < 10)
            mins = "0" + minute;
        else
            mins = String.valueOf(minute);

        time = hrs + ":" + mins + ":" + second;

        return time;
    }

    /**
     * Gets current date.
     *
     * @param separator char allowed separators are .,/:
     * @return current date
     * @throws Exception the exception
     */
    public String getCurrentDate(char separator) throws Exception {
        String formattedDate, month, dateof;
        Calendar mcurrentTime = Calendar.getInstance();

        int mYear = mcurrentTime.get(Calendar.YEAR);
        int mMonth = mcurrentTime.get(Calendar.MONTH) + 1;
        int mDay = mcurrentTime.get(Calendar.DAY_OF_MONTH);

        if (mMonth < 10) {
            month = "0" + (mMonth + 1);
        } else {
            month = String.valueOf(mMonth + 1);
        }

        if (mDay < 10) {
            dateof = "0" + mDay;
        } else {
            dateof = String.valueOf(mDay);
        }

        switch (separator) {
            case SEPARATOR_1:
                formattedDate = "" + dateof + SEPARATOR_1 + month + SEPARATOR_1 + mYear;
                return formattedDate;

            case SEPARATOR_2:
                formattedDate = "" + dateof + SEPARATOR_2 + month + SEPARATOR_2 + mYear;
                return formattedDate;

            case SEPARATOR_3:
                formattedDate = "" + dateof + SEPARATOR_3 + month + SEPARATOR_3 + mYear;
                return formattedDate;

            case SEPARATOR_4:
                formattedDate = "" + dateof + SEPARATOR_4 + month + SEPARATOR_4 + mYear;
                return formattedDate;

            default:
                formattedDate = "Invalid Separator";
                return formattedDate;
            //throw new InvalidSeparatorException("Invalid Separator");
        }
    }

    /**
     * Method will compare given time with current time
     *
     * @param time the time
     * @return string string
     */
    public String compareTime(String time) {
        String timeBetween = null;
        try {
            Date a = null;
            timeBetween = "";

            try {
                Calendar now = Calendar.getInstance();
                try {
                    a = getTime(time);
                } catch (Exception e) {
                    //  Auto-generated catch block
                    e.printStackTrace();
                }
                int day = now.get(Calendar.DAY_OF_MONTH);
                int month = now.get(Calendar.MONTH);
                int year = now.get(Calendar.YEAR);
                Calendar previous = Calendar.getInstance();
                previous.setTime(a);
                previous.set(year, month, day);
                Calendar previous1 = Calendar.getInstance();
                previous1.setTime(a);
                previous1.set(year, month, day);
                previous1.add(Calendar.MINUTE, -30);
                Date x = now.getTime();
                if (x.after(previous1.getTime()) && x.before(previous.getTime())) {
                    timeBetween = "true";
                    CommonMethods.showLogs(" get in between time ",
                            " get in between time   " + now.getTime() + "  "
                                    + previous1.getTime() + "  "
                                    + previous.getTime() + "  " + true);

                } else {
                    CommonMethods.showLogs(" get in between time ", "  " + now.getTime() + "  "
                            + previous1.getTime() + "  " + previous.getTime()
                            + "  " + false);
                    timeBetween = "false";
                }
            } catch (Exception e) {
                // : handle exception
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeBetween;
    }

    /**
     * Gets time.
     *
     * @param args the args
     * @return the time
     * @throws Exception the exception
     */
    public Date getTime(String args) throws Exception {
        Date time = null;
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date date = parseFormat.parse(args);
            String t = displayFormat.format(date);
            time = displayFormat.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * Get time string.
     *
     * @param hours   the hours
     * @param minutes the minutes
     * @return string time
     */
    public String getTime(int hours, int minutes) {
        String time;
        String hrs;
        String mins;
        if (hours < 10)
            hrs = "0" + hours;
        else
            hrs = String.valueOf(hours);

        if (minutes < 10)
            mins = "0" + minutes;
        else
            mins = String.valueOf(minutes);
        time = hrs + ":" + mins;


        return time;
    }

    /**
     * Seconds to minute int.
     *
     * @param seconds BigDecimal
     * @return int int
     */
    public int secondsToMinute(long seconds) {
        // long longVal = seconds;
        int hours = (int) seconds / 3600;
        int remainder = (int) seconds - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        return mins;
    }

    /**
     * Mili sec to date string string.
     *
     * @param miliseconds the miliseconds
     * @return the string
     */
    public String miliSecToDateString(long miliseconds) {
        return (new Date(miliseconds)).toString();
    }

    /**
     * Gets current time.
     *
     * @return the current time
     */
    public String getCurrentTime() {
        Date today = Calendar.getInstance().getTime();
        return timeFormat.format(today);
    }

    /**
     * Compare dates string.
     *
     * @param date  the date
     * @param date1 the date 1
     * @return the string
     * @throws ParseException the parse exception
     */
    public String compareDates(String date, String date1) {
        String newdate = null;
        try {
            newdate = "";
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date d1 = null;
            try {
                d1 = sdf.parse(date);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Date d2 = null;
            try {
                d2 = sdf.parse(date1);
            } catch (java.text.ParseException e) {
                //  Auto-generated catch block
                e.printStackTrace();
            }
            if (compareTo(d1, d2) < 0) {
                newdate = date1;
                System.out.println("d1 is before d2");
            } else if (compareTo(d1, d2) > 0) {
                System.out.println("d1 is after d2");
                newdate = date;
            } else {
                newdate = "0";
                System.out.println("d1 is equal to d2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newdate;
    }

    /**
     * Compare to long.
     *
     * @param date1 the date 1
     * @param date2 the date 2
     * @return the long
     */
//// TODO: 8/26/2016 return difference between two dates
    public long compareTo(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    /**
     * Gets formatted date.
     *
     * @param context        the context
     * @param smsTimeInMilis the sms time in milis
     * @return the formatted date
     */
    public String getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return timeFormat.format(smsTimeInMilis).replace("am", "AM").replace("pm", "PM");
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday ";// + dateFormat.format( smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return dateWithoutYear.format(smsTimeInMilis);
        } else {
            return dateWithoutYear.format(smsTimeInMilis);
        }
    }

    /**
     * Gets date in today yesterday formate.
     *
     * @param context        the context
     * @param smsTimeInMilis the sms time in milis
     * @return the date in today yesterday formate
     */
    public String getDateInTodayYesterdayFormate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return " today";
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday ";// + dateFormat.format( smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return dateWithoutYear.format(smsTimeInMilis);
        } else {
            return dateWithoutYear.format(smsTimeInMilis);
        }
    }

    /**
     * Gets current time stamp.
     *
     * @return the current time stamp
     */
    public String getCurrentTimeStamp() {
        long l = System.currentTimeMillis();
        return String.valueOf(l);
    }

    /**
     * Gets long current time stamp.
     *
     * @return the long current time stamp
     */
    public long getLongCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * Gets integer time stamp.
     *
     * @return the integer time stamp
     */
    public int getIntegerTimeStamp() {
        int i = (int) (new Date().getTime() / 1000);
        System.out.println("Integer : " + i);
        return i;
    }

    /**
     * Gets current time from int.
     *
     * @param a the a
     * @return the current time from int
     */
    public String getCurrentTimeFromInt(int a) {
        Date b = new Date(((long) a) * 1000L);
        return dateFormat.format(b);
    }
}