package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModeMenu extends JFrame {

    public ModeMenu() {
        setTitle("Game Start Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        JLabel titleLabel = new JLabel("Tic Tac Toe");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));

        JButton mode1Button = new JButton("P1 vs P2");
        mode1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(false, false);
            }
        });

        JButton mode2Button = new JButton("P vs AI (Easy)");
        mode2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(true, false);
            }
        });

        JButton mode3Button = new JButton("P vs AI (Hard)");
        mode3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(true, true);
            }
        });

        JButton exitButton = new JButton("Back");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    StartMenu startMenu = new StartMenu();
                    startMenu.setVisible(true);
                    setVisible(false);
                });
            }
        });

        // Create a panel to hold the components with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Set GridBagConstraints for center alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0); // Add space below the title
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        panel.add(mode1Button, gbc);

        gbc.gridy++;
        panel.add(mode2Button, gbc);

        gbc.gridy++;
        panel.add(mode3Button, gbc);

        gbc.gridy++;
        panel.add(exitButton, gbc);

        // Add the panel to the frame
        add(panel);

        // Set the preferred size and pack
        setPreferredSize(new Dimension(400, 300)); // Set the preferred width and height
        pack(); // Pack the components
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void startGame(boolean isPvsAI, boolean isHard) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Tic Tac Toe");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                if (isPvsAI) {
                    frame.setContentPane(new GameMain(isPvsAI, isHard));
                } else {
                    frame.setContentPane(new GameMain(isPvsAI));
                }

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ModeMenu modeMenu = new ModeMenu();
                modeMenu.setVisible(true);
            }
        });
    }
}
