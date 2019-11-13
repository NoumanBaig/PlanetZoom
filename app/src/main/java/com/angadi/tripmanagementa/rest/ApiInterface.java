package com.angadi.tripmanagementa.rest;


import com.angadi.tripmanagementa.models.AddMembersResponse;
import com.angadi.tripmanagementa.models.AddPlacesResponse;
import com.angadi.tripmanagementa.models.AddVolunteerResponse;
import com.angadi.tripmanagementa.models.BuyTicketResponse;
import com.angadi.tripmanagementa.models.CheckAdminResponse;
import com.angadi.tripmanagementa.models.DeleteSubEventResponse;
import com.angadi.tripmanagementa.models.EventTrackResponse;
import com.angadi.tripmanagementa.models.ImageUploadResponse;
import com.angadi.tripmanagementa.models.MyTicketsResponse;
import com.angadi.tripmanagementa.models.ProfileDislikeResponse;
import com.angadi.tripmanagementa.models.ProfileFavResponse;
import com.angadi.tripmanagementa.models.ProfileLikeResponse;
import com.angadi.tripmanagementa.models.ProfileRatingResponse;
import com.angadi.tripmanagementa.models.ProfileResponse;
import com.angadi.tripmanagementa.models.AdminEventsResponse;
import com.angadi.tripmanagementa.models.AdminResponse;
import com.angadi.tripmanagementa.models.AllEventsResponse;
import com.angadi.tripmanagementa.models.CategoriesResponse;
import com.angadi.tripmanagementa.models.CreateEventResponse;
import com.angadi.tripmanagementa.models.CreateQrResponse;
import com.angadi.tripmanagementa.models.DashboardResponse;
import com.angadi.tripmanagementa.models.EditProfileResponse;
import com.angadi.tripmanagementa.models.EventDetailsResponse;
import com.angadi.tripmanagementa.models.LoginResponse;
import com.angadi.tripmanagementa.models.LogoutResponse;
import com.angadi.tripmanagementa.models.MembersResponse;
import com.angadi.tripmanagementa.models.ProfileStatusResponse;
import com.angadi.tripmanagementa.models.QrScanResponse;
import com.angadi.tripmanagementa.models.RegisterResponse;
import com.angadi.tripmanagementa.models.ScanEventQrResponse;
import com.angadi.tripmanagementa.models.SearchVolunteerResponse;
import com.angadi.tripmanagementa.models.ShowAllMembersResponse;
import com.angadi.tripmanagementa.models.ShowMembersResponse;
import com.angadi.tripmanagementa.models.ShowPlacesResponse;
import com.angadi.tripmanagementa.models.ShowSubEventResponse;
import com.angadi.tripmanagementa.models.SubCategoriesResponse;
import com.angadi.tripmanagementa.models.SubEventResponse;
import com.angadi.tripmanagementa.models.VerifyOtp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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
    @POST("qrcode/scan_event_qr/")
    @FormUrlEncoded
    Call<QrScanResponse> scanEventResult(@Field("scan") String show,
                                    @Field("scan_id") String scan_id,
                                    @Field("token") String token);

    @POST("profile/show/")
    @FormUrlEncoded
    Call<ProfileResponse> getProfile(@Field("show") String show,
                                     @Field("token") String token);

    @POST("profile/show/")
    @FormUrlEncoded
    Call<ProfileResponse> getScanProfile(@Field("show") String show,
                                     @Field("token") String token,
                                         @Field("profile_id") String profile_id);

    @POST("profile/edit/")
    @FormUrlEncoded
    Call<EditProfileResponse> editProfile(@Field("edit_profile") String show,
                                          @Field("token") String token,
                                          @Field("fname") String fname,
                                          @Field("lname") String lname,
                                          @Field("ura_about") String ura_about,
                                          @Field("ura_address") String ura_address,
                                          @Field("ura_company_name") String ura_company_name,
                                          @Field("ura_designation") String ura_designation,
                                          @Field("ura_website") String ura_website,
                                          @Field("ura_biz_phone") String ura_biz_phone,
                                          @Field("ura_biz_email") String ura_biz_email,
                                          @Field("ura_facebook") String ura_facebook,
                                          @Field("ura_whatsapp") String ura_whatsapp,
                                          @Field("ura_linkedin") String ura_linkedin,
                                          @Field("ura_youtube") String ura_youtube,
                                          @Field("ura_instagram") String ura_instagram,
                                          @Field("photo_upload") String photo_upload);


    @POST("events/set_admin/")
    @FormUrlEncoded
    Call<AdminResponse> setAdmin(@Field("set_admin") String set,
                                 @Field("token") String token,
                                 @Field("admin_enable") String enable);

    @POST("events/add/")
    @FormUrlEncoded
    Call<CreateEventResponse> addEvents(@Field("add_events") String add,
                                        @Field("token") String token,
                                        @Field("pea_name") String pea_name,
                                        @Field("pea_price") String pea_price,
                                        @Field("pea_tickets") String pea_tickets,
                                        @Field("pea_location") String pea_location,
                                        @Field("pea_venue") String pea_venue,
                                        @Field("pea_desc") String pea_desc,
                                        @Field("pea_date") String pea_date,
                                        @Field("pea_time") String pea_time,
                                        @Field("pea_org") String pea_org,
                                        @Field("pea_report") String pea_report,
                                        @Field("pea_logo") String pea_logo,
                                        @Field("pea_contact_no") String pea_contact_no,
                                        @Field("pea_emergency_no") String pea_emergency_no);

    @POST("events/add/")
    @FormUrlEncoded
    Call<CreateEventResponse> updateEvents(@Field("add_events") String add,
                                           @Field("token") String token,
                                           @Field("pea_name") String pea_name,
                                           @Field("pea_price") String pea_price,
                                           @Field("pea_tickets") String pea_tickets,
                                           @Field("pea_location") String pea_location,
                                           @Field("pea_venue") String pea_venue,
                                           @Field("pea_desc") String pea_desc,
                                           @Field("pea_date") String pea_date,
                                           @Field("pea_time") String pea_time,
                                           @Field("pea_org") String pea_org,
                                           @Field("pea_report") String pea_report,
                                           @Field("pea_logo") String pea_logo,
                                           @Field("pea_id") String pea_id,
                                           @Field("pea_update") String pea_update,
                                           @Field("pea_contact_no") String pea_contact_no,
                                           @Field("pea_emergency_no") String pea_emergency_no);

    @POST("events/show_all_events/")
    @FormUrlEncoded
    Call<AllEventsResponse> getAllEvents(@Field("show_all_events") String show,
                                         @Field("token") String token);

    @POST("events/show_one_user_events/")
    @FormUrlEncoded
    Call<AdminEventsResponse> getAdminEvents(@Field("show_one_user_events") String show,
                                             @Field("token") String token,
                                             @Field("user_id") String user_id);

    @POST("events/show_one_event_data/")
    @FormUrlEncoded
    Call<EventDetailsResponse> getEventDetails(@Field("show_one_event_data") String show,
                                               @Field("token") String token,
                                               @Field("show_event_id") String event_id);

    @POST("events/showing_events_user_lists/")
    @FormUrlEncoded
    Call<MembersResponse> getMembers(@Field("showing_events_user_lists") String show);

    @POST("events/sub_event_creation/")
    @FormUrlEncoded
    Call<SubEventResponse> createSubEvent(@Field("sub_event_creation") String show,
                                          @Field("token") String token,
                                          @Field("event_id") String event_id,
                                          @Field("sub_title") String sub_title,
                                          @Field("sub_desc") String sub_desc);

    @POST("events/sub_event_creation/")
    @FormUrlEncoded
    Call<SubEventResponse> updateSubEvent(@Field("sub_event_creation") String show,
                                          @Field("token") String token,
                                          @Field("event_id") String event_id,
                                          @Field("sub_title") String sub_title,
                                          @Field("sub_desc") String sub_desc,
                                          @Field("esaa_id") String esaa_id,
                                          @Field("esaa_update") String esaa_update);

    @POST("events/show_sub_event/")
    @FormUrlEncoded
    Call<ShowSubEventResponse> showSubEvent(@Field("show_sub_event") String show,
                                            @Field("token") String token,
                                            @Field("event_id") String event_id);

    @POST("events/deleting_sub_events/")
    @FormUrlEncoded
    Call<DeleteSubEventResponse> deleteSubEvent(@Field("deleting_sub_events") String show,
                                                @Field("token") String token,
                                                @Field("event_id") String event_id,
                                                @Field("sub_event_id") String sub_event_id);

    @POST("events/add_members/")
    @FormUrlEncoded
    Call<AddMembersResponse> addMembers(@Field("add_members") String show,
                                        @Field("token") String token,
                                        @Field("eauqa_name") String eauqa_name,
                                        @Field("eauqa_email") String eauqa_email,
                                        @Field("eauqa_phone") String eauqa_phone,
                                        @Field("eauqa_about") String eauqa_about,
                                        @Field("eauqa_event_id") String eauqa_event_id,
                                        @Field("eauqa_users_type") String eauqa_users_type);

    @POST("events/add_volunteers/")
    @FormUrlEncoded
    Call<AddVolunteerResponse> addVolunteer(@Field("add_volunteers") String show,
                                              @Field("token") String token,
                                              @Field("eavqa_uid") String event_id,
                                              @Field("eavqa_event_id") String sub_event_id,
                                            @Field("eavqa_about") String eavqa_about,
                                            @Field("eavqa_map_place_id") String eavqa_map_place_id,
                                            @Field("eavqa_users_type") String eavqa_users_type);

    @POST("events/add_volunteers/")
    @FormUrlEncoded
    Call<AddVolunteerResponse> updateVolunteer(@Field("add_volunteers") String show,
                                            @Field("token") String token,
                                            @Field("eavqa_uid") String event_id,
                                            @Field("eavqa_event_id") String sub_event_id,
                                            @Field("eavqa_about") String eavqa_about,
                                            @Field("eavqa_map_place_id") String eavqa_map_place_id,
                                            @Field("eavqa_users_type") String eavqa_users_type,
                                               @Field("update_volunteers") String update
                                               );

    @POST("events/searching_volunteers/")
    @FormUrlEncoded
    Call<SearchVolunteerResponse> searchVolunteer(@Field("searching_volunteers") String show,
                                               @Field("login_id") String login_id);

    @POST("events/add_map_places/")
    @FormUrlEncoded
    Call<AddPlacesResponse> addPlaces(@Field("add_map_places") String show,
                                            @Field("token") String token,
                                      @Field("etlaa_event_id") String etlaa_event_id,
                                      @Field("etlaa_places") String etlaa_places);

    @POST("events/show_map_places/")
    @FormUrlEncoded
    Call<ShowPlacesResponse> showPlaces(@Field("show_map_places") String show,
                                       @Field("etlaa_event_id") String etlaa_event_id);

    @POST("events/show_added_members/")
    @FormUrlEncoded
    Call<ShowMembersResponse> showMembers(@Field("show_added_members") String show,
                                         @Field("token") String token,
                                          @Field("show_event_id") String show_event_id,
                                          @Field("list_id") String list_id);
    @POST("events/show_added_members/")
    @FormUrlEncoded
    Call<ShowAllMembersResponse> showAllMembers(@Field("show_added_members") String show,
                                             @Field("token") String token,
                                             @Field("show_event_id") String show_event_id,
                                             @Field("list_id") String list_id);

    @POST("events/is_set_admin/")
    @FormUrlEncoded
    Call<CheckAdminResponse> checkAdmin(@Field("is_set_admin") String set,
                                      @Field("token") String token);

    @POST("tickets/check_tickets/")
    @FormUrlEncoded
    Call<BuyTicketResponse> checkTicket(@Field("check_tickets") String check,
                                        @Field("token") String token,
                                        @Field("event_id") String event_id);

    @POST("tickets/buying/")
    @FormUrlEncoded
    Call<BuyTicketResponse> buyTicket(@Field("buying") String buy,
                                        @Field("token") String token,
                                        @Field("event_id") String event_id,
                                      @Field("photo_upload") String photo_upload);

    @POST("tickets/my_tickets/")
    @FormUrlEncoded
    Call<MyTicketsResponse> myTickets(@Field("my_tickets") String show,
                                      @Field("token") String token);

    @POST("qrcode/scan_event_qr/")
    @FormUrlEncoded
    Call<ScanEventQrResponse> scanEventQr(@Field("scan_event_qr") String scan,
                                        @Field("token") String token,
                                          @Field("user_id") String user_id,
                                          @Field("scan_id") String scan_id);

    @POST("EventsImage/upload/")
    @FormUrlEncoded
    Call<ImageUploadResponse> imageUpload(@Field("upload") String upload,
                                          @Field("token") String token,
                                          @Field("event_id") String event_id,
                                          @Field("event_image") String event_image);

    @POST("EventsImage/delete/")
    @FormUrlEncoded
    Call<ImageUploadResponse> imageDelete(@Field("delete") String upload,
                                          @Field("token") String token,
                                          @Field("event_id") String event_id,
                                          @Field("event_image_id") String event_image_id);
    @POST("mapping/track/")
    @FormUrlEncoded
    Call<EventTrackResponse> eventTracking(@Field("track") String track,
                                           @Field("token") String token,
                                           @Field("mtaa_uid") String mtaa_uid,
                                           @Field("mtaa_event_id") String mtaa_event_id);

    @POST("profile/fav/")
    @FormUrlEncoded
    Call<ProfileFavResponse> profileFav(@Field("fav") String fav,
                                        @Field("token") String token,
                                        @Field("pro_id") String pro_id,
                                        @Field("pro_type") String pro_type);

    @POST("profile/like/")
    @FormUrlEncoded
    Call<ProfileLikeResponse> profileLike(@Field("like") String like,
                                          @Field("token") String token,
                                          @Field("pro_id") String pro_id,
                                          @Field("pro_type") String pro_type);

    @POST("profile/dislike/")
    @FormUrlEncoded
    Call<ProfileDislikeResponse> profileDisLike(@Field("dislike") String dislike,
                                                @Field("token") String token,
                                                @Field("pro_id") String pro_id,
                                                @Field("pro_type") String pro_type);

    @POST("profile/profile_status/")
    @FormUrlEncoded
    Call<ProfileStatusResponse> profileStatus(@Field("profile_status") String dislike,
                                              @Field("token") String token,
                                              @Field("pro_id") String pro_id);

    @POST("profile/rate/")
    @FormUrlEncoded
    Call<ProfileRatingResponse> profileRating(@Field("rate") String rate,
                                              @Field("token") String token,
                                              @Field("pro_id") String pro_id,
                                              @Field("pro_rate") String pro_rate);
    @POST("qrcode/fav/")
    @FormUrlEncoded
    Call<ProfileFavResponse> qrCodeFav(@Field("fav") String fav,
                                        @Field("token") String token,
                                        @Field("pro_id") String pro_id,
                                        @Field("pro_type") String pro_type);

    @POST("qrcode/like/")
    @FormUrlEncoded
    Call<ProfileLikeResponse> qrCodeLike(@Field("like") String like,
                                          @Field("token") String token,
                                          @Field("pro_id") String pro_id,
                                          @Field("pro_type") String pro_type);

    @POST("qrcode/dislike/")
    @FormUrlEncoded
    Call<ProfileDislikeResponse> qrCodeDisLike(@Field("dislike") String dislike,
                                                @Field("token") String token,
                                                @Field("pro_id") String pro_id,
                                                @Field("pro_type") String pro_type);

    @POST("qrcode/profile_status/")
    @FormUrlEncoded
    Call<ProfileStatusResponse> qrCodeStatus(@Field("profile_status") String dislike,
                                              @Field("token") String token,
                                              @Field("pro_id") String pro_id);

    @POST("qrcode/rate/")
    @FormUrlEncoded
    Call<ProfileRatingResponse> qrCodeRating(@Field("rate") String rate,
                                              @Field("token") String token,
                                              @Field("pro_id") String pro_id,
                                              @Field("pro_rate") String pro_rate);
}
