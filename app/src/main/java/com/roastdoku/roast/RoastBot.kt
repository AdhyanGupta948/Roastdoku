package com.roastdoku.roast

class RoastBot(
    private val repo: RoastRepository = RoastRepository(),
    private val engine: RoastEngine = RoastEngine()
) {

    fun getWrongInputRoast(number: Int, row: Int, col: Int): String {
        return engine.generateMistakeRoast(number, row, col)
    }

    fun getMilestoneRoast(): String {
        return repo.getRoast(RoastType.MILESTONE)
    }

    fun getCompletionRoast(): String {
        return repo.getRoast(RoastType.COMPLETION)
    }

    fun getInactivityRoast(): String {
        return repo.getRoast(RoastType.INACTIVITY)
    }
}