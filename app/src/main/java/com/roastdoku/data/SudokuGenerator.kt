package com.roastdoku.data

import kotlin.random.Random

class SudokuGenerator {
    
    private val solver = SudokuSolver()
    
    fun generateCompleteBoard(): Array<IntArray> {
        val board = Array(9) { IntArray(9) }
        
        // Fill diagonal 3x3 boxes first (they are independent)
        fillDiagonalBoxes(board)
        
        // Solve the rest
        solver.solve(board)
        
        return board
    }
    
    private fun fillDiagonalBoxes(board: Array<IntArray>) {
        for (box in 0..2) {
            fillBox(board, box * 3, box * 3)
        }
    }
    
    private fun fillBox(board: Array<IntArray>, row: Int, col: Int) {
        val numbers = (1..9).shuffled()
        var index = 0
        for (i in 0..2) {
            for (j in 0..2) {
                board[row + i][col + j] = numbers[index++]
            }
        }
    }

    data class PuzzleResult(
        val puzzle: Array<IntArray>,
        val solution: Array<IntArray>
    )

    fun createPuzzle(difficulty: Difficulty): PuzzleResult {
        var completeBoard = generateCompleteBoard()
        var puzzle: Array<IntArray>
        var attempts = 0
        val maxAttempts = 100

        do {
            completeBoard = generateCompleteBoard()
            puzzle = completeBoard.map { it.copyOf() }.toTypedArray()
            removeCells(puzzle, difficulty.cellsToRemove)
            attempts++
        } while (!solver.hasUniqueSolution(puzzle) && attempts < maxAttempts)

        if (attempts >= maxAttempts) {
            completeBoard = generateCompleteBoard()
            puzzle = completeBoard.map { it.copyOf() }.toTypedArray()
            removeCells(puzzle, difficulty.cellsToRemove - 5)
        }

        return PuzzleResult(puzzle, completeBoard)
    }
    
    private fun removeCells(board: Array<IntArray>, count: Int) {
        var removed = 0
        val random = Random.Default
        val positions = mutableSetOf<Pair<Int, Int>>()
        
        while (removed < count && positions.size < 81) {
            val row = random.nextInt(9)
            val col = random.nextInt(9)
            val pos = Pair(row, col)
            
            if (!positions.contains(pos) && board[row][col] != 0) {
                val temp = board[row][col]
                board[row][col] = 0
                
                // Check if still has unique solution
                if (solver.hasUniqueSolution(board)) {
                    removed++
                } else {
                    board[row][col] = temp // Restore if breaks uniqueness
                }
                
                positions.add(pos)
            }
        }
    }
}

