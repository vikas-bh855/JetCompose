package com.example.jetcompose.data.source

import retrofit2.Response
import javax.inject.Inject


class RemoteDataSource @Inject constructor() {

    suspend fun <T> executeApi(call: suspend () -> Response<T>): Result {
        val response = call.invoke()
        return if (response.isSuccessful)
            Result.Success(response.body())
        else
            Result.Error(response.errorBody().toString())

    }
}

sealed class Result {
    data class Success<T>(val data: T):Result()
    data class Error<T>(val data: T):Result()
}