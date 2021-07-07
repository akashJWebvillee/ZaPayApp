package com.org.zapayapp.utils;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.org.zapayapp.R;

public class WRuntimePermissions {
    public static final int REQUEST_CODE_SMS = 10;
    public static final int REQUEST_CODE_CAMERA = 20;
    public static final int REQUEST_CODE_STORAGE = 30;
    public static final int REQUEST_CODE_MICROPHONE = 40;
    public static final int REQUEST_CODE_LOCATION = 50;
    public static final int REQUEST_CODE_PHONE = 60;
    public static final int REQUEST_CODE_CONTACTS = 70;
    public static final int REQUEST_CODE_CALENDER = 80;
    public static final int REQUEST_CODE_SENSORS = 90;

    public static final String SMS_MSG = "Please allow permission to access sms.";
    public static final String SMS_MSG_DESC = "Please allow permission to access sms to read and send sms.";

    public static final String CAMERA_MSG = "Please allow permission to access camera.";
    public static final String CAMERA_MSG_DESC = "Please allow permission to access camera to upload video and images.";

    public static final String STORAGE_MSG = "Please allow permission to access storage.";
    public static final String STORAGE_MSG_DESC = "Please allow permission to access external storage to save video and images.";

    public static final String MICROPHONE_MSG = "Please allow permission to access microphone.";
    public static final String MICROPHONE_MSG_DESC = "Please allow permission to access microphone to record audios.";

    public static final String LOCATION_MSG = "Please allow permission to access location.";
    public static final String LOCATION_MSG_DESC = "Please allow permission to access location to get current location.";

    public static final String PHONE_MSG = "Please allow permission to access phone.";
    public static final String PHONE_MSG_DESC = "Please allow permission to access phone to get phone state,read and write call logs.";

    public static final String CONTACT_MSG = "Please allow permission to access contact.";
    public static final String CONTACT_MSG_DESC = "Please allow permission to access contact to read and write contacts.";

    public static final String CALENDER_MSG = "Please allow permission to access calender.";
    public static final String CALENDER_MSG_DESC = "Please allow permission to access calender to read and write calender.";

    public static final String SENSORS_MSG = "Please allow permission to access sensors.";
    public static final String SENSORS_MSG_DESC = "Please allow permission to access sensors to get body sensors.";

    private AppCompatActivity activity;

    /*
     * @param activity the activity
     */
    public WRuntimePermissions(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * Check permission if already allowed for sms boolean.
     * Group SMS permission include -
     * android.permission.SEND_SMS
     * android.permission.RECEIVE_SMS
     * android.permission.READ_SMS
     * android.permission.RECEIVE_WAP_PUSH
     * android.permission.RECEIVE_MMS
     * android.permission.READ_CELL_BROADCAST
     *
     * @return the boolean
     */
//// : 11/4/2016  Permission for Sms
    public boolean checkPermissionForSms() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permission for sms.
     */
    public void requestPermissionForSms() {
     /*   if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            //     CommonMethods.showToast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", CommonMethods.showToast.LENGTH_LONG).show();
        } else {*/
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_SMS);
        //}
    }

    /**
     * Request permission if denied for sms denied boolean.
     *
     * @return the boolean
     */
    public boolean requestPermissionForSmsDenied() {
        // !ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName
        boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS);
        if (result) {
            return true;
        } else {
            return false;
        }
    }

    //// : 11/4/2016 end

    /**
     * Check permission if already allowed for camera boolean.
     * To allow permission for CAMERA group use this permission
     * Group CAMERA permission include -
     * android.permission.CAMERA
     *
     * @return the boolean
     */
//// : 11/4/2016  Permission for Camera
    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permission for camera.
     */
    public void requestPermissionForCamera() {
      /*  if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            alertDialogShowForPermission(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }
        //     CommonMethods.showToast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", CommonMethods.showToast.LENGTH_LONG).show();
        else {*/
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        // }
    }

    /**
     * Request permission for camera denied boolean.
     *
     * @return the boolean
     */
    public boolean requestPermissionForCameraDenied() {
        boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA);
        if (result) {
            return true;
        } else {
            return false;
        }
    }
    //// : 11/4/2016 end

    /**
     * Check permission if already allowed for external storage boolean.
     * To allow permission for STORAGE group use this permission
     * Group STORAGE permission include -
     * android.permission.READ_EXTERNAL_STORAGE
     * android.permission.WRITE_EXTERNAL_STORAGE
     *
     * @return the boolean
     */
