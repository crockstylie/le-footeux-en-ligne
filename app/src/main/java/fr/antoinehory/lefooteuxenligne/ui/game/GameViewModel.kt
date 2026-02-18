package fr.antoinehory.lefooteuxenligne.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.antoinehory.lefooteuxenligne.domain.engine.GameEngine
import fr.antoinehory.lefooteuxenligne.domain.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the main game screen, acting as a bridge between the UI and the GameEngine.
 */
class GameViewModel : ViewModel() {

    private val gameEngine = GameEngine()

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        startNewGame()
    }

    /**
     * Starts a new game by initializing the state from the GameEngine.
     */
    fun startNewGame() {
        viewModelScope.launch {
            _gameState.value = gameEngine.initializeNewGame()
        }
    }

    /**
     * Handles the user action of playing a card.
     * It delegates the logic to the GameEngine and updates the UI state.
     *
     * @param cardId The unique identifier of the card to be played.
     */
    fun onCardPlayed(cardId: String) {
        viewModelScope.launch {
            val currentState = _gameState.value
            val newState = gameEngine.playCard(currentState, cardId)
            _gameState.value = newState
        }
    }

    /**
     * Placeholder for handling cell clicks on the board.
     * This will be used for actions like choosing a destination for a throw-in or placing the ball.
     */
    fun onCellClicked(x: Int, y: Int) {
        // Logic to be implemented based on game phase
    }
}
