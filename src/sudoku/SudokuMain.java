package sudoku;

import java.awt.*;
import javax.swing.*;

/**
 * The main Sudoku program
 */
public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L; // to prevent serial warning

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("New Game");
    JButton btnSolve = new JButton("Solve");

    // Constructor
    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        // Panel for button container
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // new game button
        JButton btnNewGame = new JButton("New Game");
        btnNewGame.addActionListener(e -> board.newGame());
        buttonPanel.add(btnNewGame);

        // solve button
        JButton btnSolve = new JButton("Solve");
        btnSolve.addActionListener(e -> board.solve());
        buttonPanel.add(btnSolve);

        // hint button
        JButton btnHint = new JButton("Hint");
        btnHint.addActionListener(e -> board.hint());
        buttonPanel.add(btnHint);

        // submit button
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(e -> board.submit());
        buttonPanel.add(btnSubmit);

        // add all button to the south
        cp.add(buttonPanel, BorderLayout.SOUTH);

        // add board to the center
        cp.add(board, BorderLayout.CENTER);

        // Initialize the game board to start the game
        board.newGame();

        pack(); // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    /** The entry main() entry method */
    public static void main(String[] args) {
        // [TODO 1] Check "Swing program template" on how to run
        // the constructor of "SudokuMain"
        SwingUtilities.invokeLater(() -> {
            new SudokuMain();
        });
    }
}