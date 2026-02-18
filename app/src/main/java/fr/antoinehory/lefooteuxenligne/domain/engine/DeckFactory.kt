package fr.antoinehory.lefooteuxenligne.domain.engine

import fr.antoinehory.lefooteuxenligne.domain.model.GameAction
import fr.antoinehory.lefooteuxenligne.domain.model.GameCard
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardSymbol
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardType
import fr.antoinehory.lefooteuxenligne.domain.model.enums.Direction

/**
 * A factory for creating the standard 56-card deck for the game.
 */
object DeckFactory {

    /**
     * Creates and returns the complete, unshuffled 56-card deck.
     */
    fun createDeck(): List<GameCard> {
        return listOf(
            // --- Penalty Cards ---
            GameCard(type = CardType.PENALTY, symbol = CardSymbol.NONE, actions = emptyList()),

            // --- Corner Cards ---
            GameCard(type = CardType.CORNER, symbol = CardSymbol.BLUE_FLAG, actions = emptyList()),
            GameCard(type = CardType.CORNER, symbol = CardSymbol.BLUE_FLAG, actions = emptyList()),
            GameCard(type = CardType.CORNER, symbol = CardSymbol.RED_FLAG, actions = emptyList()),
            GameCard(type = CardType.CORNER, symbol = CardSymbol.RED_FLAG, actions = emptyList()),

            // --- Goalkeeper Cards ---
            GameCard(type = CardType.GOALKEEPER, symbol = CardSymbol.NONE, actions = listOf(GameAction.Move(Direction.FORWARD, 9))),
            GameCard(type = CardType.GOALKEEPER, symbol = CardSymbol.NONE, actions = listOf(GameAction.Move(Direction.FORWARD, 4), GameAction.Move(Direction.FORWARD_RIGHT, 4))),
            GameCard(type = CardType.GOALKEEPER, symbol = CardSymbol.NONE, actions = listOf(GameAction.Move(Direction.FORWARD, 5), GameAction.Move(Direction.FORWARD_RIGHT, 2))),
            GameCard(type = CardType.GOALKEEPER, symbol = CardSymbol.NONE, actions = listOf(GameAction.Move(Direction.FORWARD, 5), GameAction.Move(Direction.FORWARD_LEFT, 4))),
            GameCard(type = CardType.GOALKEEPER, symbol = CardSymbol.NONE, actions = listOf(GameAction.Move(Direction.FORWARD, 6), GameAction.Move(Direction.FORWARD_LEFT, 3))),

            // --- Free Kick Cards ---
            GameCard(type = CardType.FREE_KICK, symbol = CardSymbol.NONE, actions = listOf(
                GameAction.Move(Direction.FORWARD, 3),
                GameAction.Choice(listOf(
                    GameAction.Move(Direction.FORWARD, 3),
                    GameAction.Move(Direction.FORWARD_LEFT, 3),
                    GameAction.Move(Direction.FORWARD_RIGHT, 3)
                ))
            )),
            GameCard(type = CardType.FREE_KICK, symbol = CardSymbol.NONE, actions = listOf(
                GameAction.Move(Direction.FORWARD, 3),
                GameAction.Choice(listOf(
                    GameAction.Move(Direction.FORWARD, 3),
                    GameAction.Move(Direction.FORWARD_LEFT, 3),
                    GameAction.Move(Direction.FORWARD_RIGHT, 3)
                ))
            )),
            GameCard(type = CardType.FREE_KICK, symbol = CardSymbol.NONE, actions = listOf(
                GameAction.Move(Direction.FORWARD, 3),
                GameAction.Choice(listOf(
                    GameAction.Move(Direction.FORWARD_LEFT, 1),
                    GameAction.Move(Direction.FORWARD_RIGHT, 1)
                ))
            )),
            GameCard(type = CardType.FREE_KICK, symbol = CardSymbol.NONE, actions = listOf(
                GameAction.Move(Direction.FORWARD, 1),
                GameAction.Choice(listOf(
                    GameAction.Move(Direction.FORWARD, 1),
                    GameAction.Move(Direction.FORWARD_LEFT, 1),
                    GameAction.Move(Direction.FORWARD_RIGHT, 1),
                    GameAction.Move(Direction.LEFT, 1),
                    GameAction.Move(Direction.RIGHT, 1)
                ))
            )),

            // --- Attacker Cards ---
            // Hand symbol cards
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD_LEFT, 4), GameAction.Move(Direction.FORWARD, 4))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 3), GameAction.Move(Direction.FORWARD_LEFT, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 7), GameAction.Move(Direction.FORWARD_LEFT, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 3), GameAction.Move(Direction.FORWARD_RIGHT, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 7), GameAction.Move(Direction.RIGHT, 4), GameAction.Move(Direction.FORWARD, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 8), GameAction.Move(Direction.LEFT, 3), GameAction.Move(Direction.FORWARD, 4))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.RIGHT, 2), GameAction.Move(Direction.FORWARD, 7))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.RIGHT, 3), GameAction.Move(Direction.FORWARD, 6))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.RIGHT, 5), GameAction.Move(Direction.FORWARD, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.LEFT, 3), GameAction.Move(Direction.FORWARD, 6))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD_LEFT, 5))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.RIGHT, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.LEFT, 4))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 6))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 6))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 8))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 5))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 9))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.MAIN, actions = listOf(GameAction.Move(Direction.FORWARD, 5))),
            
            // Blue flag cards
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 7))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 4))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 4))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 7))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 8))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.LEFT, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD_LEFT, 6))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.LEFT, 5), GameAction.Move(Direction.FORWARD, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 9), GameAction.Move(Direction.RIGHT, 3), GameAction.Move(Direction.FORWARD, 5))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD_RIGHT, 4), GameAction.Move(Direction.FORWARD, 4))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.BLUE_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD_RIGHT, 6), GameAction.Move(Direction.FORWARD, 5))),

            // Red flag cards
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.RIGHT, 4))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.LEFT, 4))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 9))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD_RIGHT, 5))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD_RIGHT, 6))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.LEFT, 2), GameAction.Move(Direction.FORWARD, 7))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 7), GameAction.Move(Direction.LEFT, 4), GameAction.Move(Direction.FORWARD, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 7), GameAction.Move(Direction.FORWARD_LEFT, 3))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 9), GameAction.Move(Direction.LEFT, 3), GameAction.Move(Direction.FORWARD, 5))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD, 8), GameAction.Move(Direction.RIGHT, 3), GameAction.Move(Direction.FORWARD, 4))),
            GameCard(type = CardType.ATTACKER, symbol = CardSymbol.RED_FLAG, actions = listOf(GameAction.Move(Direction.FORWARD_LEFT, 6), GameAction.Move(Direction.FORWARD, 5)))
        )
    }
}
