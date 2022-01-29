package com.example.jetcompose

import java.text.SimpleDateFormat
import java.util.*

val String.srcImagePath: String
    get() = Constants.IMAGE_PATH + this

val String.formattedDate: String
    get() = let {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).let {
            it.parse(this)
        }
        SimpleDateFormat("dd MMM yy").format(date)
    }
