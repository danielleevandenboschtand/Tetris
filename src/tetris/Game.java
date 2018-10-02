package tetris;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game extends JFrame {

    // bar to show score/game message
    private JLabel statusbar;

    /*
    Default constructor to create game
     */
    public Game() {

        // create score bar
        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.NORTH);
        Board board = new Board(this);
        add(board);
        board.start();

        // create game window
        setSize(500, 515);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /*
    Label at bottom of screen for score
     */
    public JLabel getStatusBar() {
        return statusbar;
    }

    /*
    Main method to run game
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}