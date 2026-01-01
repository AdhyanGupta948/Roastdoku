package com.roastdoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roastdoku.ui.home.HomeScreen
import com.roastdoku.ui.splash.SplashScreen
import com.roastdoku.ui.theme.RoastdokuTheme
import com.roastdoku.ui.welcome.WelcomeScreen
import com.roastdoku.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val themeMode by settingsViewModel.themeMode.collectAsState()

            RoastdokuTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        onExit = { finish() } // Close app completely
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(onExit: () -> Unit) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

    // Handle system back button
    BackHandler(enabled = currentScreen != Screen.Splash) {
        when (currentScreen) {
            Screen.Welcome -> onExit() // Exit app from welcome screen
            Screen.Home -> currentScreen = Screen.Welcome // Go back to welcome
            else -> {} // Splash screen - do nothing
        }
    }

    when (currentScreen) {
        Screen.Splash -> {
            SplashScreen(
                onSplashComplete = {
                    currentScreen = Screen.Welcome
                }
            )
        }

        Screen.Welcome -> {
            WelcomeScreen(
                onStartClick = {
                    currentScreen = Screen.Home
                }
            )
        }

        Screen.Home -> {
            HomeScreen(
                onBackToWelcome = {
                    currentScreen = Screen.Welcome
                }
            )
        }
    }
}

sealed class Screen {
    object Splash : Screen()
    object Welcome : Screen()
    object Home : Screen()
}