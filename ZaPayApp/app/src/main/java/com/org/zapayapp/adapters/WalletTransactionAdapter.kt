package com.org.zapayapp.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.org.zapayapp.R
import com.org.zapayapp.model.WalletTransactionModel
import com.org.zapayapp.utils.TimeStamp
import java.util.*

class WalletTransactionAdapter(val context:Context,val transactionList: ArrayList<WalletTransactionModel>): RecyclerView.Adapter<WalletTransactionAdapter.MyHolder>() {

    class MyHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
         var idTv:TextView=itemView.findViewById(R.id.idTv)
         var tvDate:TextView=itemView.findViewById(R.id.tvDate)
         var amountTV:TextView=itemView.findViewById(R.id.amountTV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.wallet_transaction_row,parent,false)
        return MyHolder(view);
    }


    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.idTv.text = transactionList[position].getId()
        holder.amountTV.text= "USD"+transactionList[position].getAmount()

       // val date:Long= transactionList[position].createdAt as Long
       // holder.tvDate.text=TimeStamp.epochToDateTimeAppFormat(date)

    }
    override fun getItemCount(): Int {
        return transactionList.size

    }
}