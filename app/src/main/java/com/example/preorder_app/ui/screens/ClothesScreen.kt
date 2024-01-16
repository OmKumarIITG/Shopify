package com.example.preorder_app.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empty.datastore.PreferenceDataStoreHelper
import com.example.preorder_app.util.CardItem
import com.example.preorder_app.util.Item
import com.example.preorder_app.viewmodels.clothes.ClothesVM
import com.example.preorder_app.viewmodels.vegetables.VegetablesVM
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothesScreen(
    clothesVM: ClothesVM = viewModel(),
    preferenceDataStoreHelper: PreferenceDataStoreHelper,
    username:String,
    paddingValues: PaddingValues,
    drawerState: DrawerState
) {
    val clo:List<Item> = clothesVM.clothesUIState.value.clothes
    var refresh by remember{
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier= Modifier.fillMaxSize()
    ){
        TopAppBar(
            title = {
                Text(
                    "Clothes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Default,
                    modifier= Modifier.fillMaxWidth(),
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
            modifier= Modifier.fillMaxSize().padding(paddingValues)
        ){
            if(clothesVM.loading.value){
                CircularProgressIndicator(
                    modifier= Modifier.align(Alignment.Center)
                )
            }else{
                Log.d("mainnnn","refresh is ${refresh.toString()}")

                LazyColumn{
                    items(
                        clo,
                        key ={
                            it.url.toString()
                        }
                    ){item->
                        CardItem(
                            item = item,
                            preorderState = clothesVM.itemState(item,preferenceDataStoreHelper,username),
                            buttonClicked = {
                                clothesVM.PreorderClicked(item,preferenceDataStoreHelper,username)
                                refresh = ! refresh
                            }
                        )
                    }
                }
            }
        }
    }


}
