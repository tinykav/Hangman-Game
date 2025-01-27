package com.example.madgame

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModelFactory(private val sharedPrefs: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel is GameViewModel
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            // Create and return an instance of GameViewModel with shared preferences
            return GameViewModel(sharedPrefs) as T
        }
        // Throw an exception if the requested ViewModel is not recognized
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
