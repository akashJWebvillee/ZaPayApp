package com.org.zapayapp.webservices;
import com.google.gson.JsonElement;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
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

    @FormUrlEncoded
    @POST("initiatemicro")
    Call<ResponseBody> checkInitiateMicroDeposit(@Header("Authorization") String authToken, @FieldMap Map<String, Object> fields);

    @GET
    Call<JsonElement> getApi(@Url String remainingURL);

    @FormUrlEncoded
    @POST
    Call<JsonElement> postApi(@Url String remainingURL, @FieldMap Map<String, Object> fields);

    @POST
    Call<JsonElement> postApiToken(@Header("Authorization") String authToken, @Url String remainingURL);

    @GET
    Call<JsonElement> getApiToken(@Header("Authorization") String authToken, @Url String remainingURL);

    @FormUrlEncoded
    @POST
    Call<JsonElement> postWithTokenApi(@Header("Authorization") String authToken, @Url String remainingURL, @FieldMap Map<String, Object> fields);

    @Multipart
    @POST
    Call<JsonElement> postWithTokenMultiPartApi(@Header("Authorization") String authToken, @Url String remainingURL, @Part MultipartBody.Part image);

    @Multipart
    @POST
    Call<JsonElement> postWithTokenMultiPartWithDataApi(@Header("Authorization") String authToken, @Url String remainingURL, @PartMap Map<String, RequestBody> fields, @Part MultipartBody.Part image);

   }
