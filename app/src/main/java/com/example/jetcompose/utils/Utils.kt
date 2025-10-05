package com.example.jetcompose.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.jetcompose.R
import com.example.jetcompose.models.GenreData
import com.example.jetcompose.theme.colorWhite
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

val String.srcImagePath: String
    get() = Constants.IMAGE_PATH500 + this

val String.bannerImagePath: String
    get() = Constants.IMAGE_PATH780 + this

val String.formattedDate: String
    get() = let {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).let {
            it.parse(this)
        }
        SimpleDateFormat("dd MMM yyyy").format(date)
    }

val fontFamilyPR = FontFamily(
    Font(R.font.poppinsr, FontWeight.Normal),
    Font(R.font.poppinssbold, FontWeight.SemiBold),
    Font(R.font.poppinsbold, FontWeight.Bold),
    Font(R.font.poppinsebold, FontWeight.ExtraBold)
)

val genres = """{
  "genres": [
    {
      "id": 28,
      "name": "Action"
    },
    {
      "id": 12,
      "name": "Adventure"
    },
    {
      "id": 16,
      "name": "Animation"
    },
    {
      "id": 35,
      "name": "Comedy"
    },
    {
      "id": 80,
      "name": "Crime"
    },
    {
      "id": 99,
      "name": "Documentary"
    },
    {
      "id": 18,
      "name": "Drama"
    },
    {
      "id": 10751,
      "name": "Family"
    },
    {
      "id": 14,
      "name": "Fantasy"
    },
    {
      "id": 36,
      "name": "History"
    },
    {
      "id": 27,
      "name": "Horror"
    },
    {
      "id": 10402,
      "name": "Music"
    },
    {
      "id": 9648,
      "name": "Mystery"
    },
    {
      "id": 10749,
      "name": "Romance"
    },
    {
      "id": 878,
      "name": "Science Fiction"
    },
    {
      "id": 10770,
      "name": "TV Movie"
    },
    {
      "id": 53,
      "name": "Thriller"
    },
    {
      "id": 10752,
      "name": "War"
    },
    {
      "id": 37,
      "name": "Western"
    }
  ]
}"""

val genresList = Gson().fromJson(genres, GenreData::class.java)

@Composable
fun DetailTitleText(text: String) {
    Text(
        text = text,
        fontFamily = fontFamilyPR,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = colorWhite
    )
}

