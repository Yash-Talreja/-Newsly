package np.com.bimalkafle.newsnow

import HomePage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.delay
import np.com.bimalkafle.newsnow.UI.HomePageScreen
import np.com.bimalkafle.newsnow.UI.NewsArticlePage
import np.com.bimalkafle.newsnow.UI.NewsArticleScreen
import np.com.bimalkafle.newsnow.UI.SplashScreen
import np.com.bimalkafle.newsnow.ViewModel.NewsViewModel


class   MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        setContent {

            val navController = rememberNavController()

            var showSplash by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                delay(2000) // 2-second splash delay
                showSplash = false
            }


            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                if (showSplash) {
                    SplashScreen()
                } else {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "NEWS NOW",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = Color.Red,
                            fontSize = 25.sp,
                            fontFamily = FontFamily.Serif
                        )
                        NavHost(navController = navController, startDestination = HomePageScreen) {
                            composable<HomePageScreen> {
                                HomePage(newsViewModel, navController)
                            }

                            composable<NewsArticleScreen> {
                                val args = it.toRoute<NewsArticleScreen>()
                                NewsArticlePage(args.url)
                            }
                        }

                    }
                }
            }
        }
    }

}