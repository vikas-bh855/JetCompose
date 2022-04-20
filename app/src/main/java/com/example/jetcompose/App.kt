package com.example.jetcompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

interface Details{
    fun test():String
}