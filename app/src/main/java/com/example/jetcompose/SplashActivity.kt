package com.example.jetcompose

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetcompose.theme.greenLight
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ComposeView(this).setContent {
            Name()
        }
    }

    @Preview
    @Composable
    fun Name() {
        val animate = rememberInfiniteTransition()
        val value = animate.animateFloat(
            initialValue = 10f,
            targetValue = 90f,
            animationSpec = infiniteRepeatable(animation = snap(2000), repeatMode = RepeatMode.Reverse)
        )

        Text(text = "this is sample text ", modifier = Modifier.drawBehind {
            drawCircle(greenLight, value.value)
        })
    }
}

