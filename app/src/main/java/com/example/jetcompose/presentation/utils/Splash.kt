package com.example.jetcompose.presentation.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Preview
@Composable
fun Splash() {
    val name = remember { Animatable(0f) }
    val rotation = remember { Animatable(-20f) }
    LaunchedEffect(name) {
        launch {
            name.animateTo(
                300f, animationSpec = spring(
                    dampingRatio = 0.5f,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            rotation.animateTo(20f, animationSpec = spring(0.3f, Spring.StiffnessLow))
        }
        launch {
            delay(300)
            rotation.animateTo(0f, animationSpec = spring(0.3f, Spring.StiffnessLow))
        }
    }
    Canvas(
        modifier = Modifier
            .background(cyanDark)
            .fillMaxSize()
            .offset(x = (-40).dp, y = (-20).dp)
            .graphicsLayer {
                rotationX = rotation.value
            }
    ) {
        val height = size.height
        val width = size.width
        drawRoundRect(
            brush = Brush.verticalGradient(listOf(cyanLight, colorRedDark)),
            topLeft = Offset(x = width / 2 - 20f, y = height / 2 - 100f),
            size = Size(width = 20f, height = name.value),
            cornerRadius = CornerRadius(10f, 10f)
        )

        drawRoundRect(
            brush = Brush.verticalGradient(listOf(cyanLight, colorRedDark)),
            topLeft = Offset(x = width / 2 + 80f, y = height / 2 - 100f),
            size = Size(width = 20f, height = name.value),
            cornerRadius = CornerRadius(10f, 10f)
        )
        translate(top = 35f, left = 60f) {
            rotate(-30f) {
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(cyanLight, colorRedDark)),
                    topLeft = Offset(x = width / 2 + 80f, y = height / 2 - 100f),
                    size = Size(width = 20f, height = 340f),
                    cornerRadius = CornerRadius(10f, 10f)
                )
            }
        }
        translate(left = 164f) {
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(cyanLight, colorRedDark)),
                topLeft = Offset(x = width / 2 + 80f, y = height / 2 - 100f),
                size = Size(width = 20f, height = 300f),
                cornerRadius = CornerRadius(10f, 10f)
            )
        }

    }
}