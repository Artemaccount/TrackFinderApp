package com.example.myapplication.data.api

import com.example.myapplication.data.api.model.Results
import com.example.myapplication.data.api.model.Track
import com.example.myapplication.data.db.TrackDao
import com.example.myapplication.data.exceptions.NoValueFoundException
import com.example.myapplication.data.mapper.MapperObject.toSearchEntity
import com.example.myapplication.data.mapper.MapperObject.toTrack
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val trackDao: TrackDao,
    private val apiService: ItunesApi
) : TrackRepository {

    private val _data: MutableStateFlow<DataState<Results>> =
        MutableStateFlow(Result.Success(Results(emptyList())))
    override val data get() = _data

    override suspend fun getTracks(value: String) {
        try {
            updateDataWithApi(value)
        } catch (e: Throwable) {
            e.printStackTrace()
            updateDataWithSql(value)
        }
    }


    override suspend fun getTrackById(trackId: Int): Track {
        return trackDao.getTrackById(trackId).toTrack()
    }

    private suspend fun updateDataWithApi(value: String) {
        _data.value = Loading
        val resultsFromApi = apiService.getTrack(value)
        if (resultsFromApi.results.isEmpty()) {
            throw NoValueFoundException("no data found by api")
        } else {
            _data.value = Result.Success(resultsFromApi)
            insertTracks(value, resultsFromApi.results)
        }
    }

    private suspend fun updateDataWithSql(value: String) {
        trackDao.getTracksByRequest(value).also { list ->
            if (list.isEmpty()) {
                data.value = Result.Error(NoValueFoundException("no data found by sql"))
            } else {
                list.map {
                    it.toTrack()
                }.also {
                    _data.value = Result.Success(Results(it))
                }
            }
        }
    }

    private suspend fun insertTracks(request: String, tracks: List<Track>) {
        trackDao.insertTrack(tracks.map { it.toSearchEntity(request) })
    }
}