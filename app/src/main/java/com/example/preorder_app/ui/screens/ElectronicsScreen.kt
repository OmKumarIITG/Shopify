package com.example.preorder_app.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.empty.datastore.PreferenceDataStoreHelper
import com.example.preorder_app.R
import com.example.preorder_app.navigation.Screens
import com.example.preorder_app.util.CardItem
import com.example.preorder_app.util.Item
import com.example.preorder_app.viewmodels.electronics.ElectronicsVM
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectronicsScreen(
    electronicsVM: ElectronicsVM = viewModel(),
    preferenceDataStoreHelper: PreferenceDataStoreHelper,
    username:String,
    paddingValues: PaddingValues,
    drawerState: DrawerState
) {
    val tv:List<Item> = electronicsVM.electronicsUIState.value.electronicsCartTV
    val ref:List<Item> = electronicsVM.electronicsUIState.value.electronicsCartRef
    val lap:List<Item> = electronicsVM.electronicsUIState.value.electronicsCartLap
    Log.d("electronicsScreen", electronicsVM.electronicsUIState.value.electronicsCartTV.toString())
    var refresh by remember{
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier=Modifier.fillMaxSize()
    ){
        TopAppBar(
            title = {
                Text(
                    "Welcome to Shopping",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Default,
                    modifier=Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            colors= TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(Icons.Filled.Menu,"menu open")
                }
            }
        )
        Box(
            modifier=Modifier.fillMaxSize().padding(paddingValues)
        ){
            if(electronicsVM.loading.value){
                CircularProgressIndicator(
                    modifier=Modifier.align(Alignment.Center)
                )
            }else{

                Log.d("mainnnn","refresh is ${refresh.toString()}")

                LazyColumn{
                    item{
                        Text(
                            "TVs",
                            modifier=Modifier.fillMaxWidth().background(Color.White),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Monospace,
                                fontStyle = FontStyle.Normal
                            )
                        )
                    }
                    items(
                        tv,
                        key ={
                            it.url.toString()
                        }
                    ){item->
                        CardItem(
                            item = item,
                            preorderState = electronicsVM.itemState(item,preferenceDataStoreHelper,username),
                            buttonClicked = {
                                electronicsVM.PreorderClicked(item,preferenceDataStoreHelper,username)
                                refresh = ! refresh
                            }
                        )
                    }
                    item{
                        Text(
                            "Refrigerators",
                            modifier=Modifier.fillMaxWidth().background(Color.White),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Monospace,
                                fontStyle = FontStyle.Normal
                            )
                        )
                    }
                    items(
                        ref,
                        key ={
                            it.url.toString()
                        }
                    ){item->
                        CardItem(
                            item = item,
                            preorderState = electronicsVM.itemState(item,preferenceDataStoreHelper,username),
                            buttonClicked = {
                                electronicsVM.PreorderClicked(item,preferenceDataStoreHelper,username)
                                refresh = ! refresh
                            }
                        )
                    }
                    item{
                        Text(
                            "Laptops",
                            modifier=Modifier.fillMaxWidth().background(Color.White),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Monospace,
                                fontStyle = FontStyle.Normal
                            )
                        )
                    }
                    items(
                        lap,
                        key ={
                            it.url.toString()
                        }
                    ){item->
                        CardItem(
                            item = item,
                            preorderState = electronicsVM.itemState(item,preferenceDataStoreHelper,username),
                            buttonClicked = {
                                electronicsVM.PreorderClicked(item,preferenceDataStoreHelper,username)
                                refresh = ! refresh
                            }
                        )
                    }
                }
            }
        }
    }


}
