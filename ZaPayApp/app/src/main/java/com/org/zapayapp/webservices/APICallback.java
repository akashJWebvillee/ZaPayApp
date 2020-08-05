package com.org.zapayapp.webservices;

import com.google.gson.JsonObject;

public interface APICallback {

    /**
     * Api callback.
     *
     * @param json the json
     * @param from the from
     */
    void apiCallback(JsonObject json, String from);
}
