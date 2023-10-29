package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.api.TrackRepository
import com.example.myapplication.data.api.model.DataState
import com.example.myapplication.data.api.model.Loading
import com.example.myapplication.data.api.model.NoDataFound
import com.example.myapplication.data.api.model.Result
import com.example.myapplication.data.api.model.Results
import com.example.myapplication.presentation.intents.TrackListIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackListViewModel @Inject constructor(
    val repository: TrackRepository
) : ViewModel() {

    val userIntent = Channel<TrackListIntent>(Channel.UNLIMITED)

    private val _state: MutableStateFlow<DataState<Results>> =
        MutableStateFlow(Result.Success(Results(emptyList())))
    val state: StateFlow<DataState<Results>>
        get() = _state

    init {
        handleIntent()
    }


    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow()
                .filter { (it as? TrackListIntent.GetTracksByRequest)!!.request.isNotEmpty() }
                .distinctUntilChanged { old, new ->
                    val oldRequest = (old as? TrackListIntent.GetTracksByRequest)!!.request
                    val newRequest = (new as? TrackListIntent.GetTracksByRequest)!!.request
                    oldRequest == newRequest
                }
                .onEach {
                    (it as? TrackListIntent.GetTracksByRequest)!!.callback.invoke()
                }
                .debounce(600)
                .collect {
                    when (it) {
                        is TrackListIntent.GetTracksByRequest -> {
                            getTrackListByRequest(it.request)
                        }

                        else -> {
                            throw IllegalArgumentException("not a state")
                        }
                    }
                }
        }
    }


    private suspend fun getTrackListByRequest(request: String) {
        _state.value = Loading
        _state.value = try {
            repository.getDataByApi(request)
        } catch (e: Exception) {
            Result.Error(e)
        }

        if (_state.value is NoDataFound || _state.value is Result.Error) {
            _state.value = repository.getDataBySql(request)
        }
    }
}