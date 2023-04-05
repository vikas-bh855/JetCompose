package com.example.jetcompose.login

import androidx.lifecycle.ViewModel
import com.example.jetcompose.RemoteDataSource
import com.example.jetcompose.network.ApiService
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Modifier
import javax.inject.Inject


class LoginRepository @Inject constructor(
    private val dataSource: RemoteDataSource,
    private val apiService: ApiService
) : ViewModel() {

    fun getToken() = flow {
        emit(dataSource.executeApi { apiService.getToken() })
    }

    fun login(jsonObject: JsonObject) = flow {
        emit(dataSource.executeApi { apiService.login(jsonObject) })
    }

    fun createSession(jsonObject: JsonObject) = flow {
        emit(dataSource.executeApi { apiService.createSession(jsonObject) })
    }
}
