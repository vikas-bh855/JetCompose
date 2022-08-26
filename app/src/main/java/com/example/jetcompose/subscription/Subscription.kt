package com.example.jetcompose.subscription

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import coil.load
import com.example.jetcompose.databinding.SubscriptionActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Subscription : AppCompatActivity() {
    private val imageUrls =
        listOf(
            "https://terrigen-cdn-dev.marvel.com/content/prod/1x/stellarvortex_reald_digital_poster_1080x1350_v1.jpg",
            "https://stat1.bollywoodhungama.in/wp-content/uploads/2022/06/Rocketry-The-Nambi-Effect.jpg",
            "https://static.independent.co.uk/2021/03/24/16/1505763709-tomb-raider.jpg?quality=75&width=640&auto=webp",
            "https://m.media-amazon.com/images/M/MV5BMTU2NjA1ODgzMF5BMl5BanBnXkFtZTgwMTM2MTI4MjE@._V1_.jpg"
        )

    private lateinit var binding: SubscriptionActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SubscriptionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.apply {
        }
    }
}