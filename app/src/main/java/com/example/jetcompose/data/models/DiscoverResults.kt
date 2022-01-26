package com.example.jetcompose.data.models

import java.io.Serializable


data class DiscoverResults(
    val id: Int,
    val poster_path: String,
    val backdrop_path: String,
    val title: String,
    val vote_average: Double,
    val original_title: String,
    val overview: String,
    val release_date: String,
    val adult: Boolean
) : Serializable

