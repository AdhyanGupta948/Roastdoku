package com.roastdoku.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roastdoku.ui.theme.*
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

    var showQuitDialog by remember { mutableStateOf(false) }

    // Handle Android back button
    BackHandler(enabled = !isComplete) {
        showQuitDialog = true
    }

    // Quit Confirmation Dialog
    if (showQuitDialog) {
        AlertDialog(
            onDismissRequest = { showQuitDialog = false },
            title = {
                Text(
                    text = "Quit Game?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Leaving already I think you gave up.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showQuitDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CellError
                    )
                ) {
                    Text(
                        text = "Quit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuitDialog = false }) {
                    Text(
                        text = "Cancel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Minimal Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { showQuitDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Text(
                    text = "Roastdoku",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 1.sp
                )

                // Empty spacer for balance
                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Minimal Stats
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MinimalStat(
                        value = viewModel.formatTime(elapsedTime),
                        label = "time"
                    )

                    MinimalStat(
                        value = mistakes.toString(),
                        label = "mistakes",
                        isError = mistakes > 0
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

                Spacer(modifier = Modifier.weight(1f))

                // Number Pad
                AnimatedVisibility(
                    visible = showNumberPad,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
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

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Roast Message
        currentRoast?.let { roast ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp, start = 24.dp, end = 24.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PastelBlush,
                    shadowElevation = 0.dp
                ) {
                    Text(
                        text = roast,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = androidx.compose.ui.graphics.Color(0xFF5A5A5A),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(20.dp),
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Completion Screen
        if (isComplete) {
            ConfettiAnimation()
            MinimalCompletionDialog(
                time = elapsedTime,
                mistakes = mistakes,
                onDismiss = onBack
            )
        }
    }
}

@Composable
fun MinimalStat(
    value: String,
    label: String,
    isError: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Light,
            color = if (isError)
                CellError
            else
                MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun MinimalCompletionDialog(
    time: Long,
    mistakes: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Complete",
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CompletionStat(
                    label = "time",
                    value = String.format("%02d:%02d", time / 60, time % 60)
                )

                CompletionStat(
                    label = "mistakes",
                    value = mistakes.toString()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Done",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun CompletionStat(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}