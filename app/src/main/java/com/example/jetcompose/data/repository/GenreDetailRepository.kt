package com.example.jetcompose.data.repository

import com.example.jetcompose.data.ApiService
import com.example.jetcompose.data.source.RemoteDataSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GenreDetailRepository @Inject constructor(
    private val apiService: ApiService,
    private var dataSource: RemoteDataSource
) {
    private fun getGenreDetail(genreId: String) = flow {
        val hashMap = hashMapOf("with_genre" to genreId)
        emit(dataSource.executeApi { apiService.getDiscover(hashMap) })
    }
}