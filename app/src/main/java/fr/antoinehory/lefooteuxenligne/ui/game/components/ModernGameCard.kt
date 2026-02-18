package fr.antoinehory.lefooteuxenligne.ui.game.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.antoinehory.lefooteuxenligne.domain.model.GameAction
import fr.antoinehory.lefooteuxenligne.domain.model.GameCard
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardSymbol
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardType
import fr.antoinehory.lefooteuxenligne.domain.model.enums.Direction
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ModernGameCard(
    card: GameCard,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 1.05f else 1.0f, label = "scale")

    val borderColor = when (card.type) {
        CardType.ATTACKER -> Color(0xFF4CAF50) // Green
        CardType.FREE_KICK -> Color(0xFFFFC107) // Amber
        CardType.GOALKEEPER -> Color(0xFF03A9F4) // Light Blue
        CardType.CORNER, CardType.PENALTY -> Color(0xFF673AB7) // Deep Purple
    }

    Card(
        modifier = modifier
            .size(width = 100.dp, height = 150.dp)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), // Off-white
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Symbol in top-left corner
            if (card.symbol != CardSymbol.NONE) {
                CardSymbolIcon(symbol = card.symbol, modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp))
            }

            // Main content: movement indicators
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                card.actions.forEach { action ->
                    ActionRenderer(action = action)
                }
            }
        }
    }
}

@Composable
private fun ActionRenderer(action: GameAction) {
    when (action) {
        is GameAction.Move -> {
            MovementIndicator(move = action)
            Spacer(modifier = Modifier.height(4.dp))
        }
        is GameAction.Choice -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                action.options.forEach { moveOption ->
                    MovementIndicator(move = moveOption)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun MovementIndicator(move: GameAction.Move) {
    Box(contentAlignment = Alignment.Center) {
        val textDiameter = 22.dp
        // Canvas for the arrow, drawn behind the number
        Canvas(modifier = Modifier.size(textDiameter * 1.8f)) { // Canvas slightly larger than the bubble
            drawArrow(direction = move.direction)
        }
        // Number bubble, drawn on top of the arrow
        Box(
            modifier = Modifier
                .size(textDiameter)
                .background(Color.White, CircleShape)
                .border(1.5.dp, Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = move.distance.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

private fun DrawScope.drawArrow(direction: Direction) {
    val arrowColor = Color(0xFFD32F2F)
    val strokeWidth = 5.dp.toPx()
    val angle = direction.toAngleRadians()

    // Position the arrow "attached" to the circle
    val lineStartOffset = size.minDimension * 0.4f
    val lineEndOffset = size.minDimension * 0.6f

    val start = Offset(center.x + lineStartOffset * cos(angle), center.y + lineStartOffset * sin(angle))
    val end = Offset(center.x + lineEndOffset * cos(angle), center.y + lineEndOffset * sin(angle))

    // Draw the line
    drawLine(arrowColor, start, end, strokeWidth)

    // Draw the arrowhead
    val headLength = strokeWidth * 1.5f
    val headPath = Path().apply {
        moveTo(end.x, end.y)
        lineTo(end.x - headLength * cos(angle - 0.8f), end.y - headLength * sin(angle - 0.8f))
        lineTo(end.x - headLength * cos(angle + 0.8f), end.y - headLength * sin(angle + 0.8f))
        close()
    }
    drawPath(headPath, color = arrowColor)
}

@Composable
private fun CardSymbolIcon(symbol: CardSymbol, modifier: Modifier = Modifier) {
    val icon: ImageVector
    val tint: Color
    when (symbol) {
        CardSymbol.MAIN -> {
            icon = Icons.Default.PanTool
            tint = Color.Red
        }
        CardSymbol.RED_FLAG -> {
            icon = Icons.Default.Flag
            tint = Color.Red
        }
        CardSymbol.BLUE_FLAG -> {
            icon = Icons.Default.Flag
            tint = Color.Blue
        }
        CardSymbol.NONE -> return
    }

    Box(
        modifier = modifier
            .size(24.dp)
            .background(Color.White, CircleShape)
            .border(1.5.dp, Color.Black, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = symbol.name, tint = tint, modifier = Modifier.size(16.dp))
    }
}

private fun Direction.toAngleRadians(): Float {
    return when (this) {
        Direction.FORWARD -> -90f
        Direction.FORWARD_RIGHT -> -45f
        Direction.RIGHT -> 0f
        Direction.BACKWARD_RIGHT -> 45f
        Direction.BACKWARD -> 90f
        Direction.BACKWARD_LEFT -> 135f
        Direction.LEFT -> 180f
        Direction.FORWARD_LEFT -> -135f
    } * (PI / 180f).toFloat()
}
