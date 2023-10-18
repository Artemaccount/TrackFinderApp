package com.example.myapplication.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search")
    suspend fun getTrack(
        @Query("term") term: String,
        @Query("media") media: String = "music"
    ): Results
}