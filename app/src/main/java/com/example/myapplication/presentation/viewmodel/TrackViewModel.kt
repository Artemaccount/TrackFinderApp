package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.api.DataState
import com.example.myapplication.data.api.Result
import com.example.myapplication.data.api.model.Results
import com.example.myapplication.data.api.TrackRepository
import com.example.myapplication.data.api.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackViewModel @Inject constructor(val repository: TrackRepository) : ViewModel() {
    val data: StateFlow<DataState<Results>>
        get() = repository.data

    val input by lazy {
        MutableStateFlow("")
    }

    suspend fun getTrackByTrackId(trackId: Int): Track {
        return repository.getTrackById(trackId)
    }

    init {
        this.input.debounce(600).onEach {
            if (it.isNotEmpty()) {
                repository.getTracks(it)
            }
        }.launchIn(viewModelScope)
    }
}