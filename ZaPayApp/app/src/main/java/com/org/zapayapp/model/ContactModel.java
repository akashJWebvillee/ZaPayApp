package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactModel {
    @SerializedName("id")
    @Expose
    public String id;

    private boolean isSelect=false;

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("mobile_contact_name")
    @Expose
    private String mobileContactName;
    @SerializedName("mobile_contact_number")
    @Expose
    private String mobileContactNumber;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("user_primary_id")
    @Expose
    private String userPrimaryId;
    @SerializedName("user_app_first_name")
    @Expose
    private String userAppFirstName;
    @SerializedName("user_app_last_name")
    @Expose
    private String userAppLastName;
    @SerializedName("is_invite")
    @Expose
    private Integer isInvite;


    public ContactModel(String mobileContactName, boolean isSelect) {
        this.mobileContactName = mobileContactName;
        this.isSelect = isSelect;
    }

    public String getId() {
        return id;
    }

    public boolean isSelect() {
        return isSelect;
    }


    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getUserId() {
        return userId;
    }

    public String getMobileContactName() {
        return mobileContactName;
    }

    public String getMobileContactNumber() {
        return mobileContactNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getUserPrimaryId() {
        return userPrimaryId;
    }

    public String getUserAppFirstName() {
        return userAppFirstName;
    }

    public String getUserAppLastName() {
        return userAppLastName;
    }

    public Integer getIsInvite() {
        return isInvite;
    }
}
