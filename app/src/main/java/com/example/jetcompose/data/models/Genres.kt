package com.example.jetcompose.data.models

import java.io.Serializable

data class GenreData(val genres: List<Genres>) : Serializable

data class Genres(val id: String, val name: String) : Serializable

data class Discover(val results: List<DiscoverResults>) : Serializable


data class MovieCast(val cast: List<MovieCrew>) : Serializable

data class MovieCrew(
    val adult: Boolean,
    val gender: Int,
    val id: Long,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: String,
    val profile_path: String,
    val character: String,
)

