package com.example.jetcompose.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import com.example.jetcompose.R
import com.example.jetcompose.data.models.DiscoverResults
import com.example.jetcompose.data.models.DiscoverResultsParameterProvider
import com.example.jetcompose.fontFamilyPR
import com.example.jetcompose.formattedDate
import com.example.jetcompose.presentation.utils.colorGreenDark
import com.example.jetcompose.presentation.utils.colorOffWhite
import com.example.jetcompose.presentation.utils.colorOffWhiteDark
import com.example.jetcompose.presentation.utils.colorWhite
import com.example.jetcompose.presentation.viewmodel.DetailViewModel
import com.example.jetcompose.srcImagePath
import com.google.android.youtube.player.YouTubeStandalonePlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                detailViewModel.getMovieCredits(args.discoverResults.id.toString())
                detailViewModel.getMovieDetails(args.discoverResults.id.toString())
                Box {
                    detailViewModel.movieDetails.value?.let {
                        DetailPage(it)
                        PlayVideo()
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun DetailPage(@PreviewParameter(DiscoverResultsParameterProvider::class) discoverResults: DiscoverResults) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(enabled = true, state = ScrollState(0))
        ) {
            TopLayout(discoverResults)
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
                color = colorOffWhite
            )
            ShowCredits()
        }
    }


    @OptIn(ExperimentalUnitApi::class)
    @Preview
    @Composable
    fun TopLayout(@PreviewParameter(DiscoverResultsParameterProvider::class) discoverResults: DiscoverResults) {
        val floatAnimatable = remember { Animatable(30f) }
        val alphaAnimatable = remember { Animatable(0f) }
        val scaleAnimatable = remember { Animatable(2f) }
        LaunchedEffect(discoverResults.poster_path) {
            launch { floatAnimatable.animateTo(0f, tween(800)) }
            launch { alphaAnimatable.animateTo(1f, tween(800)) }
            launch { scaleAnimatable.animateTo(1f, tween(800)) }
        }
        val value = (360 * discoverResults.vote_average.toFloat()) / 10f
        val lengthAnimatable = remember {
            Animatable(0f)
        }
        LaunchedEffect(discoverResults.vote_average) {
            lengthAnimatable.animateTo(value, animationSpec = tween(2000))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp), horizontalArrangement = Arrangement.End
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
                            style = Stroke(width = 6f),
                        )
                    })
                    Text(
                        text = discoverResults.vote_average.toString(),
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
                        modifier = Modifier
                            .padding(start = 10.dp),
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
                        modifier = Modifier
                            .padding(start = 10.dp),
                        text = Locale(discoverResults.original_language).displayLanguage,
                        color = colorOffWhite,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyPR,
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color(0xFF101010))
                        .padding(4.dp),
                    text = discoverResults.genres!![0].name,
                    color = colorOffWhiteDark,
                    fontFamily = fontFamilyPR,
                )
                if (discoverResults.genres.isNotEmpty())
                    Text(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = Color(0xFF101010))
                            .padding(4.dp),
                        text = discoverResults.genres[1].name,
                        color = colorOffWhiteDark,
                        fontFamily = fontFamilyPR,
                    )

                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF101010)),
                ) {
                    Text(
                        modifier = Modifier
                            .zIndex(10f)
                            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp)
                            .clickable {
                                detailViewModel.getVideoUrl(discoverResults.id.toString())
                            },
                        text = "Play",
                        color = colorOffWhiteDark,
                        fontFamily = fontFamilyPR,
                    )
                    Image(
                        modifier = Modifier
                            .padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)
                            .graphicsLayer {
                                scaleY = scaleAnimatable.value
                                scaleX = scaleAnimatable.value
                            },
                        painter = painterResource(id = R.drawable.play),
                        contentDescription = "Play"
                    )
                }
            }
            Image(
                painter = rememberImagePainter(
                    data = discoverResults.poster_path.srcImagePath,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = "Detail Image",
                modifier = Modifier
                    .width(width = 250.dp)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .weight(1.5f)
                    .graphicsLayer {
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
                requireActivity(),
                "1234567890",
                videoUrl,
                0,
                true,
                false
            )
            startActivity(intent)

        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ShowCredits() {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Credits",
            fontFamily = fontFamilyPR,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = colorWhite
        )
        val alphaAnimation = remember { Animatable(0f) }
        LaunchedEffect(alphaAnimation) {
            alphaAnimation.animateTo(1f, tween(durationMillis = 1000))
        }
        val listCrew = detailViewModel.listMovieCrew.collectAsState()
        LazyRow(
            content = {
                itemsIndexed(listCrew.value) { index, item ->
                    Column(Modifier.padding(start = 10.dp, end = 10.dp)) {
                        Image(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            painter = rememberImagePainter(
                                data = item.profile_path?.srcImagePath,
                                builder = {
                                    crossfade(true)
                                }
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

}