//// : 11/4/2016  Permission for Storage
    public boolean checkPermissionForStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permission for external storage.
     */
    public void requestPermissionForStorage() {
       /* if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //    CommonMethods.showToast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", CommonMethods.showToast.LENGTH_LONG).show();
        } else {*/
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
        // }
    }

    /**
     * Request permission for storage denied boolean.
     *
     * @return the boolean
     */
    public boolean requestPermissionForStorageDenied() {
        // !ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName
        boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean result1 = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result && result1) {
            return true;
        } else {
            return false;
        }
    }
    //// : 11/4/2016 end

    /**
     * Check permission if already allowed for micro phone boolean.
     * To allow permission for MICROPHONE group use this permission
     * Group MICROPHONE permission include -
     * android.permission.RECORD_AUDIO
     *
     * @return the boolean
     */
//// : 11/4/2016  Permission for Audio(Microphone)
    public boolean checkPermissionForMicroPhone() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permission for micro phone.
     */
    public void requestPermissionForMicroPhone() {
       /* if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            //    CommonMethods.showToast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", CommonMethods.showToast.LENGTH_LONG).show();
        } else {*/
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_MICROPHONE);
        //}
    }

    /**
     * Request permission for micro phone denied boolean.
     *
     * @return the boolean
     */
    public boolean requestPermissionForMicroPhoneDenied() {
        // !ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName
        boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO);
        if (result) {
            return true;
        } else {
            return false;
        }
    }
    //// : 11/4/2016 end*/

    /**
     * Check permission if already allowed for location boolean.
     * To allow permission for LOCATION group use this permission
     * Group LOCATION permission include -
     * android.permission.ACCESS_FINE_LOCATION
     * android.permission.ACCESS_COARSE_LOCATION
     *
     * @return the boolean
     */
//// : 11/4/2016  Permission for Location
    public boolean checkPermissionForLocation() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permission for location.
     */
    public void requestPermissionForLocation() {
       /* if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //    CommonMethods.showToast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", CommonMethods.showToast.LENGTH_LONG).show();
           // ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE);
        } else {*/
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_LOCATION);
        //}
    }

    /**
     * Request permission for location denied boolean.
     *
     * @return the boolean
     */
    public boolean requestPermissionForLocationDenied() {
        // !ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName
        boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        boolean result1 = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        boolean result2 = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if (result && result1 && result2) {
            return true;
        } else {
            return false;
        }
    }
    //// : 11/4/2016 end

    /**
     * Check permission  if already allowed for phone boolean.
     * To allow permission for PHONE group use this permission
     * Group PHONE permission include -
     * android.permission.READ_PHONE_STATE
     * android.permission.CALL_PHONE
     * android.permission.READ_CALL_LOG
     * android.permission.WRITE_CALL_LOG
     * android.permission.USE_SIP
     * android.permission.PROCESS_OUTGOING_CALLS
     * com.android.voicemail.permission.ADD_VOICEMAIL
     *
     * @return the boolean
     */
    //// : 11/4/2016  Permission for Phone
    public boolean checkPermissionForPhone() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permission for phone.
     */
    public void requestPermissionForPhone() {
       /* if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)) {
            //    CommonMethods.showToast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", CommonMethods.showToast.LENGTH_LONG).show();
        } else {*/
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_PHONE);
        // }
    }

    /**
     * Request permission if already allowed for phone denied boolean.
     *
     * @return the boolean
     */
    public boolean requestPermissionForPhoneDenied() {
        // !ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName
        boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE);
        if (result) {
            return true;
        } else {
            return false;
        }
    }

    //// : 11/4/2016 end*/


    /**
     * Check permission if already allowed for contacts boolean.
     * To allow permission for CONTACT group use this permission
     * Group CONTACT permission include -
     * android.permission.READ_CONTACTS
     * android.permission.WRITE_CONTACTS
     * android.permission.READ_CONTACTS
     *
     * @return the boolean
     */
