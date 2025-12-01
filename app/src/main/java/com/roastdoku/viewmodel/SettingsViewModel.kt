package com.roastdoku.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    LIGHT, DARK, AMOLED
}

class SettingsViewModel : ViewModel() {
    private val _roastEnabled = MutableStateFlow(true)
    val roastEnabled: StateFlow<Boolean> = _roastEnabled.asStateFlow()
    
    private val _themeMode = MutableStateFlow(ThemeMode.DARK)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()
    
    fun toggleRoast() {
        _roastEnabled.value = !_roastEnabled.value
    }
    
    fun setTheme(mode: ThemeMode) {
        _themeMode.value = mode
    }
}

