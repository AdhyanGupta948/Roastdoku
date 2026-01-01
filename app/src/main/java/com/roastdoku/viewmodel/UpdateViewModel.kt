package com.roastdoku.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.roastdoku.update.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel to manage app update flow
 */
class UpdateViewModel(application: Application) : AndroidViewModel(application) {
    
    private val updateChecker = UpdateChecker(application)
    private val apkDownloader = ApkDownloader(application)
    
    private val _updateInfo = MutableStateFlow<UpdateInfo?>(null)
    val updateInfo: StateFlow<UpdateInfo?> = _updateInfo.asStateFlow()
    
    private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Idle)
    val downloadState: StateFlow<DownloadState> = _downloadState.asStateFlow()
    
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    
    /**
     * Checks for available updates
     */
    fun checkForUpdate() {
        viewModelScope.launch {
            try {
                val update = updateChecker.checkForUpdate()
                if (update != null) {
                    _updateInfo.value = update
                    _showDialog.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Silently fail - don't disrupt user experience
            }
        }
    }
    
    /**
     * Starts downloading the update APK
     */
    fun startDownload() {
        val url = _updateInfo.value?.downloadUrl ?: return
        
        viewModelScope.launch {
            _downloadState.value = DownloadState.Downloading(0)
            
            val filePath = apkDownloader.downloadApk(url) { progress ->
                _downloadState.value = DownloadState.Downloading(progress)
            }
            
            if (filePath != null) {
                _downloadState.value = DownloadState.Completed(filePath)
            } else {
                _downloadState.value = DownloadState.Failed("Download failed. Please try again.")
            }
        }
    }
    
    /**
     * Triggers the APK installation
     */
    fun triggerInstall(filePath: String) {
        try {
            ApkInstaller.installApk(getApplication(), filePath)
        } catch (e: Exception) {
            e.printStackTrace()
            _downloadState.value = DownloadState.Failed("Installation failed: ${e.message}")
        }
    }
    
    /**
     * Dismisses the update dialog
     */
    fun dismissDialog() {
        _showDialog.value = false
        _downloadState.value = DownloadState.Idle
    }
    
    /**
     * Checks if app can install packages
     */
    fun canInstallPackages(): Boolean {
        return ApkInstaller.canRequestPackageInstalls(getApplication())
    }
}