//// : 11/4/2016  Permission for Contacts
    public boolean checkPermissionForContacts() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permission for contacts.
     */
    public void requestPermissionForContacts() {
       /* if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
            //     CommonMethods.showToast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", CommonMethods.showToast.LENGTH_LONG).show();
        } else {*/
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_CONTACTS);
        //}
    }

    /**
     * Request permission for contacts denied boolean.
     *
     * @return the boolean
     */
    public boolean requestPermissionForContactsDenied() {
        // !ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName
        boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS);
        if (result) {
            return true;
        } else {
            return false;
        }
    }
    //// : 11/4/2016 end*/

    /**
     * Check permission if already allowed for calender boolean.
     * To allow permission for CALENDER group use this permission
     * Group CALENDER permission include -
     * android.permission.READ_CALENDER
     * android.permission.WRITE_CALENDER
     *
     * @return the boolean
     */
//// : 11/4/2016  Permission for Calender
    public boolean checkPermissionForCalender() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permission for calender.
     */
    public void requestPermissionForCalender() {
   /*     if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALENDAR)) {
            //    CommonMethods.showToast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", CommonMethods.showToast.LENGTH_LONG).show();
        } else {*/
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_CODE_CALENDER);
        //}
    }

    /**
     * Request permission for calender denied boolean.
     *
     * @return the boolean
     */
    public boolean requestPermissionForCalenderDenied() {
        // !ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName
        boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALENDAR);
        if (result) {
            return true;
        } else {
            return false;
        }
    }

    //// : 11/4/2016 end*/

    /**
     * Check permission if already allowed for sensor boolean.
     * To allow permission for SENSOR group use this permission
     * Group SENSOR permission include -
     * android.permission.BODY_SENSORS
     *
     * @return the boolean
     */
//// : 11/4/2016 Permission for Sensor
  /*  public boolean checkPermissionForSensor() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.BODY_SENSORS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }*/

    /**
     * Request permission for sensor.
     */
    /* public void requestPermissionForSensor() {
     *//*      if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BODY_SENSORS)) {
            //    CommonMethods.showToast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", CommonMethods.showToast.LENGTH_LONG).show();
        } else {*//*
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BODY_SENSORS}, REQUEST_CODE_SENSORS);
       // }
    }
*/
    /**
     * Request permission for sensor denied boolean.
     *
     * @return the boolean
     */
   /* public boolean requestPermissionForSensorDenied() {
        // !ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName
        boolean result = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BODY_SENSORS);
        if (result) {
            return true;
        } else {
            return false;
        }
    }*/
    //// : 11/4/2016 end*/


    /**
     * Open settings dialog.
     *
     * @param message the message - reason to explain why we need this permission
     * @param context the context     *
     */
    public void openSettingsDialog(String message, final Context context) {
        // AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

        alertbox.setMessage(message);
        alertbox.setTitle("Alert");

        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertbox.create();
        if (!activity.isFinishing()) {
            if (!alert.isShowing()) {
                alert.show();
            }
        }
    }

    /**
     * Show alert dialog.
     *
     * @param message the message - reason to explain why we need this permission
     * @param value   the request code of this permission
     * @param context the context
     */
    public void showAlertDialog(String message, final int value, final Context context) {
        // AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alertbox.setMessage(message);
        alertbox.setTitle("Alert");
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                if (value == REQUEST_CODE_SMS) {
                    requestPermissionForSms();
                } else if (value == REQUEST_CODE_CAMERA) {
                    requestPermissionForCamera();
                } else if (value == REQUEST_CODE_STORAGE) {
                    requestPermissionForStorage();
                } else if (value == REQUEST_CODE_MICROPHONE) {
                    requestPermissionForMicroPhone();
                } else if (value == REQUEST_CODE_LOCATION) {
                    requestPermissionForLocation();
                } else if (value == REQUEST_CODE_PHONE) {
                    requestPermissionForPhone();
                } else if (value == REQUEST_CODE_CONTACTS) {
                    requestPermissionForContacts();
                } else if (value == REQUEST_CODE_CALENDER) {
                    requestPermissionForCalender();
                } else if (value == REQUEST_CODE_SENSORS) {
                    // requestPermissionForSensor();
                }
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertbox.create();
        if (!activity.isFinishing()) {
            if (!alert.isShowing()) {
                alert.show();
            }
        }
    }
}
