package com.org.zapayapp.activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.org.zapayapp.R
import com.org.zapayapp.adapters.WalletTransactionAdapter
import java.util.*

class WalletActivity : BaseActivity(), View.OnClickListener {
   // private var addButtonTV:TextView?=null
   // private var withdrawalButtonTV:TextView?=null
    private lateinit var addButtonTV:TextView
    private lateinit var withdrawalButtonTV:TextView
    private lateinit var filterIV:ImageView
    private var transactionsRecView:RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        inIt();
        inItAction()
    }

    private fun inIt(){
        addButtonTV = findViewById(R.id.addButtonTV)
        withdrawalButtonTV=findViewById(R.id.withdrawalButtonTV)
        filterIV=findViewById(R.id.filterIV)
        transactionsRecView=findViewById(R.id.transactionsRecView)

        addButtonTV.setOnClickListener(this)
        withdrawalButtonTV.setOnClickListener(this)
        filterIV.setOnClickListener(this)
    }
    private fun inItAction(){
        val manager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        transactionsRecView?.layoutManager =manager

        val walletAdapter=WalletTransactionAdapter(this, ArrayList<String>())
        transactionsRecView?.adapter=walletAdapter
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.addButtonTV -> {
                val intent =Intent(this,AddMoneyWalletActivity::class.java)
                startActivity(intent)

            }
            R.id.withdrawalButtonTV -> {
                val intent1 =Intent(this,WithdraMoneyWalletActivity::class.java)
                startActivity(intent1)
            }
            R.id.filterIV->{

            }
        }
    }
}