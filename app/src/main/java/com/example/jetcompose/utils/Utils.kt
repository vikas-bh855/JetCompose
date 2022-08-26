package com.example.jetcompose.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.jetcompose.R
import java.text.SimpleDateFormat
import java.util.*

val String.srcImagePath: String
    get() = Constants.IMAGE_PATH + this

val String.formattedDate: String
    get() = let {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).let {
            it.parse(this)
        }
        SimpleDateFormat("yyyy").format(date)
    }

val fontFamilyPR = FontFamily(Font(R.font.poppinsr))
