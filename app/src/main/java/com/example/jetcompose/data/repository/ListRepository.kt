package com.example.jetcompose.data.repository

import com.example.jetcompose.data.ApiService
import com.example.jetcompose.data.models.DiscoverResults
import com.example.jetcompose.data.source.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataSource: RemoteDataSource
) {

    fun getGenres() = flow {
        emit(dataSource.executeApi { apiService.getGenres() })
    }

    fun getTrending() = flow {
        emit(dataSource.executeApi { apiService.getTrending() })
    }

    fun getDiscoverGenre(genreId: String) = flow {
        val hashMap = hashMapOf("with_genres" to genreId)
        emit(dataSource.executeApi { apiService.getDiscover(hashMap) })
    }

    fun getMovieCredits(movieId: String) = flow {
        emit(dataSource.executeApi { apiService.getMovieCredits(movieId) })
    }

    fun getDetails(movieId: String) = flow {
        emit(dataSource.executeApi { apiService.getMovieDetails(movieId) })
    }

    fun getNowPlaying() = flow {
        emit(dataSource.executeApi { apiService.getNowPlaying() })
    }

    fun getVideoUrl(movieId: String) = flow {
        emit(dataSource.executeApi { apiService.getVideoUrl(movieId = movieId) })
    }
}