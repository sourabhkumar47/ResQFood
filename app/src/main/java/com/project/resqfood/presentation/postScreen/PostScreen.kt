package com.project.resqfood.presentation.postScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.resqfood.R

@Composable
fun PostScreen(post: Post) {
    Column(modifier= Modifier.fillMaxWidth()){
        Row{
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape),
                model = post.authorImage,
                contentDescription = "profile image",
                error = painterResource(
                    id = R.drawable.user
                ),
                placeholder = painterResource(id = R.drawable.user),
                contentScale = ContentScale.Fit
            )
            Column {
                Text(text = post.author, fontWeight = FontWeight.SemiBold)
                Text(text = post.date)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPostScreen() {
    PostScreen(post = FakePostData.posts[0])
}