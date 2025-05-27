package com.example.jetcompose.home

import android.util.Log
import com.example.jetcompose.network.ApiService
import com.example.jetcompose.RemoteDataSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataSource: RemoteDataSource
) {

    fun getGenres() = flow {
        Log.d(TAG, "getGenres")
        emit(dataSource.executeApi { apiService.getGenres() })
    }

    fun getTrending() = flow {
        Log.d(TAG, "getTrending")
        emit(dataSource.executeApi { apiService.getTrending() })
    }

    fun getDiscoverGenre(genreId: String) = flow {
        Log.d(TAG, "getDiscoverGenre: $genreId")
        val hashMap = hashMapOf("with_genres" to genreId)
        emit(dataSource.executeApi { apiService.getDiscover(hashMap) })
    }

    fun getMovieCredits(movieId: String) = flow {
        Log.d(TAG, "getMovieCredits: $movieId")
        emit(dataSource.executeApi { apiService.getMovieCredits(movieId) })
    }

    fun getDetails(movieId: String) = flow {
        Log.d(TAG, "getDetails: $movieId")
        emit(dataSource.executeApi { apiService.getMovieDetails(movieId) })
    }

    fun getNowPlaying() = flow {
        Log.d(TAG, "getNowPlaying")
        emit(dataSource.executeApi { apiService.getNowPlaying() })
    }

    fun getVideoUrl(movieId: String) = flow {
        emit(dataSource.executeApi { apiService.getVideoUrl(movieId = movieId) })
    }

    fun getRecommendations(movieId: String) = flow {
        emit(dataSource.executeApi {
            Log.d(TAG, "getRecommendations: $movieId")
            apiService.getRecommendations(movieId) })
    }

    companion object{
        private const val TAG = "HomeRepository"
    }
}