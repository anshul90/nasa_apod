package com.example.android.nasa_apod.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.nasa_apod.domain.dao.ApodDao
import com.example.android.nasa_apod.domain.dao.FavoritesDao
import com.example.android.nasa_apod.model.ApodEntity
import com.example.android.nasa_apod.model.FavoritesEntity

@Database(entities = [ApodEntity::class, FavoritesEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun apodListDao(): ApodDao
    abstract fun favoritesListDao(): FavoritesDao
}