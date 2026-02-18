package fr.antoinehory.lefooteuxenligne.ui.game

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import fr.antoinehory.lefooteuxenligne.domain.model.GameState
import kotlin.math.min

// --- Field Dimensions Corrected based on image feedback ---
private const val GRID_COLUMNS = 12
private const val GRID_ROWS = 18
private const val GOAL_WIDTH_CELLS = 4
private const val GOAL_DEPTH_CELLS = 1
private const val PENALTY_AREA_WIDTH_CELLS = 8
private const val PENALTY_AREA_DEPTH_CELLS = 3
private const val GOAL_AREA_WIDTH_CELLS = 4
private const val GOAL_AREA_DEPTH_CELLS = 1
private const val PENALTY_SPOT_Y_CELL_INDEX = 2 // 3rd cell from top/bottom (0-indexed)
private const val CENTER_CIRCLE_RADIUS_CELLS = 2
private const val CORNER_ARC_RADIUS_CELLS = 1
private const val FIELD_PADDING_CELLS = 1.5f

private val LINE_WIDTH = 2.5.dp.value // Emphasized line width
private val DOT_RADIUS = 7f // Emphasized radius for penalty and center dots
private val BALL_RADIUS_FACTOR = 0.45f

@Composable
fun FootballField(
    modifier: Modifier = Modifier,
    gameState: GameState,
    onCellClicked: (x: Int, y: Int) -> Unit
) {
    val flagPainter = rememberVectorPainter(image = Icons.Default.Flag)
    val ballPainter = rememberVectorPainter(image = Icons.Default.SportsSoccer)

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val totalVirtualCols = GRID_COLUMNS + 2 * FIELD_PADDING_CELLS
        val totalVirtualRows = GRID_ROWS + 2 * FIELD_PADDING_CELLS
        val cellWidthBasedOnWidth = constraints.maxWidth / totalVirtualCols
        val cellHeightBasedOnHeight = constraints.maxHeight / totalVirtualRows
        val cellSize = min(cellWidthBasedOnWidth, cellHeightBasedOnHeight)

        val fieldWidth = GRID_COLUMNS * cellSize
        val fieldHeight = GRID_ROWS * cellSize

        val totalWidth = totalVirtualCols * cellSize
        val totalHeight = totalVirtualRows * cellSize
        val canvasOffsetX = (constraints.maxWidth - totalWidth) / 2
        val canvasOffsetY = (constraints.maxHeight - totalHeight) / 2

        val fieldOffsetX = canvasOffsetX + (FIELD_PADDING_CELLS * cellSize)
        val fieldOffsetY = canvasOffsetY + (FIELD_PADDING_CELLS * cellSize)

        val targetBallOffset = Offset(
            fieldOffsetX + (gameState.ballPosition.first * cellSize),
            fieldOffsetY + (gameState.ballPosition.second * cellSize)
        )

        val animatedBallPosition by animateOffsetAsState(targetValue = targetBallOffset, label = "ballPositionAnimation")

        Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures { pressOffset ->
                if (pressOffset.x in fieldOffsetX..(fieldOffsetX + fieldWidth) && pressOffset.y in fieldOffsetY..(fieldOffsetY + fieldHeight)) {
                    val x = ((pressOffset.x - fieldOffsetX) / cellSize).toInt().coerceIn(0, GRID_COLUMNS - 1)
                    val y = ((pressOffset.y - fieldOffsetY) / cellSize).toInt().coerceIn(0, GRID_ROWS - 1)
                    onCellClicked(x, y)
                }
            }
        }) {
            drawRect(color = Color(0xFF00A300), size = this.size)
            translate(left = fieldOffsetX, top = fieldOffsetY) {
                drawFieldBackground(fieldWidth, fieldHeight)
                drawGrid(cellSize, fieldWidth, fieldHeight)
                drawFieldMarkings(cellSize, fieldWidth, fieldHeight)
                drawGoals(cellSize, fieldWidth, fieldHeight)
                drawFlags(flagPainter, cellSize, fieldWidth, fieldHeight)
            }
            translate(left = animatedBallPosition.x, top = animatedBallPosition.y) {
                with(ballPainter) {
                    draw(Size(cellSize, cellSize))
                }
            }
        }
    }
}

private fun DrawScope.drawFieldBackground(fieldWidth: Float, fieldHeight: Float) {
    drawRect(color = Color(0xFF00C800), size = Size(fieldWidth, fieldHeight))
}

private fun DrawScope.drawGrid(cellSize: Float, fieldWidth: Float, fieldHeight: Float) {
    for (i in 0..GRID_COLUMNS) { drawLine(Color.Black.copy(alpha = 0.5f), start = Offset(i * cellSize, 0f), end = Offset(i * cellSize, fieldHeight), strokeWidth = 1f) }
    for (i in 0..GRID_ROWS) { drawLine(Color.Black.copy(alpha = 0.5f), start = Offset(0f, i * cellSize), end = Offset(fieldWidth, i * cellSize), strokeWidth = 1f) }
}

