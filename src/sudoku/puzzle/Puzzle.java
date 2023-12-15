package sudoku.puzzle;

import java.util.Random;

import sudoku.SudokuBlankGenerator;
import sudoku.SudokuConstants;
import sudoku.SudokuGenerator;

/**
 * The Sudoku number puzzle to be solved
 */
public class Puzzle {
    // All variables have package access
    // The numbers on the puzzle
    int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    // The clues - isGiven (no need to guess) or need to guess
    boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];

    // Constructor
    public Puzzle() {
        super();
    }

    // Generate a new puzzle given the number of cells to be guessed, which can be
    // used
    // to control the difficulty level.
    // This method shall set (or update) the arrays numbers and isGiven
    public void newPuzzle(int cellsToGuess) {
        // I hardcode a puzzle here for illustration and testing.
        int[][] hardcodedNumbers = SudokuGenerator.generateSudoku();

        // Copy from hardcodedNumbers into the array "numbers"
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                numbers[row][col] = hardcodedNumbers[row][col];
            }
        }

        // Use the Randomizer class to randomize "isGiven"
        SudokuBlankGenerator randomizer = new SudokuBlankGenerator(cellsToGuess);
        boolean[][] hardcodedIsGiven = randomizer.getIsGiven();

        // Randomly set "isGiven" cells
        Random random = new Random();
        int remainingCellsToGuess = cellsToGuess;

        while (remainingCellsToGuess > 0) {
            int row = random.nextInt(SudokuConstants.GRID_SIZE);
            int col = random.nextInt(SudokuConstants.GRID_SIZE);

            // If the cell is not already marked as to be guessed
            if (!isGiven[row][col]) {
                isGiven[row][col] = true;
                remainingCellsToGuess--;
            }
        }

        // Copy from hardcodedIsGiven into array "isGiven"
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                isGiven[row][col] = hardcodedIsGiven[row][col];
            }
        }
    }

    // (For advanced students) use singleton design pattern for this class
}