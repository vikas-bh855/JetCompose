package com.example.jetcompose.network

import com.example.jetcompose.login.Login
import com.example.jetcompose.models.Discover
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.models.GenreData
import com.example.jetcompose.models.MovieCast
import com.example.jetcompose.models.Video
import com.example.jetcompose.models.Watchlist
import com.example.jetcompose.utils.Constants.BEARER_TOKEN
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

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
        @Query("query") searchText: String, @Query("page") page: Int
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

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendations(@Path("movie_id") movieId: String): Response<Discover>


    @Headers("Authorization: $BEARER_TOKEN")
    @POST("account/{account_id}/watchlist")
    suspend fun addToWatchlist(
        @Path("account_id") accountId: String, @Body jsonObject: JsonObject
    ): Response<Watchlist>

    @Headers("Authorization: $BEARER_TOKEN")
    @GET("account/{account_id}/watchlist//movies")
    suspend fun watchlist(
        @Path("account_id") accountId: String
    ): Response<Discover>

}