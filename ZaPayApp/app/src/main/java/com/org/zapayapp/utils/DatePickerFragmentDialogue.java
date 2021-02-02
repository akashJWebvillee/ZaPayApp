package com.org.zapayapp.utils;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.org.zapayapp.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerFragmentDialogue extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Context context;
    private String from;
    private WVDateLib wvDateLib;
    private DatePickerCallback datePickerCallback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        super.onCreate(savedInstanceState);
        String show = "";
        String strDate = "";
        int strMin, strMax;
        if (getArguments() != null) {
            show = getArguments().getString(context.getResources().getString(R.string.show));
            from = getArguments().getString("from");
            strDate = getArguments().getString("date");
            strMin = getArguments().getInt("mindate");
            strMax = getArguments().getInt("maxdate");
        }
        // Locale.setDefault(Locale.ENGLISH);
        wvDateLib = new WVDateLib(context);
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        //condition to show dates visible from current date to future date
        String mon;
        if (month < 10) {
            mon = "0" + month;
        } else {
            mon = String.valueOf(month);
        }
        String date;
        if (day < 10) {
            date = "0" + day;
        } else {
            date = String.valueOf(day);
        }
        if (strDate != null && strDate.length() > 0 && getActivity() != null) {
            try {

                //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-mm-dd", Locale.ENGLISH);

                Date d = sdf.parse(strDate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);


                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH);
                int dy = cal.get(Calendar.DAY_OF_MONTH);
                Long l = wvDateLib.getTimestampOfDate(y, m + 1, dy, 0, 0, 0);
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), R.style.MyAlertDialogStyle, this, year, Integer.valueOf(mon), Integer.valueOf(date));
                mDatePicker.getDatePicker().setMinDate(l);
                setDatePickerLocale(mDatePicker);
                return mDatePicker;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (show != null && show.equals(context.getResources().getString(R.string.max_current)) && getActivity() != null) {
            long l = System.currentTimeMillis();
            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), R.style.MyAlertDialogStyle, this, year, Integer.valueOf(mon), Integer.valueOf(date));
            mDatePicker.getDatePicker().setMaxDate(l);
            setDatePickerLocale(mDatePicker);
            return mDatePicker;
        } else if (show != null && show.equals(context.getResources().getString(R.string.min_current)) && getActivity() != null) {//condition to show dates visible from current date to future date
            long l = System.currentTimeMillis();
            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), R.style.MyAlertDialogStyle, this, year, Integer.valueOf(mon), Integer.valueOf(date));
            mDatePicker.getDatePicker().setMinDate(l);
            setDatePickerLocale(mDatePicker);
            return mDatePicker;
        } else if (show != null && show.equals(context.getResources().getString(R.string.both)) && getActivity() != null) {//condition to show dates visible from past date to current date
            DatePickerDialog mDatePicker;
            mDatePicker = new DatePickerDialog(getActivity(), R.style.MyAlertDialogStyle, this, year, Integer.valueOf(mon), Integer.valueOf(date));
            long now = System.currentTimeMillis() - 1000;
            mDatePicker.getDatePicker().setMinDate(now + (24 * 60 * 60 * 1000));
            mDatePicker.getDatePicker().setMaxDate(now + (1000 * 60 * 60 * 24 * 7));

            setDatePickerLocale(mDatePicker);
            return mDatePicker;
        } else if (show != null && show.equals(context.getResources().getString(R.string.current_month)) && getActivity() != null) {//condition to show dates visible only current month date
            DatePickerDialog mDatePicker;
            mDatePicker = new DatePickerDialog(getActivity(), R.style.MyAlertDialogStyle, this, year, Integer.valueOf(mon), Integer.valueOf(date));
            /*long now = System.currentTimeMillis() - 1000;
            mDatePicker.getDatePicker().setMinDate(now + (24 * 60 * 60 * 1000));

            Calendar calendar = Calendar.getInstance();  // this is default system date
            //mDatePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());  //set min date                 // set today's date as min date
            calendar.add(Calendar.DAY_OF_MONTH, 30); // add date to 30 days later
            mDatePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis()); //set max date
            setDatePickerLocale(mDatePicker);*/

            String payDate = getArguments().getString("DATE");

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date date1 = formatter.parse(payDate);
                assert date1 != null;
                calendar.setTime(date1);
                mDatePicker.getDatePicker().setMinDate(calendar.getTimeInMillis()+(24 * 60 * 60 * 1000));

                calendar.add(Calendar.DAY_OF_MONTH, 30); // add date to 30 days later
                mDatePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis()); //set max date
                setDatePickerLocale(mDatePicker);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return mDatePicker;
        }  else {// no condition to show date
            // if (getActivity() != null)
            return new DatePickerDialog(getActivity(), R.style.MyAlertDialogStyle, this, year, Integer.valueOf(mon), Integer.valueOf(date));
        }
    }

    private void setDatePickerLocale(DatePickerDialog mDatePicker) {
        /*if (getActivity() != null) {
            mDatePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getActivity().getResources().getString(R.string.date_ok), mDatePicker);
            mDatePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getActivity().getResources().getString(R.string.date_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }*/
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        String formattedDate = sdf.format(c.getTime());
        CommonMethods.showLogs("date picker", formattedDate);
        if (datePickerCallback != null) {
            try {
                datePickerCallback.datePickerCallback(formattedDate, year, month + 1, day, from);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // initialize interface
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            try {
                this.datePickerCallback = (DatePickerCallback) a;
            } catch (final ClassCastException e) {
                throw new ClassCastException(a.toString() + " must implement OnCompleteListener");
            }
        }
    }

    //interface
    public interface DatePickerCallback {
        void datePickerCallback(String selectedDate, int year, int month, int day, String from) throws ParseException;
    }







}
