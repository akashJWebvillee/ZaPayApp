package com.org.zapayapp.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WalletTransactionModel1 {
    @SerializedName("id")
    @Expose
    lateinit var id: String

    @SerializedName("transaction_id")
    @Expose
    lateinit var transactionId: String

    @SerializedName("status")
    @Expose
    lateinit var status: String

    @SerializedName("amount")
    @Expose
    lateinit var amount: String

    @SerializedName("transaction_type")
    @Expose
    lateinit var transactionType: String

    @SerializedName("created_at")
    @Expose
    lateinit var createdAt: String
}