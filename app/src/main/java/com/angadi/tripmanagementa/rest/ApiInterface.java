package com.angadi.tripmanagementa.rest;


import com.angadi.tripmanagementa.fragments.ProfileResponse;
import com.angadi.tripmanagementa.models.CategoriesResponse;
import com.angadi.tripmanagementa.models.CreateQrResponse;
import com.angadi.tripmanagementa.models.DashboardResponse;
import com.angadi.tripmanagementa.models.EditProfileResponse;
import com.angadi.tripmanagementa.models.LoginResponse;
import com.angadi.tripmanagementa.models.LogoutResponse;
import com.angadi.tripmanagementa.models.QrScanResponse;
import com.angadi.tripmanagementa.models.RegisterResponse;
import com.angadi.tripmanagementa.models.SubCategoriesResponse;
import com.angadi.tripmanagementa.models.VerifyOtp;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("categories/all/")
    @FormUrlEncoded
    Call<CategoriesResponse> getAllCategories(@Field("show_all_categories") String cat);

    @POST("categories/one/")
    @FormUrlEncoded
    Call<SubCategoriesResponse> getSubCategories(@Field("show_one_categories") String cat,
                                                 @Field("scaa_caaid") String id);

//    @GET("movie/{id}")
//    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

//    @GET("/posts")
//    Call<List<Post>> getPosts();

    @POST("login/")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("username") String user_id);

    @POST("logout/")
    @FormUrlEncoded
    Call<LogoutResponse> logout(@Field("token_id") String token);

    @POST("signup/")
    @FormUrlEncoded
    Call<RegisterResponse> register(@Field("fname") String firstName,
                                    @Field("username") String userName);

    @POST("login/otp/")
    @FormUrlEncoded
    Call<VerifyOtp> verify_otp(@Field("username") String username,
                               @Field("otp_code") String otp_code,
                               @Field("device_token") String device_token);

    @POST("qrcode/create/")
    @FormUrlEncoded
    Call<CreateQrResponse> create_qr(@Field("token") String token,
                                     @Field("qcaa_cat") String qcaa_cat,
                                     @Field("qcaa_sub_cat") String qcaa_sub_cat,
                                     @Field("qcaa_name") String qcaa_name,
                                     @Field("qcaa_email_id") String qcaa_email_id,
                                     @Field("qcaa_phone_no") String qcaa_phone_no,
                                     @Field("qcaa_social_whatsapp") String qcaa_social_whatsapp,
                                     @Field("qcaa_social_facebook") String qcaa_social_facebook,
                                     @Field("qcaa_social_youtube") String qcaa_social_youtube,
                                     @Field("qcaa_website") String qcaa_website,
                                     @Field("qcaa_place") String qcaa_place,
                                     @Field("qcaa_profile_logo") String qcaa_profile_logo
    );

    @POST("qrcode/show_all_categories/")
    @FormUrlEncoded
    Call<DashboardResponse> dashboard(@Field("show_all_categories") String show,
                                      @Field("token") String token);
    @POST("qrcode/scan/")
    @FormUrlEncoded
    Call<QrScanResponse> scanResult(@Field("scan") String show,
                                   @Field("scan_id") String scan_id,
                                    @Field("token") String token);

    @POST("profile/show/")
    @FormUrlEncoded
    Call<ProfileResponse> getProfile(@Field("show") String  show,
                                     @Field("token") String  token);

    @POST("profile/edit/")
    @FormUrlEncoded
    Call<EditProfileResponse> editProfile(@Field("edit_profile") String  show,
                                          @Field("fname") String  fname,
                                          @Field("token") String  token);
//    @POST("logout/")
//    @FormUrlEncoded
//    Call<Logout> logout(@Field("token_id") String token);
}
