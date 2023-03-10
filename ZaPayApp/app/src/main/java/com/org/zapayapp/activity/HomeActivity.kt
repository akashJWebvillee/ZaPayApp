package com.org.zapayapp.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.org.zapayapp.R
import com.org.zapayapp.chat.ChatActivity
import com.org.zapayapp.utils.*
import com.org.zapayapp.webservices.APICallback
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.NumberFormatException
import java.util.*


class HomeActivity : BaseActivity(), View.OnClickListener, APICallback {
    private var homeLLLend: LinearLayout? = null
    private var homeLLBorrow: LinearLayout? = null
    private var intent1: Intent? = null
    private var titleTV: TextView? = null
    private var isClickable = false
    private var csv_status = false
    private var TAG =HomeActivity::class.java.simpleName
    private lateinit var context1: Context
    var cursor: Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (SharedPref.getPrefsHelper()
                .getPref<Any?>(Const.Var.FIREBASE_DEVICE_TOKEN) != null && SharedPref.getPrefsHelper()
                .getPref<Any>(
                    Const.Var.FIREBASE_DEVICE_TOKEN
                ).toString().length > 0
        ) {
            callAPIUpdateDeviceInfo()
        }
        init()
        initAction()
        notificationIntent
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            contacts
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            CommonMethods.showToast(context1, "Permission Denied\n$deniedPermissions")
            readContacts()
        }
    }

    // data is a array of String type which is
    // used to store Number ,Names and id.
    private val contacts: Unit
        get() {
            CoroutineScope(Dispatchers.IO).launch {
                createCSV()
                CoroutineScope(Dispatchers.Main).launch {
                    exportCSV()
                }
            }

        }


    private fun createCSV() {
        try {
            var writer: CSVWriter? = null
            try {
                val fileName: String
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
//            {
//                fileName =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS  +"/zapay_contacts.csv" ).absolutePath
//            }else{
////                fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
////                        .toString() + "/zapay_contacts.csv"
//                fileName = context1.cacheDir.absolutePath+"/zapay_contacts.csv"
//
//            }

                fileName = context1.cacheDir.absolutePath+"/zapay_contacts.csv"

                writer = CSVWriter(
                    FileWriter(
                        fileName
                    )
                )
            } catch (e1: IOException) {
                // TODO Auto-generated catch block
                e1.printStackTrace()
            }
            var displayName: String
            var number: String?
            var countyCode:String
            var _id: Long
            val phoneNumberUtil = PhoneNumberUtil.createInstance(context1)
            val columns = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
            )
            writer!!.writeColumnNames() // Write column header
            val cursor: Cursor = context1.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                columns,
                null,
                null,
                ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
            )!!
            if (cursor.moveToFirst()) {
                do {
                    try {
                        _id =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)).toLong()
                        displayName =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                                .trim { it <= ' ' }
                        val number1 = getPrimaryNumber(_id)?.replace(" ","")
                            ?.replace("-","")
                            ?.replace("(","")
                            ?.replace(")","")

                        val phoneNumber = phoneNumberUtil.parse(number1,Locale.getDefault().country)
                        number = phoneNumber.nationalNumber.toString()
                        countyCode = phoneNumber.countryCode.toString()

                        if (number1.equals(number)) {
                            countyCode ="0"
                        }
                        writer.writeNext("$displayName/${number}/$countyCode".split("/".toRegex()).toTypedArray())

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                } while (cursor.moveToNext())
                csv_status = true
            } else {
                csv_status = false
            }
            try {
                if (writer != null) writer.close()
            } catch (e: IOException) {
                Log.w("Test", e.toString())
            }
        } catch (e:java.lang.Exception) {

        }

    } // Method  close.


    private fun exportCSV() {
        if (csv_status) {
            //CSV file is created so we need to Export that ...
            val CSVFile:File

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
//            {
//                CSVFile =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS  +"/zapay_contacts.csv" )
//
//            }else{
//                CSVFile = File(
//                    Environment.getExternalStorageDirectory().getAbsolutePath()
//                        .toString() + "/zapay_contacts.csv"
//                )
//            }

            CSVFile = File(context1.cacheDir.absolutePath+"/zapay_contacts.csv")

            callApiUpdateContacts(CSVFile)

        } else {
            Toast.makeText(
                context1,
                "Contacts unavailable.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun callApiUpdateContacts(file: File) {

        val requestBody: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val fileToUpload: MultipartBody.Part =
            MultipartBody.Part.createFormData("contact_file", file.name, requestBody)
        try {
            zapayApp.apiCallback = this
            val call = restAPI.postWithTokenMultiPartApi(
                SharedPref.getPrefsHelper().getPref<Any>(Const.Var.TOKEN).toString(),
                getString(R.string.api_user_mobile_contact_sync),
                fileToUpload
            )
            if (apiCalling != null) {
                apiCalling.isRunInBackground = false
                apiCalling.callAPI(
                    zapayApp,
                    call,
                    getString(R.string.api_user_mobile_contact_sync),
                    homeLLLend
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get primary Number of requested  id.
     *
     * @return string value of primary number.
     */
    private fun getPrimaryNumber(_id: Long): String? {
        var primaryNumber: String? = null
        try {
            val cursor: Cursor = context1.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE),
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + _id,  // We need to add more selection for phone type
                null,
                null
            )!!
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    when (cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))) {
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> primaryNumber =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> primaryNumber =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> primaryNumber =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> {}
                    }
                    if (primaryNumber != null) break
                }
            }
        } catch (e: Exception) {
            Log.i(TAG, "Exception $e")
        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
        return primaryNumber
    }

    override fun onStart() {
        super.onStart()
        isClickable = true
    }

    private fun init() {
        homeLLLend = findViewById(R.id.homeLLLend)
        homeLLBorrow = findViewById(R.id.homeLLBorrow)
        titleTV = findViewById(R.id.titleTV)
        AppCenter.start(
            application, "7c7f48b8-92b9-419a-842c-536b68581c02",
            Analytics::class.java, Crashes::class.java
        ) // add this to trace the crashlaytics
    }

    private fun initAction() {
        context1 = this
        homeLLLend!!.setOnClickListener(this)
        homeLLBorrow!!.setOnClickListener(this)
        if (SharedPref.getPrefsHelper()
                .getPref<Any?>(Const.Var.FIRST_NAME) != null && SharedPref.getPrefsHelper()
                .getPref<Any>(
                    Const.Var.FIRST_NAME
                ).toString().length > 0
        ) {
            titleTV!!.text = getString(R.string.hello) + " " + SharedPref.getPrefsHelper()
                .getPref<Any>(Const.Var.FIRST_NAME).toString()
        }
        readContacts()
    }

    private fun readContacts() {
        TedPermission.with(context1)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage(R.string.need_to_allow_this_contact_read)
            .setPermissions(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.homeLLLend ->                 //activity_status=0  //signup
                //activity_status=1  //updated profile
                //activity_status=2   //added bank account
                //activity_status=3   //verifyed bank account(ready to send request)
                if (Const.isUserDefaulter() == "1") {
                    showSimpleAlert(
                        getString(R.string.you_have_defaulter_msg),
                        getString(R.string.you_have_defaulter_msg)
                    )
                } else if (Const.isUserDefaulter() == "2") {
                    showSimpleAlert(
                        getString(R.string.payment_initiated_it_takes_time_to_confirm_the_payment),
                        getString(R.string.payment_initiated_it_takes_time_to_confirm_the_payment)
                    )
                } else {
                    if (SharedPref.getPrefsHelper()
                            .getPref<Any?>(Const.Var.ACTIVITY_STATUS) != null && SharedPref.getPrefsHelper()
                            .getPref<Any>(
                                Const.Var.ACTIVITY_STATUS
                            ).toString().length > 0
                    ) {
                        if (SharedPref.getPrefsHelper()
                                .getPref<Any>(Const.Var.ACTIVITY_STATUS).toString() != "0"
                        ) {
                            if (isClickable) {
                                isClickable = false
                                intent1 = Intent(this@HomeActivity, LendBorrowActivity::class.java)
                                intent1!!.putExtra("isBorrow", false)
                                startActivity(intent1)
                            }
                        } else {
                            showSimpleAlert(
                                getString(R.string.update_your_profile),
                                getString(R.string.update_your_profile)
                            )
                        }
                    }
                }
            R.id.homeLLBorrow -> if (Const.isUserDefaulter() == "1") {
                showSimpleAlert(
                    getString(R.string.you_have_defaulter_msg),
                    getString(R.string.you_have_defaulter_msg)
                )
            } else if (Const.isUserDefaulter() == "2") {
                showSimpleAlert(
                    getString(R.string.payment_initiated_it_takes_time_to_confirm_the_payment),
                    getString(R.string.payment_initiated_it_takes_time_to_confirm_the_payment)
                )
            } else {
                if (SharedPref.getPrefsHelper()
                        .getPref<Any?>(Const.Var.ACTIVITY_STATUS) != null && SharedPref.getPrefsHelper()
                        .getPref<Any>(
                            Const.Var.ACTIVITY_STATUS
                        ).toString().length > 0
                ) {
                    if (SharedPref.getPrefsHelper()
                            .getPref<Any>(Const.Var.ACTIVITY_STATUS).toString() != "0"
                    ) {
                        if (isClickable) {
                            isClickable = false
                            intent1 = Intent(this@HomeActivity, LendBorrowActivity::class.java)
                            intent1!!.putExtra("isBorrow", true)
                            startActivity(intent1)
                        }
                    } else {
                        showSimpleAlert(
                            getString(R.string.update_your_profile),
                            getString(R.string.update_your_profile)
                        )
                    }
                }
            }
        }
    }

    override fun useToolbar(): Boolean {
        return true
    }

    override fun useDrawerToggle(): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        setCurrentScreen(100)
        callAPIGetUserDetail()
        //callAPIGetBankAccountDetail();
        callAPICheckUserDefaulterStatus()
    }

    private fun callAPIUpdateDeviceInfo() {
        val token = SharedPref.getPrefsHelper().getPref<Any>(Const.Var.TOKEN).toString()
        try {
            if (SharedPref.getPrefsHelper()
                    .getPref<Any?>(Const.Var.FIREBASE_DEVICE_TOKEN) != null && SharedPref.getPrefsHelper()
                    .getPref<Any>(
                        Const.Var.FIREBASE_DEVICE_TOKEN
                    ).toString().length > 0
            ) {
                val values = apiCalling.getHashMapObject(
                    "device_type",
                    Const.KEY.DEVICE_TYPE,
                    "device_token",
                    SharedPref.getPrefsHelper().getPref<Any>(Const.Var.FIREBASE_DEVICE_TOKEN)
                        .toString(),
                    "device_id",
                    Const.getDeviceId(this@HomeActivity)
                )
                zapayApp.apiCallback = this
                //Call<JsonElement> call = restAPI.postApi(getString(R.string.api_update_device_info), values);
                val call = restAPI.postWithTokenApi(
                    token,
                    getString(R.string.api_update_device_info),
                    values
                )
                if (apiCalling != null) {
                    apiCalling.callAPI(
                        zapayApp,
                        call,
                        getString(R.string.api_update_device_info),
                        homeLLBorrow
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callAPIGetUserDetail() {
        val token = SharedPref.getPrefsHelper().getPref<Any>(Const.Var.TOKEN).toString()
        // String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMyIsImZpcnN0X25hbWUiOiJBc2hvayIsImxhc3RfbmFtZSI6Ikt1bWFyIiwiZW1haWwiOiJhc2hvay53ZWJ2aWxsZWVAZ21haWwuY29tIiwicm9sZSI6IjIiLCJ0aW1lc3RhbXAiOjE2MjgwNzA4NDd9.V7d9_Lv3QJRZ88JRG6Ghx-UctL-IV5MZOyeWpX31_ao";
        Log.e("token", "token========.....====$token")
        try {
            zapayApp.apiCallback = this
            val call = restAPI.getApiToken(token, getString(R.string.api_get_user_details))
            if (apiCalling != null) {
                apiCalling.callAPI(
                    zapayApp,
                    call,
                    getString(R.string.api_get_user_details),
                    homeLLBorrow
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callAPIGetBankAccountDetail() {
        val token = SharedPref.getPrefsHelper().getPref<Any>(Const.Var.TOKEN).toString()
        try {
            zapayApp.apiCallback = this
            val call = restAPI.getApiToken(token, getString(R.string.api_get_bank_account_details))
            if (apiCalling != null) {
                apiCalling.callAPI(
                    zapayApp,
                    call,
                    getString(R.string.api_get_bank_account_details),
                    homeLLBorrow
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callAPICheckUserDefaulterStatus() {
        val token = SharedPref.getPrefsHelper().getPref<Any>(Const.Var.TOKEN).toString()
        try {
            zapayApp.apiCallback = this
            val call =
                restAPI.getApiToken(token, getString(R.string.api_check_user_defaulter_status))
            if (apiCalling != null) {
                apiCalling.callAPI(
                    zapayApp,
                    call,
                    getString(R.string.api_check_user_defaulter_status),
                    homeLLBorrow
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun apiCallback(json: JsonObject, from: String) {
        if (from != null) {
            var status = 0
            var msg: String? = ""
            try {
                status = json["status"].asInt
                msg = json["message"].asString
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (from == resources.getString(R.string.api_get_user_details)) {
                Const.logMsg(json.toString())
                if (status == 200) {
                    if (json["data"].asJsonObject != null) {
                        val jsonObject = json["data"].asJsonObject
                        MySession.MakeSession(jsonObject)
                    }
                } else if (status == 401) {
                    showForceUpdate(
                        getString(R.string.session_expired),
                        getString(R.string.your_session_expired),
                        false,
                        "",
                        false
                    )
                } else {
                    showSimpleAlert(msg, "")
                }
            } else if (from == resources.getString(R.string.api_get_bank_account_details)) {
                if (status == 200) {
                    if (json["data"].asJsonObject != null) {
                        MySession.saveBankData(json["data"].asJsonObject)
                    }
                } else if (status == 401) {
                    showForceUpdate(
                        getString(R.string.session_expired),
                        getString(R.string.your_session_expired),
                        false,
                        "",
                        false
                    )
                }
            } else if (from == resources.getString(R.string.api_logout)) {
                if (status == 200) {
                    clearLogout()
                } else {
                    showSimpleAlert(msg, "")
                }
            } else if (from == resources.getString(R.string.api_update_device_info)) {
                if (status == 200) {
                } else if (status == 401) {
                    showForceUpdate(
                        getString(R.string.session_expired),
                        getString(R.string.your_session_expired),
                        false,
                        "",
                        false
                    )
                } else {
                    showSimpleAlert(msg, "")
                }
            } else if (from == resources.getString(R.string.api_check_user_defaulter_status)) {
                if (status == 200) {
                    if (json["data"].asJsonObject != null) {
                        val jsonObject = json["data"].asJsonObject
                        if (jsonObject["is_defaulter"].asString != null && jsonObject["is_defaulter"].asString.length > 0) {
                            val is_defaulter = jsonObject["is_defaulter"].asString
                            SharedPref.getPrefsHelper()
                                .savePref(Const.Var.IsDEFAULTER, is_defaulter)
                            //is_defaulter==1 defaulter
                            Log.e("is_defaulter", "is_defaulter===============$is_defaulter")
                            Log.e("is_defaulter", "is_defaulter===============$json")
                        }
                    }
                } else if (status == 401) {
                    showForceUpdate(
                        getString(R.string.session_expired),
                        getString(R.string.your_session_expired),
                        false,
                        "",
                        false
                    )
                } else {
                    showSimpleAlert(msg, "")
                }
            }
        }
    }

    private fun clearLogout() {
        MySession.removeSession()
        intent1 = Intent(this@HomeActivity, SplashActivity::class.java)
        intent1!!.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent1)
        finish()
    }

    override fun onBackPressed() {
        // super.onBackPressed();
        showForceUpdate(
            getString(R.string.do_you_want_to_close_the_application),
            getString(R.string.do_you_want_to_close_the_application),
            false,
            getString(R.string.cancel),
            false
        )
    }//Transaction

    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//History
    // String notification_type = getIntent().getStringExtra("notification_type");
    private val notificationIntent: Unit
        private get() {
            if (getIntent() != null) {
                if (getIntent().getStringExtra("notification_type") != null) {
                    val notification_type = getIntent().getStringExtra("notification_type")
                    if (notification_type != null && notification_type.equals(
                            "CHAT_MESSAGE_RECEIVE",
                            ignoreCase = true
                        )
                    ) {
                        val transaction_request_id =
                            getIntent().getStringExtra("transaction_request_id")
                        intent1 = Intent(this, ChatActivity::class.java)
                        intent1!!.putExtra("transaction_id", transaction_request_id)
                        intent1!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent1)
                    } else {
                        // String notification_type = getIntent().getStringExtra("notification_type");
                        val request_by = getIntent().getStringExtra("request_by")
                        val status = getIntent().getStringExtra("status")
                        val transaction_request_id =
                            getIntent().getStringExtra("transaction_request_id")
                        val from_id = getIntent().getStringExtra("from_id")
                        val forWhat: String
                        if (from_id != null && from_id.equals(
                                SharedPref.getPrefsHelper().getPref(
                                    Const.Var.USER_ID
                                ), ignoreCase = true
                            )
                        ) {  //History
                            forWhat = getString(R.string.history)
                            if (request_by != null && request_by == "2") {
                                intent1 = Intent(this, LendingSummaryActivity::class.java)
                            } else if (request_by != null && request_by == "1") {
                                intent1 = Intent(this, BorrowSummaryActivity::class.java)
                            }
                        } else { //Transaction
                            forWhat = getString(R.string.transaction)
                            if (request_by != null && request_by == "1") {
                                intent1 = Intent(this, LendingSummaryActivity::class.java)
                            } else if (request_by != null && request_by == "2") {
                                intent1 = Intent(this, BorrowSummaryActivity::class.java)
                            }
                        }
                        if (notification_type.equals(
                                "NEW_TRANSACTION_REQUEST",
                                ignoreCase = true
                            )
                        ) {
                            intent1!!.putExtra("moveFrom", forWhat)
                            intent1!!.putExtra("status", status)
                            intent1!!.putExtra("transactionId", transaction_request_id)
                        } else if (notification_type.equals(
                                "REQUEST_ACCEPTED",
                                ignoreCase = true
                            )
                        ) {
                            intent1!!.putExtra("moveFrom", forWhat)
                            intent1!!.putExtra("status", status)
                            intent1!!.putExtra("transactionId", transaction_request_id)
                        } else if (notification_type.equals(
                                "REQUEST_DECLINED",
                                ignoreCase = true
                            )
                        ) {
                            intent1!!.putExtra("moveFrom", forWhat)
                            intent1!!.putExtra("status", status)
                            intent1!!.putExtra("transactionId", transaction_request_id)
                        } else if (notification_type.equals(
                                "REQUEST_NEGOTIATE",
                                ignoreCase = true
                            )
                        ) {
                            intent1!!.putExtra("moveFrom", forWhat)
                            intent1!!.putExtra("status", status)
                            intent1!!.putExtra("transactionId", transaction_request_id)
                        } else if (notification_type.equals("PAY_DATE_EXTEND", ignoreCase = true)) {
                            intent1!!.putExtra("moveFrom", forWhat)
                            intent1!!.putExtra("status", status)
                            intent1!!.putExtra("transactionId", transaction_request_id)
                        } else if (notification_type.equals(
                                "TRANSACTION_INITIATED",
                                ignoreCase = true
                            )
                        ) {
                            intent1!!.putExtra("moveFrom", forWhat)
                            intent1!!.putExtra("status", "2")
                            intent1!!.putExtra("transactionId", transaction_request_id)
                        } else if (notification_type.equals(
                                "PAY_DATE_EXTEND_ACCEPT",
                                ignoreCase = true
                            )
                        ) {
                            intent1!!.putExtra("moveFrom", forWhat)
                            intent1!!.putExtra("status", "2")
                            intent1!!.putExtra("transactionId", transaction_request_id)
                        } else if (notification_type.equals(
                                "PAY_DATE_EXTEND_DECLINE",
                                ignoreCase = true
                            )
                        ) {
                            intent1!!.putExtra("moveFrom", forWhat)
                            intent1!!.putExtra("status", "2")
                            intent1!!.putExtra("transactionId", transaction_request_id)
                        } else if (notification_type.equals(
                                "TRANSACTION_INITIATED_STATUS_UPDATE",
                                ignoreCase = true
                            )
                        ) {
                            intent1!!.putExtra("moveFrom", forWhat)
                            intent1!!.putExtra("status", "2")
                            intent1!!.putExtra("transactionId", transaction_request_id)
                        }

                        //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent1!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent1)
                    }
                }
            }
        }
}