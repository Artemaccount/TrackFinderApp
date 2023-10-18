package com.example.myapplication.data.api

interface TrackRepository {
    suspend fun getTracks(value: String)
}