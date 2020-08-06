package com.org.zapayapp.rest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
/*
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);*/


    @GET("base_url")
    Call<ResponseBody> selectLanguageData();

  /*  @FormUrlEncoded
    @POST("register")
    Call<String> registration(@Field("name") String name,
                              @Field("firm_name") String firm_name,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("cur_language") String cur_language,
                              @Field("country") String country,
                              @Field("telephone") String telephone,
                              @Field("address") String address,
                              @Field("tax_id") String tax_id);*/



  @FormUrlEncoded
    @POST("signup")
    Call<String> registration(@Field("first_name") String first_name,
                              @Field("last_name") String last_name,
                              @Field("email") String email,
                              @Field("mobile") String mobile,
                              @Field("password") String password,
                              @Field("device_type") String device_type,
                              @Field("device_token") String device_token,
                              @Field("device_id") String device_id);


    @FormUrlEncoded
    @POST("login")
    Call<String> login(@Field("email") String email,
                       @Field("password") String password,
                       @Field("cur_language") String cur_language);





}
