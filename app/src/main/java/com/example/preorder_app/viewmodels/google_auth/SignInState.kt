package com.example.preorder_app.viewmodels.google_auth

data class SignInState(
    val isSignInSuccessful : Boolean = false,
    val signInError : String? = null
)
