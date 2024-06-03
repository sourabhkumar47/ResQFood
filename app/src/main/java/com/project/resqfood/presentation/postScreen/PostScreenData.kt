package com.project.resqfood.presentation.postScreen

import kotlin.random.Random

fun getRandomImageUrl(): String {
    val randomId = Random.nextInt(1000, 9999)
    return "https://example.com/images/$randomId.jpg"
}

fun getLongContent(): String {
    return """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
        Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure 
        dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non 
        proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

        Integer nec odio. Praesent libero. Sed cursus ante dapibus diam. Sed nisi. Nulla quis sem at nibh elementum imperdiet. 
        Duis sagittis ipsum. Praesent mauris. Fusce nec tellus sed augue semper porta. Mauris massa. Vestibulum lacinia arcu 
        eget nulla. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.

        Curabitur sodales ligula in libero. Sed dignissim lacinia nunc. Curabitur tortor. Pellentesque nibh. Aenean quam. 
        In scelerisque sem at dolor. Maecenas mattis. Sed convallis tristique sem. Proin ut ligula vel nunc egestas porttitor. 
        Morbi lectus risus, iaculis vel, suscipit quis, luctus non, massa. Fusce ac turpis quis ligula lacinia aliquet. 
        Mauris ipsum. Nulla metus metus, ullamcorper vel, tincidunt sed, euismod in, nibh. Quisque volutpat condimentum velit. 
        Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.
    """.trimIndent()
}

class FakePostData{
    companion object{
        val posts = listOf(
            Post(
                id = 1,
                title = "First Post",
                replies = arrayOf("Reply 1", "Reply 2"),
                author = "Author1",
                authorImage = getRandomImageUrl(),
                date = "May 15, 2024",
                content = getLongContent(),
                contentImage = getRandomImageUrl()
            ),
            Post(
                id = 2,
                title = "Second Post",
                replies = arrayOf("Reply 1", "Reply 2", "Reply 3"),
                author = "Author2",
                authorImage = getRandomImageUrl(),
                date = "May 15, 2024",
                content = getLongContent(),
                contentImage = getRandomImageUrl()
            ),
            Post(
                id = 3,
                title = "Third Post",
                replies = arrayOf(),
                author = "Author3",
                authorImage = getRandomImageUrl(),
                date = "May 15, 2024",
                content = getLongContent(),
                contentImage = getRandomImageUrl()
            ),
            Post(
                id = 4,
                title = "Fourth Post",
                replies = arrayOf("Reply 1"),
                author = "Author4",
                authorImage = getRandomImageUrl(),
                date = "May 15, 2024",
                content = getLongContent(),
                contentImage = getRandomImageUrl()
            ),
            Post(
                id = 5,
                title = "Fifth Post",
                replies = arrayOf("Reply 1", "Reply 2", "Reply 3", "Reply 4"),
                author = "Author5",
                authorImage = getRandomImageUrl(),
                date = "May 15, 2024",
                content = getLongContent(),
                contentImage = getRandomImageUrl()
            )
        )
    }
}