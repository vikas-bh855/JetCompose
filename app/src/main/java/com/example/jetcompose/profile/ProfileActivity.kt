package com.example.jetcompose.profile

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetcompose.R
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.models.discoverList
import com.example.jetcompose.theme.colorWhite
import com.example.jetcompose.utils.ItemImage
import com.example.jetcompose.utils.fontFamilyPR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val listWatchlist = profileViewModel.listWatchlist.collectAsState()
            Log.d(TAG, "onCreate: $listWatchlist")
            Profile(listWatchlist.value)
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun Profile(listWatchlist: List<DiscoverResults> = discoverList) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.compose),
                contentDescription = "Profile"
            )
            Text(
                text = "Name",
                fontSize = 18.sp,
                fontFamily = fontFamilyPR,
                fontWeight = FontWeight.Bold,
                color = colorWhite,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "test123@gmail.com",
                fontSize = 12.sp,
                fontFamily = fontFamilyPR,
                fontWeight = FontWeight.Normal,
                color = colorWhite,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Watchlist",
                fontSize = 15.sp,
                fontFamily = fontFamilyPR,
                fontWeight = FontWeight.SemiBold,
                color = colorWhite,
                modifier = Modifier.padding(top = 10.dp)
            )
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(listWatchlist) { item ->
                    ItemImage(item!!, cornerSize = 12)
                }
            }
        }
    }

    private val TAG = "ProfileActivity"
}

