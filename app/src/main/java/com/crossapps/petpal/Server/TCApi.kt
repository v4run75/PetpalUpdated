package com.crossapps.petpal.Server


import com.crossapps.petpal.Server.Request.*
import com.crossapps.petpal.Server.Response.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface TCApi {

//  RetrofitApiAuthSingleTon.createService(WPApi::class.java,loginResponse!!.data.verifyToken)

  @POST("Pets/getPets")
  fun callPetsApi(): Call<PetResponse>
//  @POST("users/saveSocialUser")
////  fun callSaveSocialUser( @Body socialRequest : SaveSocialRequest): Call<UpdateSocialResponse>
//  fun callSaveSocialUser( @Body socialRequest : SaveSocialRequest): Call<LoginResponse>
//  @POST("users/register")
//  fun callRegisterApi( @Body loginRequest : RegisterRequest): Call<RegisterResponse>
//  @POST("users/verifyOtp")
//  fun callVerifyOtpApi( @Body verifyRequest : VerifyRequest): Call<LoginResponse>
//  @POST("users/updateSocialProfile")
//  fun callUpdateSocialApi( @Body verifySocialRequest : VerifySocialRequest): Call<DefaultResponse>
//  @POST("users/posts")
//  fun callUserPostsApi( @Body userPostRequest: UserPostRequest): Call<PostResponse>
//  @POST("users/get_otp")
//  fun callGenerateOtpApi( @Body generateOTPRequest: GenerateOTPRequest): Call<DefaultResponse>
//  @POST("users/updateProfile")
//  fun callUpdateProfile( @Body updateProfileRequest: UpdateProfileRequest): Call<LoginResponse>
//  @POST("utility/getCity")
//  fun callCityList(): Call<CityResponse>
//  @POST("post/getPostDetail")
//  fun callPostsApi(@Body getPostRequest: GetPostRequest): Call<PostResponse>
//  @POST("post/addLike")
//  fun callLikeApi(@Body likeRequest: LikeRequest): Call<LikeResponse>
//  @POST("post/comments")
//  fun callGetCommentApi(@Body commentRequest: CommentRequest): Call<CommentResponse>
//  @POST("post/addComment")
//  fun callAddCommentApi(@Body addCommentRequest: AddCommentRequest): Call<CommentResponse>
//  @POST("post/adminPosts")
//  fun callAdminPosts(@Body adminPostRequest: AdminPostRequest): Call<AdminPostResponse>
//  @POST("petition/lists")
//  fun callPetitionListApi(@Body petitionRequest: PetitionRequest): Call<PetitionResponse>
//  @POST("petition/add")
//  fun callAddPetitionApi(@Body addPetitionRequest: AddPetitionRequest): Call<DefaultResponse>
//  @POST("petition/addSign")
//  fun callSignPetitionApi(@Body signPetitionRequest: SignPetitionRequest): Call<DefaultResponse>
//  @POST("petition/signatures")
//  fun callSignaturesApi(@Body signaturesRequest: SignaturesRequest): Call<SignatureResponse>
//
//
//  @Multipart
//  @POST("post/addPost")
//  fun uploadMultipleFilesDynamic(@Part("userId") user_id: RequestBody ,@Part("description") description: RequestBody,@Part("category") category: RequestBody,@Part("location") location: RequestBody, @PartMap post_medias:Map<String, @JvmSuppressWildcards RequestBody> ): Call<ResponseBody>
//
//  @Multipart
//  @POST("users/addProfilePic")
//  fun uploadProfilePic(@Part("userId") user_id: RequestBody , @PartMap profile_picture:Map<String, @JvmSuppressWildcards RequestBody> ): Call<LoginResponse>


}