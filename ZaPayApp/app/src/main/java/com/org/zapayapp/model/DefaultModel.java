package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("from_id")
    @Expose
    private String fromId;
    @SerializedName("to_id")
    @Expose
    private String toId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("admin_commission_from_lender")
    @Expose
    private String adminCommissionFromLender;
    @SerializedName("admin_commission_is_received_from_lender")
    @Expose
    private String adminCommissionIsReceivedFromLender;
    @SerializedName("admin_commission_from_borrower")
    @Expose
    private String adminCommissionFromBorrower;
    @SerializedName("admin_commission_is_received_from_borrower")
    @Expose
    private String adminCommissionIsReceivedFromBorrower;
    @SerializedName("admin_commission_from_default_fee")
    @Expose
    private String adminCommissionFromDefaultFee;
    @SerializedName("commission_charges_detail")
    @Expose
    private String commissionChargesDetail;
    @SerializedName("is_enable")
    @Expose
    private String isEnable;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("terms_type")
    @Expose
    private String termsType;
    @SerializedName("terms_value")
    @Expose
    private String termsValue;
    @SerializedName("no_of_payment")
    @Expose
    private String noOfPayment;
    @SerializedName("pay_date")
    @Expose
    private String payDate;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("request_by")
    @Expose
    private String requestBy;
    @SerializedName("is_negotiate_after_accept")
    @Expose
    private String isNegotiateAfterAccept;
    @SerializedName("child_request_is_accepted")
    @Expose
    private String childRequestIsAccepted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("transaction_request_id")
    @Expose
    private String transactionRequestId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAdminCommissionFromLender() {
        return adminCommissionFromLender;
    }

    public void setAdminCommissionFromLender(String adminCommissionFromLender) {
        this.adminCommissionFromLender = adminCommissionFromLender;
    }

    public String getAdminCommissionIsReceivedFromLender() {
        return adminCommissionIsReceivedFromLender;
    }

    public void setAdminCommissionIsReceivedFromLender(String adminCommissionIsReceivedFromLender) {
        this.adminCommissionIsReceivedFromLender = adminCommissionIsReceivedFromLender;
    }

    public String getAdminCommissionFromBorrower() {
        return adminCommissionFromBorrower;
    }

    public void setAdminCommissionFromBorrower(String adminCommissionFromBorrower) {
        this.adminCommissionFromBorrower = adminCommissionFromBorrower;
    }

    public String getAdminCommissionIsReceivedFromBorrower() {
        return adminCommissionIsReceivedFromBorrower;
    }

    public void setAdminCommissionIsReceivedFromBorrower(String adminCommissionIsReceivedFromBorrower) {
        this.adminCommissionIsReceivedFromBorrower = adminCommissionIsReceivedFromBorrower;
    }

    public String getAdminCommissionFromDefaultFee() {
        return adminCommissionFromDefaultFee;
    }

    public void setAdminCommissionFromDefaultFee(String adminCommissionFromDefaultFee) {
        this.adminCommissionFromDefaultFee = adminCommissionFromDefaultFee;
    }

    public String getCommissionChargesDetail() {
        return commissionChargesDetail;
    }

    public void setCommissionChargesDetail(String commissionChargesDetail) {
        this.commissionChargesDetail = commissionChargesDetail;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTermsType() {
        return termsType;
    }

    public void setTermsType(String termsType) {
        this.termsType = termsType;
    }

    public String getTermsValue() {
        return termsValue;
    }

    public void setTermsValue(String termsValue) {
        this.termsValue = termsValue;
    }

    public String getNoOfPayment() {
        return noOfPayment;
    }

    public void setNoOfPayment(String noOfPayment) {
        this.noOfPayment = noOfPayment;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getIsNegotiateAfterAccept() {
        return isNegotiateAfterAccept;
    }

    public void setIsNegotiateAfterAccept(String isNegotiateAfterAccept) {
        this.isNegotiateAfterAccept = isNegotiateAfterAccept;
    }

    public String getChildRequestIsAccepted() {
        return childRequestIsAccepted;
    }

    public void setChildRequestIsAccepted(String childRequestIsAccepted) {
        this.childRequestIsAccepted = childRequestIsAccepted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTransactionRequestId() {
        return transactionRequestId;
    }

    public void setTransactionRequestId(String transactionRequestId) {
        this.transactionRequestId = transactionRequestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
