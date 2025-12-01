package com.roastdoku.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0..8) {
            Row {
                for (col in 0..8) {
                    val cell = board[row][col]
                    val isSelected = selectedCell?.let { it.first == row && it.second == col } == true
                    val isHighlighted = selectedCell?.let { (selRow, selCol) ->
                        selRow == row || selCol == col || 
                        (selRow / 3 == row / 3 && selCol / 3 == col / 3)
                    } == true
                    
                    SudokuCell(
                        cell = cell,
                        isSelected = isSelected,
                        isHighlighted = isHighlighted && !isSelected,
                        onClick = { onCellClick(row, col) }
                    )
                    
                    // Vertical border
                    if (col == 2 || col == 5) {
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }
            
            // Horizontal border
            if (row == 2 || row == 5) {
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Composable
fun SudokuCell(
    cell: Cell,
    isSelected: Boolean,
    isHighlighted: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> CellSelected.copy(alpha = 0.3f)
            isHighlighted -> CellHighlight.copy(alpha = 0.1f)
            cell.isError -> CellError.copy(alpha = 0.3f)
            else -> Color.Transparent
        }
    )
    
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 3.dp else 1.dp
    )
    
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(backgroundColor)
            .border(
                width = borderWidth,
                color = if (isSelected) CellSelected else Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (cell.value != 0) {
            Text(
                text = cell.value.toString(),
                fontSize = 18.sp,
                fontWeight = if (cell.isGiven) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    cell.isError -> CellError
                    cell.isGiven -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

