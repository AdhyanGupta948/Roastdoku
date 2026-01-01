package com.roastdoku.ui.welcome

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roastdoku.R
import com.roastdoku.ui.update.UpdateDialog
import com.roastdoku.update.DownloadState
import com.roastdoku.viewmodel.SettingsViewModel
import com.roastdoku.viewmodel.UpdateViewModel


@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {
    val updateViewModel: UpdateViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    
    // Collect update states
    val updateInfo by updateViewModel.updateInfo.collectAsState()
    val downloadState by updateViewModel.downloadState.collectAsState()
    val showUpdateDialog by updateViewModel.showDialog.collectAsState()
    val autoUpdateEnabled by settingsViewModel.autoUpdateEnabled.collectAsState()
    
    // Check for updates on launch
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
    // Fade in animation
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600),
        label = "contentAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A), // Deep blue
                        Color(0xFF1E40AF),
                        Color(0xFF1E293B), // Dark blue-gray
                        Color(0xFF0F172A)  // Very dark
                    )
                )
            )
    ) {
        // Update Dialog
        if (showUpdateDialog && updateInfo != null) {
            UpdateDialog(
                updateInfo = updateInfo!!,
                downloadState = downloadState,
                onUpdateClick = { updateViewModel.startDownload() },
                onDismiss = { updateViewModel.dismissDialog() }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .alpha(contentAlpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Main text content
            val baumansFamily = FontFamily(Font(R.font.baumans_regular, FontWeight.Normal))
            val interFamily = FontFamily(
                Font(R.font.inter_regular, FontWeight.Normal),
                Font(R.font.inter_bold, FontWeight.Bold)
            )

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontFamily = baumansFamily,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = (-1).sp
                            )
                        ) {
                            append("Roastdoku")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontFamily = interFamily,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append(" a suduku\ngame for ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF3B82F6),
                                fontFamily = interFamily,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Android")
                        }
                    },
                    fontSize = 32.sp,
                    lineHeight = 40.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Game with a unique twist: an\nin-game roast bot that playfull roates you",
                    fontSize = 16.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.6f),
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Page indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PageIndicator(isActive = true)
                    PageIndicator(isActive = false)
                    PageIndicator(isActive = false)
                    PageIndicator(isActive = false)
                }
            }

            // Start button
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6)
                )
            ) {
                Text(
                    text = "Start",
                    fontSize = 18.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun PageIndicator(isActive: Boolean) {
    Box(
        modifier = Modifier
            .width(if (isActive) 32.dp else 8.dp)
            .height(8.dp)
            .background(
                color = if (isActive) Color(0xFF3B82F6) else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(4.dp)
            )
    )
}