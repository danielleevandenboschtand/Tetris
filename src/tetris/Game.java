package tetris;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game extends JFrame {

    // bar to show score/game message
    private JLabel scorebar;

    /*
    Default constructor to create game
     */
    public Game() {

        // create score bar
        scorebar = new JLabel("Score: 0");
        add(scorebar, BorderLayout.NORTH);
        scorebar.setHorizontalAlignment(JLabel.CENTER);
        scorebar.setFont(new Font("Arial Black", Font.PLAIN, 20));
        Board board = new Board(this);
        add(board);
        board.start();

        // create game window
        setSize(500, 550);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /*
    Label at bottom of screen for score
     */
    public JLabel getStatusBar() {
        return scorebar;
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