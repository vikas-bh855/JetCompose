package com.example.jetcompose.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun Loader(isLoading: Boolean = true) {
    if (isLoading) {
        val launchedEffect = remember {
            mutableStateOf(true)
        }
        val first = remember { Animatable(0f) }
        val second = remember { Animatable(0f) }
        val third = remember { Animatable(0f) }
        LaunchedEffect(key1 = launchedEffect.value) {
            launch {
                first.animateTo(
                    20f,
                    infiniteRepeatable(
                        repeatMode = RepeatMode.Reverse,
                        animation = tween(600)
                    )
                )
            }
            launch {
                delay(200)
                second.animateTo(
                    20f,
                    infiniteRepeatable(
                        repeatMode = RepeatMode.Reverse,
                        animation = tween(600)
                    )
                )
            }
            launch {
                delay(400)
                third.animateTo(
                    20f,
                    infiniteRepeatable(
                        repeatMode = RepeatMode.Reverse,
                        animation = tween(600)
                    )
                )
            }
        }
        BoxWithConstraints {
            val x = with(LocalDensity.current) { maxWidth.toPx() }
            val y = with(LocalDensity.current) { maxHeight.toPx() }
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                drawCircle(
                    color = Color(0xFFFEC01D),
                    radius = first.value,
                    center = Offset(x = (x / 2f) - 60f, y = y / 2f)
                )
                drawCircle(color = Color(0xFFE54962), radius = second.value)
                drawCircle(
                    color = Color(0xFF31CA66),
                    radius = third.value,
                    center = Offset(x = (x / 2f) + 60f, y = y / 2f)
                )
            })
        }
    }
}