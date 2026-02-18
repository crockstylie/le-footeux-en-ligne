package fr.antoinehory.lefooteuxenligne.domain.model.enums

/**
 * Represents the type of a game card.
 */
enum class CardType {
    /** An attacker card. */
    ATTACKER,

    /** A free kick card. */
    FREE_KICK,

    /** A goalkeeper card. */
    GOALKEEPER,

    /** A corner kick card. */
    CORNER,

    /** A penalty kick card. */
    PENALTY
}
