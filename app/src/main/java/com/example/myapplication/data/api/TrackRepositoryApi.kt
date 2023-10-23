package com.example.myapplication.data.api

import com.example.myapplication.data.exceptions.NoValueFoundException
import com.example.myapplication.data.db.TrackDao
import com.example.myapplication.data.mapper.MapperObject.toSearchEntity
import com.example.myapplication.data.mapper.MapperObject.toTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TrackRepositoryApi @Inject constructor(
    private val trackDao: TrackDao,
    private val apiService: ItunesApi
) : TrackRepository {
    private val _state = MutableStateFlow(ApiState())
    override val state: StateFlow<ApiState>
        get() = _state.asStateFlow()

    private var _data = MutableStateFlow(Results(listOf()))
    override val data: StateFlow<Results>
        get() = _data.asStateFlow()

    override suspend fun getTracks(value: String) {
        try {
            updateDataWithApi(value)
        } catch (e: Throwable) {
            e.printStackTrace()
            updateDataWithSql(value)
        }
    }

    private suspend fun updateDataWithApi(value: String) {
        _state.value = ApiState(State.Loading)
        val resultsFromApi = apiService.getTrack(value)
        if (resultsFromApi.results.isEmpty()) {
            throw NoValueFoundException("no data found by api")
        } else {
            _state.value = ApiState(State.Success)
            _data.value = resultsFromApi
            insertTracks(value, resultsFromApi.results)
        }
    }

    private suspend fun updateDataWithSql(value: String) {
        trackDao.getTracksByRequest(value).also { list ->
            if (list.isEmpty()) {
                _state.value = ApiState(State.Error)
            } else {
                _data.value = Results(list.map { it.toTrack() })
                _state.value = ApiState(State.Success)
            }
        }
    }

    private suspend fun insertTracks(request: String, tracks: List<Track>) {
        trackDao.insertTrack(tracks.map { it.toSearchEntity(request) })
    }
}