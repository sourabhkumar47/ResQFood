package com.project.resqfood.presentation.login.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.resqfood.R
import com.project.resqfood.ui.theme.BlueEnd
import com.project.resqfood.ui.theme.BlueStart
import com.project.resqfood.ui.theme.GreenEnd
import com.project.resqfood.ui.theme.GreenStart
import com.project.resqfood.ui.theme.OrangeEnd
import com.project.resqfood.ui.theme.OrangeStart
import com.project.resqfood.ui.theme.PurpleEnd
import com.project.resqfood.ui.theme.PurpleStart

data class Card(
    val Menu: String,
    val RestroName: String,
    val Offer: String,
    val Icon: ImageVector,
    val color: Brush
)

val cards = listOf(

    Card(
        Menu = "Menu",
        RestroName = "Chelani's Restro",
        Offer = "Up to 60% Off",
        Icon = Icons.Rounded.ArrowForwardIos,
        color = getGradiet(PurpleStart, PurpleEnd)
    ),
    Card(
        Menu = "Menu",
        RestroName = "Tripathi Brothers",
        Offer = "Up to 50% Off",
        Icon = Icons.Rounded.ArrowForwardIos,
        color = getGradiet(BlueStart, BlueEnd)
    ),
    Card(
        Menu = "Menu",
        RestroName = "Khan Bandu",
        Offer = "Up to 70% Off",
        Icon = Icons.Rounded.ArrowForwardIos,
        color = getGradiet(OrangeStart, OrangeEnd)
    ),
    Card(
        Menu = "Menu",
        RestroName = "Just Ride On Food",
        Offer = "Up to 80% Off",
        Icon = Icons.Rounded.ArrowForwardIos,
        color = getGradiet(GreenStart, GreenEnd)
    )

)

fun getGradiet(
    startColor: Color,
    endColor: Color,
): Brush {
    return Brush.horizontalGradient(
        colors = listOf(startColor, endColor)
    )
}

@Preview
@Composable
fun CardsSection() {
    LazyRow {
        items(cards.size) { index ->
            TopOffers(index)
        }
    }
}

@Composable
fun TopOffers(
    index: Int
) {
    val card = cards[index]
    var lastItemPaddingEnd = 0.dp
    if (index == cards.size - 1) {
        lastItemPaddingEnd = 16.dp
    }


    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = lastItemPaddingEnd)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(card.color)
                .width(250.dp)
                .height(150.dp)
                .clickable { }
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = card.RestroName,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = card.Offer,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Row(

            ) {
                Text(
                    text = card.Menu,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    modifier = Modifier.clickable {

                    },
                    tint = Color.White,
                    imageVector = card.Icon,
                    contentDescription = " Menu")

            }


        }
    }
}