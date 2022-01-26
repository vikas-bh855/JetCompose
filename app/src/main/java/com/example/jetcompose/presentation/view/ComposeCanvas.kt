package com.example.jetcompose.presentation.view

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val colorLightBackground = Color(0xFF00FF98)
val greenLight = Color(0xFF8BC34A)
val colorYellowLight = Color(0xFFE4D446)
val colorYellowDark = Color(0xFFF7D502)
val colorRedLight = Color(0xFFEF5350)
val colorRedDark = Color(0xFFFC4444)
val colorPurpleLight = Color(0xFFAA00FF)
val colorVioletDark = Color(0xFF6200EA)
val colorSkyLight = Color(0xFF0CDBF5)
val colorDarkGrey = Color(0xFFB3CDCE)
val colorSkyDark = Color(0xFF4AD6E7)
val colorGrey = Color(0xFF758486)
val blue = Color(0xFFEDF6FC)
var angle = 0f


@Preview
@Composable
private fun ComposeCanvas() {
    val rememberInfiniteTransition = rememberInfiniteTransition()
    val scale = rememberInfiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        )
    )
    val rotationYellow by rememberInfiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, 1000, FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )
    val rotationSky by rememberInfiniteTransition.animateFloat(
        initialValue = -270f,
        targetValue = 90f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, 1000, FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )
    val rotationViolet by rememberInfiniteTransition.animateFloat(
        initialValue = -180f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, 1000, FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )
    val rotationRed by rememberInfiniteTransition.animateFloat(
        initialValue = -90f,
        targetValue = 270f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, 1000, FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
        onDraw = {
            Log.d("scale", "ComposeCanvas: " + scale)
            scale(scale.value) {
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorLightBackground,
                            greenLight
                        )
                    ), radius = size.width / 32
                )
            }
            rotate(rotationYellow, block = {
                Log.d("angle", "ComposeCanvas: " + angle)
                drawCircle(
                    brush = Brush.linearGradient(listOf(colorYellowDark, colorYellowLight)),
                    radius = 20f,
                    center = Offset(
                        x = size.width / 2 + 10 + scale.value * 50,
                        y = size.height / 2
                    ),
                )
            })

            rotate(rotationRed, block = {
                Log.d("angle", "ComposeCanvas: " + angle)
                drawCircle(
                    brush = Brush.linearGradient(listOf(colorRedDark, colorRedLight)),
                    radius = 20f,
                    center = Offset(
                        x = size.width / 2 + 10 + scale.value * 50,
                        y = size.height / 2
                    )
                )
            })

            rotate(rotationViolet, block = {
                Log.d("angle", "ComposeCanvas: " + angle)
                drawCircle(
                    brush = Brush.linearGradient(listOf(colorPurpleLight, colorVioletDark)),
                    radius = 20f,
                    center = Offset(
                        x = size.width / 2 + 10 + scale.value * 50,
                        y = size.height / 2
                    )
                )
            })

            rotate(rotationSky, block = {
                Log.d("angle", "ComposeCanvas: " + angle)
                drawCircle(
                    brush = Brush.linearGradient(listOf(colorSkyLight, colorSkyDark)),
                    radius = 20f,
                    center = Offset(
                        x = size.width / 2 + 10 + scale.value * 50,
                        y = size.height / 2
                    )
                )
            })
        })

}
