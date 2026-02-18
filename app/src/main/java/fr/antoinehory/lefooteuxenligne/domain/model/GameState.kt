package fr.antoinehory.lefooteuxenligne.domain.model

import fr.antoinehory.lefooteuxenligne.domain.model.enums.GamePhase

/**
 * Represents the overall state of the game at any given moment.
 *
 * @property gamePhase The current phase of the game (e.g., PLAYER_TURN, AWAITING_SAVE).
 * @property currentPlayerId The ID of the player whose turn it is.
 * @property score A map holding the score for each player.
 * @property ballPosition The current coordinates of the ball on the field (Column, Row).
 * @property playerHands A map where keys are player IDs and values are the list of cards in their hand.
 * @property drawPile The list of cards remaining in the draw pile.
 * @property discardPile The list of cards that have been played.
 * @property message A message to be displayed to the user (e.g., for instructions or results).
 */
data class GameState(
    val gamePhase: GamePhase = GamePhase.PRE_GAME,
    val currentPlayerId: String = "Player 1",
    val score: Map<String, Int> = mapOf("Player 1" to 0, "Player 2" to 0),
    val ballPosition: Pair<Int, Int> = Pair(6, 9), // Default to center
    val playerHands: Map<String, List<GameCard>> = emptyMap(),
    val drawPile: List<GameCard> = emptyList(),
    val discardPile: List<GameCard> = emptyList(),
    val message: String? = null
)
