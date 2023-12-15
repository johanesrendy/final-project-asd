package sudoku;

import java.util.Random;

public class SudokuBlankGenerator {
    private final boolean[][] isGiven;
    private final int cellsToGuess;

    public SudokuBlankGenerator(int cellsToGuess) {
        this.isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
        this.cellsToGuess = cellsToGuess;
        initializeIsGiven();
        randomizeIsGiven();
    }

    private void initializeIsGiven() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                isGiven[row][col] = true;
            }
        }
    }

    private void randomizeIsGiven() {
        Random random = new Random();
        int remainingCellsToGuess = cellsToGuess;

        while (remainingCellsToGuess > 0) {
            int row = random.nextInt(SudokuConstants.GRID_SIZE);
            int col = random.nextInt(SudokuConstants.GRID_SIZE);

            // If the cell is not already marked as to be guessed
            if (isGiven[row][col]) {
                isGiven[row][col] = false;
                remainingCellsToGuess--;
            }
        }
    }

    public boolean[][] getIsGiven() {
        return isGiven;
    }
}
