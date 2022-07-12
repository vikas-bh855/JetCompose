package com.example.jetcompose.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.WindowCompat
import com.example.jetcompose.R
import com.example.jetcompose.ui.theme.colorAppBackground
import com.example.jetcompose.ui.theme.colorBlue
import com.example.jetcompose.ui.theme.colorDarkGrey
import com.example.jetcompose.ui.theme.colorWhite
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(ComposeView(this).apply {
            setContent {
                ProfileImage()
            }
        })
    }

    @Preview
    @Composable
    fun ProfileImage() {
        BoxWithConstraints(
            Modifier.fillMaxSize()
        ) {
            ConstraintLayout {
                val (background, button, profile) = createRefs()
                Image(
                    painter = painterResource(id = R.drawable.marvel),
                    contentDescription = "Profile Background",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .constrainAs(background) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.compose),
                    contentDescription = "Profile Background",
                    Modifier
                        .size(100.dp)
                        .constrainAs(profile) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(background.bottom, (-50).dp)
                        },
                    contentScale = ContentScale.Crop
                )

                Box(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .constrainAs(button) {
                            top.linkTo(profile.bottom)
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .padding(10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorDarkGrey)
                        ) {
                            Text(text = "Edit Profile", color = colorWhite)
                        }
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .padding(10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xffA06EF2))
                        ) {
                            Text(text = "Subscribe", color = colorAppBackground)
                        }
                    }

                }

            }


        }
    }
}

