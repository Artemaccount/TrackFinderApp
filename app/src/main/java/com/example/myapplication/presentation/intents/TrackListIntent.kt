package com.example.myapplication.presentation.intents

sealed class TrackListIntent {

    data class GetTracksByRequest(val request: String, val callback: ()-> Unit) : TrackListIntent()
    data class GetTrackInfo(val trackId: Int) : TrackListIntent()
}