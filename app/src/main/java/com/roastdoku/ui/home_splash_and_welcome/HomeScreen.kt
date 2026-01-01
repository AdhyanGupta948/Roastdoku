package com.roastdoku.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roastdoku.data.Difficulty
import com.roastdoku.ui.game.GameScreen
import com.roastdoku.ui.settings.SettingsScreen
import com.roastdoku.ui.theme.*
import com.roastdoku.ui.update.UpdateDialog
import com.roastdoku.update.DownloadState
import com.roastdoku.viewmodel.GameViewModel
import com.roastdoku.viewmodel.SettingsViewModel
import com.roastdoku.viewmodel.UpdateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBackToWelcome: () -> Unit = {}
) {
    val gameViewModel: GameViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val updateViewModel: UpdateViewModel = viewModel()

    var showGame by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.MEDIUM) }
    
    // Collect update states
    val updateInfo by updateViewModel.updateInfo.collectAsState()
    val downloadState by updateViewModel.downloadState.collectAsState()
    val showUpdateDialog by updateViewModel.showDialog.collectAsState()
    val autoUpdateEnabled by settingsViewModel.autoUpdateEnabled.collectAsState()
    
    // Check for updates when HomeScreen appears (if enabled in settings)
    LaunchedEffect(Unit) {
        if (autoUpdateEnabled) {
            updateViewModel.checkForUpdate()
        }
    }
    
    // Auto-trigger installer when download completes
    LaunchedEffect(downloadState) {
        if (downloadState is DownloadState.Completed) {
            val filePath = (downloadState as DownloadState.Completed).filePath
            updateViewModel.triggerInstall(filePath)
        }
    }

    // Handle back button
    BackHandler {
        when {
            showSettings -> showSettings = false
            showGame -> showGame = false
            else -> onBackToWelcome()
        }
    }

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
                .background(Color(0xFF1A1D2E))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Back action */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Select Difficulty",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }

                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Choose Your Challenge",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 38.sp
                    )

                    Text(
                        text = "Pick the mode that matches your skill level.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Difficulty Cards
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DifficultyCard(
                        title = "Easy",
                        description = "Relaxed gameplay.",
                        emoji = "😀",
                        stars = 1,
                        isSelected = selectedDifficulty == Difficulty.EASY,
                        backgroundColor = Color(0xFF2D4A3E),
                        accentColor = Color(0xFF4ADE80),
                        onClick = { selectedDifficulty = Difficulty.EASY }
                    )

                    DifficultyCard(
                        title = "Medium",
                        description = "The standard experience.",
                        emoji = "⚖️",
                        stars = 2,
                        isSelected = selectedDifficulty == Difficulty.MEDIUM,
                        backgroundColor = Color(0xFF2A3F5F),
                        accentColor = Color(0xFF60A5FA),
                        onClick = { selectedDifficulty = Difficulty.MEDIUM }
                    )

                    DifficultyCard(
                        title = "Hard",
                        description = "Strategic & unforgiving.",
                        emoji = "💀",
                        stars = 3,
                        isSelected = selectedDifficulty == Difficulty.HARD,
                        backgroundColor = Color(0xFF4A2D3E),
                        accentColor = Color(0xFFEF4444),
                        onClick = { selectedDifficulty = Difficulty.HARD }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Start Button
                Button(
                    onClick = {
                        gameViewModel.startGame(selectedDifficulty)
                        showGame = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                        .height(64.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB)
                    )
                ) {
                    Text(
                        text = "Start Game",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "▶",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
            
            // Update Dialog
            if (showUpdateDialog && updateInfo != null) {
                UpdateDialog(
                    updateInfo = updateInfo!!,
                    downloadState = downloadState,
                    onUpdateClick = { updateViewModel.startDownload() },
                    onDismiss = { updateViewModel.dismissDialog() }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultyCard(
    title: String,
    description: String,
    emoji: String,
    stars: Int,
    isSelected: Boolean,
    backgroundColor: Color,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = accentColor,
                        shape = RoundedCornerShape(24.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) accentColor else Color.White
                    )

                    if (isSelected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = accentColor.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "SELECTED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Text(
                    text = description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Stars
                Row(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    repeat(3) { index ->
                        Text(
                            text = "★",
                            fontSize = 16.sp,
                            color = if (index < stars) accentColor else Color.White.copy(alpha = 0.2f)
                        )
                        if (index < 2) Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            // Icon circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 36.sp
                )
            }
        }
    }
}