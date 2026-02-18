package fr.antoinehory.lefooteuxenligne.ui.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.antoinehory.lefooteuxenligne.domain.engine.DeckFactory
import fr.antoinehory.lefooteuxenligne.domain.model.GameAction
import fr.antoinehory.lefooteuxenligne.domain.model.GameCard
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardSymbol
import fr.antoinehory.lefooteuxenligne.domain.model.enums.CardType
import fr.antoinehory.lefooteuxenligne.domain.model.enums.Direction
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun FinalGameCard(card: GameCard, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .size(width = 60.dp, height = 110.dp) // Drastically reduced width
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CardHeader(card.type)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFE8F5E9))
                    .padding(4.dp) // Reduced padding
            ) {
                if (card.symbol != CardSymbol.NONE) {
                    SymbolIcon(symbol = card.symbol, modifier = Modifier.align(Alignment.TopStart))
                }
                ArrowCanvas(actions = card.actions)
            }
        }
    }
}

@Composable
private fun CardHeader(type: CardType) {
    val headerText = when (type) {
        CardType.GOALKEEPER, CardType.FREE_KICK, CardType.CORNER, CardType.PENALTY -> type.name.replace("_", " ")
        else -> null
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(18.dp)
            .background(if (headerText != null) Color(0xFFFFD600) else Color.Transparent)
    ) {
        if (headerText != null) {
            Text(text = headerText, modifier = Modifier.align(Alignment.Center), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ArrowCanvas(actions: List<GameAction>) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier.fillMaxSize()) { drawPaths(actions, textMeasurer) }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawPaths(actions: List<GameAction>, textMeasurer: TextMeasurer) {
    // Start arrows a bit higher to give them more space
    var currentPos = Offset(center.x, size.height * 0.9f)
    actions.forEachIndexed { index, action ->
        when (action) {
            is GameAction.Move -> {
                currentPos = drawArrowPath(start = currentPos, move = action, textMeasurer = textMeasurer, isEndpoint = index == actions.lastIndex)
            }
            is GameAction.Choice -> {
                action.options.forEach { option ->
                    drawArrowPath(start = currentPos, move = option, textMeasurer = textMeasurer, isEndpoint = true)
                }
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawArrowPath(start: Offset, move: GameAction.Move, textMeasurer: TextMeasurer, isEndpoint: Boolean): Offset {
    val color = Color(0xFFD32F2F)
    val strokeWidth = 3.dp.toPx()
    val angle = move.direction.toAngleRadians()

    // Refined pseudo-logarithmic scaling for arrow length
    val baseLength = size.minDimension * 0.15f
    val scaledLength = size.minDimension * 0.1f * sqrt(move.distance.toFloat())
    val length = (baseLength + scaledLength).coerceAtMost(size.height * 0.85f)

    val end = Offset(start.x + length * cos(angle), start.y + length * sin(angle))

    val arrowPath = Path().apply {
        moveTo(start.x, start.y)
        lineTo(end.x, end.y)
    }
    drawPath(arrowPath, color, style = Stroke(strokeWidth))

    if (isEndpoint) {
        val headLength = strokeWidth * 2.0f
        val headPath = Path().apply {
            moveTo(end.x, end.y)
            lineTo(end.x - headLength * cos(angle - 0.5f), end.y - headLength * sin(angle - 0.5f))
            moveTo(end.x, end.y)
            lineTo(end.x - headLength * cos(angle + 0.5f), end.y - headLength * sin(angle + 0.5f))
        }
        drawPath(headPath, color, style = Stroke(strokeWidth))
    }

    val textLayoutResult: TextLayoutResult = textMeasurer.measure(
        text = AnnotatedString(move.distance.toString()),
        // Black color and larger font for high contrast and readability
        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    )
    val textCenter = Offset((start.x + end.x) / 2, (start.y + end.y) / 2)
    drawText(textLayoutResult, topLeft = Offset(textCenter.x - textLayoutResult.size.width / 2, textCenter.y - textLayoutResult.size.height / 2))

    return end
}

@Composable
private fun SymbolIcon(symbol: CardSymbol, modifier: Modifier = Modifier) {
    val icon: ImageVector
    val tint: Color
    when (symbol) {
        CardSymbol.MAIN -> { icon = Icons.Default.PanTool; tint = Color.Red }
        CardSymbol.RED_FLAG -> { icon = Icons.Default.Flag; tint = Color.Red }
        CardSymbol.BLUE_FLAG -> { icon = Icons.Default.Flag; tint = Color.Blue }
        CardSymbol.NONE -> return
    }
    Box(
        modifier = modifier.size(18.dp).clip(RoundedCornerShape(4.dp)).background(Color.White).border(1.dp, Color.Black, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(11.dp))
    }
}

private fun Direction.toAngleRadians(): Float = (when (this) {
    Direction.FORWARD -> -90f
    Direction.FORWARD_RIGHT -> -45f
    Direction.RIGHT -> 0f
    Direction.BACKWARD_RIGHT -> 45f
    Direction.BACKWARD -> 90f
    Direction.BACKWARD_LEFT -> 135f
    Direction.LEFT -> 180f
    Direction.FORWARD_LEFT -> -135f
}) * (PI / 180f).toFloat()

@Preview(showBackground = true, name = "Coup Franc")
@Composable
fun PreviewFreeKick() {
    FinalGameCard(card = DeckFactory.createDeck().first { it.type == CardType.FREE_KICK })
}

@Preview(showBackground = true, name = "Gardien")
@Composable
fun PreviewGoalkeeper() {
    FinalGameCard(card = DeckFactory.createDeck().first { it.type == CardType.GOALKEEPER })
}

@Preview(showBackground = true, name = "Attaquant multiple")
@Composable
fun PreviewAttackerMulti() {
    FinalGameCard(card = DeckFactory.createDeck().first { it.actions.size > 1 && it.type == CardType.ATTACKER })
}
