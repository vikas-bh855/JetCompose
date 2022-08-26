package com.example.jetcompose.list;

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetcompose.Result
import com.example.jetcompose.models.Discover
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.models.GenreData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(private val repository: ListRepository) : ViewModel() {

    val listDiscover = mutableStateOf(mutableMapOf<String, List<DiscoverResults>>())
    val listNowPlaying = mutableStateOf(listOf<DiscoverResults>())
    val listTrending: MutableStateFlow<List<DiscoverResults>> = MutableStateFlow(emptyList())
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
            repository.getTrending().collect {
                when (it) {
                    is Result.Success<*> -> listTrending.emit((it.data as Discover).results)
                    else -> error.emit("")
                }
            }
        }

        viewModelScope.launch {
            repository.getNowPlaying().collect {
                when (it) {
                    is Result.Success<*> -> {
                        listNowPlaying.value = (it.data as Discover).results
                    }
                    else -> {
                        error.emit("")
                    }
                }
            }
        }
    }

    private val map = linkedMapOf<String, List<DiscoverResults>>()
    private fun getDiscoverGenres(genreId: String, genreName: String, size: Int) {
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
}
