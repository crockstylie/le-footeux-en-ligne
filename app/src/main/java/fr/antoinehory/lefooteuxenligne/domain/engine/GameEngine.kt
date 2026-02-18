package fr.antoinehory.lefooteuxenligne.domain.engine

import fr.antoinehory.lefooteuxenligne.domain.model.GameAction
import fr.antoinehory.lefooteuxenligne.domain.model.GameCard
import fr.antoinehory.lefooteuxenligne.domain.model.GameState
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardType
import fr.antoinehory.lefooteuxenligne.domain.model.enums.Direction
import fr.antoinehory.lefooteuxenligne.domain.model.enums.FieldZone
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
    private val GOAL_RANGE = 4..7
    private val PLAYER_1_GOAL_ROW = 0 // Player 2 scores here
    private val PLAYER_2_GOAL_ROW = 17 // Player 1 scores here

    /**
     * Initializes a new game, creating and shuffling a fresh deck.
     *
     * @return The initial [GameState] ready for the first kick-off.
     */
    fun initializeNewGame(): GameState {
        val deck = DeckFactory.createDeck().shuffled()
        val player1Hand = deck.take(8)
        val player2Hand = deck.drop(8).take(8)
        val drawPile = deck.drop(16)

        return GameState(
            gamePhase = GamePhase.AWAITING_KICKOFF,
            currentPlayerId = "Player 1",
            playerHands = mapOf("Player 1" to player1Hand, "Player 2" to player2Hand),
            drawPile = drawPile,
            discardPile = emptyList(),
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

        val newHand = playerHand - cardToPlay
        val newDiscardPile = currentState.discardPile + cardToPlay
        val (newDrawPile, cardDrawn) = currentState.drawPile.let { it.drop(1) to it.firstOrNull() }
        val finalHand = if (cardDrawn != null) newHand + cardDrawn else newHand

        // TODO: Implement logic for choices and sequences
        (cardToPlay.actions.firstOrNull() as? GameAction.Move)?.let {
            val newBallPosition = applyMove(currentState.ballPosition, it, playerId)

            // Check for a goal
            val scorer = checkForGoal(newBallPosition)
            if (scorer != null) {
                val newScore = currentState.score.toMutableMap()
                newScore[scorer] = (newScore[scorer] ?: 0) + 1
                val concedingPlayer = if (scorer == "Player 1") "Player 2" else "Player 1"

                return currentState.copy(
                    score = newScore,
                    ballPosition = Pair(GRID_COLUMNS / 2, GRID_ROWS / 2), // Reset ball to center
                    playerHands = currentState.playerHands + (playerId to finalHand),
                    drawPile = newDrawPile,
                    discardPile = newDiscardPile,
                    currentPlayerId = concedingPlayer, // Conceding player kicks off
                    gamePhase = GamePhase.AWAITING_KICKOFF,
                    message = "GOAL for $scorer! ${newScore["Player 1"]} - ${newScore["Player 2"]}"
                )
            }
            
            val nextPlayerId = if (playerId == "Player 1") "Player 2" else "Player 1"
            return currentState.copy(
                ballPosition = newBallPosition,
                playerHands = currentState.playerHands + (playerId to finalHand),
                drawPile = newDrawPile,
                discardPile = newDiscardPile,
                currentPlayerId = nextPlayerId,
                gamePhase = GamePhase.PLAYER_TURN,
                message = "Player $nextPlayerId's turn."
            )
        }

        // If card has no move action, just pass the turn
        val nextPlayerId = if (playerId == "Player 1") "Player 2" else "Player 1"
        return currentState.copy(
            playerHands = currentState.playerHands + (playerId to finalHand),
            drawPile = newDrawPile,
            discardPile = newDiscardPile,
            currentPlayerId = nextPlayerId,
            gamePhase = GamePhase.PLAYER_TURN,
            message = "Player $nextPlayerId's turn."
        )
    }

    private fun validateMove(currentState: GameState, card: GameCard): ValidationResult {
        if (currentState.gamePhase == GamePhase.AWAITING_KICKOFF && card.type != CardType.ATTACKER) {
            return ValidationResult(false, "Must use an ATTACKER card for kick-off.")
        }

        val zone = getZoneForPosition(currentState.ballPosition)
        return when (card.type) {
            CardType.PENALTY -> if (zone == FieldZone.PENALTY_AREA) ValidationResult(true) else ValidationResult(false, "Penalty can only be played in the penalty area.")
            CardType.CORNER -> if (zone == FieldZone.CORNER_ARC) ValidationResult(true) else ValidationResult(false, "Corner can only be played from a corner arc.")
            else -> ValidationResult(true) // Other cards are valid for now
        }
    }

    private fun getZoneForPosition(position: Pair<Int, Int>): FieldZone {
        val (col, row) = position

        // Player 1's side (bottom of the grid)
        if (row >= GRID_ROWS - 4) { // Penalty Area
            if (col >= 4 && col <= 7) return FieldZone.PENALTY_AREA
        }
        // Player 2's side (top of the grid)
        if (row <= 3) { // Penalty Area
            if (col >= 4 && col <= 7) return FieldZone.PENALTY_AREA
        }

        // Corner Arcs
        if ((row == 0 || row == GRID_ROWS - 1) && (col == 0 || col == GRID_COLUMNS - 1)) {
            return FieldZone.CORNER_ARC
        }

        // More zones can be defined here

        return FieldZone.CENTER_CIRCLE // Default zone for now
    }

    private fun applyMove(currentPos: Pair<Int, Int>, move: GameAction.Move, playerId: String): Pair<Int, Int> {
        val forward = if (playerId == "Player 1") -1 else 1 // Player 1 moves up (decreasing row index)
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

    private fun checkForGoal(ballPosition: Pair<Int, Int>): String? {
        val (col, row) = ballPosition
        return when {
            row == PLAYER_1_GOAL_ROW && col in GOAL_RANGE -> "Player 2"
            row == PLAYER_2_GOAL_ROW && col in GOAL_RANGE -> "Player 1"
            else -> null
        }
    }

    private data class ValidationResult(val isValid: Boolean, val errorMessage: String? = null)
}
