package com.example.preorder_app.viewmodels.signup

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firebase.dataLayer.RegistrationUiState
import com.example.firebase.dataLayer.SignUpUIEvent
import com.example.preorder_app.navigation.AppRouter
import com.example.preorder_app.navigation.Screens
import com.example.preorder_app.rules.Validator
import com.example.preorder_app.viewmodels.google_auth.SignInResult
import com.example.preorder_app.viewmodels.google_auth.SignInState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel() : ViewModel(){

    private val TAG = SignUpViewModel::class.simpleName

    var registrationUiState = mutableStateOf(RegistrationUiState())

    var allValidationsPassed = mutableStateOf(false)

    var signUpInProgress = mutableStateOf(false)

    //call this function to update data in data layer on change in ui layer i.e ui state
    fun onEvent(event: SignUpUIEvent){

        validateDataWithRules()//validate dynamically on value change
        when(event){
            is SignUpUIEvent.FirstNameChanged -> {
                registrationUiState.value = registrationUiState.value.copy(
                    firstName = event.firstName
                )
                printState()
            }
            is SignUpUIEvent.LastNameChanged -> {
                registrationUiState.value = registrationUiState.value.copy(
                    lastName = event.lastName
                )
                printState()
            }
            is SignUpUIEvent.EmailNameChanged -> {
                registrationUiState.value = registrationUiState.value.copy(
                    email = event.email
                )
                printState()
            }
            is SignUpUIEvent.PasswordChanged -> {
                registrationUiState.value = registrationUiState.value.copy(
                    password = event.password
                )
                printState()
            }
            is SignUpUIEvent.RegisterButtonClicked -> {
                signUp()
            }
        }
    }

    private fun signUp(){
        Log.d(TAG,"inside_signUp")
        printState()

        //on successful signup create user
        createUserInFirebase(
            email = registrationUiState.value.email,
            password = registrationUiState.value.password
        )
    }

    private fun validateDataWithRules() {
        val fNameResult = Validator.validateFirstName(
            fName = registrationUiState.value.firstName
        )

        val lNameResult = Validator.validateLastName(
            lName = registrationUiState.value.lastName
        )

        val emailResult = Validator.validateEmail(
            email = registrationUiState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = registrationUiState.value.password
        )

        //update in data layer

        registrationUiState.value = registrationUiState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        //check is all validations passed or not
        allValidationsPassed.value = fNameResult.status && lNameResult.status &&
                emailResult.status && passwordResult.status
    }

    //func to check state
    private fun printState(){
        Log.d(TAG,"inside_printState")
        Log.d(TAG,registrationUiState.value.toString())
    }

    private fun createUserInFirebase(email:String,password:String){

        signUpInProgress.value =true
        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_OnCompleteListener")
                Log.d(TAG, " isSuccessful = ${it.isSuccessful}")

                signUpInProgress.value = false
                if(it.isSuccessful){
                    AppRouter.navigateTo(Screens.ElectronicsScreen)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_OnFailureListener")
                Log.d(TAG, "Exception= ${it.message}")
                Log.d(TAG, "Exception= ${it.localizedMessage}")

            }
    }

    fun logout(){

        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener = AuthStateListener{
            if(it.currentUser == null){
                Log.d(TAG,"inside sign out success")
                AppRouter.navigateTo(Screens.LoginScreen)
            }else{
                Log.d(TAG,"inside sig out failure")
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)
    }


    //functions for google signUp or login
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _state.update{
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState(){
        _state.update { SignInState() }
    }
}