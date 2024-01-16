package com.example.firebase.dataLayer

data class LoginUiState(
    var email : String = "",
    var password : String = "",

    var emailError :Boolean = false,
    var passwordError : Boolean = false
)
