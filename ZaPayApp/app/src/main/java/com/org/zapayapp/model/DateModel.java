package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DateModel implements Serializable {
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
    @SerializedName("new_pay_date")
    @Expose
    private String new_pay_date;

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

    @SerializedName("new_pay_date_status")
    @Expose
    private String new_pay_date_status;

    @SerializedName("amendment_pdf_details")
    @Expose
    private List<AmendmentPdfDetailModel> amendmentPdfDetails = null;


    @SerializedName("cancel_from")
    @Expose
    private String cancel_from;
    @SerializedName("is_default_txn")
    @Expose
    private String is_default_txn;
    @SerializedName("default_payment_pay_done")
    @Expose
    private String default_payment_pay_done;
    @SerializedName("emi_amount")
    @Expose
    private String emi_amount;
    @SerializedName("default_fee_amount")
    @Expose
    private String default_fee_amount;

    @SerializedName("agreement_id_sr_number")
    @Expose
    private String agreementIdSrNumber;

    private boolean isEditable;
    private boolean isLatestRemaining;

    public String getAgreementIdSrNumber() {
        return agreementIdSrNumber;
    }

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

    public String getNew_pay_date_status() {
        return new_pay_date_status;
    }

    public List<AmendmentPdfDetailModel> getAmendmentPdfDetails() {
        return amendmentPdfDetails;
    }

    public String getNew_pay_date() {
        return new_pay_date;
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

    public String getCancel_from() {
        return cancel_from;
    }

    public String getIs_default_txn() {
        return is_default_txn;
    }

    public String getDefault_payment_pay_done() {
        return default_payment_pay_done;
    }

    public String getEmi_amount() {
        return emi_amount;
    }

    public String getDefault_fee_amount() {
        return default_fee_amount;
    }
}
