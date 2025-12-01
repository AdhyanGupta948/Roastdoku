package com.roastdoku.roast

class RoastEngine {

    private val mistakeRoasts = listOf(
        "Bro typed {number} on ({row},{col}) like he's speedrunning disappointment.",
        "{number}? THERE?? Sudoku gods are facepalming rn.",
        "If brain had RAM, yours just thermal throttled harder than Exynos.",
        "Wrong move detected. Even the empty cells are judging you 💀.",
        "My guy, that number fits there like Samsung ads fit inside settings.",
        "That's not a mistake, that's a personality trait now 😭.",
        "Bro is choosing violence against the puzzle at this point.",
        "That move was so wrong, even your calculator sighed.",
        "If guesses were crimes, you'd be in jail for life.",
        "Sudoku: 1 • You: 0 — keep trying champ 😂"
    )

    fun generateMistakeRoast(number: Int, row: Int, col: Int): String {
        val roast = mistakeRoasts.random()
        return roast
            .replace("{number}", number.toString())
            .replace("{row}", row.toString())
            .replace("{col}", col.toString())
    }
}