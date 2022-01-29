package com.example.jetcompose.data

import com.example.jetcompose.data.models.Discover
import com.example.jetcompose.data.models.GenreData
import com.example.jetcompose.data.models.MovieCast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @GET("genre/movie/list")
    suspend fun getGenres(): Response<GenreData>

    @GET("trending/all/week")
    suspend fun getPopulars(): Response<Discover>

    @GET("discover/movie")
    suspend fun getDiscover(@QueryMap hashMap: HashMap<String, String>): Response<Discover>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieId: String): Response<MovieCast>
}