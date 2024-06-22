import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.resqfood.R
import com.project.resqfood.presentation.itemdetailscreen.SuccessScreen
import com.project.resqfood.presentation.login.Screens.NavMainScreen
import kotlinx.serialization.Serializable


@Serializable
object NavOrderingPickup


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderingPickup(navController: NavHostController) {
    val leftOverFoodItemsList = listOf(
        FoodItem(
            itemName = "Rice and Curry",
            itemDescription = "Leftover rice and curry from lunch.",
            pickupTime = "Pickup window: 3 PM - 5 PM",
            price = "₹10"
        )
    )


    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Adding Leftovers") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
            )
        )
    }){ paddingValues->
        Surface(color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Item details
                ItemDetails(
                    onClaimClicked = {
                        navController.navigate(SuccessScreen)
                    },
                    leftOverFoodItemsList.firstOrNull(),
                )


                Spacer(modifier = Modifier.height(16.dp))


                // Order confirmation details
                OrderConfirmation(
                    confirmationMessage = "Your order has been confirmed.",
                    restaurantAddress = "123 Main St, Cityville",
                    directions = "Follow the directions to reach the restaurant."
                )
            }


        }
    }
}


@Composable
fun ItemDetails(
    onClaimClicked: () -> Unit,
    leftoverFoodItem: FoodItem? = null,
    isLoading: Boolean = false // Optional flag for showing progress indicator
) {
    Column(
        modifier = Modifier.fillMaxSize()


    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .fillMaxSize()
                        .clip(RectangleShape),
                    painter = painterResource(id = R.drawable.fooditem),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }
        }




        Spacer(modifier = Modifier.height(16.dp))


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .background(Color.White)
                .weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        ) {
            Column() {
                DetailsLayout(title = "Item", value = leftoverFoodItem?.itemName ?: "Non")
                DetailsLayout(title = "Details", value = leftoverFoodItem?.itemDescription ?: "Non")
                DetailsLayout(title = "Pickup Time", value = leftoverFoodItem?.pickupTime ?: "Non")
                DetailsLayout(title = "Price", value = leftoverFoodItem?.price ?: "Non")
            }
        }


        Button(
            onClick = onClaimClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(4.dp))
            } else {
                Text(text = "Claim")
            }
        }
    }
}


@Composable
fun DetailsLayout(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 4.dp),
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                .padding(12.dp)
        ) {
            Text(
                text = value,
            )
        }
    }
}




@Composable
fun OrderConfirmation(
    confirmationMessage: String,
    restaurantAddress: String,
    directions: String
) {
    Text(text = confirmationMessage)
    Text(text = restaurantAddress)
    Text(text = directions)
}


data class FoodItem(
    val itemName: String,
    val itemDescription: String,
    val pickupTime: String,
    val price: String
)



