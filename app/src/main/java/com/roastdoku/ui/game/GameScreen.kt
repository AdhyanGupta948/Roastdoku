package com.roastdoku.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roastdoku.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit
) {
    val board by viewModel.board.collectAsState()
    val selectedCell by viewModel.selectedCell.collectAsState()
    val isComplete by viewModel.isComplete.collectAsState()
    val mistakes by viewModel.mistakes.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val currentRoast by viewModel.currentRoast.collectAsState()
    val showNumberPad by viewModel.showNumberPad.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roastdoku") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Timer and Mistakes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = viewModel.formatTime(elapsedTime),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mistakes: $mistakes",
                        fontSize = 18.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Sudoku Board
                SudokuBoard(
                    board = board,
                    selectedCell = selectedCell,
                    onCellClick = { row, col ->
                        viewModel.selectCell(row, col)
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Number Pad
                AnimatedVisibility(
                    visible = showNumberPad,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    NumberPad(
                        onNumberClick = { number ->
                            viewModel.placeNumber(number)
                        },
                        onClearClick = {
                            viewModel.clearCell()
                        }
                    )
                }
            }
            
            // Roast Message
            currentRoast?.let { roast ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 80.dp)
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = roast,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }
            
            // Completion Screen
            if (isComplete) {
                ConfettiAnimation()
                CompletionDialog(
                    time = elapsedTime,
                    mistakes = mistakes,
                    onDismiss = onBack
                )
            }
        }
    }
}

@Composable
fun CompletionDialog(
    time: Long,
    mistakes: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Puzzle Complete!") },
        text = {
            Column {
                Text("Time: ${String.format("%02d:%02d", time / 60, time % 60)}")
                Text("Mistakes: $mistakes")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

