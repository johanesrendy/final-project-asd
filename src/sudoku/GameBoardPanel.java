package sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60; // Cell width/height in pixels
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels

    // Define properties
    /** The game board composes of 9x9 Cells (customized JTextFields) */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /** It also contains a Puzzle with array numbers and isGiven */
    private Puzzle puzzle = new Puzzle();

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE)); // JPanel

        // Allocate the 2D array of Cell, and added into JPanel.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]); // JPanel
            }
        }

        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
        // Cells (JTextFields)
        CellInputListener listener = new CellInputListener();

        // [TODO 4] Adds this common listener to all editable cells
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener); // For all editable rows and cols
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    /**
     * Generate a new puzzle; and reset the gameboard of cells based on the puzzle.
     * You can call this method to start a new game.
     */
    public void newGame() {
        // Generate a new puzzle
        puzzle.newPuzzle(10);

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    public void solve() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], true);
            }
        }
        if (isSolved()) {
            JOptionPane.showMessageDialog(null, "Congratulations! You have solved the Sudoku Puzzle!");
        }

    }

    public void hint() {
        // Temukan sel kosong yang dapat diisi dengan petunjuk
        Cell emptyCell = findEmptyCell();

        if (emptyCell != null) {
            // Isi sel kosong dengan petunjuk dari puzzle
            emptyCell.setText(Integer.toString(puzzle.numbers[emptyCell.getRow()][emptyCell.getCol()]));
            emptyCell.setEditable(false); // Set sel tidak dapat diedit
            emptyCell.status = CellStatus.GIVEN; // Set status sel menjadi diberikan
            emptyCell.paint(); // Re-paint sel

            // Cek apakah pemain telah menyelesaikan puzzle setelah petunjuk ini
            if (isSolved()) {
                JOptionPane.showMessageDialog(null, "Congratulations! You have solved the Sudoku Puzzle!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No more hints available.");
        }
    }

    // Metode untuk mencari sel kosong di papan permainan
    private Cell findEmptyCell() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                Cell cell = cells[row][col];

                // Cek apakah sel dapat diisi dan belum diisi oleh pengguna atau nilai yang
                // salah
                if (cell.isEditable() && (cell.getText().isEmpty() || cell.status == CellStatus.WRONG_GUESS)) {
                    return cell;
                }
            }
        }
        return null; // Jika tidak ada sel kosong yang dapat diisi
    }

    public void submit() {
        boolean allCellsFilled = true;

        // Cek setiap sel
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                Cell cell = cells[row][col];

                // Jika sel dapat diisi (editable) dan masih kosong
                if (cell.isEditable() && cell.getText().isEmpty()) {
                    allCellsFilled = false;
                    cell.setBackground(Color.RED); // Ubah warna latar belakang menjadi merah
                }
            }
        }

        // Jika masih ada sel yang kosong, tampilkan pesan kesalahan
        if (!allCellsFilled) {
            JOptionPane.showMessageDialog(null, "Oops! There are empty cells. Please fill in all cells.");
            return;
        }

        // Semua sel sudah diisi, lakukan pemeriksaan
        boolean allCellsCorrect = true;
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                Cell cell = cells[row][col];

                // Cek apakah sel dapat diisi dan tidak kosong
                if (cell.isEditable() && !cell.getText().isEmpty()) {
                    // Cek angka di sel dengan angka di puzzle
                    if (Integer.parseInt(cell.getText()) != puzzle.numbers[row][col]) {
                        allCellsCorrect = false;
                        cell.status = CellStatus.WRONG_GUESS;
                    } else {
                        cell.status = CellStatus.CORRECT_GUESS;
                    }
                    cell.paint(); // Re-paint sel
                }
            }
        }

        // Tampilkan pesan berdasarkan hasil pemeriksaan
        if (allCellsCorrect) {
            JOptionPane.showMessageDialog(null, "Congratulations! You have solved the Sudoku Puzzle!");
        } else {
            JOptionPane.showMessageDialog(null, "Oops! Some cells are filled incorrectly. Please review your answers.");
        }

    }

    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    // [TODO 2] Define a Listener Inner Class for all the editable Cells
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference of the JTextField that triggers this action event
            Cell sourceCell = (Cell) e.getSource();

            // Retrieve the int entered
            int numberIn = Integer.parseInt(sourceCell.getText());
            // For debugging
            System.out.println("You entered " + numberIn);

            /*
             * [TODO 5] (later - after TODO 3 and 4)
             * Check the numberIn against sourceCell.number.
             * Update the cell status sourceCell.status,
             * and re-paint the cell via sourceCell.paint().
             */
            if (numberIn == sourceCell.number) {
                sourceCell.status = CellStatus.CORRECT_GUESS;
            } else {
                sourceCell.status = CellStatus.WRONG_GUESS;
            }
            sourceCell.paint(); // re-paint this cell based on its status

            /*
             * [TODO 6] (later)
             * Check if the player has solved the puzzle after this move,
             * by calling isSolved(). Put up a congratulation JOptionPane, if so.
             */
            if (isSolved()) {
                JOptionPane.showMessageDialog(null, "Congratulations! You have solved the Sudoku Puzzle!");
            }

        }
    }
}