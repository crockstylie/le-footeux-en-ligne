package fr.antoinehory.lefooteuxenligne.ui.game.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.antoinehory.lefooteuxenligne.domain.model.GameAction
import fr.antoinehory.lefooteuxenligne.domain.model.enums.Direction
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A Composable that visualizes game actions with independent movement indicators.
 */
@OptIn(ExperimentalTextApi::class)
@Composable
fun CardMovementDisplay(modifier: Modifier = Modifier, actions: List<GameAction>) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        // Group all moves to lay them out properly
        val allMoves = actions.flatMap {
            when (it) {
                is GameAction.Move -> listOf(it to false) // false = not a choice
                is GameAction.Choice -> it.options.map { move -> move to true } // true = is a choice
            }
        }

        val totalItems = allMoves.size
        val itemHeight = size.height / (totalItems + 1)

        allMoves.forEachIndexed { index, (move, isChoice) ->
            val verticalPosition = itemHeight * (index + 1)
            val centerPoint = Offset(center.x, verticalPosition)
            drawMovementIndicator(move, centerPoint, textMeasurer)
        }
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawMovementIndicator(
    move: GameAction.Move,
    center: Offset,
    textMeasurer: TextMeasurer
) {
    val arrowColor = Color(0xFFD32F2F)
    val strokeWidth = 4.dp.toPx()

    // --- Text and Circle ---
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(move.distance.toString()),
        style = TextStyle(fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Bold)
    )
    val textRadius = textLayoutResult.size.height * 0.8f // Smaller radius

    drawCircle(color = Color.White, radius = textRadius, center = center)
    drawCircle(color = Color.Black, radius = textRadius, center = center, style = Stroke(width = 1.5.dp.toPx()))
    drawText(textLayoutResult, topLeft = Offset(center.x - textLayoutResult.size.width / 2, center.y - textLayoutResult.size.height / 2))

    // --- Arrow ---
    val angle = move.direction.toAngleRadians()
    val arrowLength = textRadius * 1.5f
    val arrowStartOffset = textRadius * 0.8f

    val start = Offset(center.x + arrowStartOffset * cos(angle), center.y + arrowStartOffset * sin(angle))
    val end = Offset(center.x + (arrowStartOffset + arrowLength) * cos(angle), center.y + (arrowStartOffset + arrowLength) * sin(angle))

    drawLine(arrowColor, start, end, strokeWidth)

    // Arrowhead
    val arrowHeadLength = strokeWidth * 1.5f
    val path = Path().apply {
        moveTo(end.x, end.y)
        lineTo(end.x - arrowHeadLength * cos(angle - 0.7f), end.y - arrowHeadLength * sin(angle - 0.7f))
        lineTo(end.x - arrowHeadLength * cos(angle + 0.7f), end.y - arrowHeadLength * sin(angle + 0.7f))
        close()
    }
    drawPath(path, color = arrowColor)
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
