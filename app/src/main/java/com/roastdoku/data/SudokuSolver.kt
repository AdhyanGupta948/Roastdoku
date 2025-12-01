package com.roastdoku.data

class SudokuSolver {
    
    fun isValid(board: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        // Check row
        for (x in 0..8) {
            if (board[row][x] == num) return false
        }
        
        // Check column
        for (x in 0..8) {
            if (board[x][col] == num) return false
        }
        
        // Check 3x3 box
        val startRow = row - row % 3
        val startCol = col - col % 3
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i + startRow][j + startCol] == num) return false
            }
        }
        
        return true
    }
    
    fun solve(board: Array<IntArray>): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                if (board[row][col] == 0) {
                    for (num in 1..9) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num
                            if (solve(board)) {
                                return true
                            }
                            board[row][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }
    
    fun hasUniqueSolution(board: Array<IntArray>): Boolean {
        val copy = board.map { it.copyOf() }.toTypedArray()
        val solutionCount = countSolutions(copy, 0, 0)
        return solutionCount == 1
    }
    
    private fun countSolutions(board: Array<IntArray>, row: Int, col: Int): Int {
        if (row == 9) {
            return 1
        }
        
        val nextRow = if (col == 8) row + 1 else row
        val nextCol = if (col == 8) 0 else col + 1
        
        if (board[row][col] != 0) {
            return countSolutions(board, nextRow, nextCol)
        }
        
        var count = 0
        for (num in 1..9) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num
                count += countSolutions(board, nextRow, nextCol)
                board[row][col] = 0
                if (count > 1) break // Early exit if multiple solutions
            }
        }
        
        return count
    }
}

