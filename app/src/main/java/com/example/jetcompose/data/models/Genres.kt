package com.example.jetcompose.data.models

import java.io.Serializable

data class GenreData(val genres: List<Genres>) : Serializable

data class Genres(val id: String, val name: String) : Serializable

data class Discover(val results: List<DiscoverResults>) : Serializable

