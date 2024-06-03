package com.project.resqfood.presentation.communityScreen

enum class CommunityTabItem(val label: String) {
    POPULAR("Popular"),
    NEW("New"),
    FOLLOWING("Following"),
    MY_THREAD("My Thread")
}

data class HeadingPost(
    val id: Int,
    val title: String,
    val noOfReplies: Int,
    val author: String,
    val authorImage: String,
    val date: String
)


class FakeCommunityData {
    companion object {
        val listOfHeadingPosts = listOf(
            HeadingPost(
                id = 1,
                title = "What's the best way to store leftover pasta?",
                noOfReplies = 82,
                author = "Sarah",
                authorImage = "https://randomuser.me/api/portraits/women/1.jpg",
                date = "May 15, 2022"
            ),
            HeadingPost(
                id = 2,
                title = "How do you make perfect scrambled eggs?",
                noOfReplies = 45,
                author = "John",
                authorImage = "https://randomuser.me/api/portraits/men/2.jpg",
                date = "June 1, 2022"
            ),
            HeadingPost(
                id = 3,
                title = "What are some healthy snack ideas?",
                noOfReplies = 67,
                author = "Emily",
                authorImage = "https://randomuser.me/api/portraits/women/3.jpg",
                date = "July 10, 2022"
            ),
            HeadingPost(
                id = 4,
                title = "How to bake the best chocolate cake?",
                noOfReplies = 101,
                author = "David",
                authorImage = "https://randomuser.me/api/portraits/men/4.jpg",
                date = "August 5, 2022"
            ),
            HeadingPost(
                id = 5,
                title = "How to bake the best strawberry cake?",
                noOfReplies = 79,
                author = "Micheal",
                authorImage = "https://randomuser.me/api/portraits/women/5.jpg",
                date = "August 1, 2022"
            ),
            HeadingPost(
                id = 6,
                title = "Tips for making homemade pizza dough?",
                noOfReplies = 54,
                author = "Emma",
                authorImage = "https://randomuser.me/api/portraits/men/6.jpg",
                date = "September 20, 2022"
            ),
            HeadingPost(
                id = 7,
                title = "What's the secret to a great lasagna?",
                noOfReplies = 89,
                author = "Michael",
                authorImage = "https://randomuser.me/api/portraits/women/7.jpg",
                date = "October 15, 2022"
            )
        )
    }
}