package fr.antoinehory.lefooteuxenligne.domain.model

import fr.antoinehory.lefooteuxenligne.domain.model.enums.Direction

/**
 * Represents a movement or a choice of movements on the board.
 */
sealed class GameAction {
    /**
     * A single, mandatory movement.
     * @property direction The direction of the movement.
     * @property distance The number of cells to move.
     */
    data class Move(val direction: Direction, val distance: Int) : GameAction()

    /**
     * A list of moves where the player must choose one.
     * @property options The list of possible moves.
     */
    data class Choice(val options: List<Move>) : GameAction()
}
