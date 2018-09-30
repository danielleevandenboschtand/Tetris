package tetris;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game extends JFrame {
    JLabel statusbar;

    /*
    Default constructor to create game
     */
    public Game() {

        // create board
        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);
        Board board = new Board(this);
        add(board);
        board.start();

        setSize(300, 600);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

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