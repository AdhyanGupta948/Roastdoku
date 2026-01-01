package com.roastdoku.update

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

/**
 * Service to download APK files from GitHub releases
 */
class ApkDownloader(private val context: Context) {
    
    private val client = OkHttpClient()
    
    /**
     * Downloads APK from URL with progress callback
     * @param url Download URL from GitHub release
     * @param onProgress Callback for progress updates (0-100)
     * @return File path of downloaded APK, or null on failure
     */
    suspend fun downloadApk(
        url: String,
        onProgress: (Int) -> Unit
    ): String? = withContext(Dispatchers.IO) {
        try {
            // Create updates directory in cache
            val updatesDir = File(context.cacheDir, "updates")
            if (!updatesDir.exists()) {
                updatesDir.mkdirs()
            }
            
            // Create APK file
            val apkFile = File(updatesDir, "roastdoku_update.apk")
            if (apkFile.exists()) {
                apkFile.delete()
            }
            
            // Download APK
            val request = Request.Builder()
                .url(url)
                .build()
            
            val response = client.newCall(request).execute()
            
            if (!response.isSuccessful) {
                return@withContext null
            }
            
            val body = response.body ?: return@withContext null
            val contentLength = body.contentLength()
            
            // Stream download with progress tracking
            body.byteStream().use { input ->
                FileOutputStream(apkFile).use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    var totalBytesRead = 0L
                    
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        
                        // Calculate and report progress
                        if (contentLength > 0) {
                            val progress = (totalBytesRead * 100 / contentLength).toInt()
                            withContext(Dispatchers.Main) {
                                onProgress(progress)
                            }
                        }
                    }
                }
            }
            
            apkFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
