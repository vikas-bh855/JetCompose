package com.example.jetcompose

import com.example.jetcompose.data.models.DiscoverResults

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_PATH = "https://image.tmdb.org/t/p/w780/"

    @JvmField
    val discoverResults = DiscoverResults(
        id = 1,
        poster_path = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
        backdrop_path = "/1Rr5SrvHxMXHu5RjKpaMba8VTzi.jpg",
        title = "Spider-Man: No Way Home",
        vote_average = 8.5,
        original_title = "Spider-Man: No Way Home",
        overview = "Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.",
        release_date = "2021-12-15",
        adult = false
    )
}