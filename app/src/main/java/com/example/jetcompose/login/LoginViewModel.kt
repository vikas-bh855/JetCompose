package com.example.jetcompose.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetcompose.Result
import com.example.jetcompose.models.Login
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) :
    ViewModel() {

    val loginState = MutableStateFlow("")
    val error = mutableStateOf("")

    fun login(userId: String, password: String) {
        viewModelScope.launch {
            loginRepository.getToken().collect { it ->
                when (it) {
                    is Result.Success<*> -> {
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("username", userId)
                        jsonObject.addProperty("password", password)
                        jsonObject.addProperty("request_token", (it.data as Login).request_token)
                        loginRepository.login(jsonObject).collect {
                            when (it) {
                                is Result.Success<*> -> {
                                    loginRepository.createSession(JsonObject().apply {
                                        addProperty(
                                            "request_token", (it.data as Login).request_token
                                        )
                                    }).collect {
                                        when (it) {
                                            is Result.Success<*> -> {
                                                loginState.value = (it.data as Login).session_id
                                            }
                                            else -> error.value = "Login Failed"
                                        }
                                    }
                                }
                                else -> error.value = "Login Failed"
                            }
                        }
                    }
                    else -> error.value = "Login Failed"
                }
            }
        }
    }
}