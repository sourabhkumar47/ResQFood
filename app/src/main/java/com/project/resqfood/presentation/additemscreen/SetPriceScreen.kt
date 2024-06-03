package com.project.resqfood.presentation.additemscreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.resqfood.R
import com.project.resqfood.presentation.Destinations

@Composable
fun SetPriceScreen(navController: NavController){
    var itemPrice by rememberSaveable { mutableStateOf("") }
    var isChecked = mutableStateOf(false)
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {
                        navController.navigate(Destinations.AddItemScreen.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Return to previous page"
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
//                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.set_price),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall
                )
            }


            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = stringResource(id = R.string.categorize_your_leftover),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            CategorizeLeftover()

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = stringResource(id = R.string.price),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = itemPrice,
                onValueChange =  {newValue ->
                    if(newValue.all { it.isDigit() }){
                        itemPrice = newValue
                    } },
                placeholder = { Text(text = "e.g. 5")}
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.padding(8.dp),
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "For Free")
            }



            Spacer(modifier = Modifier.padding(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /*TODO*/ }) {
                    Text(text = "Save for later")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    navController.navigate(Destinations.PickTimeScreen.route)
                }) {
                    Text(text = "List my item")
                }
            }
        }
    }
}

@Composable
fun CategorizeLeftover(){
    val selectedValue = remember { mutableStateOf("") }
    val isSelectedItem: (String) -> Boolean = { selectedValue.value == it}
    val onChangeState: (String) -> Unit = { selectedValue.value = it}
    val items = listOf("Half Portion", "Full Portion", "Family Portion")
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Selected value: ${selectedValue.value.ifEmpty { "None" }}")
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .selectable(
                        selected = isSelectedItem(item),
                        onClick = { onChangeState(item) },
                        role = Role.RadioButton
                    )
                    .padding(
                        8.dp
                    )
            ) {
                RadioButton(
                    selected = isSelectedItem(item),
                    onClick = null
                )
                Text(
                    text = item,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

