package com.example.jetcompose.presentation.viewmodel;

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
class ListViewModel @Inject constructor(private val repository: ListRepository) : ViewModel() {

    val listGenres = mutableStateOf<List<Genres>>(emptyList())
    val listDiscover =
        mutableStateOf(mutableMapOf<String, List<DiscoverResults>>())
    val listPopular: MutableStateFlow<List<DiscoverResults>> = MutableStateFlow(emptyList())
    val listMovieCrew: MutableStateFlow<List<MovieCrew>> = MutableStateFlow(emptyList())
    private val error: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            repository.getGenres().collect {
                when (it) {
                    is Result.Success<*> -> {
                        val genres = ((it.data as GenreData).genres)
                        genres.forEach { genre ->
                            getDiscoverGenres(genre.id, genre.name, genres.size)
                        }
                    }
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

    val map = mutableMapOf<String, List<DiscoverResults>>()

    fun getDiscoverGenres(genreId: String, genreName: String, size: Int) {
        viewModelScope.launch {
            repository.getDiscoverGenre(genreId = genreId).collect {
                when (it) {
                    is Result.Success<*> -> {
                        map[genreName] = (it.data as Discover).results
                        if (map.size == size)
                            listDiscover.value = map
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
