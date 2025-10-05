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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.jetcompose.R
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.models.DiscoverResultsParameterProvider
import com.example.jetcompose.models.MovieCrew
import com.example.jetcompose.models.discoverList
import com.example.jetcompose.models.movieCrew
import com.example.jetcompose.theme.colorBlue
import com.example.jetcompose.theme.colorOffWhite
import com.example.jetcompose.theme.colorWhite
import com.example.jetcompose.theme.genreColor4
import com.example.jetcompose.utils.DetailTitleText
import com.example.jetcompose.utils.ItemCrew
import com.example.jetcompose.utils.ItemImage
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
        detailViewModel.getMovieCredits(args.discoverResults.id.toString())
        detailViewModel.getMovieDetails(args.discoverResults.id.toString())
        detailViewModel.getRecommendations(args.discoverResults.id.toString())
        return ComposeView(requireContext()).apply {
            setContent {
                Box {
                    val movieDetails = detailViewModel.movieDetails.collectAsState().value
                    Loader(movieDetails == null)
                    movieDetails?.let {
                        val listCrew = detailViewModel.listMovieCrew.collectAsState().value
                        val recommendations =
                            detailViewModel.listRecommendations.collectAsState().value
                        DetailPage(it, listCrew, recommendations)
                        PlayVideo(detailViewModel.videoUrl.value)
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun DetailPage(
        @PreviewParameter(
            DiscoverResultsParameterProvider::class
        ) discoverResults: DiscoverResults,
        listCrew: List<MovieCrew> = movieCrew,
        movieRecommendation: List<DiscoverResults> = discoverList
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(enabled = true, state = ScrollState(0))
        ) {
            TopLayout(discoverResults)
            BottomLayout(discoverResults, listCrew, movieRecommendation)
        }
    }


    @OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun TopLayout(
        @PreviewParameter(DiscoverResultsParameterProvider::class) discoverResults: DiscoverResults
    ) {
        val floatAnimatable = remember { Animatable(30f) }
        val alphaAnimatable = remember { Animatable(0f) }
        LaunchedEffect(discoverResults.poster_path) {
            launch { floatAnimatable.animateTo(0f, tween(800)) }
            launch { alphaAnimatable.animateTo(1f, tween(800)) }
        }
        val value = (360 * discoverResults.vote_average.toFloat()) / 10f
        val lengthAnimatable = remember {
            Animatable(0f)
        }
        LaunchedEffect(discoverResults.vote_average) {
            lengthAnimatable.animateTo(
                value, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
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
                            color = colorBlue,
                            startAngle = -90f,
                            sweepAngle = lengthAnimatable.value,
                            useCenter = false,
                            style = Stroke(width = 8f, cap = StrokeCap.Round),
                        )
                    })
                    Text(
                        text = String.format("%.1f", discoverResults.vote_average),
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
                        modifier = Modifier.padding(start = 5.dp),
                        text = discoverResults.release_date.formattedDate,
                        color = colorOffWhite,
                        fontWeight = FontWeight.SemiBold,
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
                        modifier = Modifier.padding(start = 5.dp),
                        text = Locale(discoverResults.original_language).displayLanguage,
                        color = colorOffWhite,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyPR,
                    )
                }

                Row(Modifier.padding(top = 20.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.watch_time),
                        contentDescription = "",
                        Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = "${discoverResults.runtime / 60} h ${discoverResults.runtime % 60} m",
                        color = colorOffWhite,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyPR,
                    )
                }
                if (!discoverResults.genres.isNullOrEmpty()) Text(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(5.dp),
                    text = discoverResults.genres[1].name,
                    color = colorOffWhite,
                    fontFamily = fontFamilyPR,
                    fontWeight = FontWeight.SemiBold,
                )
                val image = AnimatedImageVector.animatedVectorResource(R.drawable.avd_anim)
                var atEnd by remember { mutableStateOf(false) }
                Icon(
                    modifier = Modifier
                        .clickable {
                            atEnd = !atEnd
                            detailViewModel.addToWatchlist(discoverResults.id.toString())
                        }
                        .padding(top = 20.dp),
                    painter = rememberAnimatedVectorPainter(image, atEnd),
                    contentDescription = "Add", // decorative element,
                    tint = {
                        colorBlue
                    })
                Image(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clickable {
                            detailViewModel.getVideoUrl(discoverResults.id.toString())
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
    fun BottomLayout(
        discoverResults: DiscoverResults,
        listCrew: List<MovieCrew>,
        recommendations: List<DiscoverResults>
    ) {
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Spacer(modifier = Modifier.padding(top = 15.dp))
            Description(discoverResults)
            Spacer(modifier = Modifier.padding(top = 15.dp))
            ShowCredits(listCrew)
            Spacer(modifier = Modifier.padding(top = 15.dp))
            ShowRecommendations(recommendations)
        }
    }

    @Composable
    fun PlayVideo(videoUrl: String) {
        if (videoUrl.isNotBlank()) {
            val intent = YouTubeStandalonePlayer.createVideoIntent(
                requireActivity(), "1234567890", videoUrl, 0, true, false
            )
            startActivity(intent)
        }
    }

    @Composable
    fun Description(discoverResults: DiscoverResults) {
        DetailTitleText(text = "Description")
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = discoverResults.overview,
            fontFamily = fontFamilyPR,
            fontSize = 11.sp,
            color = colorOffWhite,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun ShowCredits(listCrew: List<MovieCrew>) {
        DetailTitleText(text = "Credits")
        LazyRow(
            modifier = Modifier.padding(top = 5.dp), content = {
                itemsIndexed(listCrew) { _, item ->
                    Column(
                        Modifier.padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ItemCrew(item)
                        Text(
                            text = item.name,
                            color = colorOffWhite,
                            textAlign = TextAlign.Center,
                            fontFamily = fontFamilyPR,
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp,
                        )
                    }
                }
            })
    }

    @Composable
    fun ShowRecommendations(recommendations: List<DiscoverResults>) {
        DetailTitleText("Recommendations")
        LazyRow(modifier = Modifier.padding(top = 5.dp)) {
            items(recommendations) { results ->
                ItemImage(results, cornerSize = 12, fragment = this@DetailFragment)
            }
        }
    }
}




