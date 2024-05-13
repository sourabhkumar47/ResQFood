package com.project.resqfood.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.resqfood.R

@Preview
@Composable
fun loginPage() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                painter = painterResource(id = R.drawable.logo),

                contentDescription = "null"
            )

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(6.dp)

        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .height(350.dp)
                    .clip(RoundedCornerShape(16.dp)),
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 5.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(top = 16.dp, start = 60.dp),
                        text = "Welcome To ResQFood",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold

                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "Sign In",
                            fontWeight = FontWeight.SemiBold
                        )

                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "Sign Up",
                            fontWeight = FontWeight.SemiBold
                        )

                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Divider(color = Color.Black, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(30.dp))


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "google",
                            modifier = Modifier.size(32.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = "facebook",
                            modifier = Modifier.size(32.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.instagram),
                            contentDescription = "instagram",
                            modifier = Modifier.size(32.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.twitter),
                            contentDescription = "twitter",
                            modifier = Modifier.size(32.dp)
                        )

                    }


                }


            }

        }


    }


}