package com.example.myapplication.data.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
class SearchEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("request") val request: String,
    @ColumnInfo("trackId") val trackId: Int,
    @ColumnInfo("artistName") val artistName: String,
    @ColumnInfo("collectionName") val collectionName: String?,
    @ColumnInfo("trackName") val trackName: String,
    @ColumnInfo("artworkUrl60") val artworkUrl60: String,
)
