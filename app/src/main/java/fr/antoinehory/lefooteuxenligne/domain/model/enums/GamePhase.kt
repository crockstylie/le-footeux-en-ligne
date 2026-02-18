package fr.antoinehory.lefooteuxenligne.domain.model.enums

/**
 * Represents the current phase of the game, determining which actions are possible.
 */
enum class GamePhase {
    /** The game has not started yet. Waiting for the first kick-off. */
    PRE_GAME,

    /** Standard gameplay, where a player plays a card to move the ball. */
    PLAYER_TURN,

    /** After a goal is scored, waiting for the other team to kick-off. */
    AWAITING_KICKOFF,

    /** A goal has been scored, waiting for the defending player to play a Goalkeeper or Corner card. */
    AWAITING_SAVE,

    /** A Corner card has been played, waiting for the attacking player to cut the deck. */
    AWAITING_CORNER_CUT,

    /** A Penalty card has been played, waiting for the defending player to cut the deck. */
    AWAITING_PENALTY_CUT,

    /** The ball is out of bounds, waiting for a throw-in. */
    AWAITING_THROW_IN,

    /** The first half has ended. */
    HALF_TIME,

    /** The game has finished. */
    GAME_OVER
}
