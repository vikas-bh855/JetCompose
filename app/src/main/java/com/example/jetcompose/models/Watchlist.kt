package com.example.jetcompose.models

import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
data class Watchlist(
    val success: Boolean
) : Serializable