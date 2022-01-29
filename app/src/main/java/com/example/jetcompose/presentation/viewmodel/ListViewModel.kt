package com.example.jetcompose.presentation.viewmodel;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetcompose.data.models.*
import com.example.jetcompose.data.repository.ListRepository
import com.example.jetcompose.data.source.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(private val repository: ListRepository) : ViewModel() {

    val listGenres: MutableStateFlow<List<Genres>> = MutableStateFlow(emptyList())
    val listDiscover: MutableStateFlow<List<DiscoverResults>> = MutableStateFlow(emptyList())
    val listPopular: MutableStateFlow<List<DiscoverResults>> = MutableStateFlow(emptyList())
    val listMovieCrew: MutableStateFlow<List<MovieCrew>> = MutableStateFlow(emptyList())
    private val error: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            repository.getGenres().collect {
                when (it) {
                    is Result.Success<*> -> listGenres.emit((it.data as GenreData).genres)
                    else -> error.emit("")
                }
            }
            repository.getPopulars().collect {
                when (it) {
                    is Result.Success<*> -> listPopular.emit((it.data as Discover).results)
                    else -> error.emit("")
                }
            }
        }
    }

    fun getDiscoverGenres(genreId: String) {
        viewModelScope.launch {
            repository.getDiscoverGenre(genreId = genreId).collect {
                when (it) {
                    is Result.Success<*> -> {
                        listDiscover.emit((it.data as Discover).results)
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
                        listMovieCrew.emit((it.data as MovieCast).cast)
                    }
                    else -> error.emit("")
                }
            }
        }
    }

}
