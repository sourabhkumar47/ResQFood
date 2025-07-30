package com.project.resqfood.presentation.onboardingProcess

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.project.resqfood.R
import com.project.resqfood.presentation.MainActivity
import com.project.resqfood.presentation.login.mainlogin.NavSignInUI
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object NavOnboarding

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Onboarding(
    modifier: Modifier = Modifier, navController: NavHostController?
) {
    val animations = listOf(
        R.raw.intro1,
        R.raw.intro2,
        R.raw.intro3,
        R.raw.intro4,
    )
    val title = listOf(
        "Save Money on Quality Meals",
        "Help Reduce Food Waste",
        "Support Local Restaurants",
        "Join a Sustainable Community"
    )
    val descriptions = listOf(
        R.string.des1,
        R.string.des2,
        R.string.des3,
        R.string.des4,
    )
    val pageState = rememberPagerState(pageCount = 4)
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .wrapContentSize()
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val composition by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(
                        animations[it]
                    )
                )
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(400.dp)
                )
                Text(
                    text = title[it],
                    textAlign = TextAlign.Center,
                    fontSize = 44.sp,
                    lineHeight = TextUnit(44f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = descriptions[it]),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    lineHeight = TextUnit(14f, TextUnitType.Sp),
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
    navController?.let {
        ButtonsSection(
            pagerState = pageState,
            navController = navController,
            context = MainActivity()
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ButtonsSection(
    pagerState: PagerState,
    navController: NavHostController,
    context: MainActivity
) {
    val scope = rememberCoroutineScope()
    val animatedIndicator by animateDpAsState(
        targetValue =20.dp * pagerState.currentPage,
        animationSpec = tween()
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(42.dp)
    ) {
        OutlinedButton(
            onClick = { navController.navigate(NavSignInUI) },
            modifier = Modifier
                .align(AbsoluteAlignment.BottomLeft)
                .padding(8.dp),
            ) {
            Text(text = "Skip")
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 25.dp, end = if (pagerState.currentPage != 3) 0.dp else 44.dp)
        ){
            repeat(pagerState.pageCount){
                Spacer(modifier = Modifier.size(3.dp))
                Box(modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .border(1.dp, color = MaterialTheme.colorScheme.outline, CircleShape)
                ){}
                Spacer(modifier = Modifier.size(3.dp))
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = 25.dp, end = if (pagerState.currentPage != 3) 60.dp else 104.dp)
                .align(Alignment.BottomCenter)
                .offset(x = animatedIndicator)
                .size(14.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
        )

        if (pagerState.currentPage != 3) {
            Button(
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier
                    .align(AbsoluteAlignment.BottomRight)
                    .padding(8.dp)
            ) {
                Text(text = "Next")
            }
        } else {
            Button(
                onClick = { navController.navigate(NavSignInUI) },
                modifier = Modifier
                    .align(AbsoluteAlignment.BottomRight)
                    .padding(8.dp)
            ) {
                Text(text = "Get Started")
            }
        }
    }
}

private fun onBoardingIsFinished(context: MainActivity) {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isFinished", true)
    editor.apply()

}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewOnboarding() {
    Onboarding(navController = null)
}
