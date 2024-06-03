package com.project.resqfood.presentation.postScreen

data class Reply(
    val author: String,
    val reply: String,
    val date: String
)


data class Post(
    val id: Int,
    val title: String,
    val replies: Array<Reply>,
    val author: String,
    val authorImage: String,
    val date: String,
    val content: String,
    val contentImage: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false
        if (title != other.title) return false
        if (!replies.contentEquals(other.replies)) return false
        if (author != other.author) return false
        if (authorImage != other.authorImage) return false
        if (date != other.date) return false
        if (content != other.content) return false
        if (contentImage != other.contentImage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + replies.contentHashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + authorImage.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + (contentImage?.hashCode() ?: 0)
        return result
    }
}

fun getChickenBiryaniRecipe(): String {
    return """
        **Chicken Biryani Recipe**

        **Ingredients:**
        - 2 cups Basmati rice
        - 1 lb (500g) chicken, cut into pieces
        - 1 large onion, thinly sliced
        - 2 tomatoes, chopped
        - 1 cup plain yogurt
        - 2 tablespoons ginger-garlic paste
        - 1/4 cup chopped cilantro (coriander leaves)
        - 1/4 cup chopped mint leaves
        - 4 green chilies, slit
        - 1 teaspoon red chili powder
        - 1 teaspoon turmeric powder
        - 2 teaspoons biryani masala
        - 1 teaspoon garam masala
        - 4 tablespoons ghee (clarified butter) or oil
        - 1/2 teaspoon saffron threads soaked in 2 tablespoons warm milk
        - Salt to taste
        - 4 cups water

        **Whole Spices:**
        - 4 cloves
        - 4 green cardamom pods
        - 2 bay leaves
        - 1 cinnamon stick
        - 1 teaspoon cumin seeds

        **Instructions:**

        1. **Rinse and Soak Rice:**
            - Rinse the Basmati rice under cold water until the water runs clear.
            - Soak the rice in water for about 30 minutes. Drain and set aside.

        2. **Marinate the Chicken:**
            - In a large bowl, combine chicken pieces, yogurt, ginger-garlic paste, red chili powder, turmeric powder, and salt.
            - Mix well and let it marinate for at least 30 minutes (or overnight in the refrigerator for better flavor).

        3. **Cook the Rice:**
            - In a large pot, bring 4 cups of water to a boil.
            - Add the soaked and drained rice to the boiling water along with a pinch of salt and a few whole spices (cumin seeds, cloves, cardamom pods, bay leaves, and cinnamon stick).
            - Cook the rice until it is 70-80% cooked (it should still have a bite to it).
            - Drain the rice and set aside.

        4. **Cook the Chicken:**
            - Heat ghee or oil in a large, heavy-bottomed pan over medium heat.
            - Add the sliced onions and sauté until they turn golden brown.
            - Add the chopped tomatoes, green chilies, biryani masala, and garam masala. Cook until the tomatoes turn soft and oil starts to separate from the mixture.
            - Add the marinated chicken to the pan and cook until the chicken is browned and cooked through.
            - Stir in the chopped cilantro and mint leaves.

        5. **Layering the Biryani:**
            - In a large pot or a Dutch oven, layer half of the cooked rice at the bottom.
            - Add the cooked chicken mixture over the rice layer.
            - Top with the remaining rice, spreading it evenly.
            - Drizzle the saffron-infused milk over the top layer of rice.

        6. **Dum Cooking:**
            - Cover the pot with a tight-fitting lid. If the lid is not tight enough, seal the edges with dough to ensure no steam escapes.
            - Cook on low heat for about 20-25 minutes to allow the flavors to meld together.

        7. **Serve:**
            - Gently fluff up the biryani with a fork.
            - Serve hot with raita (yogurt sauce) or a side salad.

        Enjoy your delicious homemade Chicken Biryani!
    """.trimIndent()
}

class FakePostData {
    companion object {
        val post = Post(
            id = 1,
            title = "How to make delicious Chicken Biryani",
            replies = arrayOf(
                Reply(
                    author = "Author8",
                    reply = "This is a reply to the fifth post.",
                    date = "May 16, 2024"
                ),
                Reply(
                    author = "Author9",
                    reply = "This is another reply to the fifth post.",
                    date = "May 17, 2024"
                )
            ),
            author = "Jenny Walker",
            authorImage = "https://randomuser.me/api/portraits/women/1.jpg",
            date = "May 15, 2024",
            content = getChickenBiryaniRecipe(),
            contentImage = "https://foodish-api.com/images/biryani/biryani32.jpg"
        )
    }
}