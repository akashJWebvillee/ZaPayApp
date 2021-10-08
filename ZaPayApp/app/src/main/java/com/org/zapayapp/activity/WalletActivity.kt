package com.org.zapayapp.activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.org.zapayapp.R
import com.org.zapayapp.adapters.WalletTransactionAdapter
import com.org.zapayapp.model.BalanceDetailsModel
import com.org.zapayapp.model.WalletModel
import com.org.zapayapp.model.WalletTransactionModel1
import com.org.zapayapp.utils.Const
import com.org.zapayapp.utils.MyBottomSheet
import com.org.zapayapp.utils.SharedPref
import com.org.zapayapp.webservices.APICallback
import org.json.JSONObject

class WalletActivity : BaseActivity(), View.OnClickListener, MyBottomSheet.BottomSheetListener, APICallback {
    // private var addButtonTV:TextView?=null
    // private var withdrawalButtonTV:TextView?=null
    private lateinit var noDataTv: TextView
    private lateinit var amountTV: TextView
    private lateinit var addButtonTV: TextView
    private lateinit var withdrawalButtonTV: TextView
    private lateinit var filterIV: ImageView
    private lateinit var transactionsRecView: RecyclerView
    private lateinit var transactionList: ArrayList<WalletTransactionModel1>
    private lateinit var walletAdapter: WalletTransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        inIt()
        inItAction()
    }

    override fun useToolbar(): Boolean {
        return true
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        getUserWalletBalance("0")
    }

    private fun inIt() {
        transactionList = ArrayList()
        noDataTv = findViewById(R.id.noDataTv)
        amountTV = findViewById(R.id.amountTV)
        addButtonTV = findViewById(R.id.addButtonTV)
        withdrawalButtonTV = findViewById(R.id.withdrawalButtonTV)
        filterIV = findViewById(R.id.filterIV)
        transactionsRecView = findViewById(R.id.transactionsRecView)

        addButtonTV.setOnClickListener(this)
        withdrawalButtonTV.setOnClickListener(this)
        filterIV.setOnClickListener(this)
    }

    private fun inItAction() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        transactionsRecView.layoutManager = manager

        /* val walletAdapter = WalletTransactionAdapter(this, transactionList)
         transactionsRecView.adapter = walletAdapter*/
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.addButtonTV -> {
                val intent = Intent(this, AddMoneyWalletActivity::class.java)
                startActivity(intent)
            }
            R.id.withdrawalButtonTV -> {
                val intent1 = Intent(this, WithdraMoneyWalletActivity::class.java)
                startActivity(intent1)
            }
            R.id.filterIV -> {
                MyBottomSheet().walletFilterBottomSheet(this, this, getString(R.string.filter));
            }
        }
    }

    override fun onSheetClick(forwhat: String?, response: String?) {
        Log.e("response", "response========" + response);
        if(forwhat==getString(R.string.filter)){
            val jsonObject = JSONObject(response)
            val FILTER_TYPE: String = jsonObject.getString("FILTER_TYPE");
            getUserWalletBalance(FILTER_TYPE)
        }else if (forwhat==getString(R.string.clear_all)){
            getUserWalletBalance("0")
        }
    }

    private fun getUserWalletBalance(filterType: String) {
        var by_add_money: String = "0"
        var by_withdraw_money: String = "0"
        var sort_by_date: String = "0"
        if (filterType.equals("1")) {
            by_add_money = "1"
        } else if (filterType.equals("2")) {
            by_withdraw_money = "1"
        } else if (filterType.equals("3")) {
            sort_by_date = "1"
        }

        val token = SharedPref.getPrefsHelper().getPref<Any>(Const.Var.TOKEN).toString()
        try {
            val values = apiCalling!!.getHashMapObject(
                    "by_add_money", by_add_money,
                    "by_withdraw_money", by_withdraw_money,
                    "sort_by_date", sort_by_date)  //(filter param value- -0,1)

            Log.e("values", "values=======" + values.toString())
            zapayApp!!.setApiCallback(this)
            val call = restAPI!!.postWithTokenApi(token, getString(R.string.api_get_user_wallet_balance), values)
            if (apiCalling != null) {
                apiCalling!!.callAPI(zapayApp, call, getString(R.string.api_get_user_wallet_balance), withdrawalButtonTV)
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
            if (from == resources.getString(R.string.api_get_user_wallet_balance)) {
                if (status == 200) {
                    transactionList.clear()
                    val walletModel: WalletModel = gson.fromJson(json?.get("data")?.asJsonObject.toString(), WalletModel::class.java)
                    val balanceDetailsModel: BalanceDetailsModel = walletModel.balanceDetailsModel
                    //val walletTransactionModelList: List<WalletTransactionModel> = walletModel.walletTransactionModelList
                    val walletTransactionModelList: List<WalletTransactionModel1> = walletModel.walletTransactionModelList

                    if (walletTransactionModelList.isNotEmpty() && walletTransactionModelList.size > 0) {
                        transactionList.addAll(walletTransactionModelList)
                        noDataTv.visibility = View.GONE
                    } else {
                        noDataTv.visibility = View.VISIBLE
                    }

                    setAdapterFun()
                    if (balanceDetailsModel.currency != null && balanceDetailsModel.currency.length > 0 && balanceDetailsModel.value != null && balanceDetailsModel.value.length > 0) {
                        amountTV.text = SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + "" + balanceDetailsModel.value
                    }
                    //showSimpleAlert(msg, resources.getString(R.string.api_get_user_wallet_balance))
                } else {
                    showSimpleAlert(msg, "")
                }
            }
        }
    }

    private fun setAdapterFun() {
        val walletAdapter = WalletTransactionAdapter(this, transactionList)
        transactionsRecView.adapter = walletAdapter
    }
}