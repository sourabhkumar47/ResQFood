package com.project.resqfood.presentation.communityScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.project.resqfood.R
import com.project.resqfood.presentation.communityScreen.FakeCommunityData.Companion.listOfHeadingPosts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object NavCommunityScreen

@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    navigateBackToHome: () -> Unit,
    navigateToPost: () -> Unit
) {
    val pagerState = rememberPagerState { CommunityTabItem.entries.size }

    val selectedTabItemIndex by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }
    val scope = rememberCoroutineScope()
    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            CommunityTopAppBar(navigateBackToHome)
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            CommunityTabRow(
                selectedTabIndex = selectedTabItemIndex,
                pagerState = pagerState,
                scope = scope
            )
            HorizontalPager(state = pagerState) { index ->
                when (index) {
                    // Changes to be made when using real data, replace list according to the category
                    CommunityTabItem.POPULAR.ordinal -> HeadingPosts(
                        list = listOfHeadingPosts,
                        navigateToPost = navigateToPost
                    )

                    CommunityTabItem.NEW.ordinal -> HeadingPosts(
                        list = listOfHeadingPosts,
                        navigateToPost = navigateToPost
                    )

                    CommunityTabItem.FOLLOWING.ordinal -> HeadingPosts(
                        list = listOfHeadingPosts,
                        navigateToPost = navigateToPost
                    )

                    CommunityTabItem.MY_THREAD.ordinal -> HeadingPosts(
                        list = listOfHeadingPosts,
                        navigateToPost = navigateToPost
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommunityTopAppBar(navigateBackToHome: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Community",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }, navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
                    .clickable {
                        navigateBackToHome()
                    },
                imageVector = Icons.Default.Home,
                contentDescription = "go to home screen"
            )
        })
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CommunityTabRow(
    selectedTabIndex: Int,
    pagerState: PagerState,
    scope: CoroutineScope
) {
    TabRow(
        selectedTabIndex = selectedTabIndex
    ) {
        CommunityTabItem.entries.forEach { tabItem ->
            Tab(
                modifier = Modifier.height(44.dp),
                selected = selectedTabIndex == tabItem.ordinal,
                onClick = { scope.launch { pagerState.animateScrollToPage(tabItem.ordinal) } }) {
                val isSelected = selectedTabIndex == tabItem.ordinal
                Text(
                    text = tabItem.label,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun HeadingPosts(
    modifier: Modifier = Modifier,
    list: List<HeadingPost>,
    navigateToPost: () -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = list,
            key = { item: HeadingPost -> item.id }) { headingPost ->
            HeadingPostContent(
                modifier = Modifier.fillMaxWidth(),
                post = headingPost,
                navigateToPost = navigateToPost
            )
        }
    }
}

@Composable
fun HeadingPostContent(
    modifier: Modifier = Modifier,
    post: HeadingPost,
    navigateToPost: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                navigateToPost()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(shape = CircleShape),
            model = post.authorImage,
            contentDescription = "profile image",
            error = painterResource(
                id = R.drawable.user
            ),
            placeholder = painterResource(id = R.drawable.user),
            contentScale = ContentScale.Fit
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = post.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = "${post.noOfReplies} ${replyOrReplies(post.noOfReplies)}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Posted by ${post.author} on ${post.date}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun replyOrReplies(numOfReplies: Int): String {
    return if (numOfReplies > 1) "Replies" else "Reply"
}

@Preview(showBackground = true)
@Composable
private fun PreviewHeadingPostContent() {
    HeadingPostContent(post = listOfHeadingPosts[0], navigateToPost = {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewCommunityScreen() {
    CommunityScreen(navigateBackToHome = {}, navigateToPost = {})
}