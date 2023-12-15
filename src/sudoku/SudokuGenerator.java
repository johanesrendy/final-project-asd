package sudoku;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SudokuGenerator {

    public static void main(String[] args) {
        int[][] sudoku = generateSudoku();
        printSudoku(sudoku);
    }

    public static int[][] generateSudoku() {
        int[][] sudoku = new int[9][9];
        generateSudokuHelper(sudoku);
        return sudoku;
    }

    private static boolean generateSudokuHelper(int[][] sudoku) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (sudoku[row][col] == 0) {
                    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
                    Collections.shuffle(numbers);
                    for (int num : numbers) {
                        if (isValidMove(sudoku, row, col, num)) {
                            sudoku[row][col] = num;
                            if (generateSudokuHelper(sudoku)) {
                                return true;
                            }
                            sudoku[row][col] = 0; // Backtrack
                        }
                    }
                    return false; // No valid number found for this cell
                }
            }
        }
        return true; // Successfully filled the entire board
    }

    private static boolean isValidMove(int[][] sudoku, int row, int col, int num) {
        // Check if the number is not in the current row and column
        for (int i = 0; i < 9; i++) {
            if (sudoku[row][i] == num || sudoku[i][col] == num) {
                return false;
            }
        }

        // Check if the number is not in the current 3x3 box
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudoku[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void printSudoku(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku[i][j] + " ");
            }
            System.out.println();
        }
    }
}
