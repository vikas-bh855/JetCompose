package com.example.jetcompose.data.repository

import com.example.jetcompose.data.ApiService
import com.example.jetcompose.data.source.RemoteDataSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val apiService: ApiService,
    private var dataSource: RemoteDataSource
) {

    fun getGenres() = flow {
        emit(dataSource.executeApi { apiService.getGenres() })
    }

    fun getPopulars() = flow {
        emit(dataSource.executeApi { apiService.getPopulars() })
    }

    fun getDiscoverGenre(genreId: String) = flow {
        val hashMap = hashMapOf("with_genres" to genreId)
        emit(dataSource.executeApi { apiService.getDiscover(hashMap) })
    }

    fun getMovieCredits(movieId: String) = flow {
        emit(dataSource.executeApi { apiService.getMovieCredits(movieId) })
    }
}