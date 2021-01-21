package com.org.zapayapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmendmentPdfDetailModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("transaction_request_id")
    @Expose
    private String transactionRequestId;
    @SerializedName("pay_date_id")
    @Expose
    private String payDateId;
    @SerializedName("pdf_url")
    @Expose
    private String pdfUrl;
    @SerializedName("pdf_type")
    @Expose
    private String pdfType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionRequestId() {
        return transactionRequestId;
    }

    public void setTransactionRequestId(String transactionRequestId) {
        this.transactionRequestId = transactionRequestId;
    }

    public String getPayDateId() {
        return payDateId;
    }

    public void setPayDateId(String payDateId) {
        this.payDateId = payDateId;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPdfType() {
        return pdfType;
    }

    public void setPdfType(String pdfType) {
        this.pdfType = pdfType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
