package com.roastdoku.roast

class RoastRepository {

    private val usedRoasts = mutableSetOf<String>()

    private val milestoneRoasts = listOf(
        "Ayyy you're actually cooking now 🔥",
        "Ok speedrunner, slow down lmao.",
        "Look at you solving numbers like your crush finally replied.",
        "Main character arc unlocked 🔥",
        "Einstein junior moment detected 👀"
    )

    private val completionRoasts = listOf(
        "WTF you actually finished?? Skill issue — NOT TODAY 🔥",
        "Damn bro, even the puzzle didn’t expect that.",
        "Certified Sudoku demon mode.",
        "Your brain finally woke up?? Miracles do happen.",
        "Puzzle: 0 • You: 1 — redemption arc complete."
    )

    private val inactivityRoasts = listOf(
        "Bro went AFK waiting for brain update v2.0.",
        "You alive?? Puzzle ain’t gonna solve itself 😭",
        "Break longer than Samsung animations 💀",
        "Thinking status: buffering… still buffering…",
        "Touch the puzzle na 😭 don’t be shy"
    )

    fun getRoast(type: RoastType): String {
        val list = when (type) {
            RoastType.MILESTONE -> milestoneRoasts
            RoastType.COMPLETION -> completionRoasts
            RoastType.INACTIVITY -> inactivityRoasts
            else -> error("Mistake roasts handled by engine.")
        }

        if (usedRoasts.size == list.size) usedRoasts.clear()

        val available = list.filterNot { usedRoasts.contains(it) }
        val roast = available.random()
        usedRoasts.add(roast)
        return roast
    }
}