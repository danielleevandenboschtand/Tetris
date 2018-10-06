package tetris;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Game extends JFrame implements ActionListener{

    // bar to show score/game message
    private JLabel scorebar;

    // game title
    private JLabel title;

    // start button to launch game
    private JButton start;

    // help button to show help menu
    private JButton help;

    // back button to return to start menu
    private JButton back;

    /*
    Default constructor to create game
     */
    public Game() {

        frame();
    }

    /*
    Label at bottom of screen for score
     */
    public JLabel getStatusBar() {
        return scorebar;
    }

    private void startGame() {

        JFrame f = new JFrame();

        // create game window
        f.setVisible(true);
        f.setSize(500, 550);
        f.setTitle("Tetris");
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel();

        // create score bar
        scorebar = new JLabel("Score: 0");
        p.add(scorebar, BorderLayout.NORTH);
        scorebar.setHorizontalAlignment(JLabel.CENTER);
        scorebar.setFont(new Font("Arial Black", Font.PLAIN, 20));

        Board board = new Board(this);
        p.add(board);
        board.start();
        f.add(p);
    }

    // frame for menu screen
    public void frame() {

        JFrame f = new JFrame();

        // create game window
        f.setVisible(true);
        f.setSize(500, 550);
        f.setTitle("Tetris");
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel();

        title = new JLabel("TETRIS");
        start = new JButton("Start");
        help = new JButton("Help");


        f.add(title, BorderLayout.NORTH);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Arial Black", Font.PLAIN, 30));
        p.add(start);
        p.add(help);
        f.add(p);

        start.addActionListener(this);
        help.addActionListener(this);
    }

    /*
    Main method to run game
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == start) {
            startGame();
        }

    }
}