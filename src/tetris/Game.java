package tetris;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame implements ActionListener{
    private JFrame helpWindow;

    // bar to show score/game message
    private JLabel scorebar, helpText;

    // game title
    private JLabel title;

    // buttons for game navigation
    private JButton start, help, back;

    private JTextArea helpTextArea;

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

        // create score bar
        scorebar = new JLabel("Score: 0");

        f.add(scorebar, BorderLayout.NORTH);
        scorebar.setHorizontalAlignment(JLabel.CENTER);
        scorebar.setFont(new Font("Arial Black", Font.PLAIN, 20));

        Board board = new Board(this);
        f.add(board);
        board.start();
    }

    public void helpMenu() {
        helpWindow = new JFrame();

        // create help window
        helpWindow.setVisible(true);
        helpWindow.setSize(500, 550);
        helpWindow.setTitle("Tetris Help");

        helpText = new JLabel("Tetris Gameplay");
        helpWindow.add(helpText, BorderLayout.NORTH);
        helpText.setHorizontalAlignment(JLabel.CENTER);
        helpText.setFont(new Font("Arial Black", Font.PLAIN, 20));

        helpTextArea = new JTextArea(
                "                             Right:              Up Arrow\n" +
                "                             Rotate Left:     Down Arrow\n" +
                "                             Move Right:    Right Arrow\n" +
                "                             Move Left:      Left Arrow\n" +
                "                             Fast Drop:       Shift\n" +
                "                             Instant Drop:   Space\n" +
                "                             Pause:              p"
        );
        Insets i = new Insets(50,50,50,50);
        helpTextArea.setMargin(i);

        helpTextArea.setEditable(false);

        helpTextArea.setFont(new Font("Serif", Font.PLAIN, 16));
        helpTextArea.setLineWrap(true);
        helpTextArea.setWrapStyleWord(true);
        helpWindow.add(helpTextArea, BorderLayout.CENTER);

        back = new JButton("Back");
        helpWindow.add(back, BorderLayout.SOUTH);
        back.addActionListener(this);
    }

    /*
    Frame for menu screen
      */
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
        title.setFont(new Font("Arial Black", Font.PLAIN, 80));
        p.add(start);
        p.add(help);
        f.add(p);

        start.addActionListener(this);
        help.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == start) {
            startGame();
        }
        if (e.getSource() == help) {
            helpMenu();
        }
        if (e.getSource() == back) {
            helpWindow.dispose();
        }
    }

    /*
    Main method to run game
     */
    public static void main(String[] args) {
        Game game = new Game();
    }
}
