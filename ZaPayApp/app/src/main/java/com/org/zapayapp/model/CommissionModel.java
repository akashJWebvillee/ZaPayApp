package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommissionModel {
    @SerializedName("borrower_charge_value")
    @Expose
    private String borrowerChargeValue;
    @SerializedName("borrower_charge_type")
    @Expose
    private String borrowerChargeType;
    @SerializedName("lender_charge_value")
    @Expose
    private String lenderChargeValue;
    @SerializedName("lender_charge_type")
    @Expose
    private String lenderChargeType;
    @SerializedName("default_fee_value")
    @Expose
    private String defaultFeeValue;
    @SerializedName("default_fee_type")
    @Expose
    private String defaultFeeType;

    public String getBorrowerChargeValue() {
        return borrowerChargeValue;
    }

    public void setBorrowerChargeValue(String borrowerChargeValue) {
        this.borrowerChargeValue = borrowerChargeValue;
    }

    public String getBorrowerChargeType() {
        return borrowerChargeType;
    }

    public void setBorrowerChargeType(String borrowerChargeType) {
        this.borrowerChargeType = borrowerChargeType;
    }

    public String getLenderChargeValue() {
        return lenderChargeValue;
    }

    public void setLenderChargeValue(String lenderChargeValue) {
        this.lenderChargeValue = lenderChargeValue;
    }

    public String getLenderChargeType() {
        return lenderChargeType;
    }

    public void setLenderChargeType(String lenderChargeType) {
        this.lenderChargeType = lenderChargeType;
    }

    public String getDefaultFeeValue() {
        return defaultFeeValue;
    }

    public void setDefaultFeeValue(String defaultFeeValue) {
        this.defaultFeeValue = defaultFeeValue;
    }

    public String getDefaultFeeType() {
        return defaultFeeType;
    }

    public void setDefaultFeeType(String defaultFeeType) {
        this.defaultFeeType = defaultFeeType;
    }

}
