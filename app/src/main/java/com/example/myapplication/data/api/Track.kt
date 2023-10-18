package com.example.myapplication.data.api

data class Track(
    val trackId: Int,
    val artistName: String,
    val collectionName: String?,
    val trackName: String,
    val artworkUrl60: String,
)
