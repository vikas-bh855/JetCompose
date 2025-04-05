package com.example.jetcompose.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.jetcompose.R
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.models.DiscoverResultsParameterProvider
import com.example.jetcompose.theme.colorGreenDark
import com.example.jetcompose.theme.colorOffWhite
import com.example.jetcompose.theme.colorOffWhiteDark
import com.example.jetcompose.theme.colorWhite
import com.example.jetcompose.utils.Loader
import com.example.jetcompose.utils.fontFamilyPR
import com.example.jetcompose.utils.formattedDate
import com.example.jetcompose.utils.srcImagePath
import com.google.android.youtube.player.YouTubeStandalonePlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                detailViewModel.getMovieCredits(args.discoverResults.id.toString())
                detailViewModel.getMovieDetails(args.discoverResults.id.toString())
                Box {
                    Loader(detailViewModel.movieDetails.value == null)
                    detailViewModel.movieDetails.value?.let {
                        DetailPage(it)
                        PlayVideo()
                    }
                }
            }
        }
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun DetailPage(@PreviewParameter(DiscoverResultsParameterProvider::class) discoverResults: DiscoverResults) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(enabled = true, state = ScrollState(0))
        ) {
            TopLayout(discoverResults)
            Description(discoverResults)
            ShowCredits()
            ShowRecommendations()
        }
    }

    @OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun TopLayout(
        @PreviewParameter(DiscoverResultsParameterProvider::class) discoverResults: DiscoverResults
    ) {
        val floatAnimatable = remember { Animatable(30f) }
        val alphaAnimatable = remember { Animatable(0f) }
        val scaleAnimatable = remember { Animatable(1.5f) }
        val fadeAnimatable = remember { Animatable(0f) }
        LaunchedEffect(discoverResults.poster_path) {
            launch { floatAnimatable.animateTo(0f, tween(800)) }
            launch { alphaAnimatable.animateTo(1f, tween(800)) }
            launch { scaleAnimatable.animateTo(1f, tween(800)) }
            launch { fadeAnimatable.animateTo(1f, tween(500)) }
        }
        val value = (360 * discoverResults.vote_average.toFloat()) / 10f
        val lengthAnimatable = remember {
            Animatable(0f)
        }
        LaunchedEffect(discoverResults.vote_average) {
            lengthAnimatable.animateTo(
                value, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Canvas(modifier = Modifier.size(40.dp, 40.dp), onDraw = {
                        drawArc(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    colorGreenDark, colorGreenDark
                                )
                            ),
                            startAngle = -90f,
                            sweepAngle = lengthAnimatable.value,
                            useCenter = false,
                            style = Stroke(width = 8f),
                        )
                    })
                    Text(
                        text = discoverResults.vote_average.toFloat().toString(),
                        color = colorOffWhite,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyPR,
                    )
                }
                Row(Modifier.padding(top = 20.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "",
                        Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = discoverResults.release_date.formattedDate,
                        color = colorOffWhite,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyPR,
                    )
                }
                Row(Modifier.padding(top = 20.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.language),
                        contentDescription = "",
                        Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = Locale(discoverResults.original_language).displayLanguage,
                        color = colorOffWhite,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyPR,
                    )
                }
                if (!discoverResults.genres.isNullOrEmpty()) Text(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color(0xFF101010))
                        .padding(5.dp),
                    text = discoverResults.genres[1].name,
                    color = colorOffWhiteDark,
                    fontFamily = fontFamilyPR,
                )
                val image = AnimatedImageVector.animatedVectorResource(R.drawable.avd_anim)
                var atEnd by remember { mutableStateOf(false) }
                Icon(
                    modifier = Modifier
                        .clickable {
                            atEnd = !atEnd
                        }
                        .size(30.dp),
                    painter = rememberAnimatedVectorPainter(image, atEnd),
                    contentDescription = "Add", // decorative element,
                    tint = {
                        Color(0xFF758486)
                    }
                )
                Image(
                    modifier = Modifier
                        .clickable {
                            detailViewModel.getVideoUrl(discoverResults.id.toString())
                        }
                        .graphicsLayer {
                            scaleY = scaleAnimatable.value
                            scaleX = scaleAnimatable.value
                        },
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play"
                )
            }
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = discoverResults.poster_path.srcImagePath)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                        }).build()
                ),
                contentDescription = "Detail Image",
                modifier = Modifier
                    .width(width = 250.dp)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp
                        )
                    )
                    .weight(1.5f)
                    .graphicsLayer {
                        alpha = alphaAnimatable.value
                        translationX = floatAnimatable.value
                    },
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopEnd
            )
        }
    }

    @Composable
    fun PlayVideo() {
        val videoUrl = detailViewModel.videoUrl.value
        if (videoUrl.isNotBlank()) {
            val intent = YouTubeStandalonePlayer.createVideoIntent(
                requireActivity(), "1234567890", videoUrl, 0, true, false
            )
            startActivity(intent)
        }
    }

    @Composable
    fun Description(discoverResults: DiscoverResults) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Description",
            fontFamily = fontFamilyPR,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = colorWhite
        )
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            text = discoverResults.overview,
            fontFamily = fontFamilyPR,
            fontSize = 10.sp,
            color = colorOffWhite,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun ShowCredits() {
        val alphaAnimation = remember { Animatable(0f) }
        LaunchedEffect(alphaAnimation) {
            alphaAnimation.animateTo(1f, tween(durationMillis = 1000))
        }
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Credits",
            fontFamily = fontFamilyPR,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = colorWhite
        )
        val listCrew = detailViewModel.listMovieCrew.collectAsState()
        LazyRow(
            content = {
                itemsIndexed(listCrew.value) { _, item ->
                    Column(Modifier.padding(start = 10.dp, end = 10.dp)) {
                        Image(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = item.profile_path?.srcImagePath)
                                    .apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                    }).build()
                            ),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .align(Alignment.CenterHorizontally),
                            text = item.name,
                            color = colorOffWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            })

    }

    @Composable
    fun ShowRecommendations() {
        detailViewModel.getRecommendations(args.discoverResults.id.toString())
        val recommendations = detailViewModel.listRecommendations.collectAsState().value
        Column {
            if (recommendations.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Recommendations",
                    fontFamily = fontFamilyPR,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorWhite
                )
                LazyRow() {
                    items(recommendations) { results ->
                        AsyncImage(
                            model = results.poster_path.srcImagePath,
                            contentDescription = "Recommendations",
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp)
                                .size(120.dp, 180.dp)
                        )
                    }
                }
            }
        }
    }
}




