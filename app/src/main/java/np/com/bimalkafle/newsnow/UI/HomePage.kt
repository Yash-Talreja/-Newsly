import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import androidx.navigation.NavHostController
import com.kwabenaberko.newsapilib.models.Article
import np.com.bimalkafle.newsnow.UI.NewsArticleScreen
import np.com.bimalkafle.newsnow.ViewModel.NewsViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(newsViewModel: NewsViewModel, navController: NavHostController) {

    val articles by newsViewModel.articles.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 🔍 Search + Categories
        CategoriesBar(newsViewModel)

        Spacer(modifier = Modifier.height(8.dp))

        // 📰 Horizontal Pager (Top news)
        if (articles.isNotEmpty()) {
            val pagerState = rememberPagerState()

            HorizontalPager(
                count = minOf(articles.size, 5),
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 12.dp)
            ) { page ->
                FeaturedNewsCard(article = articles[page], navController)
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp),
                activeColor = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔽 Vertical list of news
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            items(articles) { article ->
                ArticleListItem(article, navController)
            }
        }
    }
}

@Composable
fun CategoriesBar(newsViewModel: NewsViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    val categoriesList = listOf(
        "GENERAL", "BUSINESS", "ENTERTAINMENT",
        "HEALTH", "SCIENCE", "SPORTS", "TECHNOLOGY"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (isSearchExpanded) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search News" , fontSize = 15.sp) },
                trailingIcon = {
                    IconButton(onClick = {
                        isSearchExpanded = false
                        if (searchQuery.isNotEmpty()) {
                            newsViewModel.fetchEverythingWithQuery(searchQuery)
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(60.dp)
                    .width(250.dp),
                shape = CircleShape
            )
        } else {
            IconButton(onClick = { isSearchExpanded = true }) {
                Icon(Icons.Default.Search, contentDescription = "Open search")
            }
        }

        categoriesList.forEach { category ->
            Button(
                onClick = { newsViewModel.fetchNewsTopHeadlines(category) },
                colors = ButtonDefaults.buttonColors(Color.Blue),
                shape = CircleShape,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(36.dp)
            ) {
                Text(text = category, fontSize = 12.sp)
            }

        }
    }
}

@Composable
fun FeaturedNewsCard(article: Article, navController: NavHostController) {
    Card(
        onClick = { navController.navigate(NewsArticleScreen(article.url)) },
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Box {
            AsyncImage(
                model = article.urlToImage ?: "https://via.placeholder.com/400x200",
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = article.title ?: "No Title Available",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.source?.name ?: "Unknown Source",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ArticleListItem(article: Article, navController: NavHostController) {
    Card(
        onClick = { navController.navigate(NewsArticleScreen(article.url)) },
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = article.urlToImage ?: "https://via.placeholder.com/150",
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title ?: "No Title Available",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.source?.name ?: "Unknown",
                    color = Color.Blue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
