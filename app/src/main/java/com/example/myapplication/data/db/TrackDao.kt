package com.example.myapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.api.model.Track

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(searchEntity: SearchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(searchEntity: List<SearchEntity>)

    @Query("SELECT * FROM tracks WHERE request = :request")
    suspend fun getTracksByRequest(request: String): List<SearchEntity>

    @Query("SELECT * FROM tracks WHERE trackId = :trackId limit 1")
    suspend fun getTrackById(trackId: Int): SearchEntity
}