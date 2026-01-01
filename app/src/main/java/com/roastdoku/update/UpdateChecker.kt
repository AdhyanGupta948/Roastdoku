package com.roastdoku.update

import android.content.Context
import com.roastdoku.util.VersionComparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * GitHub API service interface
 */
interface GitHubApiService {
    @GET("repos/AdhyanGupta948/Roastdoku/releases/latest")
    suspend fun getLatestRelease(): GitHubRelease
}

/**
 * Service to check for app updates from GitHub releases
 */
class UpdateChecker(private val context: Context) {
    
    private val apiService: GitHubApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }
    
    /**
     * Checks if a newer version is available
     * @return UpdateInfo if update available, null otherwise
     */
    suspend fun checkForUpdate(): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val release = apiService.getLatestRelease()
            val currentVersion = getCurrentVersion()
            val latestVersion = release.tagName
            
            android.util.Log.d("UpdateChecker", "Checking for updates...")
            android.util.Log.d("UpdateChecker", "Current App Version: $currentVersion")
            android.util.Log.d("UpdateChecker", "Latest GitHub Version: $latestVersion")
            
            // Check if newer version is available
            if (VersionComparator.isNewerVersion(currentVersion, latestVersion)) {
                android.util.Log.d("UpdateChecker", "Newer version found!")
                // Find APK asset
                val apkAsset = release.assets.firstOrNull { asset ->
                    asset.name.endsWith(".apk", ignoreCase = true)
                }
                
                if (apkAsset != null) {
                    return@withContext UpdateInfo(
                        version = latestVersion,
                        downloadUrl = apkAsset.downloadUrl,
                        releaseNotes = release.body ?: "No release notes available"
                    )
                }
            }
            
            null
        } catch (e: Exception) {
            // Network error or API error - return null (no update)
            e.printStackTrace()
            null
        }
    }
    
    private fun getCurrentVersion(): String {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val version = pInfo.versionName
            if (version != null) {
                return version
            } else {
                android.util.Log.w("UpdateChecker", "versionName was null, falling back to 1.0")
                return "1.0"
            }
        } catch (e: Exception) {
            android.util.Log.e("UpdateChecker", "Error getting package info", e)
            return "1.0" // Fallback version
        }
    }
}
