package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalanceDetailsModel {
    @SerializedName("value")
    @Expose
    public String value;
    @SerializedName("currency")
    @Expose
    public String currency;
}
