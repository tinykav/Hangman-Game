package com.example.madgame

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel(private val sharedPrefs: SharedPreferences) : ViewModel() {
    private val _currentScore = MutableLiveData<Int>()
    val currentScore: LiveData<Int> get() = _currentScore

    private val _highestScore = MutableLiveData<Int>()
    val highestScore: LiveData<Int> get() = _highestScore

    init {
        // Load the initial highest score from shared preferences
        _highestScore.value = sharedPrefs.getInt("highest_score", 0)
    }

    // Update the current score and notify observers
    fun updateCurrentScore(score: Int) {
        _currentScore.value = score
    }

    // Update the highest score and notify observers
    fun updateHighestScore(score: Int) {
        val currentHighestScore = highestScore.value ?: 0
        if (score > currentHighestScore) {
            _highestScore.value = score

            // Save the new highest score in shared preferences
            sharedPrefs.edit().putInt("highest_score", score).apply()
        }
    }
}
