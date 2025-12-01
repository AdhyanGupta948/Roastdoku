package com.roastdoku.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.roastdoku.viewmodel.SettingsViewModel
import com.roastdoku.viewmodel.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val roastEnabled by viewModel.roastEnabled.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    
    var showInfoPage by remember { mutableStateOf(false) }

    if (showInfoPage) {
        DeveloperInfoScreen(
            onBack = { showInfoPage = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Settings") },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingsItem(
                    title = "Roast Bot",
                    checked = roastEnabled,
                    onCheckedChange = { viewModel.toggleRoast() }
                )
                
                Divider()
                
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleMedium
                )
                
                ThemeOption(
                    title = "Light",
                    selected = themeMode == ThemeMode.LIGHT,
                    onClick = { viewModel.setTheme(ThemeMode.LIGHT) }
                )
                
                ThemeOption(
                    title = "Dark",
                    selected = themeMode == ThemeMode.DARK,
                    onClick = { viewModel.setTheme(ThemeMode.DARK) }
                )
                
                ThemeOption(
                    title = "AMOLED Black",
                    selected = themeMode == ThemeMode.AMOLED,
                    onClick = { viewModel.setTheme(ThemeMode.AMOLED) }
                )

                Divider()

                Button(
                    onClick = { showInfoPage = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("About the Developer")
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun ThemeOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeveloperInfoScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Developer Info") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("=============================")
            Text("       About the Developer")
            Text("=============================")
            Text("")
            Text("Name: Adhyan Gupta")
            Text("Role: Lead Developer, Designer, and Everything Else 😎")
            Text("Experience: Solo Project Enthusiast | Code Wizard | Bug Slayer")
            Text("")
            Text("Bio:")
            Text(
                "Hi! I’m Adhyan, the one-man army behind this app. I love turning ideas into clean, functional code and making user experiences smooth as butter. This project was crafted with care, late-night coffee, and a bit of stubbornness. 💻☕️"
            )
            Text("")
            Text("Contact:")
            Text(
                text = "Email: adhyan948@gmail.com",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    uriHandler.openUri("mailto:adhyan948@gmail.com")
                }
            )
            Text(
                text = "GitHub: https://github.com/AdhyanGupta948",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    uriHandler.openUri("https://github.com/AdhyanGupta948")
                }
            )
            Text("")
            Text("Fun Fact:")
            Text("I built this app solo, so technically I had meetings with myself. And guess what? I always agreed. 😎")
        }
    }
}

