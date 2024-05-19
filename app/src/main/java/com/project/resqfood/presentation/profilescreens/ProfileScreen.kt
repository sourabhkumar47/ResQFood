package com.project.resqfood.presentation.profilescreens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.HouseSiding
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.project.resqfood.R
import com.project.resqfood.presentation.Destinations
import com.project.resqfood.presentation.MainActivity
import com.project.resqfood.presentation.login.SignInViewModel

@Composable
fun ProfileScreen(paddingValues: PaddingValues, navController: NavController) {
    val viewModel: SignInViewModel = viewModel()
    val user by viewModel.user.collectAsState()
    Log.i("ProfileScreen", "User data fetched: $user")
    val auth = FirebaseAuth.getInstance()
    LaunchedEffect(key1 = MainActivity.userEntity) {
        try {
            if (auth.currentUser != null)
                viewModel.getUserData(auth.currentUser!!.uid)
        }catch (e: Exception){
            Log.e("ProfileScreen", "Error: ${e.message}")
        }
    }
    var areButtonsVisible by remember {
        mutableStateOf(true)
    }
    var selectedTabIsOrder by remember{mutableStateOf(true)}
    var sizeState by remember {
        mutableStateOf(140.dp)
    }

    val animatedSize by animateDpAsState(
        targetValue = sizeState,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing
        ), label = "animated size"
    )
    val scrollSate = rememberScrollState()
    LaunchedEffect(key1 = scrollSate.value) {
        sizeState = (140 - (60.0/scrollSate.maxValue)*scrollSate.value).toInt().dp
        if(sizeState > 110.dp)
            areButtonsVisible = true
        else
            areButtonsVisible = false
        Log.i("scroll", "scroll value: ${scrollSate.value} && size = $sizeState && are")
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()

    ) {
            Column(
            ) {
                Box {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    ProfilePictureView(size = animatedSize, profilePictureUrl = user?.profileUrl ?: "",
                        ){
                        navController.navigate(Destinations.PersonalDetails.route)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.height(animatedSize)
                    ) {
                        Text(text = user?.name ?: "User", style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary)
                        Text(text = user?.email?:"Email", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            //This Row has to be removed while animating
                AnimatedVisibility(visible = areButtonsVisible) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly) {
                            AnimatedCards(vectorImage = Icons.Default.StarRate, text = "Rating", onClick = { /*TODO*/ })
                            AnimatedCards(vectorImage = Icons.Default.Favorite, text = "Favorites", onClick = { /*TODO*/ }, width = 1f)
                        }
                    }

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                    SelectionCardsUI(text = "My Orders", onClick = {selectedTabIsOrder = true}, isSelected = selectedTabIsOrder
                    , width = 0.5f)
                    SelectionCardsUI(text = "More", onClick = {selectedTabIsOrder = false}, isSelected = !selectedTabIsOrder,
                        width = 1.0f)
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .verticalScroll(scrollSate)
                ) {
                        when(selectedTabIsOrder){
                            true ->{
                                    RowOfProfile(
                                        vectorImage = Icons.Default.Fastfood,
                                        text = "Your Orders",
                                        onClick = { /*TODO*/ }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    RowOfProfile(
                                        vectorImage = Icons.Default.House,
                                        text = "Your Addresses",
                                        onClick = { /*TODO*/ }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    RowOfProfile(
                                        vectorImage = Icons.Default.Payments,
                                        text = "Payment Methods",
                                        onClick = { /*TODO*/ }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    RowOfProfile(
                                        vectorImage = Icons.Default.Restaurant,
                                        text = "Add Restaurant",
                                        onClick = { /*TODO*/ }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    RowOfProfile(
                                        vectorImage = Icons.Default.HouseSiding,
                                        text = "Add Trusts",
                                        onClick = { /*TODO*/ }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            else ->{
                                RowOfProfile(
                                    vectorImage = Icons.Default.Person,
                                    text = "Edit Personal Details",
                                    onClick = {
                                        navController.navigate(Destinations.PersonalDetails.route)
                                    })
                                Spacer(modifier = Modifier.height(8.dp))
                                RowOfProfile(
                                    vectorImage = Icons.Default.Star,
                                    text = "Rate Us",
                                    onClick = { /*TODO*/ })
                                Spacer(modifier = Modifier.height(8.dp))
                                RowOfProfile(
                                    vectorImage = Icons.Default.Feedback,
                                    text = "Send Feedback",
                                    onClick = { /*TODO*/ }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                RowOfProfile(
                                    vectorImage = Icons.Default.Delete,
                                    text = "Delete Account",
                                    onClick = { /*TODO*/ }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                RowOfProfile(
                                    vectorImage = Icons.AutoMirrored.Filled.Logout,
                                    text = "Log Out",
                                    onClick = {
                                        logoutUser(navController)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                            }
                    }
                    Spacer(modifier =Modifier.height(180.dp))
                }
        }
    }
}

@Composable
fun RowOfProfile(vectorImage: ImageVector, text: String, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()

                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = vectorImage, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null)
        }
    }
}

@Composable
fun AnimatedCards(vectorImage: ImageVector, text: String, onClick: () -> Unit,
                  width: Float = 0.5f){
    OutlinedButton(onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier.width(180.dp)) {
            Icon(imageVector = vectorImage, contentDescription = text)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text)
    }
}
@Composable
fun SelectionCardsUI(text: String, onClick: () -> Unit,
                     width: Float = 0.5f, isSelected: Boolean){
    Box(
        modifier = Modifier
            .fillMaxWidth(width)
            .height(44.dp)
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, style =if(isSelected) MaterialTheme.typography.bodyMedium
            else MaterialTheme.typography.bodySmall,
                fontWeight = if(isSelected) FontWeight.Bold
                else MaterialTheme.typography.titleSmall.fontWeight,
                color = if(isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
                )
        }
        if(isSelected){
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomEnd),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarProfileScreen(){
    TopAppBar(title = {
        Text("Profile")
    })
}

@Composable
fun ProfilePictureView(profilePictureUrl: String = "", size: Dp = 100.dp, onClick: () -> Unit){
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .width(size)
            .height(size)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .clickable {
                    onClick()
                }) {
            if (profilePictureUrl == "null" || profilePictureUrl.isEmpty()) {
                Icon(
                    imageVector = Icons.Default.Person,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            shape = CircleShape
                        )
                )

            } else {
                AsyncImage(
                    model = profilePictureUrl,
                    contentDescription = "profile",
                    imageLoader = ImageLoader(context),
                    placeholder = painterResource(id = R.drawable.user),
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

fun logoutUser(navController: NavController) {
    MainActivity.userEntity = null
    FirebaseAuth.getInstance().signOut()
    navController.popBackStack(navController.graph.startDestinationId, true)
    navController.navigate(Destinations.SignIn.route)
}

