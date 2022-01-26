package com.example.jetcompose.data

import com.example.jetcompose.data.models.Discover
import com.example.jetcompose.data.models.GenreData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("genre/movie/list")
    suspend fun getGenres(@QueryMap hashMap: HashMap<String,String>):Response<GenreData>

    @GET("trending/all/week")
    suspend fun getPopulars(@QueryMap hashMap: HashMap<String,String>):Response<Discover>

    @GET("discover/movie")
    suspend fun getDiscover(@QueryMap hashMap: HashMap<String,String>):Response<Discover>
}