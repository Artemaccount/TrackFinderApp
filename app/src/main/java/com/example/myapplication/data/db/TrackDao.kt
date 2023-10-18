package com.example.myapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(searchEntity: SearchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(searchEntity: List<SearchEntity>)

    @Query("SELECT * FROM tracks WHERE request = :request")
    fun getTracksByRequest(request: String): Flow<List<SearchEntity>>
}