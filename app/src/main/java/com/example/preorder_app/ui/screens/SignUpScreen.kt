package com.example.preorder_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.preorder_app.viewmodels.signup.SignUpViewModel
import com.example.firebase.dataLayer.SignUpUIEvent
import com.example.preorder_app.R
import com.example.preorder_app.components.ButtonComponent
import com.example.preorder_app.components.ClickableLoginTextComponent
import com.example.preorder_app.components.HeadingTextComponent
import com.example.preorder_app.components.MyTextFieldComponent
import com.example.preorder_app.components.NormalTextComponent
import com.example.preorder_app.components.PasswordTextFieldComponent
import com.example.preorder_app.navigation.AppRouter
import com.example.preorder_app.navigation.Screens
import com.example.preorder_app.viewmodels.google_auth.SignInState

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = viewModel(),
    state: SignInState,
    onSignInClick: ()->Unit
) {
    val context =  LocalContext.current
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let { error->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
      modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Surface (
            modifier= Modifier
                .fillMaxSize()
                .background(Color.White)//order matters first background ,then padding
                .padding(28.dp)
        ){
            Column (
                modifier= Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ){
                NormalTextComponent(value = "Hello there,")
                HeadingTextComponent(value = "Create an Account")
                Spacer(modifier = Modifier.height(20.dp))
                MyTextFieldComponent(
                    label = "First Name",
                    painterResource(id= R.drawable.profile),
                    onTextChanged = {
                        //update data in data layer
                        signUpViewModel.onEvent(SignUpUIEvent.FirstNameChanged(it))
                    },
                    signUpViewModel.registrationUiState.value.firstNameError
                )
                MyTextFieldComponent(
                    label = "Last Name",
                    painterResource(id= R.drawable.profile),
                    onTextChanged = {
                        signUpViewModel.onEvent(SignUpUIEvent.LastNameChanged(it))

                    },
                    signUpViewModel.registrationUiState.value.lastNameError
                )
                MyTextFieldComponent(
                    label = "Email",
                    painterResource(id= R.drawable.message),
                    onTextChanged = {
                        signUpViewModel.onEvent(SignUpUIEvent.EmailNameChanged(it))
                    },
                    signUpViewModel.registrationUiState.value.emailError
                )
                PasswordTextFieldComponent(
                    label = "Password",
                    painterResource(id= R.drawable.ic_lock),
                    onTextChanged = {
                        signUpViewModel.onEvent(SignUpUIEvent.PasswordChanged(it))

                    },
                    signUpViewModel.registrationUiState.value.passwordError
                )
                Spacer(modifier=Modifier.height(40.dp))
                ButtonComponent(
                    value = "Register",
                    onButtonClicked = {
                        signUpViewModel.onEvent(SignUpUIEvent.RegisterButtonClicked)
                    },
                    isEnabled = signUpViewModel.allValidationsPassed.value
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "google_sign_in",
                        modifier=Modifier
                            .clickable (
                                onClick = onSignInClick
                            )
                            .width(500.dp)
                            .height(50.dp)
                    )
                }
                ClickableLoginTextComponent(true,
                    onTextSelected = {
                        AppRouter.navigateTo(Screens.LoginScreen)
                    }
                )
            }
        }

        if(signUpViewModel.signUpInProgress.value){
            CircularProgressIndicator()
        }
    }

}


//@Preview
//@Composable
//fun P(){
//    SignUpScreen()
//}