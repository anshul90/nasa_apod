package com.example.android.nasa_apod.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android.nasa_apod.di.RoomModule
import com.example.android.nasa_apod.model.FavoritesEntity
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(val context: Application) : AndroidViewModel(context) {

    fun loadData(): List<FavoritesEntity> {
        return RoomModule.provideDatabase(context.applicationContext).favoritesListDao().getAllFavorites(true)
    }
}