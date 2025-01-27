package com.example.madgame

import kotlin.random.Random

class GameManager {
    private var lettersUsed = ""
    private var underscoreWord = ""
    private var wordToGuess = ""
    private var currentTries = 0
    private var drawable = R.drawable.game0

    fun startNewGame(): GameState {
        lettersUsed = ""
        currentTries = 0
        drawable = R.drawable.game0

        val randomIndex = Random.nextInt(GameConstants.words.size)
        wordToGuess = GameConstants.words[randomIndex]
        generateUnderscores(wordToGuess)

        return getGameState()
    }

    fun generateUnderscores(word: String) {
        val sb = StringBuilder()
        word.forEach { char ->
            if (char == '/') {
                sb.append('/')
            } else {
                sb.append("_")
            }
        }
        underscoreWord = sb.toString()
    }

    fun play(letter: Char): GameState {
        if (lettersUsed.contains(letter)) {
            return GameState.Running(lettersUsed, underscoreWord, drawable)
        }

        lettersUsed += letter
        val indexes = mutableListOf<Int>()

        wordToGuess.forEachIndexed { index, char ->
            if (char.equals(letter, ignoreCase = true)) {
                indexes.add(index)
            }
        }

        val newUnderscoreWord = StringBuilder(underscoreWord)
        indexes.forEach { index ->
            newUnderscoreWord.setCharAt(index, letter)
        }
        underscoreWord = newUnderscoreWord.toString()

        if (indexes.isEmpty()) {
            currentTries++
        }

        return getGameState()
    }

    private fun getHangmanDrawable(): Int {
        return when (currentTries) {
            0 -> R.drawable.game0
            1 -> R.drawable.game1
            2 -> R.drawable.game2
            3 -> R.drawable.game3
            4 -> R.drawable.game4
            5 -> R.drawable.game5
            6 -> R.drawable.game6
            else -> R.drawable.game7
        }
    }

    private fun getGameState(): GameState {
        if (underscoreWord.equals(wordToGuess, ignoreCase = true)) {
            return GameState.Won(wordToGuess)
        }

        if (currentTries >= 7) {
            return GameState.Lost(wordToGuess)
        }

        drawable = getHangmanDrawable()
        return GameState.Running(lettersUsed, underscoreWord, drawable)
    }

    fun calculateScore(gameState: GameState): Int {
        var score = 0

        // Calculate score based on the game state
        when (gameState) {
            is GameState.Won -> {
                // Award points for winning
                score += 100 // Bonus points for winning
                score += (7 - currentTries) * 10 // Points for remaining tries
            }
            is GameState.Lost -> {
                // Deduct points for losing
                score -= 50
            }
            is GameState.Running -> {
                // Award points for each correct guess made
                score += lettersUsed.filter { wordToGuess.contains(it, ignoreCase = true) }.length * 10
            }
        }
        println("calculateScore: $score for game state: $gameState")
        return score
    }
}
