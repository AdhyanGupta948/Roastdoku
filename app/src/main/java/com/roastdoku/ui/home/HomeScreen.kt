package com.roastdoku.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roastdoku.data.Difficulty
import com.roastdoku.ui.game.GameScreen
import com.roastdoku.ui.settings.SettingsScreen
import com.roastdoku.viewmodel.GameViewModel
import com.roastdoku.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val gameViewModel: GameViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    
    var showGame by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    
    if (showGame) {
        GameScreen(
            viewModel = gameViewModel,
            onBack = { showGame = false }
        )
    } else if (showSettings) {
        SettingsScreen(
            viewModel = settingsViewModel,
            onBack = { showSettings = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Roastdoku") },
                    actions = {
                        IconButton(onClick = { showSettings = true }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Roastdoku",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 64.dp)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                DifficultyButton(
                    text = "Easy",
                    onClick = {
                        gameViewModel.startGame(Difficulty.EASY)
                        showGame = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                DifficultyButton(
                    text = "Medium",
                    onClick = {
                        gameViewModel.startGame(Difficulty.MEDIUM)
                        showGame = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                DifficultyButton(
                    text = "Hard",
                    onClick = {
                        gameViewModel.startGame(Difficulty.HARD)
                        showGame = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun DifficultyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

