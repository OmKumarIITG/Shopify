package com.example.preorder_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebase.dataLayer.LoginUIEvent
import com.example.preorder_app.viewmodels.login.LoginViewModel
import com.example.preorder_app.R
import com.example.preorder_app.components.ButtonComponent
import com.example.preorder_app.components.ClickableLoginTextComponent
import com.example.preorder_app.components.DividerTextComponent
import com.example.preorder_app.components.HeadingTextComponent
import com.example.preorder_app.components.MyTextFieldComponent
import com.example.preorder_app.components.NormalTextComponent
import com.example.preorder_app.components.PasswordTextFieldComponent
import com.example.preorder_app.components.UnderlinedTextComponent
import com.example.preorder_app.navigation.AppRouter
import com.example.preorder_app.navigation.Screens

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel()
) {
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
                HeadingTextComponent(value = "Welcome Back")
                MyTextFieldComponent(
                    label = "Email",
                    painterResource = painterResource(id = R.drawable.message),
                    onTextChanged = {
                        loginViewModel.onEvent(LoginUIEvent.EmailNameChanged(it))
                    },
                    errorStatus = loginViewModel.loginUiState.value.emailError
                )
                PasswordTextFieldComponent(
                    label = "Password",
                    painterResource = painterResource(id = R.drawable.lock),
                    onTextChanged = {
                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    },
                    errorStatus = loginViewModel.loginUiState.value.passwordError
                )
                Spacer(modifier=Modifier.height(40.dp))
//                UnderlinedTextComponent(value = "Forgot your password")
                Spacer(modifier=Modifier.height(40.dp))
                ButtonComponent(
                    value = "Login",
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    },
                    loginViewModel.allValidationsPassed.value
                )
                Spacer(modifier=Modifier.height(20.dp))
                DividerTextComponent()
                ClickableLoginTextComponent(false,
                    onTextSelected = {
                        AppRouter.navigateTo(Screens.SignUpScreen)
                    }
                )
            }
        }

        if(loginViewModel.LoginInProgress.value){
            CircularProgressIndicator()
        }
    }
}

//@Preview
//@Composable
//fun  Pr(){
//    LoginScreen()
//}