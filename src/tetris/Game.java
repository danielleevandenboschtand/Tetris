package tetris;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game extends JFrame {

    /*
    Default constructor to create game
     */
    public Game() {

        // create board
        Board board = new Board(this);
        add(board);
        board.start();

        setSize(200, 400);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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