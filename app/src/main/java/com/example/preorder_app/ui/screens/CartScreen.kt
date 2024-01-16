package com.example.preorder_app.ui.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.empty.datastore.PreferenceDataStoreHelper
import com.example.preorder_app.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun extractNameAndPrice(input: String): Pair<String, String>? {
    val parts = input.split("$$$")
    return if (parts.size == 2) {
        Pair(parts[0].trim(), parts[1].trim())
    } else {
        null // Return null if the input format is not as expected
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    preferenceDataStoreHelper: PreferenceDataStoreHelper,
    paddingValues: PaddingValues,
    drawerState:DrawerState
) {

    val cart = mutableListOf<Pair<String, String>>()
    var refresh by remember{
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    runBlocking {
        val keys = preferenceDataStoreHelper.getAllKeys()
        keys.forEach { key ->
            extractNameAndPrice(key.toString())?.let { (name, price) ->
                // Handle the non-null name and price values here
                cart.add(name to price)
            }
        }
        Log.d("mainnnn",preferenceDataStoreHelper.getAllKeys().toString())
    }

    val scrollState = rememberScrollState()


    Column(
        modifier=Modifier.fillMaxSize()
    ){
        TopAppBar(
            title = {
                Text(
                    "Cart",
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
            modifier= Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Log.d("refreshh",refresh.toString())
            Column(
                modifier= Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .animateContentSize()
            ){
                if(cart.isEmpty()){
                    EmptyCart()
                }else{
                    cart.forEachIndexed { index, item ->
                        Card(
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            elevation= CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ){
                            Row(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ){
                                Column(
                                    modifier= Modifier
                                        .weight(0.8f)
                                ){
                                    Text(
                                        item.first,
                                        modifier=Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "${stringResource(id = R.string.Rs)} ${item.second}",
                                        modifier=Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Column(
                                    modifier= Modifier
                                        .weight(0.2f)
                                ){

                                    Button(
                                        onClick = {
                                            runBlocking {
                                                preferenceDataStoreHelper.removePreference(stringPreferencesKey(item.first+"$$$"+item.second))
                                                refresh = !refresh
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Filled.Cancel,"cancel")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyCart() {

        Column(
            modifier= Modifier
                .background(Color.White)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painterResource(id = R.drawable.empty_cart),
                "empty_cart"
            )
            Text(
                "Cart is empty",
                modifier=Modifier.fillMaxWidth(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
        }

}

@Preview
@Composable
fun P(){
    EmptyCart()
}