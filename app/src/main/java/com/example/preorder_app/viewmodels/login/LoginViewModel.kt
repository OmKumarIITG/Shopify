package com.example.preorder_app.viewmodels.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firebase.dataLayer.LoginUIEvent
import com.example.firebase.dataLayer.LoginUiState
import com.example.preorder_app.navigation.AppRouter
import com.example.preorder_app.navigation.Screens
import com.example.preorder_app.rules.Validator
import com.example.preorder_app.viewmodels.google_auth.SignInResult
import com.example.preorder_app.viewmodels.google_auth.SignInState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel() : ViewModel(){

    private val TAG = LoginViewModel::class.simpleName

    var loginUiState = mutableStateOf(LoginUiState())

    var allValidationsPassed = mutableStateOf(false)

    var LoginInProgress = mutableStateOf(false)

    //call this function to update data in data layer on change in ui layer i.e ui state
    fun onEvent(event: LoginUIEvent){
        when(event){
            is LoginUIEvent.EmailNameChanged -> {
                loginUiState.value = loginUiState.value.copy(
                    email = event.email
                )
            }
            is LoginUIEvent.PasswordChanged -> {
                loginUiState.value = loginUiState.value.copy(
                    password = event.password
                )
            }
            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
        validateLoginUIDataWithRules()
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUiState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = loginUiState.value.password
        )

        loginUiState.value = loginUiState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value = emailResult.status && passwordResult.status

    }

    private fun login() {

        LoginInProgress.value = true
        val email = loginUiState.value.email
        val password = loginUiState.value.password

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG,"Inside_login_success")
                Log.d(TAG,"${it.isSuccessful}")

                if(it.isSuccessful){
                    LoginInProgress.value = false
                    AppRouter.navigateTo(Screens.ElectronicsScreen)
                }
            }
            .addOnFailureListener {
                Log.d(TAG,"Inside_login_failure")
                Log.d(TAG, it.localizedMessage!!)

                LoginInProgress.value = false

            }
    }

}