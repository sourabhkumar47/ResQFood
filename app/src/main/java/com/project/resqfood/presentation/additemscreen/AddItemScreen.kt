package com.project.resqfood.presentation.additemscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.resqfood.R
import com.project.resqfood.presentation.Destinations

@Composable
fun AddItemScreen(navController: NavController){
    var itemName by rememberSaveable { mutableStateOf("") }
    var itemDescription by rememberSaveable { mutableStateOf("") }
    var itemQuantity by rememberSaveable { mutableStateOf("") }
    var itemEstimatedValue by rememberSaveable { mutableStateOf("") }
   Surface(
       modifier = Modifier
           .fillMaxSize()
           .padding(top = 20.dp),
       color = MaterialTheme.colorScheme.background
       ) {
       Column(
           modifier = Modifier
               .padding(16.dp),
           verticalArrangement = Arrangement.spacedBy(16.dp)
       ) {
           Row (
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ){
               Text(
                   text = stringResource(id = R.string.add_item),
                   fontWeight = FontWeight.Bold,
                   style = MaterialTheme.typography.displaySmall,
                   modifier = Modifier.padding(top = 8.dp)
               )
               Spacer(modifier = Modifier.width(85.dp))
               IconButton(
                   onClick = {
                       navController.navigate(Destinations.MainScreen.route)
                   },
                   modifier = Modifier
                       .size(100.dp)
               ) {
                   Icon(
                       imageVector = Icons.Rounded.Close,
                       contentDescription = "Close this window"
                   )
               }
           }

//             Spacer(modifier = Modifier.padding(8.dp))

           Text(
               text = stringResource(id = R.string.item_name),
               style = MaterialTheme.typography.bodyLarge,
               modifier = Modifier.padding(bottom = 2.dp)
           )

           TextField(
               modifier = Modifier.fillMaxWidth(),
               value = itemName,
               onValueChange = {newValue ->
                   if(newValue.all { it.isLetter() || it.isWhitespace() }){
                       itemName = newValue
                   } },
               placeholder = { Text(text= "e.g. Paneer curry")},
               isError = itemName.isEmpty()
           )

//           Spacer(modifier = Modifier.padding(2.dp))

           Text(
               text = stringResource(id = R.string.item_description),
               style = MaterialTheme.typography.bodyLarge
           )
           TextField(
               modifier = Modifier.fillMaxWidth(),
               value = itemDescription,
               onValueChange =  {newValue ->
                   if(newValue.all { it.isLetter() || it.isWhitespace() }){
                       itemDescription = newValue
                   } },
               maxLines = 5, // Set the desired number of lines
               isError = itemDescription.isEmpty()
           )

//           Spacer(modifier = Modifier.padding(2.dp))

           Text(
               text = stringResource(id = R.string.item_quantity),
               style = MaterialTheme.typography.bodyLarge
           )

           TextField(
               modifier = Modifier.fillMaxWidth(),
               value = itemQuantity,
               onValueChange =  {newValue ->
                   if(newValue.all { it.isDigit()}){
                       itemQuantity = newValue
                   } },
               placeholder = { Text(text= "e.g. 3")},
               isError = itemQuantity.isEmpty()
           )

//           Spacer(modifier = Modifier.padding(2.dp))

           Text(
               text = stringResource(id = R.string.item_estimated_value),
               style = MaterialTheme.typography.bodyLarge
           )

           TextField(
               modifier = Modifier.fillMaxWidth(),
               value = itemEstimatedValue,
               onValueChange =  {newValue ->
                   if(newValue.all { it.isDigit()}){
                       itemEstimatedValue = newValue
                   } },
               placeholder = { Text(text= "e.g. $ 5.0")},
               isError = itemEstimatedValue.isEmpty()
           )

//           Spacer(modifier = Modifier.padding(2.dp))

           Button(
               modifier = Modifier
                   .fillMaxWidth()
                   .height(56.dp)
                   .align(Alignment.CenterHorizontally),
               onClick = {
                   navController.navigate(Destinations.SetPriceScreen.route)
               }
           ) {
               Text(
                   text = stringResource(id = R.string.save_item),
                   style = MaterialTheme.typography.bodyLarge
               )
           }
       }
   }
}

