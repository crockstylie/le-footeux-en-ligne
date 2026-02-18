package fr.antoinehory.lefooteuxenligne.domain.engine

import fr.antoinehory.lefooteuxenligne.domain.model.GameAction
import fr.antoinehory.lefooteuxenligne.domain.model.GameCard
import fr.antoinehory.lefooteuxenligne.domain.model.GameState
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardType
import fr.antoinehory.lefooteuxenligne.domain.model.enums.Direction
import fr.antoinehory.lefooteuxenligne.domain.model.enums.GamePhase

/**
 * Manages the game's logic, state transitions, and rules enforcement.
 *
 * @property GRID_COLUMNS The number of columns on the game board.
 * @property GRID_ROWS The number of rows on the game board.
 */
class GameEngine(
    private val GRID_COLUMNS: Int = 12,
    private val GRID_ROWS: Int = 18
) {
    private val drawPile: MutableList<GameCard> = mutableListOf()
    private val discardPile: MutableList<GameCard> = mutableListOf()

    /**
     * Initializes a new game, creating and shuffling a fresh deck.
     *
     * @return The initial [GameState] ready for the first kick-off.
     */
    fun initializeNewGame(): GameState {
        drawPile.clear()
        drawPile.addAll(DeckFactory.createDeck().shuffled())
        discardPile.clear()

        val player1Hand = (1..8).mapNotNull { drawPile.takeFirst() }
        val player2Hand = (1..8).mapNotNull { drawPile.takeFirst() }

        return GameState(
            gamePhase = GamePhase.AWAITING_KICKOFF,
            currentPlayerId = "Player 1",
            playerHands = mapOf("Player 1" to player1Hand, "Player 2" to player2Hand),
            drawPile = drawPile.toList(),
            discardPile = discardPile.toList(),
            message = "Player 1 to kick-off."
        )
    }

    /**
     * Handles the primary action of a player playing a card from their hand.
     *
     * @param currentState The current state of the game.
     * @param cardId The ID of the card being played.
     * @return The updated [GameState] after the card has been played.
     */
    fun playCard(currentState: GameState, cardId: String): GameState {
        val playerId = currentState.currentPlayerId
        val playerHand = currentState.playerHands[playerId] ?: return currentState
        val cardToPlay = playerHand.find { it.id.toString() == cardId }
            ?: return currentState.copy(message = "Card not found in hand.")

        val validationResult = validateMove(currentState, cardToPlay)
        if (!validationResult.isValid) {
            return currentState.copy(message = validationResult.errorMessage)
        }

        var newBallPosition = currentState.ballPosition
        // TODO: Implement logic for choices and sequences
        (cardToPlay.actions.firstOrNull() as? GameAction.Move)?.let {
            newBallPosition = applyMove(newBallPosition, it, playerId)
        }

        val newHand = playerHand.filterNot { it.id == cardToPlay.id }
        discardPile.add(cardToPlay)

        val cardDrawn = drawPile.takeFirst()
        val finalHand = if (cardDrawn != null) newHand + cardDrawn else newHand

        // TODO: Check for goals, out of bounds, etc. to change GamePhase

        return currentState.copy(
            ballPosition = newBallPosition,
            playerHands = currentState.playerHands + (playerId to finalHand),
            drawPile = drawPile.toList(),
            discardPile = discardPile.toList(),
            currentPlayerId = if (playerId == "Player 1") "Player 2" else "Player 1",
            gamePhase = GamePhase.PLAYER_TURN,
            message = "Player ${if (playerId == "Player 1") "2" else "1"}'s turn."
        )
    }

    private fun validateMove(currentState: GameState, card: GameCard): ValidationResult {
        if (currentState.gamePhase == GamePhase.AWAITING_KICKOFF && card.type != CardType.ATTACKER) {
            return ValidationResult(false, "Must use an ATTACKER card for kick-off.")
        }
        return ValidationResult(true)
    }

    private fun applyMove(currentPos: Pair<Int, Int>, move: GameAction.Move, playerId: String): Pair<Int, Int> {
        val forward = if (playerId == "Player 1") 1 else -1
        val (dx, dy) = when (move.direction) {
            Direction.FORWARD -> 0 to move.distance * forward
            Direction.FORWARD_RIGHT -> move.distance to move.distance * forward
            Direction.RIGHT -> move.distance to 0
            Direction.BACKWARD_RIGHT -> move.distance to -move.distance * forward
            Direction.BACKWARD -> 0 to -move.distance * forward
            Direction.BACKWARD_LEFT -> -move.distance to -move.distance * forward
            Direction.LEFT -> -move.distance to 0
            Direction.FORWARD_LEFT -> -move.distance to move.distance * forward
        }
        return (currentPos.first + dx).coerceIn(0, GRID_COLUMNS - 1) to
               (currentPos.second + dy).coerceIn(0, GRID_ROWS - 1)
    }

    private fun <T> MutableList<T>.takeFirst(): T? = if (isNotEmpty()) removeAt(0) else null

    private data class ValidationResult(val isValid: Boolean, val errorMessage: String? = null)
}
