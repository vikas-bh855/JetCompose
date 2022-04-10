package com.example.jetcompose.presentation.view

import android.os.Bundle
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.jetcompose.R
import com.example.jetcompose.fontFamilyPR
import com.example.jetcompose.presentation.utils.colorAppBackground
import com.example.jetcompose.presentation.utils.colorLightGrey
import com.example.jetcompose.presentation.utils.colorOffWhite
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

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

    companion object {
        private const val TAG = "ProfileActivity"
    }

    @Preview
    @Composable
    fun ProfileImage() {
        BoxWithConstraints(
            Modifier
                .fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 10.dp, end = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = ""
                    )
                    Column {
                        Text(
                            text = "Hey Vikas",
                            fontFamily = fontFamilyPR,
                            color = colorOffWhite,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                        )
                        Text(
                            text = SimpleDateFormat("dd, MMM yyyy").format(Date()),
                            fontFamily = fontFamilyPR,
                            color = colorLightGrey,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)

                        )

                    }
                }

                Card(
                    backgroundColor = Color(0xFF2B2A2A),elevation = 0.dp, modifier = Modifier
                        .padding(top = 15.dp), shape = RoundedCornerShape(30.dp)
                ) {
                    AccountDetails()
                }
            }
        }
    }

    @Composable
    fun AccountDetails() {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.name), contentDescription = "")
                Text(
                    text = "Vikas Singh",
                    fontFamily = fontFamilyPR,
                    color = colorOffWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.location), contentDescription = "")
                Text(
                    text = "Mumbai, India",
                    fontFamily = fontFamilyPR,
                    color = colorOffWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.work), contentDescription = "")
                Text(
                    text = "Android Developer",
                    fontFamily = fontFamilyPR,
                    color = colorOffWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.email), contentDescription = "")
                Text(
                    text = "test.test@mailinator.com",
                    fontFamily = fontFamilyPR,
                    color = colorOffWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.call), contentDescription = "")
                Text(
                    text = "+91********67",
                    fontFamily = fontFamilyPR,
                    color = colorOffWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
            Button(colors = ButtonDefaults.buttonColors(backgroundColor = colorAppBackground),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                onClick = { /*TODO*/ }) {
                Text(text = "Show More", fontFamily = fontFamilyPR, color = colorOffWhite)
                Image(
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = "arrow_down",
                    Modifier.padding(start = 10.dp)
                )
            }
        }
    }

}

