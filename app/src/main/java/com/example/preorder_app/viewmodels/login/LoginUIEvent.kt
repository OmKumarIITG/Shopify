package com.example.firebase.dataLayer

sealed class LoginUIEvent {

    data class EmailNameChanged(val email : String) : LoginUIEvent()
    data class PasswordChanged(val password : String) : LoginUIEvent()

    object LoginButtonClicked : LoginUIEvent()
}