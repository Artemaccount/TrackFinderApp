package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.api.Results
import com.example.myapplication.data.api.TrackRepositoryApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackViewModel(val repository: TrackRepositoryApi) : ViewModel() {
    private val _data = repository.data

    val data: StateFlow<Results>
        get() = _data

    fun getTracksNew(value: String) {
        viewModelScope.launch {
            repository.getTracks(value)
        }
    }
}