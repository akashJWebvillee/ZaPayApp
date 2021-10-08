package com.org.zapayapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.org.zapayapp.R
import com.org.zapayapp.model.WalletTransactionModel1
import com.org.zapayapp.utils.Const
import com.org.zapayapp.utils.SharedPref
import com.org.zapayapp.utils.TimeStamp
import java.util.*

class WalletTransactionAdapter(val context: Context, val transactionList: ArrayList<WalletTransactionModel1>) : RecyclerView.Adapter<WalletTransactionAdapter.MyHolder>() {

    class MyHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var idTv: TextView
        var tvDate: TextView
        var amountTV: TextView
        var titleTV: TextView

        init {
            idTv = itemView.findViewById(R.id.idTv)
            tvDate = itemView.findViewById(R.id.tvDate)
            amountTV = itemView.findViewById(R.id.amountTV)
            titleTV = itemView.findViewById(R.id.titleTV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wallet_transaction_row, parent, false)
        return MyHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val model: WalletTransactionModel1 = transactionList[position]

        if (model.id.isNotEmpty()) {
            holder.idTv.text = model.id
        }
        if (model.createdAt.isNotEmpty()) {
            val date: Long = model.createdAt.toLong()
            holder.tvDate.text = TimeStamp.epochToDateTimeAppFormat(date)
        }
        if (model.amount.isNotEmpty()) {
            if (model.transactionType.isNotEmpty()) {
                if (model.transactionType == "1") {
                    // holder.amountTV.text = "+ USD " + model.amount
                        holder.titleTV.text=context.getString(R.string.added_in_wallet)
                    holder.amountTV.text = "+" + SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + model.amount
                    holder.amountTV.setTextColor(Color.parseColor("#00AD15"))
                } else if (model.transactionType == "2") {
                    // holder.amountTV.text = "- USD " + model.amount
                    holder.titleTV.text=context.getString(R.string.withdrawal)
                    holder.amountTV.text = "-" + SharedPref.getPrefsHelper().getPref(Const.Var.CURRENCY, "") + model.amount
                    holder.amountTV.setTextColor(Color.parseColor("#EF0606"))
                }
            }
        }

        /*  if (model.getId() != null && model.getId().length > 0) {
              holder.idTv.text = model.getId()
          }
          if (model.getCreatedAt() != null && model.getCreatedAt().length > 0) {
              val date: Long = model.createdAt.toLong()
              holder.tvDate.text = TimeStamp.epochToDateTimeAppFormat(date)
          }
          if (model.getAmount() != null && model.getAmount().length > 0) {
              if (model.getTransactionType() != null && model.getTransactionType().length > 0) {
                  if (model.getTransactionType().equals("1")){
                      holder.amountTV.text = "+ USD " + model.getAmount()
                      holder.amountTV.setTextColor(Color.parseColor("#00AD15"));
                  }else if (model.getTransactionType().equals("2")){
                      holder.amountTV.text = "- USD " + model.getAmount()
                      holder.amountTV.setTextColor(Color.parseColor("#EF0606"));
                  }
              }
          }*/
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}