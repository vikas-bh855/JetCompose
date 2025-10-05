package com.example.jetcompose.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetcompose.Result
import com.example.jetcompose.models.Discover
import com.example.jetcompose.models.DiscoverResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(val profileRepository: ProfileRepository) : ViewModel() {

    val listWatchlist: MutableStateFlow<List<DiscoverResults>> = MutableStateFlow(listOf())

    private val error: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            profileRepository.fetchWatchlist().collect {
                when (it) {
                    is Result.Success<*> -> listWatchlist.emit((it.data as Discover).results)
                    else -> error.emit("")
                }

            }
        }
    }

}