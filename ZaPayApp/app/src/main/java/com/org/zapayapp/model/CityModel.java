package com.org.zapayapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("state_id")
    @Expose
    public String stateId;
    @SerializedName("city")
    @Expose
    public String city;

    public String getId() {
        return id;
    }

    public String getStateId() {
        return stateId;
    }

    public String getCity() {
        return city;
    }
}
