package com.example.jetcompose.subscription

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.jetcompose.theme.colorWhite
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
        val updateState = remember { mutableStateOf(0) }
        HorizontalPager(
            count = 3,
            modifier = Modifier
                .wrapContentWidth(Alignment.Start)
                .padding(top = 20.dp),
            contentPadding = PaddingValues(start = 20.dp, end = 150.dp),
            itemSpacing = 10.dp
        ) { page: Int ->
            Column {
                Log.d("Subscription", "SubscriptionPlan: $page")
                updateState.value = page
                Card(Modifier.graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 2f.toDp(),
                        stop = 2.6f.toDp(),
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale.value
                        scaleY = scale.value
                    }

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 1f.toDp(),
                        stop = 2.6f.toDp(),
                        fraction = (1f - pageOffset.coerceIn(0f, 1f))
                    ).value
                }) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = listPlans[page], fontFamily = fontFamilyPR, fontSize = 22.sp
                        )
                        Text(
                            text = listAmount[page],
                            fontFamily = fontFamilyPR,
                            fontSize = 16.sp,
                            fontWeight = W600
                        )
                    }
                }
            }
        }

        Column() {
            val planDetails = map[listPlans[updateState.value]]
            planDetails?.forEach {
                Text(text = it, color = colorWhite)
            }
        }
    }
}