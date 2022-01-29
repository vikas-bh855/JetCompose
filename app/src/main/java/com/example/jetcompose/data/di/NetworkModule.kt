package com.example.jetcompose.data.di

import com.example.jetcompose.Constants.BASE_URL
import com.example.jetcompose.data.ApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun getApiService(okHttpClient: OkHttpClient) =
        Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()
            .create(ApiService::class.java)

    @Singleton
    @Provides
    fun getHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(Interceptor {
            val url = it.request().url.newBuilder()
                .addQueryParameter("api_key", "1d9b898a212ea52e283351e521e17871").build()
            return@Interceptor it.proceed(it.request().newBuilder().url(url).build())
        })
        .build()
}