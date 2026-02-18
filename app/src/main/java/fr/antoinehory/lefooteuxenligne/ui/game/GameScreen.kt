package fr.antoinehory.lefooteuxenligne.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.antoinehory.lefooteuxenligne.ui.game.components.DrawPileDisplay
import fr.antoinehory.lefooteuxenligne.ui.game.components.FinalGameCard
import kotlinx.coroutines.launch

/**
 * The main screen for the game, combining the football field, player hand, and game messages.
 */
@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameState by gameViewModel.gameState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val currentPlayerHand = gameState.playerHands[gameState.currentPlayerId] ?: emptyList()
    val drawPileSize = gameState.drawPile.size

    LaunchedEffect(gameState.message) {
        gameState.message?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.Center))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Scoreboard(score = gameState.score)
            FootballField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                gameState = gameState,
                onCellClicked = { x, y -> gameViewModel.onCellClicked(x, y) }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LazyRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp), // Reduced spacing
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(currentPlayerHand) { card ->
                        FinalGameCard(
                            card = card,
                            onClick = { gameViewModel.onCardPlayed(card.id.toString()) }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                DrawPileDisplay(cardsInPile = drawPileSize)
            }
        }
    }
}

@Composable
private fun Scoreboard(score: Map<String, Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val player1Score = score["Player 1"] ?: 0
        val player2Score = score["Player 2"] ?: 0

        Text(text = "Player 1", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "$player1Score - $player2Score",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(text = "Player 2", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
