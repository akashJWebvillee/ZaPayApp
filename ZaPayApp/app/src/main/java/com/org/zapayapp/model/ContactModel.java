package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;

    private boolean isSelect=false;


    public ContactModel(String firstName, boolean isSelect) {
        this.firstName = firstName;
        this.isSelect = isSelect;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isSelect() {
        return isSelect;
    }


    public void setSelect(boolean select) {
        isSelect = select;
    }
}
