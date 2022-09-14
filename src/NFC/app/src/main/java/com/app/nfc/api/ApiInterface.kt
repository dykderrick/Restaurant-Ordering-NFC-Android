package hr.apps.n80982.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    //Login Api 1
    @POST ("login")
    fun getLogin(@Body map: HashMap<String, String>): Call<JsonObject>

    //Registration Api 2
    @FormUrlEncoded
    @POST("register")
    fun setRegistration(
        @Field("login_type") strLoginType: String
        , @Field("name") strName: String
        , @Field("email") strEmail: String
        , @Field("phone_no") strMobile: String
        , @Field("password") strPassword: String
        , @Field("lat") strLat: String
        , @Field("lang") strLang: String
        , @Field("device_id") strDevice_id: String
        , @Field("device_token") strDevice_Token: String
        , @Field("device_type") strDevice_Type: String
    ): Call<JsonObject>


    //single image upload
    @Multipart
    @POST("update-user")
    fun SetProfile(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone_no") phone: RequestBody,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lang") lang: RequestBody,
        @Part("location") locationbody: RequestBody,
        @Part("google_link") googleLink: RequestBody,
        @Part("fb_link") facebooklink: RequestBody,
        @Part("insta_link") instagramlink: RequestBody,
        @Part profileimage: MultipartBody.Part?,
        @Part coverImage: MultipartBody.Part?,
        @Header("Authorization") strToken: String
    ): Call<JsonObject>

    //Multiple image upload
    @Multipart
    @POST("add-event")
    fun SetAddEvent(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("location") location: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lang") lang: RequestBody,
        @Part("date") date: RequestBody,
        @Part("price") price: RequestBody,
        @Part("website") website: RequestBody,
        @Part("startTime") startTime: RequestBody,
        @Part("endTime") endTime: RequestBody,
        @Part("category") categoryIds:  RequestBody,
        @Part addImage: ArrayList<MultipartBody.Part>?,
        @Header("Authorization") strToken: String
    ): Call<JsonObject>

    //Get hot Event 10
    @GET("get-hot-events")
    fun getHotEvent(
        @Query("page") strPageNo: String,
        @Query(" user_id") strId: String
    ): Call<JsonObject>

}