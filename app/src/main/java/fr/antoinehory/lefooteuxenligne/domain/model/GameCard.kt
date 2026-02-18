package fr.antoinehory.lefooteuxenligne.domain.model

import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardSymbol
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardType
import java.util.UUID

/**
 * Represents a single game card with its type, symbol, and associated actions.
 *
 * @property id Unique identifier for the card.
 * @property type The type of the card (e.g., ATTACKER, GOALKEEPER).
 * @property symbol The special symbol on the card (e.g., MAIN for a hand, RED_FLAG, BLUE_FLAG).
 * @property actions A list of game actions associated with the card. This allows for complex moves
 *                   like sequences (e.g., Free Kick followed by an Attacker move) or choices.
 */
data class GameCard(
    val id: UUID = UUID.randomUUID(),
    val type: CardType,
    val symbol: CardSymbol,
    val actions: List<GameAction>
)
