package com.example.jetcompose.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jetcompose.data.repository.GenreDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class GenreDetailViewModel @Inject constructor(val genreDetailRepository: GenreDetailRepository) :
    ViewModel() {
}