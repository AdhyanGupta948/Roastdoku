package com.roastdoku.util

/**
 * Utility for comparing semantic version strings
 */
object VersionComparator {
    
    /**
     * Compares two version strings
     * @return true if newVersion is greater than currentVersion
     */
    fun isNewerVersion(currentVersion: String, newVersion: String): Boolean {
        val current = parseVersion(currentVersion)
        val new = parseVersion(newVersion)
        
        android.util.Log.d("VersionComparator", "Comparing: Local=$current vs Remote=$new")
        val isNewer = new > current
        android.util.Log.d("VersionComparator", "Result: isNewer=$isNewer")
        
        return isNewer
    }
    
    /**
     * Parses version string into comparable list of integers
     * Handles formats: "1.0.1", "v1.0.1", "1.0", etc.
     */
    private fun parseVersion(version: String): List<Int> {
        // Remove 'v' prefix if present
        val cleanVersion = version.trim().removePrefix("v").removePrefix("V")
        
        // Split by '.' and convert to integers
        return cleanVersion.split(".")
            .mapNotNull { part ->
                // Extract only numeric part (handles "1.0.1-beta" -> "1.0.1")
                part.takeWhile { it.isDigit() }.toIntOrNull()
            }
    }
    
    /**
     * Extension function to compare version lists
     */
    private operator fun List<Int>.compareTo(other: List<Int>): Int {
        val maxLength = maxOf(this.size, other.size)
        
        for (i in 0 until maxLength) {
            val thisPart = this.getOrNull(i) ?: 0
            val otherPart = other.getOrNull(i) ?: 0
            
            if (thisPart != otherPart) {
                return thisPart.compareTo(otherPart)
            }
        }
        
        return 0
    }
}
