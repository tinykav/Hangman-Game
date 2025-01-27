package com.example.madgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences


class MainActivity : AppCompatActivity() {

    private lateinit var highestScoreTextView: TextView
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton: Button = findViewById(R.id.playButton)
        highestScoreTextView = findViewById(R.id.highestScoreTextView)

        // Initialize SharedPreferences
        sharedPrefs = getSharedPreferences("game_prefs", MODE_PRIVATE)

        // Display the highest score when the activity is created
        displayHighestScore()

        playButton.setOnClickListener {
            // Start GameActivity when the play button is clicked
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayHighestScore() {
        // Retrieve the highest score from SharedPreferences
        val highestScore = sharedPrefs.getInt("highest_score", 0)

        // Update the TextView with the highest score
        highestScoreTextView.text = "Highest Score: $highestScore"
    }
}
