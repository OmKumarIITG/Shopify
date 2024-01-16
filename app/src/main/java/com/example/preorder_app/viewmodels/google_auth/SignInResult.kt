package com.example.preorder_app.viewmodels.google_auth

data class SignInResult(
    val data:UserData?,
    val errorMessage:String?
)

data class UserData(
    val userId:String,
    val username:String?,
    val profilePictureUrl:String?
)
