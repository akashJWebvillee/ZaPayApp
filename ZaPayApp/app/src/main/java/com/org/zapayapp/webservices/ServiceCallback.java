package com.org.zapayapp.webservices;

import android.view.View;

import com.google.gson.JsonElement;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The interface Service callback.
 *
 * @param <T> the type parameter
 */
public interface ServiceCallback<T> {

    /**
     * Unauthenticated.
     *
     * @param response the response
     * @param call     the call
     * @param callback the callback
     * @param view     the view
     */
    void unauthenticated(Response<?> response, Call<JsonElement> call, Callback<JsonElement> callback, View view);

    /**
     * Client error.
     *
     * @param response the response
     * @param call     the call
     * @param callback the callback
     * @param view     the view
     */
    void clientError(Response<?> response, Call<JsonElement> call, Callback<JsonElement> callback, View view);

    /**
     * Server error.
     *
     * @param response the response
     * @param call     the call
     * @param callback the callback
     * @param view     the view
     */
    void serverError(Response<?> response, Call<JsonElement> call, Callback<JsonElement> callback, View view);

    /**
     * Network error.
     *
     * @param e        the e
     * @param call     the call
     * @param callback the callback
     * @param view     the view
     */
    void networkError(IOException e, Call<JsonElement> call, Callback<JsonElement> callback, View view);

    /**
     * Unexpected error.
     *
     * @param t        the t
     * @param call     the call
     * @param callback the callback
     * @param view     the view
     */
    void unexpectedError(Throwable t, Call<JsonElement> call, Callback<JsonElement> callback, View view);
}