private fun DrawScope.drawFieldMarkings(cellSize: Float, fieldWidth: Float, fieldHeight: Float) {
    // --- Main Lines ---
    drawRect(Color.White, topLeft = Offset(0f, 0f), size = Size(fieldWidth, fieldHeight), style = Stroke(width = LINE_WIDTH))
    drawLine(Color.White, start = Offset(0f, fieldHeight / 2), end = Offset(fieldWidth, fieldHeight / 2), strokeWidth = LINE_WIDTH) // Midfield line
    drawCircle(Color.White, radius = CENTER_CIRCLE_RADIUS_CELLS * cellSize, center = Offset(fieldWidth / 2, fieldHeight / 2), style = Stroke(width = LINE_WIDTH))
    drawCircle(Color.White, radius = DOT_RADIUS, center = Offset(fieldWidth / 2, fieldHeight / 2)) // Center dot

    // --- Penalty and Goal Areas ---
    val penaltyAreaWidth = PENALTY_AREA_WIDTH_CELLS * cellSize
    val penaltyAreaDepth = PENALTY_AREA_DEPTH_CELLS * cellSize
    drawRect(Color.White, topLeft = Offset((fieldWidth - penaltyAreaWidth) / 2, 0f), size = Size(penaltyAreaWidth, penaltyAreaDepth), style = Stroke(width = LINE_WIDTH))
    drawRect(Color.White, topLeft = Offset((fieldWidth - penaltyAreaWidth) / 2, fieldHeight - penaltyAreaDepth), size = Size(penaltyAreaWidth, penaltyAreaDepth), style = Stroke(width = LINE_WIDTH))

    val goalAreaWidth = GOAL_AREA_WIDTH_CELLS * cellSize
    val goalAreaDepth = GOAL_AREA_DEPTH_CELLS * cellSize
    drawRect(Color.White, topLeft = Offset((fieldWidth - goalAreaWidth) / 2, 0f), size = Size(goalAreaWidth, goalAreaDepth), style = Stroke(width = LINE_WIDTH))
    drawRect(Color.White, topLeft = Offset((fieldWidth - goalAreaWidth) / 2, fieldHeight - goalAreaDepth), size = Size(goalAreaWidth, goalAreaDepth), style = Stroke(width = LINE_WIDTH))

    // --- Dots ---
    val penaltySpotYTop = (PENALTY_SPOT_Y_CELL_INDEX + 0.5f) * cellSize
    val penaltySpotYBottom = fieldHeight - penaltySpotYTop
    drawCircle(Color.White, radius = DOT_RADIUS, center = Offset(fieldWidth / 2, penaltySpotYTop))
    drawCircle(Color.White, radius = DOT_RADIUS, center = Offset(fieldWidth / 2, penaltySpotYBottom))

    // --- Corners ---
    val cornerArcRadius = CORNER_ARC_RADIUS_CELLS * cellSize
    val cornerArcSize = Size(cornerArcRadius * 2, cornerArcRadius * 2)
    // Top-left
    drawArc(Color.White, 0f, 90f, false, Offset(-cornerArcRadius, -cornerArcRadius), cornerArcSize, style = Stroke(width = LINE_WIDTH))
    // Top-right
    drawArc(Color.White, 90f, 90f, false, Offset(fieldWidth - cornerArcRadius, -cornerArcRadius), cornerArcSize, style = Stroke(width = LINE_WIDTH))
    // Bottom-left
    drawArc(Color.White, 270f, 90f, false, Offset(-cornerArcRadius, fieldHeight - cornerArcRadius), cornerArcSize, style = Stroke(width = LINE_WIDTH))
    // Bottom-right
    drawArc(Color.White, 180f, 90f, false, Offset(fieldWidth - cornerArcRadius, fieldHeight - cornerArcRadius), cornerArcSize, style = Stroke(width = LINE_WIDTH))
}

private fun DrawScope.drawGoals(cellSize: Float, fieldWidth: Float, fieldHeight: Float) {
    val goalNetColor = Color(0x99FFFFFF)
    val goalWidth = GOAL_WIDTH_CELLS * cellSize
    val goalDepth = GOAL_DEPTH_CELLS * cellSize
    drawRect(color = goalNetColor, topLeft = Offset((fieldWidth - goalWidth) / 2, -goalDepth), size = Size(goalWidth, goalDepth))
    drawRect(color = goalNetColor, topLeft = Offset((fieldWidth - goalWidth) / 2, fieldHeight), size = Size(goalWidth, goalDepth))
}

private fun DrawScope.drawFlags(flagPainter: androidx.compose.ui.graphics.vector.VectorPainter, cellSize: Float, fieldWidth: Float, fieldHeight: Float) {
    val flagSize = Size(cellSize, cellSize)
    // Top flags (Red)
    translate(left = -flagSize.width, top = -flagSize.height) {
        with(flagPainter) { draw(flagSize, alpha = 1f, colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Red)) }
    }
    translate(left = fieldWidth, top = -flagSize.height) {
        with(flagPainter) { draw(flagSize, alpha = 1f, colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Red)) }
    }
    // Bottom flags (Blue)
    translate(left = -flagSize.width, top = fieldHeight) {
        with(flagPainter) { draw(flagSize, alpha = 1f, colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Blue)) }
    }
    translate(left = fieldWidth, top = fieldHeight) {
        with(flagPainter) { draw(flagSize, alpha = 1f, colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Blue)) }
    }
}
