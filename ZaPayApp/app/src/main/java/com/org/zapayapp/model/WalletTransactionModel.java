package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletTransactionModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("transaction_id")
    @Expose
    public String transactionId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("transaction_type")
    @Expose
    public String transactionType;
    @SerializedName("created_at")
    @Expose
    public String createdAt;


    public String getId() {
        return id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getStatus() {
        return status;
    }

    public String getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
