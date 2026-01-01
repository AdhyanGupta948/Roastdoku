package com.roastdoku.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * Utility to trigger system APK installer
 */
object ApkInstaller {
    
    /**
     * Opens the system installer for the downloaded APK
     * @param context Android context
     * @param apkFilePath Absolute path to the downloaded APK file
     */
    fun installApk(context: Context, apkFilePath: String) {
        val apkFile = File(apkFilePath)
        
        if (!apkFile.exists()) {
            throw IllegalArgumentException("APK file does not exist: $apkFilePath")
        }
        
        val apkUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Android 7.0+ requires FileProvider
            FileProvider.getUriForFile(
                context,
                "com.roastdoku.fileprovider",
                apkFile
            )
        } else {
            Uri.fromFile(apkFile)
        }
        
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        
        context.startActivity(intent)
    }
    
    /**
     * Checks if the app has permission to install packages
     * Required for Android 8.0 (API 26) and above
     */
    fun canRequestPackageInstalls(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.canRequestPackageInstalls()
        } else {
            true // Not required for older versions
        }
    }
}
