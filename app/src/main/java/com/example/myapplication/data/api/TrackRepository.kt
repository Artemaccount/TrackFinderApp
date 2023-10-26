package com.example.myapplication.data.api

import com.example.myapplication.data.api.model.Results
import com.example.myapplication.data.api.model.Track
import kotlinx.coroutines.flow.StateFlow

interface TrackRepository {
    suspend fun getTracks(value: String)
    suspend fun getTrackById(trackId: Int): Track
    val data: StateFlow<DataState<Results>>
}