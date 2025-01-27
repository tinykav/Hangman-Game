package com.example.madgame

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.madgame.GameViewModelFactory


class GameActivity : AppCompatActivity() {
    private val gameViewModel: GameViewModel by viewModels {
        GameViewModelFactory(getSharedPreferences("game_prefs", MODE_PRIVATE))
    }

    private lateinit var wordTextView: TextView
    private lateinit var lettersUsedTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var gameLostTextView: TextView
    private lateinit var gameWonTextView: TextView
    private lateinit var newGameButton: Button
    private lateinit var lettersLayout: ConstraintLayout
    private lateinit var currentScoreTextView: TextView
    private lateinit var highestScoreTextView: TextView
    private lateinit var imageView2: ImageView
    private lateinit var gameManager: GameManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Initialize UI components
        imageView = findViewById(R.id.imageView)
        wordTextView = findViewById(R.id.wordTextView)
        lettersUsedTextView = findViewById(R.id.lettersUsedTextView)
        gameLostTextView = findViewById(R.id.gameLostTextView)
        gameWonTextView = findViewById(R.id.gameWonTextView)
        newGameButton = findViewById(R.id.newGameButton)
        lettersLayout = findViewById(R.id.lettersLayout)
        currentScoreTextView = findViewById(R.id.currentScoreTextView)
        highestScoreTextView = findViewById(R.id.highestScoreTextView)
        imageView2 = findViewById(R.id.imageView2)

        // Initialize gameManager instance
        gameManager = GameManager()

        // Observe current score and highest score using LiveData from the ViewModel
        gameViewModel.currentScore.observe(this) { score ->
            currentScoreTextView.text = "Current Score: $score"
        }

        gameViewModel.highestScore.observe(this) { score ->
            highestScoreTextView.text = "Highest Score: $score"
        }

        // Set listeners
        imageView2.setOnClickListener {
            navigateToSettings()
        }

        newGameButton.setOnClickListener {
            startNewGame()
        }

        // Start a new game initially
        startNewGame()

        // Set up listeners for letter views
        setupLetterListeners()
    }

    private fun setupLetterListeners() {
        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    val gameState = gameManager.play(letterView.text[0])
                    updateUI(gameState)
                    letterView.visibility = View.GONE
                }
            }
        }
    }

    private fun updateUI(gameState: GameState) {
        when (gameState) {
            is GameState.Lost -> showGameLost(gameState.wordToGuess)
            is GameState.Running -> {
                wordTextView.text = gameState.underscoreWord
                lettersUsedTextView.text = "Letters used: ${gameState.lettersUsed}"
                imageView.setImageDrawable(ContextCompat.getDrawable(this, gameState.drawable))

                // Update current score and check for highest score
                val currentScore = gameManager.calculateScore(gameState)
                currentScoreTextView.text = "Current Score: $currentScore"
                updateHighestScore(currentScore)
            }
            is GameState.Won -> showGameWon(gameState.wordToGuess)
        }
    }

    private fun showGameLost(wordToGuess: String) {
        wordTextView.text = wordToGuess
        gameLostTextView.visibility = View.VISIBLE
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.game7))
        lettersLayout.visibility = View.GONE

        val score = gameManager.calculateScore(GameState.Lost(wordToGuess))
        updateHighestScore(score)
    }

    private fun showGameWon(wordToGuess: String) {
        wordTextView.text = wordToGuess
        gameWonTextView.visibility = View.VISIBLE
        lettersLayout.visibility = View.GONE

        // Calculate and update the current score
        val score = gameManager.calculateScore(GameState.Won(wordToGuess))
        currentScoreTextView.text = "Current Score: $score"
        updateHighestScore(score)
    }

    private fun startNewGame() {
        gameLostTextView.visibility = View.GONE
        gameWonTextView.visibility = View.GONE
        val gameState = gameManager.startNewGame()
        lettersLayout.visibility = View.VISIBLE
        lettersLayout.children.forEach { letterView ->
            letterView.visibility = View.VISIBLE
        }
        updateUI(gameState)
    }

    private fun updateHighestScore(currentScore: Int) {
        gameViewModel.updateHighestScore(currentScore)
    }

    private fun navigateToSettings() {
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }
}
