package com.crossapps.petpal.Server


import com.crossapps.petpal.Server.Request.*
import com.crossapps.petpal.Server.Response.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface TCApi {

    //  RetrofitApiAuthSingleTon.createService(WPApi::class.java,loginResponse!!.data.verifyToken)
    @POST("users/login")
    fun callLoginApi(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("users/register")
    fun callRegisterApi( @Body loginRequest : RegisterRequest): Call<RegisterResponse>

    @POST("Pets/getPets")
    fun callPetsApi(): Call<PetResponse>

    @POST("post/deletePost")
    fun callDeletePost(@Body deletePostRequest: DeletePostRequest): Call<DefaultResponse>

    @POST("post/getPostDetail")
    fun callPostsApi(@Body getPostRequest: GetPostRequest): Call<PostResponse>

    @Multipart
    @POST("post/addPost")
    fun uploadMultipleFilesDynamic(
        @Part("userId") user_id: RequestBody, @Part("description") description: RequestBody, @Part(
            "category"
        ) category: RequestBody, @Part("location") location: RequestBody, @PartMap post_medias: Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<ResponseBody>

//  @Multipart
//  @POST("users/addProfilePic")
//  fun uploadProfilePic(@Part("userId") user_id: RequestBody , @PartMap profile_picture:Map<String, @JvmSuppressWildcards RequestBody> ): Call<LoginResponse>


}