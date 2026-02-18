package fr.antoinehory.lefooteuxenligne.ui.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.antoinehory.lefooteuxenligne.domain.engine.DeckFactory
import fr.antoinehory.lefooteuxenligne.ui.game.components.FinalGameCard

/**
 * A temporary screen to display all 56 cards for design validation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckPreviewScreen() {
    val allCards = DeckFactory.createDeck()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Deck Preview (${allCards.size} cards)") })
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(allCards) { card ->
                FinalGameCard(card = card, onClick = {})
            }
        }
    }
}
