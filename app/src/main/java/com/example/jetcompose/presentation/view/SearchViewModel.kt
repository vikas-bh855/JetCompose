package com.example.jetcompose.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {

    fun getSearch(searchText: String) = Pager(PagingConfig(pageSize = 6), pagingSourceFactory = {
        SearchSource(searchText, repository)
    }).flow.cachedIn(viewModelScope)

    companion object {
        private const val TAG = "SearchViewModel"
    }

}