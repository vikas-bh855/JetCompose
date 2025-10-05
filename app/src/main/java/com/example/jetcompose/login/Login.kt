package com.example.jetcompose.login

data class Login(val success: Boolean, val session_id: String, val request_token: String)

data class AccountDetails(val id: String, val name: String, val username: String)