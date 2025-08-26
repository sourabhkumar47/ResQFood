package com.project.resqfood.presentation.profilescreens


import NavOrderingPickup
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.project.resqfood.R
import com.project.resqfood.presentation.MainActivity
import com.project.resqfood.presentation.itemdetailscreen.NavAddingLeftovers
import com.project.resqfood.presentation.login.NavPersonalDetails
import com.project.resqfood.presentation.login.mainlogin.NavSignInUI
import com.project.resqfood.presentation.login.SignInDataViewModel
import com.project.resqfood.presentation.restaurantonboarding.NavListingRestaurant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


data class ProfileItem(val vectorImage: ImageVector, val label: String, val onClick: (NavController) -> Unit)


val myOrderList = listOf(
    ProfileItem(vectorImage = Icons.Default.Fastfood, label = "Your Orders", onClick = {/*TODO*/}),
    //address of person
    ProfileItem(
        vectorImage = Icons.Default.House,
        label = "Your Addresses",
        onClick = { it.navigate("address_screen") }
    ),
    ProfileItem(vectorImage = Icons.Default.Payments, label = "Payments Methods", onClick = {it.navigate(NavAddingLeftovers)}),
    ProfileItem(vectorImage = Icons.Default.Restaurant, label = "Add Restaurant", onClick = {it.navigate(NavListingRestaurant(entryPoint = "normalFlow"))}),
    ProfileItem(vectorImage = Icons.Default.HouseSiding, label = "Add Trusts", onClick = {/*TODO*/}),
    ProfileItem(vectorImage = Icons.Default.ShoppingCart, label = "Ordering and Pickup", onClick = {it.navigate(NavOrderingPickup)})
)


val moreList = listOf(
    ProfileItem(vectorImage = Icons.Default.Person, label = "Edit Personal Details", onClick = {navController -> navController.navigate(NavPersonalDetails) }),
    ProfileItem(vectorImage = Icons.Default.Star, label = "Rate Us", onClick = {/*TODO*/}),
    ProfileItem(
        vectorImage = Icons.Default.Feedback,
        label = "Send Feedback",
        onClick = {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@ProfileItem
            it.navigate("feedback/$userId")
        }
    ),
    ProfileItem(vectorImage = Icons.Default.Delete, label = "Delete Account", onClick = {/*TODO*/}),
    ProfileItem(vectorImage = Icons.AutoMirrored.Filled.Logout, label = "Log Out", onClick = {navController -> logoutUser(navController) }),
)


enum class TabItem(val label: String){
    MY_ORDERS(label ="My Orders"),
    MORE(label = "More")
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(paddingValues: PaddingValues, navController: NavController) {
    val viewModel: SignInDataViewModel = viewModel()
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


    val pagerState = rememberPagerState { TabItem.entries.size }


    val selectedTabItemIndex by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }
    val scope = rememberCoroutineScope()
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
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = scrollState.value) {
        sizeState = (140 - (60.0/scrollState.maxValue)*scrollState.value).toInt().dp
        if(sizeState > 110.dp)
            areButtonsVisible = true
        else
            areButtonsVisible = false
        Log.i("scroll", "scroll value: ${scrollState.value} && size = $sizeState && are")
    }


    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()


    ) {
        Column {
            Box {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    ProfilePictureView(size = animatedSize, profilePictureUrl = user?.profileUrl ?: "",
                    ){
                        navController.navigate(NavPersonalDetails)
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
            ProfileTabRow(selectedTabItemIndex = selectedTabItemIndex, pagerState = pagerState, scope = scope)
            HorizontalPager(state = pagerState) { index ->
                when(index){
                    TabItem.MY_ORDERS.ordinal -> TabItemContent(
                        scrollState = scrollState,
                        profileItemList = myOrderList,
                        navController = navController
                    )
                    TabItem.MORE.ordinal -> TabItemContent(
                        scrollState = scrollState,
                        profileItemList = moreList,
                        navController = navController
                    )
                }
            }


        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileTabRow(selectedTabItemIndex: Int, pagerState: PagerState, scope: CoroutineScope) {
    val modifier = Modifier.height(44.dp)
    TabRow(selectedTabIndex = selectedTabItemIndex) {
        TabItem.entries.forEach{ tabItem ->
            Tab(
                modifier = modifier,
                selected = selectedTabItemIndex == tabItem.ordinal,
                onClick = {scope.launch { pagerState.animateScrollToPage(tabItem.ordinal) }}) {
                val isSelected = selectedTabItemIndex == tabItem.ordinal
                Text(
                    text = tabItem.label,
                    textAlign = TextAlign.Center,
                    style = if (isSelected) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.bodySmall,
                    fontWeight = if (isSelected) FontWeight.Bold
                    else MaterialTheme.typography.titleSmall.fontWeight,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Composable
fun TabItemContent(scrollState: ScrollState, profileItemList: List<ProfileItem>, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        profileItemList.forEach{
            val (vectorImage, label, onClick) = it
            RowOfProfile(vectorImage = vectorImage, text = label, onClick = { onClick(navController) })
        }
        Spacer(modifier =Modifier.height(180.dp))
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
                .padding(horizontal = 16.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(imageVector = vectorImage, contentDescription = null)
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
    navController.navigate(NavSignInUI)
}



