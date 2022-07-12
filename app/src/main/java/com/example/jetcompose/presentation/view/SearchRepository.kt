package com.example.jetcompose.presentation.view

import com.example.jetcompose.data.ApiService
import com.example.jetcompose.data.source.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val apiService: ApiService,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun fetchSearch(searchText: String, page: Int) =
        remoteDataSource.executeApi { apiService.getSearch(searchText, page) }
}