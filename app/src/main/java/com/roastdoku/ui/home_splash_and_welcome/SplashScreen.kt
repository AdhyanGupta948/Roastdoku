package com.roastdoku.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import com.roastdoku.R

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    // Auto-navigate after 2 seconds
    LaunchedEffect(Unit) {
        delay(2000)
        onSplashComplete()
    }

    // Fade in animation for text
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800),
        label = "alpha"
    )

    // Local Font configuration
    val fontFamily = FontFamily(
        Font(R.font.baumans_regular, FontWeight.Normal)
    )

    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF1E3A8A), // Deep blue
                    Color(0xFF3B82F6), // Medium blue
                    Color(0xFF60A5FA), // Light blue
                    Color(0xFF93C5FD)  // Very light blue
                )
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Roastdoku",
            fontSize = 56.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = alpha),
            letterSpacing = -4.sp
        )
    }
}