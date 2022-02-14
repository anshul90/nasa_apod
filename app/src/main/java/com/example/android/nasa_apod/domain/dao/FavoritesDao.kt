package com.example.android.nasa_apod.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.nasa_apod.model.FavoritesEntity

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorites where isFav = :value ORDER BY favorites.date DESC")
    fun getAllFavorites(value: Boolean): List<FavoritesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavorites(item: FavoritesEntity)
}