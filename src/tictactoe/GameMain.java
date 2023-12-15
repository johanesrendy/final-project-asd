package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMain extends JPanel {
    public static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private int playerOnePoints = 0;
    private int playerTwoPoints = 0;

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private int xWins = 0;
    private int oWins = 0;
    public static boolean isPvsAI;
    private boolean isHard;

    public GameMain(boolean isPvsAI) {
        this.isPvsAI = isPvsAI;
        this.isHard = false;
        // halo

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {
                    newGame();
                }
                repaint();

                if (isPvsAI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                    AIMove();
                }
            }
        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        JButton newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(100, 30));
        newGameButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
                repaint();
            }
        });

        JPanel statusBarPanel = new JPanel();
        statusBarPanel.setLayout(new BorderLayout());

        JPanel statusBarContentPanel = new JPanel();
        statusBarContentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusBarContentPanel.add(statusBar);

        statusBarPanel.add(statusBarContentPanel, BorderLayout.WEST);
        statusBarPanel.add(newGameButton, BorderLayout.EAST);

        super.setLayout(new BorderLayout());
        super.add(statusBarPanel, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, true));

        initGame();
        newGame();


        if (isPvsAI && currentPlayer == Seed.NOUGHT) {
            AIMove();
        }
    }

    public GameMain(boolean isPvsAI, boolean isHard) {
        this.isPvsAI = isPvsAI;
        this.isHard = isHard;

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {
                    newGame();
                }
                repaint();

                if (isPvsAI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                    AIMove();
                }
            }
        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        JButton newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(100, 30));
        newGameButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
                repaint();
            }
        });

        JPanel statusBarPanel = new JPanel();
        statusBarPanel.setLayout(new BorderLayout());

        JPanel statusBarContentPanel = new JPanel();
        statusBarContentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusBarContentPanel.add(statusBar);

        statusBarPanel.add(statusBarContentPanel, BorderLayout.WEST);
        statusBarPanel.add(newGameButton, BorderLayout.EAST);

        super.setLayout(new BorderLayout());
        super.add(statusBarPanel, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, true));

        initGame();
        newGame();

        if (isPvsAI && currentPlayer == Seed.NOUGHT) {
            AIMove();
        }
    }


    private void AIMove() {
        int[] move;
        if (isHard) {
            move = findBestMoveHard();
        } else {
            move = findBestMoveEasy();
        }

        int row = move[0];
        int col = move[1];

        currentState = board.stepGame(Seed.NOUGHT, row, col);
        currentPlayer = Seed.CROSS;
        repaint();
    }

    private int[] findBestMoveEasy() {
        int row, col;
        do {
            row = (int) (Math.random() * Board.ROWS);
            col = (int) (Math.random() * Board.COLS);
        } while (board.cells[row][col].content != Seed.NO_SEED);

        return new int[]{row, col};
    }

    private int[] findBestMoveHard() {
        int[] bestMove = new int[]{-1, -1};
        int bestScore = Integer.MIN_VALUE;

        // Loop through all empty cells and evaluate each move
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    board.cells[row][col].content = Seed.NOUGHT;
                    int score = minimax(0, false);
                    board.cells[row][col].content = Seed.NO_SEED; // Undo the move

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing) {
        State result = board.checkGameStatus();
        if (result != State.PLAYING) {
            // Game over, return the score
            if (result == State.CROSS_WON) {
                return -1; // Assuming CROSS is the maximizing player
            } else if (result == State.NOUGHT_WON) {
                return 1; // Assuming NOUGHT is the minimizing player
            } else {
                return 0; // It's a draw
            }
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < Board.ROWS; ++row) {
                for (int col = 0; col < Board.COLS; ++col) {
                    if (board.cells[row][col].content == Seed.NO_SEED) {
                        board.cells[row][col].content = Seed.NOUGHT;
                        bestScore = Math.max(bestScore, minimax(depth + 1, !isMaximizing));
                        board.cells[row][col].content = Seed.NO_SEED; // Undo the move
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int row = 0; row < Board.ROWS; ++row) {
                for (int col = 0; col < Board.COLS; ++col) {
                    if (board.cells[row][col].content == Seed.NO_SEED) {
                        board.cells[row][col].content = Seed.CROSS;
                        bestScore = Math.min(bestScore, minimax(depth + 1, !isMaximizing));
                        board.cells[row][col].content = Seed.NO_SEED; // Undo the move
                    }
                }
            }
            return bestScore;
        }
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw!");
        } else if (currentState == State.CROSS_WON) {
            playerOnePoints++;
            statusBar.setForeground(Color.RED);
            statusBar.setText("X Wins! Points: " + playerOnePoints);
            updateScoreboard();
        } else if (currentState == State.NOUGHT_WON) {
            playerTwoPoints++;
            statusBar.setForeground(Color.RED);
            statusBar.setText("O Wins! Points: " + playerTwoPoints);
            updateScoreboard();
        }
    }

    private void updateScoreboard() {
        statusBar.setForeground(Color.BLACK);

        if (playerOnePoints == 3) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Player X WON THE GAME!!");
            playerOnePoints = 0;
            playerTwoPoints = 0;
        } else if (playerTwoPoints == 3) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Player O WON THE GAME!! ");
            playerOnePoints = 0;
            playerTwoPoints = 0;
        } else {
            statusBar.setForeground(Color.BLACK);
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame(TITLE);
                frame.setContentPane(new GameMain(isPvsAI));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}