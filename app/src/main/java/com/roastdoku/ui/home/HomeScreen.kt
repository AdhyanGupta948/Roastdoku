package com.roastdoku.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roastdoku.data.Difficulty
import com.roastdoku.ui.game.GameScreen
import com.roastdoku.ui.settings.SettingsScreen
import com.roastdoku.ui.theme.*
import com.roastdoku.viewmodel.GameViewModel
import com.roastdoku.viewmodel.SettingsViewModel

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    Text(
                        text = "Roastdoku",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "Choose your pace",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Difficulty Tiles
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp)
                ) {
                    PastelTile(
                        text = "Easy",
                        backgroundColor = PastelMint,
                        onClick = {
                            gameViewModel.startGame(Difficulty.EASY)
                            showGame = true
                        }
                    )

                    PastelTile(
                        text = "Medium",
                        backgroundColor = PastelLavender,
                        onClick = {
                            gameViewModel.startGame(Difficulty.MEDIUM)
                            showGame = true
                        }
                    )

                    PastelTile(
                        text = "Hard",
                        backgroundColor = PastelBlush,
                        onClick = {
                            gameViewModel.startGame(Difficulty.HARD)
                            showGame = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }

            // Settings button at bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { showSettings = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Settings",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastelTile(
    text: String,
    backgroundColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                color = androidx.compose.ui.graphics.Color(0xFF5A5A5A),
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp
            )
        }
    }
}