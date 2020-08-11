package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionModel implements Serializable {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("from_id")
    @Expose
    public String fromId;
    @SerializedName("to_id")
    @Expose
    public String toId;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("total_amount")
    @Expose
    public String totalAmount;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("terms_type")
    @Expose
    public String termsType;
    @SerializedName("terms_value")
    @Expose
    public String termsValue;
    @SerializedName("no_of_payment")
    @Expose
    public String noOfPayment;
    @SerializedName("pay_date")
    @Expose
    public String payDate;
    @SerializedName("updated_by")
    @Expose
    public String updatedBy;
    @SerializedName("request_by")
    @Expose
    public String requestBy;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;


    public String getId() {
        return id;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getTermsType() {
        return termsType;
    }

    public String getTermsValue() {
        return termsValue;
    }

    public String getNoOfPayment() {
        return noOfPayment;
    }

    public String getPayDate() {
        return payDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
