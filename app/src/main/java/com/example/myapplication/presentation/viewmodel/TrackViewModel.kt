package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.api.ApiState
import com.example.myapplication.data.api.Results
import com.example.myapplication.data.api.TrackRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackViewModel @Inject constructor(val repository: TrackRepository) : ViewModel() {
    val data: StateFlow<Results>
        get() = repository.data
    val state: StateFlow<ApiState>
        get() = repository.state

    fun getTracks(value: String) {
        viewModelScope.launch {
            repository.getTracks(value)
        }
    }
}