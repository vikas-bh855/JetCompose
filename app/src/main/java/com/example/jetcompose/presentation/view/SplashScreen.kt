package com.example.jetcompose.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.jetcompose.presentation.utils.Splash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Splash()
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            startActivity(
                Intent(this@SplashScreen, MainActivity::class.java)
            )
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }


    @Preview
    @Composable
    fun SplashCom() {
        Splash()
    }
}