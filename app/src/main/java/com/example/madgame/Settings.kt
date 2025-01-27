package com.example.madgame

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView


class Settings : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var resetScoreButton: Button
    private lateinit var highestScoreTextViewSettings: TextView
    private lateinit var imageView3: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize SharedPreferences
        sharedPrefs = getSharedPreferences("game_prefs", MODE_PRIVATE)

        // Initialize the reset score button and highest score TextView
        resetScoreButton = findViewById(R.id.resetScoreButton)
        highestScoreTextViewSettings = findViewById(R.id.highestScoreTextViewSettings)
        imageView3 = findViewById(R.id.imageView3)

        // Set an OnClickListener for the reset score button
        resetScoreButton.setOnClickListener {
            resetHighestScore()
        }
        imageView3.setOnClickListener {
            navigateToGameActivity()
        }

        // Display the highest score in Settings activity
        displayHighestScore()
    }

    private fun resetHighestScore() {
        // Reset the highest score in SharedPreferences to zero
        sharedPrefs.edit().putInt("highest_score", 0).apply()

        // Update the highest score TextView in Settings activity
        highestScoreTextViewSettings.text = "Highest Score: 0"
        println("Highest score reset to zero")
    }

    private fun displayHighestScore() {
        // Retrieve the highest score from SharedPreferences
        val highestScore = sharedPrefs.getInt("highest_score", 0)

        // Display the highest score in the Settings activity
        highestScoreTextViewSettings.text = "Highest Score: $highestScore"
    }

    private fun navigateToGameActivity() {
        // Create an intent to start the new activity
        val intent = Intent(this, GameActivity::class.java)

        // Start the new activity
        startActivity(intent)
    }
}
