package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("short_code")
    @Expose
    private String short_code;

    @SerializedName("state")
    @Expose
    private String state;


    public String getId() {
        return id;
    }

    public String getShort_code() {
        return short_code;
    }

    public String getState() {
        return state;
    }
}
