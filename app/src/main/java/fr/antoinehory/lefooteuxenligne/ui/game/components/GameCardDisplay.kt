package fr.antoinehory.lefooteuxenligne.ui.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.antoinehory.lefooteuxenligne.domain.model.GameAction
import fr.antoinehory.lefooteuxenligne.domain.model.GameCard
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardSymbol
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardType
import fr.antoinehory.lefooteuxenligne.domain.model.enums.Direction
import fr.antoinehory.lefooteuxenligne.ui.theme.LeFooteuxEnLigneTheme

@Composable
fun GameCardDisplay(card: GameCard, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.size(width = 100.dp, height = 150.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (card.type != CardType.ATTACKER) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFD700))
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = card.type.name.replace("_", " "),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE8F5E9)))

                if (card.symbol != CardSymbol.NONE) {
                    SymbolIcon(card.symbol, Modifier.align(Alignment.TopStart).padding(4.dp))
                }

                CardMovementDisplay(
                    modifier = Modifier.fillMaxSize(),
                    actions = card.actions
                )
            }
        }
    }
}

@Composable
private fun SymbolIcon(symbol: CardSymbol, modifier: Modifier = Modifier) {
    val icon: ImageVector
    val color: Color
    when (symbol) {
        CardSymbol.MAIN -> {
            icon = Icons.Default.PanTool
            color = Color.Red
        }
        CardSymbol.RED_FLAG -> {
            icon = Icons.Default.Flag
            color = Color.Red
        }
        CardSymbol.BLUE_FLAG -> {
            icon = Icons.Default.Flag
            color = Color.Blue
        }
        CardSymbol.NONE -> return
    }

    Box(
        modifier = modifier
            .size(24.dp)
            .background(Color.White, shape = MaterialTheme.shapes.small)
            .border(BorderStroke(1.dp, Color.Black), shape = MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = symbol.name, tint = color, modifier = Modifier.size(16.dp))
    }
}

@Preview(name = "Attacker Card - Sequential")
@Composable
fun PreviewAttackerCard() {
    LeFooteuxEnLigneTheme {
        GameCardDisplay(
            card = GameCard(
                type = CardType.ATTACKER,
                symbol = CardSymbol.MAIN,
                actions = listOf(
                    GameAction.Move(Direction.FORWARD, 3),
                    GameAction.Move(Direction.FORWARD_RIGHT, 7)
                )
            )
        )
    }
}

@Preview(name = "Free Kick Card - Choice")
@Composable
fun PreviewFreeKickCard() {
    LeFooteuxEnLigneTheme {
        GameCardDisplay(
            card = GameCard(
                type = CardType.FREE_KICK,
                symbol = CardSymbol.NONE,
                actions = listOf(
                    GameAction.Choice(listOf(
                        GameAction.Move(Direction.FORWARD, 3),
                        GameAction.Move(Direction.FORWARD_LEFT, 3),
                        GameAction.Move(Direction.FORWARD_RIGHT, 3)
                    ))
                )
            )
        )
    }
}
