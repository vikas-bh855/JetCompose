package com.example.jetcompose.network

import com.example.jetcompose.models.*
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("genre/movie/list")
    suspend fun getGenres(): Response<GenreData>

    @GET("trending/all/week")
    suspend fun getTrending(): Response<Discover>

    @GET("discover/movie")
    suspend fun getDiscover(@QueryMap hashMap: HashMap<String, String>): Response<Discover>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieId: String): Response<MovieCast>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String): Response<DiscoverResults>

    @GET("movie/now_playing")
    suspend fun getNowPlaying(): Response<Discover>

    @GET("movie/{movie_id}/videos")
    suspend fun getVideoUrl(@Path("movie_id") movieId: String): Response<Video>

    @GET("search/movie")
    suspend fun getSearch(
        @Query("query") searchText: String,
        @Query("page") page: Int
    ): Response<Discover>

    /**
     * Login Token Generation
     */
    @GET("authentication/token/new")
    suspend fun getToken(): Response<Login>

    @POST("authentication/token/validate_with_login")
    suspend fun login(@Body jsonObject: JsonObject): Response<Login>

    @POST("authentication/session/new")
    suspend fun createSession(@Body jsonObject: JsonObject): Response<Login>
}