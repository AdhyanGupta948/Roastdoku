package com.roastdoku.update

import com.google.gson.annotations.SerializedName

/**
 * GitHub Release API response model
 */
data class GitHubRelease(
    @SerializedName("tag_name")
    val tagName: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("body")
    val body: String?,
    
    @SerializedName("assets")
    val assets: List<ReleaseAsset>
)

/**
 * GitHub Release Asset (APK file)
 */
data class ReleaseAsset(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("browser_download_url")
    val downloadUrl: String,
    
    @SerializedName("size")
    val size: Long
)

/**
 * Simplified update information for UI
 */
data class UpdateInfo(
    val version: String,
    val downloadUrl: String,
    val releaseNotes: String
)

/**
 * Download state for progress tracking
 */
sealed class DownloadState {
    object Idle : DownloadState()
    data class Downloading(val progress: Int) : DownloadState()
    data class Completed(val filePath: String) : DownloadState()
    data class Failed(val error: String) : DownloadState()
}
