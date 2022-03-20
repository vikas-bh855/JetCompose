package com.example.jetcompose.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.jetcompose.R
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


@ExperimentalMaterialApi
@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply {
            setContent {
                ProfileImage()
            }
        })
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    companion object {
        private const val TAG = "ProfileActivity"
    }

    @Preview
    @Composable
    fun ProfileImage() {
        BoxWithConstraints(Modifier
                .fillMaxSize()
                .padding(10.dp)) {
            val squareSize = maxWidth / 2
            val swipeableState = rememberSwipeableState(0)
            val sizePx = with(LocalDensity.current) { squareSize.toPx() }
            val anchors = mapOf(0f to 0, sizePx to 1)
            Column(Modifier
                    .fillMaxSize()
                    .swipeable(
                            state = swipeableState,
                            anchors = anchors,
                            thresholds = { _, _ -> FractionalThreshold(0.4f) },
                            orientation = Orientation.Vertical
                    )
            ) {
                Card(shape = CircleShape, modifier = Modifier
                        .offset {
                            IntOffset(swipeableState.offset.value
                                    .times(0.9)
                                    .roundToInt(),
                                    swipeableState.offset.value
                                            .times(0.5)
                                            .roundToInt())

                        }
                        .size(40.dp)
                        .graphicsLayer {
                            this.scaleX = 1 + swipeableState.offset.value / 300f
                            this.scaleY = 1 + swipeableState.offset.value / 300f
                        }) {
                    Image(painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = ""
                    )
                }

            }
        }
    }

}

