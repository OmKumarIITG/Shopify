package com.example.preorder_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.empty.datastore.PreferenceDataStoreHelper
import com.example.preorder_app.navigation.AppRouter
import com.example.preorder_app.navigation.Screens
import com.example.preorder_app.ui.screens.AboutScreen
import com.example.preorder_app.ui.screens.CartScreen
import com.example.preorder_app.ui.screens.ClothesScreen
import com.example.preorder_app.ui.screens.ElectronicsScreen
import com.example.preorder_app.ui.screens.LoginScreen
import com.example.preorder_app.ui.screens.SignUpScreen
import com.example.preorder_app.ui.screens.VegetablesScreen
import com.example.preorder_app.ui.theme.Preorder_appTheme
import com.example.preorder_app.viewmodels.google_auth.GoogleAuthUIClient
import com.example.preorder_app.viewmodels.signup.SignUpViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.initialize
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class BottomNavigationItem(
    val title:String,
    val selectedIcon: ImageVector,
    val unselectedIcon : ImageVector,
    val route :Screens
)

data class NavigationItem(
    val title:String,
    val hasNews : Boolean,
    val badgeCount:Int?=null,
    val route:Screens
)

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy{
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Preorder_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val datastore= PreferenceDataStoreHelper(LocalContext.current)
                    Firebase.initialize(this) //not use ktx
                    //google auth


                    //////////////   nav drawer + bottom nav

                    val itemsBottomNav = listOf(
                        BottomNavigationItem(
                            "Home",
                            Icons.Filled.Home,
                            Icons.Outlined.Home,
                            Screens.ElectronicsScreen
                        ),
                        BottomNavigationItem(
                            "Cart",
                            Icons.Filled.ShoppingCart,
                            Icons.Outlined.ShoppingCart,
                            Screens.CartScreen
                        ),
                        BottomNavigationItem(
                            "Profile",
                            Icons.Filled.Person,
                            Icons.Outlined.Person,
                            Screens.About
                        ),
                    )

                    val itemsDrawer = listOf(
                        NavigationItem(
                            "Electronics",
                            false,
                            route = Screens.ElectronicsScreen
                        ),
                        NavigationItem(
                            "Vegetables",
                            false,
                            badgeCount = 45,//unread emails,
                            Screens.VegetablesScreen
                        ),
                        NavigationItem(
                            "Clothes",
                            true, // update for app,
                            route = Screens.ClothesScreen
                        ),
                    )
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndexDrawer by rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    var selectedItemIndexBottomNav by rememberSaveable {
                        mutableIntStateOf(0)
                    }

                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet(
                                modifier=Modifier.fillMaxWidth(0.7f)
                            ) {
                                Box(
                                    modifier= Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.4f)
                                        .background(MaterialTheme.colorScheme.secondary),
                                    contentAlignment = Alignment.Center,
                                ){
                                    Text(
                                        "Shopify",
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp,
                                        color=MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                                itemsDrawer.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        label = {
                                            Text(item.title)
                                        },
                                        selected = selectedItemIndexDrawer == index,
                                        onClick = {
                                            selectedItemIndexDrawer = index
                                            scope.launch {
                                                Log.d("checkkkkkkk", "is drawer open before click ${drawerState.isOpen}")
                                                drawerState.close()
                                                Log.d("checkkkkkkk", "is drawer open after click ${drawerState.isOpen}")

                                            }
                                            AppRouter.navigateTo(item.route)
                                        },
                                        modifier=Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState //important to pass otherwise not work
                    ) {
                        Scaffold(
                            modifier=Modifier.fillMaxSize(),
                            bottomBar = {
                                NavigationBar {
                                    itemsBottomNav.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            selected = selectedItemIndexBottomNav == index,
                                            label = {
                                                Text(item.title)
                                            },
                                            onClick = {
                                                selectedItemIndexBottomNav = index
                                                AppRouter.navigateTo(item.route)
                                            },
                                            icon = {
                                                Icon(
                                                    if(selectedItemIndexBottomNav == index){
                                                        item.selectedIcon
                                                    }else{
                                                        item.unselectedIcon
                                                    },
                                                    item.title
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        ) {

                            Crossfade(targetState = AppRouter.currentScreen, label = "") {currentState->
                                when(currentState.value){

                                    is Screens.ElectronicsScreen ->{
                                        ElectronicsScreen(preferenceDataStoreHelper = datastore, username = "testUser", paddingValues = it, drawerState = drawerState)
                                    }
                                    is Screens.CartScreen ->{
                                        CartScreen(datastore,it,drawerState)
                                    }
                                    is Screens.About ->{
                                        AboutScreen(paddingValues = it, drawerState = drawerState)
                                    }
                                    is Screens.VegetablesScreen ->{
                                        VegetablesScreen(preferenceDataStoreHelper = datastore, username = "testUser", paddingValues = it, drawerState = drawerState)
                                    }
                                    is Screens.ClothesScreen ->{
                                        ClothesScreen(preferenceDataStoreHelper = datastore, username = "testUser", paddingValues = it, drawerState = drawerState)
                                    }
                                    else -> {

                                    }
                                }
                            }

                        }
                    }

                    Crossfade(targetState = AppRouter.currentScreen, label = "") {currentState->
                        when(currentState.value){
                            is Screens.SignUpScreen ->{
                                val viewModel = viewModel<SignUpViewModel>()
                                val state by viewModel.state.collectAsState()

                                LaunchedEffect(key1 =Unit){
                                    if(googleAuthUiClient.getSignedInUser()!=null){
                                        AppRouter.currentScreen.value = Screens.ElectronicsScreen
                                    }
                                }

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = {result->
                                        if(result.resultCode== RESULT_OK){
                                            lifecycleScope.launch {
                                                val signInResult = googleAuthUiClient.SignInWithIntent(
                                                    intent= result.data?: return@launch
                                                )
                                                viewModel.onSignInResult(signInResult)
                                            }
                                        }
                                    }
                                )

                                LaunchedEffect(key1 = state.isSignInSuccessful){
                                    if(state.isSignInSuccessful){
                                        Toast.makeText(
                                            applicationContext,
                                            "Sign in successful",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        AppRouter.navigateTo(Screens.ElectronicsScreen)
                                        viewModel.resetState()
                                    }
                                }

                                SignUpScreen(
                                    state = state,
                                    onSignInClick = {
                                        lifecycleScope.launch {
                                            val signInIntentSender = googleAuthUiClient.signIn()
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    signInIntentSender ?: return@launch
                                                ).build()
                                            )
                                        }
                                    }
                                )
                            }
                            is Screens.LoginScreen ->{
                                LoginScreen()
                            }
                            else -> {

                            }
                        }
                    }


                }
            }
        }
    }
}
