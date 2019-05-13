package com.crossapps.petpal.Server.Request

data class RegisterRequest(
    var name:String,
    var email: String,
    var password: String,
    var mobileNo: String
    )