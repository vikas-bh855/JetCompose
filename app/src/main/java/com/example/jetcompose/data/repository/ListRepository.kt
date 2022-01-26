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
        val hashMap = hashMapOf("api_key" to "1d9b898a212ea52e283351e521e17871")
        emit(dataSource.executeApi { apiService.getGenres(hashMap) })
    }

    fun getPopulars() =  flow{
        val hashMap = hashMapOf("api_key" to "1d9b898a212ea52e283351e521e17871")
        emit(dataSource.executeApi { apiService.getPopulars(hashMap) })
    }

    fun getDiscoverGenre(genreId:String) = flow {
        val hashMap = hashMapOf(
            "api_key" to "1d9b898a212ea52e283351e521e17871",
            "with_genres" to genreId
        )
        emit(dataSource.executeApi { apiService.getDiscover(hashMap) })
    }
}