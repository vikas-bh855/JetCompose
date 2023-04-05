package com.example.jetcompose.subscription

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.jetcompose.R
import com.example.jetcompose.theme.*
import com.example.jetcompose.utils.fontFamilyPR
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@AndroidEntryPoint
class Subscription : ComponentActivity() {

    private val listPlans = listOf("Basic", "Premium", "VIP")
    private val listAmount = listOf("$1.99", "$2.99", "$3.99")
    private val listBasicDetails = listOf("720p", "Ads", "One Screen")
    private val listPremiumDetails = listOf("1080p", "Ads", "Two Screen")
    private val listVIPDetails = listOf("4K", "No Ads", "Four Screen")

    private val map = mutableMapOf(
        listPlans[0] to listBasicDetails,
        listPlans[1] to listPremiumDetails,
        listPlans[2] to listVIPDetails
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComposeView(this).apply {
            setContent {
                SubscriptionPlan()
            }
        }
    }

    @Preview
    @Composable
    fun SubscriptionPlan() {
        Column {
            val pageCount = remember { mutableStateOf(0) }
            val pageState = rememberPagerState()
            HorizontalPager(
                count = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 150.dp),
                itemSpacing = 10.dp,
                state = pageState
            ) { page: Int ->
                Column {
                    Card(shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                                lerp(
                                    start = 2f.toDp(),
                                    stop = 2.6f.toDp(),
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale.value
                                    scaleY = scale.value
                                }
                                alpha = lerp(
                                    start = 1f.toDp(),
                                    stop = 2.6f.toDp(),
                                    fraction = (1f - pageOffset.coerceIn(0f, 1f))
                                ).value
                            }) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = listPlans[page],
                                    fontFamily = fontFamilyPR,
                                    fontSize = 22.sp
                                )
                                Text(
                                    text = listAmount[page],
                                    fontFamily = fontFamilyPR,
                                    fontSize = 16.sp,
                                    fontWeight = W600
                                )
                            }
                            val painter = painterResource(id = R.drawable.payment)
                            Image(
                                painter,
                                contentDescription = "payment",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(80.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
            LaunchedEffect(key1 = pageState) {
                snapshotFlow { pageState.currentPage }.collect {
                    pageCount.value = it
                    Log.d("TAG", "SubscriptionPlan: $it")
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 15.dp, top = 10.dp)
                    .fillMaxWidth()
            ) {
                val planDetails = map[listPlans[pageCount.value]]
                planDetails?.forEach {
                    Text(
                        text = it,
                        color = colorWhite,
                        fontSize = 18.sp,
                        fontFamily = fontFamilyPR,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(colorPurpleLight)
                    .padding(7.dp), text = "Subscribe", fontFamily = fontFamilyPR
            )
        }

    }
}
