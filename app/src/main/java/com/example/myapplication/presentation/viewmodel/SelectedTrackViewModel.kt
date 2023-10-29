package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.api.TrackRepository
import com.example.myapplication.data.api.model.Track
import com.example.myapplication.presentation.intents.TrackListIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectedTrackViewModel @Inject constructor(
    val repository: TrackRepository
) : ViewModel() {

    val userIntent = Channel<TrackListIntent>(Channel.UNLIMITED)

    private val _state: MutableStateFlow<Track> = MutableStateFlow(
        Track(-1, "", "", "", "", "")
    )
    val state: StateFlow<Track>
        get() = _state

    init {
        handleIntent()
    }


    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow()
                .collect {
                    when (it) {
                        is TrackListIntent.GetTrackInfo -> {
                            getTrackByTrackId(it.trackId)
                        }

                        else -> {
                            throw IllegalArgumentException("not a state")
                        }
                    }
                }
        }
    }


    private suspend fun getTrackByTrackId(trackId: Int) {
        try {
            _state.value = repository.getTrackById(trackId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}