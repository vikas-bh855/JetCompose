package com.example.jetcompose.detail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetcompose.Result
import com.example.jetcompose.home.HomeRepository
import com.example.jetcompose.models.Discover
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.models.MovieCast
import com.example.jetcompose.models.MovieCrew
import com.example.jetcompose.models.Video
import com.example.jetcompose.models.Watchlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    val movieDetails = MutableStateFlow<DiscoverResults?>(null)
    val listMovieCrew: MutableStateFlow<List<MovieCrew>> = MutableStateFlow(emptyList())
    val videoUrl: MutableState<String> = mutableStateOf("")

    val listRecommendations: MutableStateFlow<List<DiscoverResults>> = MutableStateFlow(emptyList())

    val addToWatchlist: MutableStateFlow<Watchlist?> = MutableStateFlow(null)

    private val error: MutableStateFlow<String> = MutableStateFlow("")
    fun getMovieDetails(movieId: String) {
        viewModelScope.launch {
            repository.getDetails(movieId).collect {
                when (it) {
                    is Result.Success<*> -> {
                        movieDetails.emit(it.data as DiscoverResults)
                    }

                    else -> error.emit("")
                }
            }
        }
    }

    fun getMovieCredits(movieId: String) {
        viewModelScope.launch {
            repository.getMovieCredits(movieId = movieId).collect {
                when (it) {
                    is Result.Success<*> -> {
                        listMovieCrew.emit((it.data as MovieCast).cast.subList(0, 5))
                    }

                    else -> error.emit("")
                }
            }
        }
    }

    fun getVideoUrl(movieId: String) {
        viewModelScope.launch {
            repository.getVideoUrl(movieId = movieId).collect {
                when (it) {
                    is Result.Success<*> -> {
                        (it.data as Video).results.forEach {
                            if (it.type.equals("trailer", ignoreCase = true) && it.official) {
                                videoUrl.value = it.key
                                Log.d("TAG", "getVideoUrl: ${it.type}")
                                return@forEach
                            }
                        }
                    }

                    else -> error.emit("")
                }
            }
        }
    }

    fun getRecommendations(movieId: String) {
        viewModelScope.launch {
            repository.getRecommendations(movieId).collect {
                when (it) {
                    is Result.Success<*> -> {
                        val results = (it.data as Discover).results
                        listRecommendations.emit((results))
                    }

                    else -> error.emit("")
                }
            }
        }
    }

    fun addToWatchlist(movieId: String) {
        viewModelScope.launch {
            repository.addToWatchlist(movieId).collect {
                when (it) {
                    is Result.Success<*> -> {
                        val results = (it.data as Watchlist)
                        addToWatchlist.emit((results))
                    }
                    else -> error.emit("")
                }
            }
        }
    }
}
