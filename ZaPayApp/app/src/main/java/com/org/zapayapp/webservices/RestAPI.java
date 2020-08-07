package com.org.zapayapp.webservices;

import com.google.gson.JsonElement;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RestAPI {

    @GET
    Call<JsonElement> getApi(@Url String remainingURL);


    @FormUrlEncoded
    @PUT
    Call<JsonElement> putApi(@Url String remainingURL, @FieldMap Map<String, Object> fields);

    @HTTP(method = "DELETE", hasBody = true)
    Call<JsonElement> deleteApi(@Url String remainingURL, @Body JsonElement params);

    @FormUrlEncoded
    @POST
    Call<JsonElement> postApi(@Url String remainingURL, @FieldMap Map<String, Object> fields);

    @POST
    Call<JsonElement> postApiToken(@Header("authorization") String authToken, @Url String remainingURL);

    @GET
    Call<JsonElement> getApiToken(@Header("authorization") String authToken, @Url String remainingURL);


    @FormUrlEncoded
    @POST
    Call<JsonElement> postWithTokenApi(@Header("authorization") String authToken,@Url String remainingURL, @FieldMap Map<String, Object> fields);

    @Multipart
    @POST
    Call<JsonElement> postWithTokenMultiPartApi(@Header("authorization") String authToken,@Url String remainingURL, @Part MultipartBody.Part image);







    @POST
    Call<JsonElement> postObjectApi(@Url String remainingURL, @Body Map<String, Object> fields);

    @Multipart
    @POST
    Call<JsonElement> postMultiPartApi(@Url String remainingURL, @PartMap Map<String, RequestBody> fields, @Part MultipartBody.Part image);

    @Multipart
    @POST
    Call<JsonElement> postMultiPartApi(@Url String remainingURL, @Part MultipartBody.Part image);

    @Multipart
    @POST
    Call<JsonElement> postMultipleMultiPartApi(@Url String remainingURL, @PartMap Map<String, RequestBody> fields, @Part(encoding = "8-bit") MultipartBody.Part[] image);

    @GET
    Call<ResponseBody> download(@Url String fileUrl);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST
    Call<JsonElement> postApiWithToken(@Header("token") String authToken, @Url String remainingURL, @Body String params);

    @FormUrlEncoded
    @POST
    Call<JsonElement> postApiWithToken(@Header("token") String authToken, @Url String remainingURL, @FieldMap Map<String, Object> fields);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET
    Call<JsonElement> getApiWithToken(@Header("token") String authToken, @Url String remainingURL);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST
    Call<JsonElement> postApiWithToken(@Header("token") String authToken, @Url String remainingURL, @Body JsonElement params);
}
