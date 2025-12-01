package com.roastdoku.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roastdoku.data.Difficulty
import com.roastdoku.data.SudokuGenerator
import com.roastdoku.roast.RoastBot
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Cell(
    val row: Int,
    val col: Int,
    var value: Int,
    val isGiven: Boolean,
    var isError: Boolean = false
)

class GameViewModel : ViewModel() {

    private val generator = SudokuGenerator()
    private val roastBot = RoastBot()

    private val _board = MutableStateFlow<List<List<Cell>>>(emptyList())
    val board: StateFlow<List<List<Cell>>> = _board.asStateFlow()

    private val _selectedCell = MutableStateFlow<Pair<Int, Int>?>(null)
    val selectedCell: StateFlow<Pair<Int, Int>?> = _selectedCell.asStateFlow()

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete.asStateFlow()

    private val _mistakes = MutableStateFlow(0)
    val mistakes: StateFlow<Int> = _mistakes.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()

    private val _currentRoast = MutableStateFlow<String?>(null)
    val currentRoast: StateFlow<String?> = _currentRoast.asStateFlow()

    private val _showNumberPad = MutableStateFlow(false)
    val showNumberPad: StateFlow<Boolean> = _showNumberPad.asStateFlow()

    private var timerJob: Job? = null
    private var inactivityJob: Job? = null

    private var solution: Array<IntArray> = Array(9) { IntArray(9) }
    private var difficulty: Difficulty = Difficulty.EASY


    // ---------------------------------------------------------------------
    // GAME SETUP
    // ---------------------------------------------------------------------
    fun startGame(difficulty: Difficulty) {
        this.difficulty = difficulty

        val result = generator.createPuzzle(difficulty)
        val puzzle = result.puzzle
        solution = result.solution

        _board.value = puzzle.mapIndexed { row, rowData ->
            rowData.mapIndexed { col, value ->
                Cell(row, col, value, value != 0)
            }
        }

        _selectedCell.value = null
        _isComplete.value = false
        _mistakes.value = 0
        _elapsedTime.value = 0L
        _currentRoast.value = null

        startTimer()
        resetInactivityTimer()
    }


    // ---------------------------------------------------------------------
    // TIMER
    // ---------------------------------------------------------------------
    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (!_isComplete.value) {
                delay(1000)
                _elapsedTime.value++

                // Roast every 60 seconds
                if (_elapsedTime.value > 0 && _elapsedTime.value % 60 == 0L) {
                    showRoast(roastBot.getMilestoneRoast())
                }
            }
        }
    }


    // ---------------------------------------------------------------------
    // USER INTERACTIONS
    // ---------------------------------------------------------------------
    fun selectCell(row: Int, col: Int) {
        val cell = _board.value[row][col]
        if (!cell.isGiven) {
            _selectedCell.value = Pair(row, col)
            _showNumberPad.value = true
            resetInactivityTimer()
        }
    }

    fun placeNumber(number: Int) {
        val selected = _selectedCell.value ?: return
        val (row, col) = selected

        val boardCopy = deepCopyBoard()
        val cell = boardCopy[row][col]

        if (!cell.isGiven) {
            if (number == solution[row][col]) {
                cell.value = number
                cell.isError = false
            } else {
                cell.value = number
                cell.isError = true
                _mistakes.value++
                showRoast(
                    roastBot.getWrongInputRoast(
                        number = number,
                        row = row,
                        col = col
                    )
                )
            }

            _board.value = boardCopy
            checkCompletion()
        }

        _showNumberPad.value = false
        resetInactivityTimer()
    }

    fun clearCell() {
        val selected = _selectedCell.value ?: return
        val (row, col) = selected

        val boardCopy = deepCopyBoard()
        val cell = boardCopy[row][col]

        if (!cell.isGiven) {
            cell.value = 0
            cell.isError = false
            _board.value = boardCopy
        }

        _showNumberPad.value = false
        resetInactivityTimer()
    }


    // ---------------------------------------------------------------------
    // LOGIC
    // ---------------------------------------------------------------------
    private fun checkCompletion() {
        val board = _board.value

        val allFilled = board.all { row -> row.all { it.value != 0 } }
        val noErrors = board.all { row -> row.none { it.isError } }

        if (allFilled && noErrors) {
            _isComplete.value = true
            timerJob?.cancel()

            showRoast(roastBot.getCompletionRoast())
        }
    }

    private fun deepCopyBoard(): MutableList<MutableList<Cell>> {
        return _board.value.map { row ->
            row.map { cell -> cell.copy() }.toMutableList()
        }.toMutableList()
    }


    // ---------------------------------------------------------------------
    // ROAST HANDLING
    // ---------------------------------------------------------------------
    private fun resetInactivityTimer() {
        inactivityJob?.cancel()

        inactivityJob = viewModelScope.launch {
            delay(30_000) // 30 seconds
            showRoast(roastBot.getInactivityRoast())
        }
    }

    private fun showRoast(message: String) {
        _currentRoast.value = message
        viewModelScope.launch {
            delay(4000)
            _currentRoast.value = null
        }
    }

    fun dismissRoast() {
        _currentRoast.value = null
    }


    // ---------------------------------------------------------------------
    // TIMER FORMAT
    // ---------------------------------------------------------------------
    fun formatTime(seconds: Long): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }


    // ---------------------------------------------------------------------
    // CLEANUP
    // ---------------------------------------------------------------------
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        inactivityJob?.cancel()
    }
}
