package sudoku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
   private static final long serialVersionUID = 1L;  // to prevent serial warning

   private class CellInputListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
       // Get a reference of the JTextField that triggers this action event
       Cell sourceCell = (Cell)e.getSource();
       
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
          ......
        }
        sourceCell.paint();   // re-paint this cell based on its status

       /*
        * [TODO 6] (later)
        * Check if the player has solved the puzzle after this move,
        *   by calling isSolved(). Put up a congratulation JOptionPane, if so.
        */
        if(isSolved()){
            JOptionPane.showMessageDialog(null, "Congratulation!");
    }
 }
}

public enum CellStatus {
   GIVEN,         // clue, no need to guess
   TO_GUESS,      // need to guess - not attempted yet
   CORRECT_GUESS, // need to guess - correct guess
   WRONG_GUESS    // need to guess - wrong guess
      // The puzzle is solved if none of the cells have 
      //  status of TO_GUESS or WRONG_GUESS
}
   // Define named constants for UI sizes
   public static final int CELL_SIZE = 60;   // Cell width/height in pixels
   public static final int BOARD_WIDTH  = CELL_SIZE * SudokuConstants.GRID_SIZE;
   public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
                                             // Board width/height in pixels

   // Define properties
   /** The game board composes of 9x9 Cells (customized JTextFields) */
   private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
   /** It also contains a Puzzle with array numbers and isGiven */
   private Puzzle puzzle = new Puzzle();

 public GameBoardPanel(){
        CellInputListener listener = new CellInputListener();
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++){
            for (int col = 0 ; col < SudokuConstants.GRID_SIZE;col++){
                cells[row][col] = new Cell(row,col);
                super.add(cells[row][col]);
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);   // For all editable rows and cols
                }
            }
        }
        
        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }
    public void newGame(){
        puzzle.newPuzzle(2);

        for (int row = 0 ;row < SudokuConstants.GRID_SIZE ; row++){
            for (int col = 0 ; col < SudokuConstants.GRID_SIZE ; col++){
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }
    public boolean isSolved(){
        for (int row = 0 ; row < SudokuConstants.GRID_SIZE ; row++){
            for (int col = 0 ; col < SudokuConstants.GRID_SIZE ; col++){
                if(cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS){
                    return false;
                }
            }
        }
        return true;
    }
}