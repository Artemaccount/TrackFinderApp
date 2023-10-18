package com.example.myapplication.data.api

import com.example.myapplication.data.db.SearchEntity
import com.example.myapplication.data.db.TrackDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TrackRepositoryApi(
    private val trackDao: TrackDao,
    private val apiService: ItunesApi
) :
    TrackRepository {
    private var results = Results(mutableListOf())
    private var _data = MutableStateFlow(results)
    val data: StateFlow<Results> = _data.asStateFlow()

    override suspend fun getTracks(value: String) {
        updateDataWithApi(value)
        updateDataWithSql(value)
    }


    private suspend fun updateDataWithApi(value: String) {
        flow<Unit> {
            var resultsFromApi = results
            try {
                resultsFromApi = apiService.getTrack(value)
                insertTracks(value, resultsFromApi.results)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            _data.value = resultsFromApi
        }.collect()
    }

    private suspend fun updateDataWithSql(value: String) {
        trackDao.getTracksByRequest(value).map { list ->
            list.map { searchItem ->
                Track(
                    searchItem.trackId,
                    searchItem.artistName,
                    searchItem.collectionName,
                    searchItem.trackName,
                    searchItem.artworkUrl60
                )
            }.toMutableList()
        }.collect {
            if (_data.value.results.isEmpty()) {
                _data.value = Results((it))
            }
        }
    }

    private suspend fun insertTracks(request: String, tracks: List<Track>) {
        withContext(Dispatchers.IO) {
            val searchList = tracks.map {
                SearchEntity(
                    0,
                    request,
                    it.trackId,
                    it.artistName,
                    it.collectionName,
                    it.trackName,
                    it.artworkUrl60
                )
            }
            trackDao.insertTrack(searchList)
        }
    }
}