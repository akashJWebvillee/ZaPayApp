package com.org.zapayapp.activity
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.org.zapayapp.R
import com.org.zapayapp.ZapayApp
import com.org.zapayapp.alert_dialog.SimpleAlertFragment
import com.org.zapayapp.uihelpers.CustomTextInputLayout
import com.org.zapayapp.utils.CommonMethods
import com.org.zapayapp.utils.Const
import com.org.zapayapp.utils.SharedPref
import com.org.zapayapp.utils.WValidationLib
import com.org.zapayapp.webservices.APICallback
import com.org.zapayapp.webservices.APICalling
import com.org.zapayapp.webservices.RestAPI

class WithdraMoneyWalletActivity : AppCompatActivity(), View.OnClickListener, APICallback, SimpleAlertFragment.AlertSimpleCallback {
    private lateinit var closeTV: ImageView
    private lateinit var amountEditText: TextInputEditText
    private lateinit var amountInputLayout: CustomTextInputLayout
    private lateinit var withdrawalButtonTV: TextView

    /*Code for API calling*/
    private var zapayApp: ZapayApp? = null
    private var gson: Gson? = null
    private var apiCalling: APICalling? = null
    private var restAPI: RestAPI? = null
    private var wValidationLib: WValidationLib? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(CommonMethods.getDrawableWrapper(this, android.R.color.transparent))
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_withdra_money_wallet)
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        apiCodeInit()
        inIt()
        inItAction()
    }

    private fun apiCodeInit() {
        zapayApp = applicationContext as ZapayApp
        restAPI = APICalling.webServiceInterface()
        gson = Gson()
        apiCalling = APICalling(this)
        wValidationLib = WValidationLib(this@WithdraMoneyWalletActivity)
    }

    private fun inIt() {
        amountInputLayout = findViewById(R.id.amountInputLayout)
        amountEditText = findViewById(R.id.amountEditText)
        withdrawalButtonTV = findViewById(R.id.withdrawalButtonTV)
        closeTV = findViewById(R.id.closeTV)
        closeTV.setOnClickListener(this)
        withdrawalButtonTV.setOnClickListener(this)
    }

    private fun inItAction() {

    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.closeTV -> {
                finish()
            }
            R.id.withdrawalButtonTV -> {
                /* if (wValidationLib!!.isValidAmount2(amountInputLayout, amountEditText, getString(R.string.important), getString(R.string.enter_amount), true)) {
                     amountAddFunc();
                 }*/

                if (amountEditText.text.toString().isNotEmpty()) {
                    amountAddFunc();
                } else {
                    showSimpleAlert(getString(R.string.enter_amount), "")

                }
            }
        }
    }

    private fun amountAddFunc() {
        val token = SharedPref.getPrefsHelper().getPref<Any>(Const.Var.TOKEN).toString()
        try {
            val values = apiCalling!!.getHashMapObject(
                    "amount", amountEditText.text.toString(),
                    "transaction_type", "2")  //transaction_type- 1=add, 2=withdrawal
            zapayApp!!.setApiCallback(this)
            val call = restAPI!!.postWithTokenApi(token, getString(R.string.api_add_withdrawal_balance_in_wallet), values)
            if (apiCalling != null) {
                apiCalling!!.callAPI(zapayApp, call, getString(R.string.api_add_withdrawal_balance_in_wallet), withdrawalButtonTV)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun apiCallback(json: JsonObject?, from: String?) {
        if (from != null) {
            var status = 0
            var msg: String? = ""
            try {
                status = json!!["status"].asInt
                msg = json["message"].asString
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (from == resources.getString(R.string.api_add_withdrawal_balance_in_wallet)) {
                if (status == 200) {
                    showSimpleAlert(msg, resources.getString(R.string.api_add_withdrawal_balance_in_wallet))
                } else {
                    showSimpleAlert(msg, "")
                }
            }
        }
    }

    fun showSimpleAlert(message: String?, from: String?) {
        try {
            val fm = supportFragmentManager
            val args = Bundle()
            args.putString("header", message)
            args.putString("textOk", getString(R.string.ok))
            args.putString("textCancel", getString(R.string.cancel))
            args.putString("from", from)
            val alert = SimpleAlertFragment()
            alert.arguments = args
            alert.show(fm, "")
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onSimpleCallback(from: String) {
        if (from == resources.getString(R.string.api_add_withdrawal_balance_in_wallet)) {
            finish()
        }
    }
}