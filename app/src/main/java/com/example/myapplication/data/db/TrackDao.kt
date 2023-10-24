package com.example.myapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(searchEntity: SearchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(searchEntity: List<SearchEntity>)

    @Query("SELECT * FROM tracks WHERE request = :request")
    suspend fun getTracksByRequest(request: String): List<SearchEntity>
}