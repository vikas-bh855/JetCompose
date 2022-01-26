package com.example.jetcompose.presentation.view

import com.example.jetcompose.Constants
import java.text.SimpleDateFormat
import java.util.*

val String.srcImagePath
    get() = Constants.IMAGE_PATH + this

val String.formattedDate
    get() = let {
        val date  = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).let {
            it.parse(this)
        }
        SimpleDateFormat("dd MMM yy").format(date)
    }
