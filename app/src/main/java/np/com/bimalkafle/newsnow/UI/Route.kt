package np.com.bimalkafle.newsnow.UI

import kotlinx.serialization.Serializable


@Serializable
object HomePageScreen


@Serializable
data class NewsArticleScreen(
    val url : String
)