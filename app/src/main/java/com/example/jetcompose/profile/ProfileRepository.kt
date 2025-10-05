package com.example.jetcompose.profile

import com.example.jetcompose.RemoteDataSource
import com.example.jetcompose.network.ApiService
import com.example.jetcompose.utils.Constants
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataSource: RemoteDataSource
) {

    fun fetchWatchlist() = flow {
        emit(dataSource.executeApi {
            apiService.watchlist(Constants.ACCOUNT_ID)
        })
    }

    companion object {
        private const val TAG = "ProfileRepository"
    }

}