package fr.antoinehory.lefooteuxenligne.domain.model.enums

/**
 * Represents the 8 possible directions of movement on the game board.
 * The directions are from the perspective of the player whose turn it is.
 */
enum class Direction {
    FORWARD,
    FORWARD_RIGHT,
    RIGHT,
    BACKWARD_RIGHT,
    BACKWARD,
    BACKWARD_LEFT,
    LEFT,
    FORWARD_LEFT
}
