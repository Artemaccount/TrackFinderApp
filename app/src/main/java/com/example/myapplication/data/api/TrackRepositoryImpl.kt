package com.example.myapplication.data.api

import com.example.myapplication.data.api.model.DataState
import com.example.myapplication.data.api.model.Loading
import com.example.myapplication.data.api.model.NoDataFound
import com.example.myapplication.data.api.model.Result
import com.example.myapplication.data.api.model.Results
import com.example.myapplication.data.api.model.Track
import com.example.myapplication.data.db.SearchEntity
import com.example.myapplication.data.db.TrackDao
import com.example.myapplication.data.mapper.MapperObject.toSearchEntity
import com.example.myapplication.data.mapper.MapperObject.toTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val trackDao: TrackDao,
    private val apiService: ItunesApi
) : TrackRepository {

    override suspend fun getTrackById(trackId: Int): Track {
        return trackDao.getTrackById(trackId).toTrack()
    }

    override suspend fun getDataByApi(request: String): DataState<Results> {
        val resultsFromApi = apiService.getTrack(request)
        return if (resultsFromApi.results.isEmpty()) {
            NoDataFound
        } else {
            insertTracks(request, resultsFromApi.results)
            Result.Success(resultsFromApi)
        }
    }

    override suspend fun getDataBySql(request: String): DataState<Results> {
        val tracks = try {
            trackDao.getTracksByRequest(request)
        } catch (e: Exception) {
            emptyList()
        }
        return if (tracks.isEmpty()) {
            NoDataFound
        } else {
            Result.Success(Results(tracks.map { s -> s.toTrack() }))
        }
    }

    private suspend fun insertTracks(request: String, tracks: List<Track>) {
        trackDao.insertTrack(tracks.map { it.toSearchEntity(request) })
    }
}