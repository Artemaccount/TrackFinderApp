package com.example.myapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [SearchEntity::class], version = 2)
abstract class AppDb : RoomDatabase() {
    abstract val trackDAO: TrackDao
}