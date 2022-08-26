package com.example.jetcompose.models

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import java.io.Serializable

data class DiscoverResults(
    val id: Int,
    val poster_path: String,
    val backdrop_path: String,
    val title: String,
    val vote_average: Double,
    val original_title: String?,
    val overview: String,
    val release_date: String,
    val adult: Boolean,
    val original_language: String,
    val genres: List<Genres>? = null
) : Serializable


object DiscoverResultsParameterProvider : PreviewParameterProvider<DiscoverResults> {
    override val values = sequenceOf(
        DiscoverResults(
            id = 634649,
            poster_path = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            backdrop_path = "/iQFcwSGbZXMkeyKrxbPnwnRo5fl.jpg",
            title = "Spider-Man: No Way Home",
            vote_average = 8.3,
            original_title = "Spider-Man: No Way Home",
            overview = "Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.",
            release_date = "2021-12-15",
            adult = false,
            original_language = "en",
            genres = listOf(Genres("12", "Action"), Genres("12", "Adventure"))
        )
    )
}

