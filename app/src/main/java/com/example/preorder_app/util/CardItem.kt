package com.example.preorder_app.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.preorder_app.R

@Composable
fun CardItem(
    item: Item,
    preorderState:Boolean,
    buttonClicked:()->Unit
) {
    Card(
        modifier= Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp),
        elevation= CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Box(
            modifier= Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Row(
                modifier= Modifier.fillMaxSize()
            ){
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(item.url)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 10.dp)
                        .weight(0.5f),
                    contentScale = ContentScale.Inside, //to fill whole screen vertically and horizontally both
                    error = painterResource(id = R.drawable.ic_broken_image),//in case image failed to load
                    placeholder = painterResource(id = R.drawable.loading_img),//while its loaded , its shown
                    alignment = Alignment.Center
                )
                Spacer(modifier= Modifier.width(5.dp))
                Column(
                    modifier= Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .padding(end = 5.dp),
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        item.name!!,
                        style= TextStyle(
                            color= Color.Blue,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier= Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "${stringResource(id = R.string.Rs)}${item.price}",
                        style= TextStyle(color= Color.Black, fontSize = 16.sp),
                        modifier= Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier= Modifier.height(5.dp))
                    Button(
                        onClick = {
                            buttonClicked()
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 15.dp,
                            disabledElevation = 0.dp,
                            hoveredElevation = 15.dp,
                            focusedElevation = 10.dp
                        ),
                        modifier= Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(!preorderState){
                                MaterialTheme.colorScheme.primary
                            }else MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        if(!preorderState){
                            Text("Preorder Now")
                        }else{
                            Text("Cancel Preorder")
                        }
                    }
                }
            }
        }

    }
}
