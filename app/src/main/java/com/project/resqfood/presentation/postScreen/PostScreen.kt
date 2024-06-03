package com.project.resqfood.presentation.postScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.resqfood.R
import kotlinx.serialization.Serializable


@Serializable
object NavPostScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(navigateBackToCommunity: () -> Unit) {
    val post = FakePostData.post
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Post",
                    style = MaterialTheme.typography.titleLarge
                )
            }, navigationIcon = {
                IconButton(onClick = navigateBackToCommunity) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PostHeader(post = post)
            if (post.contentImage != null) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    model = post.contentImage,
                    contentDescription = "content image",
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge
            )
            ExpandableText(modifier = Modifier.fillMaxWidth(), content = post.content)
            PostDivider()
            PostReaction(
                modifier = Modifier
                    .fillMaxWidth()
            )
            PostDivider()
            PostReply(replies = post.replies)
        }
    }
}

@Composable
fun PostHeader(post: Post) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(56.dp)
                .clip(shape = CircleShape),
            model = post.authorImage,
            contentDescription = "profile image",
            error = painterResource(
                id = R.drawable.user
            ),
            placeholder = painterResource(id = R.drawable.user),
            contentScale = ContentScale.Fit
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = post.author,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = post.date, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PostDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier
            .height(1.dp)
            .fillMaxWidth()
    )
}

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    content: String,
    maxLines: Int = 10
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    ) {
        Text(
            text = content,
            maxLines = if (expanded) Int.MAX_VALUE else maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .fillMaxWidth()
        )
        if (!expanded) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                textAlign = TextAlign.Right,
                text = "Read more",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PostReaction(modifier: Modifier = Modifier) {
    var isPostLiked by remember {
        mutableStateOf(false)
    }
    var comment by remember {
        mutableStateOf("")
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = { isPostLiked = !isPostLiked }) {
            Icon(
                imageVector = if (isPostLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                contentDescription = "like button"
            )
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(), value = comment, onValueChange = { text ->
                comment = text
            },
            label = {
                Text("Comment")
            },
            trailingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "post the comment"
                    )
                }
            },
            shape = RectangleShape
        )
    }
}

@Composable
fun PostReply(modifier: Modifier = Modifier, replies: Array<Reply>) {
    Column {
        replies.forEach { reply ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = reply.author,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = reply.reply)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewPostScreen() {
    PostScreen {}
}