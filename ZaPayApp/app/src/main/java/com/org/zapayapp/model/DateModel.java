package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("transaction_request_id")
    @Expose
    private String transactionRequestId;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("pay_date")
    @Expose
    private String payDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_extended")
    @Expose
    private String is_extended;

    private boolean isEditable;
    private boolean isLatestRemaining;


    public DateModel(String payDate, boolean isEditable) {
        this.payDate = payDate;
        this.isEditable = isEditable;
    }

    public String getId() {
        return id;
    }

    public String getTransactionRequestId() {
        return transactionRequestId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPayDate() {
        return payDate;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public String getIs_extended() {
        return is_extended;
    }

    public boolean isLatestRemaining() {
        return isLatestRemaining;
    }

    public void setLatestRemaining(boolean latestRemaining) {
        isLatestRemaining = latestRemaining;
    }
}
