package com.example.jetcompose.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import com.example.jetcompose.data.models.DiscoverResults
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Box {
                    Image(
                        painter = rememberImagePainter(data = args.discoverResults.poster_path.srcImagePath),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(20.dp)
                            .alpha(0.4f),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp, end = 20.dp, top = 40.dp,)
                    ) {
                        DetailPage(args.discoverResults)
                        ShowArc(args.discoverResults)
                    }

                }
            }
        }
    }


    @Composable
    fun DetailPage(discoverResults: DiscoverResults) {

        val floatAnimatable = remember { Animatable(10f) }
        val alphaAnimatable = remember { Animatable(0f) }
        LaunchedEffect(discoverResults.poster_path) {
            launch { floatAnimatable.animateTo(0f, tween(800)) }
            launch { alphaAnimatable.animateTo(1f, tween(800)) }


        }
        Row {
            Card(
                modifier = Modifier
                    .offset(y = floatAnimatable.value.dp)
                    .alpha(alphaAnimatable.value)
                    .size(150.dp, 230.dp)
                    .shadow(5.dp),
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(0.5.dp, blue)
            ) {
                Image(
                    painter = rememberImagePainter(data = discoverResults.poster_path.srcImagePath),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,

                    )
            }
            Column(Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp)) {
                Text(
                    text = discoverResults.title,
                    Modifier.fillMaxWidth(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = discoverResults.overview,
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 5.dp),
                    color = colorDarkGrey,
                    fontStyle = FontStyle.Normal,
                    fontSize = 12.sp
                )
            }
        }


    }

    @Composable
    fun ShowArc(discoverResults: DiscoverResults) {
        Card(
            shape = RoundedCornerShape(10.dp), modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp), backgroundColor = blue
        ) {
            val value = (360 * discoverResults.vote_average.toFloat()) / 10f
            val lengthAnimatable = remember {
                Animatable(0f)
            }

            LaunchedEffect(discoverResults.vote_average) {
                lengthAnimatable.animateTo(value, animationSpec = tween(2000))
            }


            Row(horizontalArrangement = Arrangement.Center) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Canvas(modifier = Modifier.size(40.dp, 40.dp), onDraw = {
                            drawArc(
                                brush = Brush.linearGradient(
                                    listOf(
                                        colorYellowDark,
                                        colorVioletDark
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
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = " IMDB ",
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                }

                Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Box(
                        modifier = Modifier.size(50.dp, 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val age = if (discoverResults.adult) "18+" else "13+"
                        Text(
                            text = age,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = " AGE ",
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                }

                Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Box(
                        modifier = Modifier.size(70.dp, 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = discoverResults.release_date.formattedDate,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = " RELEASE ",
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                }
            }


        }
    }

}




