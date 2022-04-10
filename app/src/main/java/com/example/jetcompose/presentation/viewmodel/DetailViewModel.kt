package com.example.jetcompose.presentation.viewmodel;

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetcompose.data.models.*
import com.example.jetcompose.data.repository.ListRepository
import com.example.jetcompose.data.source.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: ListRepository) : ViewModel() {

    val movieDetails = mutableStateOf<DiscoverResults?>(null)
    val listMovieCrew: MutableStateFlow<List<MovieCrew>> = MutableStateFlow(emptyList())
    val videoUrl: MutableState<String> = mutableStateOf("")

    private val error: MutableStateFlow<String> = MutableStateFlow("")
    fun getMovieDetails(movieId: String) {
        viewModelScope.launch {
            repository.getDetails(movieId).collect {
                when (it) {
                    is Result.Success<*> -> {
                        movieDetails.value = it.data as DiscoverResults
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

}
