package com.project.resqfood.presentation.login.BottomNavigation

import android.graphics.drawable.Icon
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CurrencyYen
import androidx.compose.material.icons.rounded.Euro
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.resqfood.R

data class Trust(
    val name :String,
    val address : String,
    val distance : Float,
    val icon: ImageVector

)
val Trusts = listOf(
    Trust(

        name  = "Trust 1 ",
        address  = "Area 1 ",
        distance  =  1.12f,
        icon = Icons.Rounded.ArrowForwardIos
    ),
    Trust(
        name  = "Trust 2 ",
        address  = "Area 2",
        distance  =  2.56f,
        icon = Icons.Rounded.ArrowForwardIos
    ),
    Trust(
        name  = "Trust 3 ",
        address  = "Area 3",
        distance  =  5.46f,
        icon = Icons.Rounded.ArrowForwardIos
    ),
    Trust(

        name  = "Trust 4 ",
        address  = "Area 4 ",
        distance  =  6.48f,
        icon = Icons.Rounded.ArrowForwardIos
    ),
    Trust(

        name  = "Trust 5 ",
        address  = "Area 5",
        distance  =  9.28f,
        icon = Icons.Rounded.ArrowForwardIos
    ),
    Trust(

        name  = "Trust 6 ",
        address  = "Area 6",
        distance  =  0.56f,
        icon = Icons.Rounded.ArrowForwardIos
    ),

    )



@Composable
fun Trust(){
    var isVisible by remember {
        mutableStateOf(false)
    }
    var iconState by remember {
        mutableStateOf(Icons.Rounded.KeyboardArrowUp)
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 32.dp),
    ){
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .animateContentSize()
        ) {
            Row (
                modifier = Modifier
                    .padding(16.dp)
                    .animateContentSize()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                        isVisible = !isVisible
                        iconState = if (isVisible) {
                            Icons.Rounded.KeyboardArrowUp
                        } else {
                            Icons.Rounded.KeyboardArrowDown
                        }
                    }

                ) {
                    Icon(
                        modifier = Modifier.size(25.dp) ,
                        imageVector = iconState,
                        contentDescription = "Trusts",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Trusts",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold
                )

            }
            Spacer(modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
            )
            if(isVisible){
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    val boxWithConstraintsScope = this
                    val width = boxWithConstraintsScope.maxWidth
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)

                    ) {
                        Spacer(modifier = Modifier.height(16.dp))


                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn {
                            items(Trusts.size) { index ->
                                CurrencyItem(
                                    index = index,
                                    width = width
                                )
                            }
                        }


                    }
                }
            }
        }




    }



@Composable
fun CurrencyItem(index:Int, width: Dp){
    val trust = Trusts[index]
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 40.dp),
        

    ){
        Row (
            modifier = Modifier.width(width)
                .padding(top = 16.dp),
           
        ) {
            Column(
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    text = trust.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,

                    )
                Row {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        text = trust.address,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground,

                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        text = "${trust.distance} Km",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground,

                    )

                }


            }
            Row (
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ){
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .padding(4.dp)

                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = trust.icon,
                        contentDescription = trust.name,
                        tint = Color.Black
                    )
                }

            }




        }



    }
}