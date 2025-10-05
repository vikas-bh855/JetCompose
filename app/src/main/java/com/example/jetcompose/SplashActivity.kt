package com.example.jetcompose

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetcompose.theme.colorButtonBlue
import com.example.jetcompose.theme.colorButtonGreen

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ComposeView(this).setContent {
            Name()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Name() {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                brush = Brush.verticalGradient(listOf(colorButtonGreen, colorButtonBlue)),
                start = Offset(337f, 1180f),
                end = Offset(514f, 1180f),
                strokeWidth = 15f,
                cap = StrokeCap.Round
            )
            drawLine(
                brush = Brush.verticalGradient(listOf(colorButtonGreen, colorButtonBlue)),
                start = Offset(514f, 1180f),
                end = Offset(514f, 1238f),
                strokeWidth = 15f,
                cap = StrokeCap.Round
            )
            drawLine(
                brush = Brush.verticalGradient(listOf(colorButtonGreen, colorButtonBlue)),
                start = Offset(514f, 1238f),
                end = Offset(717f, 1238f),
                strokeWidth = 15f,
                cap = StrokeCap.Round
            )
        }
    }
}

