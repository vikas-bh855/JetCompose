package com.example.jetcompose.data.models

import java.io.Serializable

data class VideoResults(
    val iso_639_1: String,
    val iso_3166_1: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Long,
    val type: String,
    val official: Boolean,
    val published_at: String,
    val id: String
) : Serializable