package com.example.jetcompose.search

import com.example.jetcompose.network.ApiService
import com.example.jetcompose.RemoteDataSource
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val apiService: ApiService,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun fetchSearch(searchText: String, page: Int) =
        remoteDataSource.executeApi { apiService.getSearch(searchText, page) }
}