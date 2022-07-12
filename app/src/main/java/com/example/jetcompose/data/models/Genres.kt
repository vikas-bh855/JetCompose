package com.example.jetcompose.data.models

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import java.io.Serializable

data class GenreData(val genres: List<Genres>) : Serializable

data class Genres(val id: String, val name: String) : Serializable

data class Discover(val results: List<DiscoverResults>, val page: Int) : Serializable

data class Video(val results: List<VideoResults>) : Serializable

data class MovieCast(val cast: List<MovieCrew>) : Serializable

data class MovieCrew(
    val adult: Boolean,
    val gender: Int,
    val id: Long,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: String,
    val profile_path: String?,
    val character: String,
)

object DiscoverParameterProvider : PreviewParameterProvider<Discover> {
    override val values = sequenceOf(
        Discover(
            results = listOf(
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
            ),
            page = 1
        )
    )
}

