package com.roastdoku.ui.game

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roastdoku.ui.theme.*
import com.roastdoku.viewmodel.Cell

@Composable
fun SudokuBoard(
    board: List<List<Cell>>,
    selectedCell: Pair<Int, Int>?,
    onCellClick: (Int, Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            for (row in 0..8) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0..8) {
                        val cell = board[row][col]
                        val isSelected = selectedCell?.let {
                            it.first == row && it.second == col
                        } == true

                        val isHighlighted = selectedCell?.let { (selRow, selCol) ->
                            selRow == row || selCol == col ||
                                    (selRow / 3 == row / 3 && selCol / 3 == col / 3)
                        } == true

                        MinimalSudokuCell(
                            cell = cell,
                            isSelected = isSelected,
                            isHighlighted = isHighlighted && !isSelected,
                            onClick = { onCellClick(row, col) },
                            modifier = Modifier.weight(1f)
                        )

                        // Vertical spacing
                        if (col < 8) {
                            Spacer(modifier = Modifier.width(if (col == 2 || col == 5) 3.dp else 1.dp))
                        }
                    }
                }

                // Horizontal spacing
                if (row < 8) {
                    Spacer(modifier = Modifier.height(if (row == 2 || row == 5) 3.dp else 1.dp))
                }
            }
        }
    }
}

@Composable
fun MinimalSudokuCell(
    cell: Cell,
    isSelected: Boolean,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            cell.isError -> CellErrorLight
            isSelected -> CellSelectedLight
            isHighlighted -> MaterialTheme.colorScheme.surfaceVariant
            else -> Color.Transparent
        },
        animationSpec = tween(150),
        label = "backgroundColor"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (cell.value != 0) {
            Text(
                text = cell.value.toString(),
                fontSize = 24.sp,
                fontWeight = if (cell.isGiven) FontWeight.SemiBold else FontWeight.Normal,
                color = when {
                    cell.isError -> CellError
                    cell.isGiven -> MaterialTheme.colorScheme.onSurface
                    else -> CellUser
                }
            )
        }
    }
}