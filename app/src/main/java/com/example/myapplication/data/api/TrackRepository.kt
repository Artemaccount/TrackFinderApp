package com.example.myapplication.data.api

import com.example.myapplication.data.api.model.DataState
import com.example.myapplication.data.api.model.Results
import com.example.myapplication.data.api.model.Track
import kotlinx.coroutines.flow.StateFlow

interface TrackRepository {
    suspend fun getTrackById(trackId: Int): Track

    suspend fun getDataByApi(request: String): DataState<Results>
    suspend fun getDataBySql(request: String): DataState<Results>


}