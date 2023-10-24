package com.example.myapplication.data.api

import kotlinx.coroutines.flow.StateFlow

interface TrackRepository {
    suspend fun getTracks(value: String)
    val data: StateFlow<Results>
    val state: StateFlow<ApiState>
}