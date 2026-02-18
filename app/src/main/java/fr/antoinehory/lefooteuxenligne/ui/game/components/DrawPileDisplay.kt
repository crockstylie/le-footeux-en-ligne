package fr.antoinehory.lefooteuxenligne.ui.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.antoinehory.lefooteuxenligne.ui.theme.LeFooteuxEnLigneTheme

/**
 * A Composable that displays the draw pile (face down card).
 *
 * @param modifier The modifier to be applied to the draw pile.
 * @param cardsInPile The number of cards currently in the draw pile.
 */
@Composable
fun DrawPileDisplay(modifier: Modifier = Modifier, cardsInPile: Int) {
    Card(
        modifier = modifier.size(width = 80.dp, height = 120.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Blue),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue.copy(alpha = 0.8f)), // Slightly transparent blue
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = cardsInPile.toString(),
                color = Color.White,
                fontSize = 24.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDrawPileDisplay() {
    LeFooteuxEnLigneTheme {
        DrawPileDisplay(cardsInPile = 40)
    }
}
