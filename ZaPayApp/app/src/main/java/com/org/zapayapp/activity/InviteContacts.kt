package com.org.zapayapp.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.org.zapayapp.R
import com.org.zapayapp.databinding.FragmentInviteBinding
import com.org.zapayapp.utils.CSVWriter
import com.org.zapayapp.utils.CommonMethods
import java.io.File
import java.io.FileWriter
import java.io.IOException


class InviteContacts : Fragment() {
    private var binding: FragmentInviteBinding? = null
    private var context1: Context? = null
    var cursor: Cursor? = null
    private var csv_status = false
    private var TAG = InviteContacts::class.java.simpleName

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            contacts
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            CommonMethods.showToast(context1, "Permission Denied\n$deniedPermissions")
        }
    }

    // data is a array of String type which is
    // used to store Number ,Names and id.
    private val contacts: Unit
        private get() {
            createCSV()
            exportCSV()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInviteBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction() {
        context1 = activity
        TedPermission.with(context1)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage(R.string.if_you_reject_permissions)
            .setPermissions(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }


    private fun createCSV() {
        var writer: CSVWriter? = null
        try {
            writer = CSVWriter(
                FileWriter(
                    Environment.getExternalStorageDirectory().getAbsolutePath()
                        .toString() + "/zapay.csv"
                )
            )
        } catch (e1: IOException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        var displayName: String
        var number: String?
        var _id: Long
        val columns = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )
        writer!!.writeColumnNames() // Write column header
        val cursor: Cursor = context1!!.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            columns,
            null,
            null,
            ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        )!!
        if (cursor.moveToFirst()) {
            do {
                _id =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)).toLong()
                displayName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        .trim { it <= ' ' }
                number = getPrimaryNumber(_id)
                writer.writeNext("$displayName/$number".split("/".toRegex()).toTypedArray())
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
    } // Method  close.


    private fun exportCSV() {
        if (csv_status === true) {
            //CSV file is created so we need to Export that ...
            val CSVFile = File(
                Environment.getExternalStorageDirectory().getAbsolutePath()
                    .toString() + "/my_test_contact.csv"
            )


            //Log.i("SEND EMAIL TESTING", "Email sending");
           /* val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/csv"
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test contacts ")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "\n\nAdroid developer\n Pankaj")
            emailIntent.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse("file://" + CSVFile.getAbsolutePath())
            )
            emailIntent.type =
                "message/rfc822" // Shows all application that supports SEND activity
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    context1,
                    "Email client : $ex", Toast.LENGTH_SHORT
                )
            }*/
        } else {
            Toast.makeText(
               context1,
                "Contacts unavailable.",
                Toast.LENGTH_SHORT
            ).show()
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
            val cursor: Cursor = context1!!.contentResolver.query(
                Phone.CONTENT_URI, arrayOf(Phone.NUMBER, Phone.TYPE),
                Phone.CONTACT_ID + " = " + _id,  // We need to add more selection for phone type
                null,
                null
            )!!
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    when (cursor.getInt(cursor.getColumnIndex(Phone.TYPE))) {
                        Phone.TYPE_MOBILE -> primaryNumber =
                            cursor.getString(cursor.getColumnIndex(Phone.NUMBER))
                        Phone.TYPE_HOME -> primaryNumber =
                            cursor.getString(cursor.getColumnIndex(Phone.NUMBER))
                        Phone.TYPE_WORK -> primaryNumber =
                            cursor.getString(cursor.getColumnIndex(Phone.NUMBER))
                        Phone.TYPE_OTHER -> {}
                    }
                    if (primaryNumber != null) break
                }
            }
        } catch (e: Exception) {
            Log.i(TAG, "Exception $e")
        } finally {
            if (cursor != null) {
                cursor!!.deactivate()
                cursor!!.close()
            }
        }
        return primaryNumber
    }
}


