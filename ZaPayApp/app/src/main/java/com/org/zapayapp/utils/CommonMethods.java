package com.org.zapayapp.utils;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;

public class CommonMethods {

    /**
     * Gets color wrapper.
     *
     * @param context the context
     * @param id      the id
     * @return the color wrapper
     */
    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    /**
     * Gets drawable wrapper.
     *
     * @param context the context
     * @param id      the id
     * @return the drawable wrapper
     */
    public static Drawable getDrawableWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getDrawable(id, null);
        } else {
            //noinspection deprecation
            return context.getResources().getDrawable(id);
        }
    }

    /**
     * Open keyboard.
     *
     * @param context the context
     * @param view    the view
     */
    public static void openKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Close keyboard.
     *
     * @param context the context
     * @param view    the view
     */
    public static void closeKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**t
     * Close keyboard.
     *
     * @param context the context
     */
    public static void closeKeyboard(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && context.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Invite contact.
     *
     * @param context the context
     */
    public static void inviteContact(Context context) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "My app name");
            String strShareMessage = "\nLet me recommend you this application\n\n";
            strShareMessage = strShareMessage + "https://play.google.com.admin/store/apps/details?id=" + context.getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            context.startActivity(Intent.createChooser(i, "Invite a friend"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy text.
     *
     * @param context the context
     * @param message the message
     */
    public static void copyText(Context context, String message) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text label", message);
        if (clipboard != null)
            clipboard.setPrimaryClip(clip);
    }

    /**
     * Paste text string.
     *
     * @param context the context
     * @return the string
     */
    public static String pasteText(Context context) {
        CharSequence pasteString = "";
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.getPrimaryClip() != null) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            pasteString = item.getText();
        }
        if (pasteString != null) {
            // use it
            return pasteString.toString();
        }
        return "";
    }

    /**
     * Clear text.
     *
     * @param context the context
     */
    public static void clearText(Context context) {
        ClipboardManager clipBoard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("", "");
        if (clipBoard != null)
            clipBoard.setPrimaryClip(data);
    }

    /**
     * Share media.
     *
     * @param context  the context
     * @param filePath the file path
     */
    public static void shareMedia(Context context, String filePath) {
        try {
            if (filePath != null && filePath.length() > 0) {
                final File file = new File(filePath); //for sharing in image from sdcard
                Uri uri = Uri.fromFile(file);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(shareIntent, "app name"));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open pdf.
     *
     * @param context  the context
     * @param filePath the file path
     */
    public static void openPdf(Context context, String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    /**
     * Share location.
     *
     * @param context the context
     * @param lat     the lat
     * @param lng     the lng
     * @param name    the name
     */
    public static void shareLocation(Context context, double lat, double lng, String name) {
        String geoUri = "http://maps.google.com.admin/maps?q=loc:" + lat + "," + lng + " (" + name + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        context.startActivity(intent);
    }

    /**
     * Strike through text.
     *
     * @param price the price
     */
    public static void strikeThroughText(TextView price) {
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * Total payment double.
     *
     * @param quantity the quantity
     * @param price    the price
     * @return the double
     */
    public static double totalPayment(int quantity, double price) {
        return quantity * price;
    }

    /**
     * Show logs.
     *
     * @param TAG the tag
     * @param msg the msg
     */
    public static void showLogs(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    /**
     * Show toast.
     *
     * @param context the context
     * @param msg     the msg
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * From html spanned.
     *
     * @param html the html
     * @return the spanned
     */
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    /**
     * Gets screen width.
     *
     * @return the screen width
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * Gets screen height.
     *
     * @return the screen height
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * Round two decimals double.
     *
     * @param d the d
     * @return the double
     */
    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    /**
     * Capitalize first letter string.
     *
     * @param original the original
     * @return the string
     */
    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    /**
     * Capitalize string.
     *
     * @param input the input
     * @return the string
     */
    public static String capitalize(@NonNull String input) {

        String[] words = input.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (i > 0 && word.length() > 0) {
                builder.append(" ");
            }

            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
        }
        return builder.toString();
    }

    /**
     * Gets initial from name.
     *
     * @param fullNameText the full name text
     * @return the initial from name
     */
    public static String getInitialFromName(String fullNameText) {
        String initials = "";
        if (fullNameText != null && fullNameText.length() > 0) {
            String[] text = fullNameText.split(" ");
            if (text.length > 1) {
               /* for (int i = 0; i < text.length;i++) {
                    initials =  initials +text[i].charAt(0);
                }*/
                initials = text[0].charAt(0) + " " + text[text.length - 1].charAt(0);
            } else {
                initials = text[0].charAt(0) + "";
            }
        }

        return initials.toUpperCase();
    }

    /**
     * First upper case string string.
     *
     * @param text the text
     * @return the string
     */
    public static String firstUpperCaseString(String text) {
        try {
            StringBuilder result = new StringBuilder(text.length());
            String[] words = text.split("\\ ");
            for (int i = 0; i < words.length; i++) {
                result.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1)).append(" ");

            }
            return result + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * First upper case sentence string.
     *
     * @param text the text
     * @return the string
     */
    public static String firstUpperCaseSentence(String text) {
        try {
            String upperString = text.substring(0, 1).toUpperCase() + text.substring(1);
            return upperString + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Sets margins.
     *
     * @param view   the view
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     */
    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    /**
     * Sets margins.
     *
     * @param context       the context
     * @param view          the view
     * @param dpValueLeft   the left
     * @param dpValueTop    the top
     * @param dpValueRight  the right
     * @param dpValueBottom the bottom
     */
    public static void setMarginInDp(Context context, View view, int dpValueLeft, int dpValueTop, int dpValueRight, int dpValueBottom) {

//        int dpValueTop = 8; // margin in dips
        float top = context.getResources().getDisplayMetrics().density;
        int marginTop = (int) (dpValueTop * top);

//        int dpValueBottom= 50; // margin in dips
        float bottom = context.getResources().getDisplayMetrics().density;
        int marginBottom = (int) (dpValueBottom * bottom);

        float left = context.getResources().getDisplayMetrics().density;
        int marginLeft = (int) (dpValueBottom * left);

        float right = context.getResources().getDisplayMetrics().density;
        int marginRight = (int) (dpValueBottom * right);

        int buttonHeight = 22; // margin in dips
        float height = context.getResources().getDisplayMetrics().density;
        int btnheight = (int) (buttonHeight * height);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        view.setLayoutParams(params);
        view.requestLayout();
    }

    /**
     * Enable text view scroll.
     *
     * @param view the view
     */
    public static void enableTextViewScroll(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setMovementMethod(new ScrollingMovementMethod());
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    public static String getCurrencyValue(String number) {
        DecimalFormat formatter = new DecimalFormat("#,##,###.##");
        return formatter.format(Double.valueOf(number));
    }

    public static String roundNumber(String strValue, int precision) {
        try {
            double value = Double.parseDouble(strValue);
            int scale = (int) Math.pow(10, precision);
            double val = ((double) Math.round(value * scale) / scale);
            strValue = val + "";
            if (precision != 1) {
                if (strValue.endsWith(".0") || strValue.endsWith(".00") || strValue.endsWith(".000") || strValue.endsWith(".0000") || strValue.endsWith(".00000")) {
                    strValue = ((int) val) + "";
                }
            } else {
                if (strValue.endsWith(".00") || strValue.endsWith(".000") || strValue.endsWith(".0000") || strValue.endsWith(".00000")) {
                    strValue = ((int) val) + "";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return strValue;
    }

    public static void darkenStatusBar(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //activity.getWindow().setStatusBarColor(darkenColor(ContextCompat.getColor(activity, color)));
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }

    // Code to darken the color supplied (mostly color of toolbar)
    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String getMaskedEmailId(String emailId) {
        return emailId.replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*");
    }

    public static String getMaskedMobileNumber(String mobNumber) {
        return mobNumber.replaceAll("\\d(?=(?:\\D*\\d){4})", "*");
    }

    public static String getBase64EncodedImage(String imageURL) throws IOException {
        Bitmap bitmap;
        URL newurl = null;
        String base64 = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            newurl = new URL(imageURL);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }


    public static String setDigitAfterDecimalValue(double value,int count){
        String value1 = null;
        if (count==1){
             value1=  new DecimalFormat("0.0").format(value);
        }else if (count==2){
             value1=  new DecimalFormat("0.00").format(value);
        }else if (count==3){
             value1=  new DecimalFormat("0.000").format(value);
        }else {
            value1=  new DecimalFormat("0.00").format(value);
        }

      return value1;
    }


    public static String roundedDoubleWithoutZero(double value) {
        return roundNumber(String.valueOf((roundStringTwoDecimals(value))), 2);
    }
    public static double roundStringTwoDecimals(double d) {
        double dValue = 0;
        try {
            String dd = String.valueOf(d);
            Number value = NumberFormat.getInstance(new Locale("en", "US")).parse(enToAr(dd));
            assert value != null;
            dValue = value.doubleValue();
            dValue = roundTwoDecimals(dValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dValue;

    }


    private static String enToAr(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (isNonstandardDigit(ch)) {
                int numericValue = Character.getNumericValue(ch);
                if (numericValue >= 0) {
                    builder.append(numericValue);
                }
            } else {
                builder.append(ch);
            }
        }

        return builder.toString().replace("٫", ".");
    }

    private static boolean isNonstandardDigit(char ch) {
        return Character.isDigit(ch) && !(ch >= '0' && ch <= '9');
    }

    public static String fmt(float d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

}
