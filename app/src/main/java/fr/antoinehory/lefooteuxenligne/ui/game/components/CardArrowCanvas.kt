package fr.antoinehory.lefooteuxenligne.ui.game.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
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
 * A dedicated Canvas for drawing the complex arrow paths and numbers, as seen on the original cards.
 */
@OptIn(ExperimentalTextApi::class)
@Composable
fun CardArrowCanvas(modifier: Modifier = Modifier, actions: List<GameAction>) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.fillMaxSize()) {
        val startPos = Offset(center.x, size.height * 0.9f)
        drawPath(startPos, actions, textMeasurer)
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawPath(
    startPosition: Offset,
    actions: List<GameAction>,
    textMeasurer: TextMeasurer
) {
    var currentPos = startPosition

    actions.forEachIndexed { index, action ->
        val isLastAction = index == actions.lastIndex
        when (action) {
            is GameAction.Move -> {
                currentPos = drawSegment(currentPos, action, textMeasurer, isLastAction)
            }
            is GameAction.Choice -> {
                action.options.forEach { move ->
                    drawSegment(currentPos, move, textMeasurer, true)
                }
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawSegment(
    start: Offset,
    move: GameAction.Move,
    textMeasurer: TextMeasurer,
    isEndPoint: Boolean
): Offset {
    val arrowColor = Color(0xFFC62828)
    val strokeWidth = 5.dp.toPx()
    val baseLengthPerUnit = size.minDimension * 0.05f
    val length = move.distance * baseLengthPerUnit
    val angle = move.direction.toAngleRadians()
    val end = Offset(start.x + length * cos(angle), start.y + length * sin(angle))

    drawLine(arrowColor, start, end, strokeWidth)

    if (isEndPoint) {
        val headLength = strokeWidth * 1.8f
        val headPath = Path().apply {
            moveTo(end.x, end.y)
            lineTo(end.x - headLength * cos(angle - 0.7f), end.y - headLength * sin(angle - 0.7f))
            lineTo(end.x - headLength * cos(angle + 0.7f), end.y - headLength * sin(angle + 0.7f))
            close()
        }
        drawPath(headPath, color = arrowColor)
    }

    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString("${move.distance}"),
        style = TextStyle(fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.ExtraBold)
    )
    val textRadius = textLayoutResult.size.height * 0.8f
    val textCenter = Offset((start.x + end.x) / 2, (start.y + end.y) / 2)

    drawCircle(color = Color.White, radius = textRadius, center = textCenter)
    drawCircle(color = Color.Black, radius = textRadius, center = textCenter, style = Stroke(width = 1.5.dp.toPx()))
    drawText(textLayoutResult, topLeft = Offset(textCenter.x - textLayoutResult.size.width / 2, textCenter.y - textLayoutResult.size.height / 2))

    return end
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
