package com.example.jetcompose.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.WindowCompat
import coil.compose.rememberAsyncImagePainter
import com.example.jetcompose.R
import com.example.jetcompose.subscription.Subscription
import com.example.jetcompose.theme.*
import com.example.jetcompose.utils.fontFamilyPR
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@ExperimentalMaterialApi
@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val favGenres = listOf("Action", "Thriller", "Drama", "Anime")
    private val genreColors = listOf(genreColor1, genreColor2, genreColor3, genreColor4)
    private val imageUrls =
        listOf(
            "https://terrigen-cdn-dev.marvel.com/content/prod/1x/stellarvortex_reald_digital_poster_1080x1350_v1.jpg",
            "https://stat1.bollywoodhungama.in/wp-content/uploads/2022/06/Rocketry-The-Nambi-Effect.jpg",
            "https://static.independent.co.uk/2021/03/24/16/1505763709-tomb-raider.jpg?quality=75&width=640&auto=webp",
            "https://m.media-amazon.com/images/M/MV5BMTU2NjA1ODgzMF5BMl5BanBnXkFtZTgwMTM2MTI4MjE@._V1_.jpg"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(ComposeView(this).apply {
            setContent {
                ProfileImage()
            }
        })
    }

    @Preview(showBackground = true)
    @Composable
    fun ProfileImage() {
        val animateGenres = remember { Animatable(100f) }
        val animateWatched = remember { Animatable(100f) }
        LaunchedEffect(key1 = animateGenres) {
            animateGenres.animateTo(
                0f,
                spring(dampingRatio = DampingRatioLowBouncy, stiffness = StiffnessLow)
            )
        }
        LaunchedEffect(key1 = animateGenres) {
            delay(50)
            animateWatched.animateTo(
                0f,
                spring(dampingRatio = DampingRatioLowBouncy, stiffness = StiffnessLow)
            )
        }
        BoxWithConstraints(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ConstraintLayout {
                val (background, button, profile, name, genres, recently) = createRefs()
                Image(
                    painter = painterResource(id = R.drawable.marvel),
                    contentDescription = "Profile Background",
                    modifier = Modifier
                        .fillMaxWidth()
                        .blur(3.dp)
                        .padding(20.dp)
                        .height(150.dp)
                        .constrainAs(background) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier
                    .constrainAs(profile) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(background.bottom, (-50).dp)
                    }
                    .fillMaxWidth(), contentAlignment = Alignment.Center) {

                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.compose),
                            contentDescription = "Profile Background",
                            Modifier.size(100.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(20.dp)
                        .constrainAs(name) {
                            top.linkTo(profile.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    text = "Vikas Singh",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontFamily = fontFamilyPR
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .constrainAs(button) {
                            top.linkTo(name.bottom)
                        },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier.padding(10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorLightBlue)
                        ) {
                            Text(text = "Edit Profile", color = Color(0Xff081119))
                        }
                        Button(
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@ProfileActivity,
                                        Subscription::class.java
                                    )
                                )
                            },
                            modifier = Modifier.padding(10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorLightBlue)
                        ) {
                            Text(text = "Subscribe", color = Color(0Xff081119))
                        }
                    }
                }
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .constrainAs(genres) {
                            top.linkTo(button.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .graphicsLayer {
                            translationY = animateGenres.value
                        },
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = Color(0xFF292125),
                    elevation = 5.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Row(modifier = Modifier.padding(top = 20.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.genres),
                                contentDescription = "Genre",
                            )
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = "Favourite genres",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontFamily = fontFamilyPR,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 15.dp)
                        ) {
                            itemsIndexed(favGenres) { index, item ->
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    backgroundColor = genreColors[index],
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            top = 7.dp,
                                            bottom = 7.dp,
                                            end = 10.dp,
                                            start = 10.dp
                                        ),
                                        text = favGenres[index],
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        color = colorAppBackground,
                                        fontFamily = fontFamilyPR,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .constrainAs(recently) {
                            top.linkTo(genres.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .graphicsLayer {
                            translationY = animateWatched.value
                        },
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = Color(0xFF202726),
                    elevation = 5.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Row(modifier = Modifier.padding(top = 20.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.genres),
                                contentDescription = "Genre",
                            )
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = "Recently watched",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontFamily = fontFamilyPR,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 15.dp)
                        ) {
                            items(imageUrls) { item ->
                                Image(
                                    modifier = Modifier
                                        .size(100.dp, 150.dp)
                                        .clip(
                                            RoundedCornerShape(15.dp)
                                        ),
                                    painter = rememberAsyncImagePainter(model = item),
                                    contentDescription = "Recently watched",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

