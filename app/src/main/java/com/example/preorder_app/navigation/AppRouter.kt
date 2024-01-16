package com.example.preorder_app.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screens{
    object SignUpScreen : Screens()
    object LoginScreen : Screens()
    object ElectronicsScreen : Screens()
    object VegetablesScreen : Screens()
    object ClothesScreen:Screens()
    object CartScreen:Screens()
    object About:Screens()
}

object AppRouter{

    var currentScreen : MutableState<Screens> = mutableStateOf(Screens.SignUpScreen)

    fun navigateTo(destination:Screens){
        currentScreen.value = destination
    }
}