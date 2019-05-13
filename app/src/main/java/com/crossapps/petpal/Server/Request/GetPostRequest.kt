package com.crossapps.petpal.Server.Request

data class GetPostRequest (
    var userId:String,
    var category:String,
    var offset:String

)

//{"userId":"30","category":"HomePosts","offset":"0"}


