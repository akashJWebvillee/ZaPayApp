package com.org.zapayapp.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.org.zapayapp.R
import java.util.*

class WalletTransactionAdapter(val context:Context,val list:ArrayList<String>): RecyclerView.Adapter<WalletTransactionAdapter.MyHolder>() {

    class MyHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.wallet_transaction_row,parent,false)
        return MyHolder(view);
    }


    override fun onBindViewHolder(holder: MyHolder, position: Int) {

    }
    override fun getItemCount(): Int {
        return 10

    }
}