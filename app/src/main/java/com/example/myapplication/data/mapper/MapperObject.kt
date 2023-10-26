package com.example.myapplication.data.mapper

import com.example.myapplication.data.api.model.Track
import com.example.myapplication.data.db.SearchEntity
import com.example.myapplication.data.mapper.MapperObject.toTrack

object MapperObject {
    fun SearchEntity.toTrack(): Track = Track(
        this.trackId,
        this.artistName,
        this.collectionName,
        this.trackName,
        this.artworkUrl60,
        this.artworkUrl100
    )

    fun Track.toSearchEntity(request: String): SearchEntity = SearchEntity(
        0,
        request,
        this.trackId,
        this.artistName,
        this.collectionName,
        this.trackName,
        this.artworkUrl60,
        this.artworkUrl100
    )
}