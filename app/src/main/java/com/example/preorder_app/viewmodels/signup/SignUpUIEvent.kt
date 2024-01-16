package com.example.firebase.dataLayer

sealed class SignUpUIEvent {

    data class FirstNameChanged(val firstName : String) : SignUpUIEvent()
    data class LastNameChanged(val lastName : String) : SignUpUIEvent()
    data class EmailNameChanged(val email : String) : SignUpUIEvent()
    data class PasswordChanged(val password : String) : SignUpUIEvent()

    //now clicking register is also an event
    //since all data is already in view model
    //use object not data class
    object RegisterButtonClicked : SignUpUIEvent()
}