package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

class Game extends JFrame implements ActionListener {
    private JFrame helpWindow, settingsWindow, highScoresWindow;

    /** bar to show score/game message */
    private JLabel scorebar;

    /** tetris logo */
    private final ImageIcon logo = new ImageIcon("tetris.png");

    /** buttons for game navigation */
    private JButton start, help, helpBack, settings, settingsBack, highscores, highscoresBack;

    /** settings radio buttons **/
    private JRadioButton easyButton, mediumButton, hardButton;

    /** color radio buttons */
    private JRadioButton defaultColors, dullColors, blackAndWhiteColors, fallColors, springColors, blackColors;

    /** speed of timer */
    private int speed = 400;

    /** top ten scores */
    private int[] scores = new int[10];
    private int[] orderedScores = new int[10];

    /** color selector */
    private int color;

    /**
     * Default constructor to create game
     */
    private Game() {
        frame();
    }

    /**
     * Label at bottom of screen for score
     * @return score bar
     */
    public JLabel getStatusBar() {
        return scorebar;
    }

    /**
     * Starts the game
     */
    private void startGame() {

        JFrame f = new JFrame();

        // create game window
        f.setVisible(true);
        f.setSize(500, 550);
        f.setTitle("Tetris");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // create score bar
        scorebar = new JLabel("Score: 0");

        f.add(scorebar, BorderLayout.NORTH);
        scorebar.setHorizontalAlignment(JLabel.CENTER);
        scorebar.setFont(new Font("Arial Black", Font.PLAIN, 20));

        Board board = new Board(this);
        f.add(board);
        board.start();
    }

    /**
     * Loads help menu window
     */
    private void helpMenu() {
        helpWindow = new JFrame();

        // create help window
        helpWindow.setVisible(true);
        helpWindow.setSize(500, 550);
        helpWindow.setTitle("Tetris Help");

        JLabel helpText = new JLabel("Tetris Gameplay");
        helpWindow.add(helpText, BorderLayout.NORTH);
        helpText.setHorizontalAlignment(JLabel.CENTER);
        helpText.setFont(new Font("Arial Black", Font.PLAIN, 20));

        /* text for controls */
        JTextArea helpTextArea = new JTextArea(
                "                            Right:              Up Arrow\n" +
                        "                            Rotate Left:    Down Arrow\n" +
                        "                            Move Right:   Right Arrow\n" +
                        "                            Move Left:      Left Arrow\n" +
                        "                            Fast Drop:      Shift\n" +
                        "                            Instant Drop:  Space\n" +
                        "                            Pause:            p"
        );
        Insets i = new Insets(50,50,50,50);
        helpTextArea.setMargin(i);

        helpTextArea.setEditable(false);

        helpTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        helpTextArea.setLineWrap(true);
        helpTextArea.setWrapStyleWord(true);
        helpWindow.add(helpTextArea, BorderLayout.CENTER);

        helpBack = new JButton("Back");
        helpWindow.add(helpBack, BorderLayout.SOUTH);
        helpBack.addActionListener(this);
    }

    /**
     * Loads settings menu
     */
    private void settingsMenu() {
        settingsWindow = new JFrame();

        JLabel settingsText = new JLabel("Settings");
        settingsText.setHorizontalAlignment(JLabel.CENTER);
        settingsText.setFont(new Font("Arial Black", Font.PLAIN, 20));
        settingsWindow.add(settingsText, BorderLayout.NORTH);

        // create settings window
        settingsWindow.setVisible(true);
        settingsWindow.setSize(500, 550);
        settingsWindow.setTitle("Settings");

        JPanel buttonPanel = new JPanel();

        easyButton = new JRadioButton("Easy");
        mediumButton = new JRadioButton("Normal");
        hardButton = new JRadioButton("Hard");

        defaultColors = new JRadioButton("Default");
        dullColors = new JRadioButton("Dull");
        blackAndWhiteColors = new JRadioButton("Black and White");
        fallColors = new JRadioButton("Fall");
        springColors = new JRadioButton("Spring");
        blackColors = new JRadioButton("Black");

        //Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(easyButton);
        group.add(mediumButton);
        group.add(hardButton);

        ButtonGroup colorGroup = new ButtonGroup();
        colorGroup.add(defaultColors);
        colorGroup.add(dullColors);
        colorGroup.add(blackAndWhiteColors);
        colorGroup.add(fallColors);
        colorGroup.add(springColors);
        colorGroup.add(blackColors);

        JTextArea difficultyLabel = new JTextArea("Difficulty");
        difficultyLabel.setEditable(false);
        difficultyLabel.setFont(new Font("Arial Black", Font.PLAIN, 16));
        buttonPanel.add(difficultyLabel);

        buttonPanel.add(easyButton);
        buttonPanel.add(mediumButton);
        buttonPanel.add(hardButton);

        JTextArea themeLabel = new JTextArea("Theme");
        themeLabel.setEditable(false);
        themeLabel.setFont(new Font("Arial Black", Font.PLAIN, 16));
        buttonPanel.add(themeLabel);

        buttonPanel.add(defaultColors);
        buttonPanel.add(dullColors);
        buttonPanel.add(blackAndWhiteColors);
        buttonPanel.add(fallColors);
        buttonPanel.add(springColors);
        buttonPanel.add(blackColors);

        easyButton.addActionListener(this);
        mediumButton.addActionListener(this);
        hardButton.addActionListener(this);

        defaultColors.addActionListener(this);
        dullColors.addActionListener(this);
        blackAndWhiteColors.addActionListener(this);
        fallColors.addActionListener(this);
        springColors.addActionListener(this);
        blackColors.addActionListener(this);

        if (speed == 100) {
            hardButton.setSelected(true);
        }
        else if (speed == 700) {
            easyButton.setSelected(true);
        }
        else {
            mediumButton.setSelected(true);
        }

        if (color == 1) {
            dullColors.setSelected(true);
        }
        else if (color == 2) {
            blackAndWhiteColors.setSelected(true);
        }
        else if (color == 3) {
            fallColors.setSelected(true);
        }
        else if (color == 4) {
            springColors.setSelected(true);
        }
        else if (color == 5) {
            blackColors.setSelected(true);
        }
        else {
            defaultColors.setSelected(true);
        }

        settingsWindow.add(buttonPanel);

        settingsBack = new JButton("Back");
        settingsWindow.add(settingsBack, BorderLayout.SOUTH);
        settingsBack.addActionListener(this);
    }

    /**
     * Loads high scores menu
     */
    private void highScoresMenu() {
        highScoresWindow = new JFrame();

        JLabel highScoresText = new JLabel("High Scores");
        highScoresWindow.add(highScoresText, BorderLayout.NORTH);
        highScoresText.setHorizontalAlignment(JLabel.CENTER);
        highScoresText.setFont(new Font("Arial Black", Font.PLAIN, 20));

        JPanel scoresPanel = new JPanel();

        JTextArea scoreTextArea = new JTextArea();
        scoreTextArea.setEditable(false);

        for (int i = 0; i < orderedScores.length; i++) {
            scoreTextArea.append((i + 1) + ".  ");
            scoreTextArea.append(Integer.toString(orderedScores[i]) + "\n");
        }

        scoreTextArea.setFont(new Font("Arial Black", Font.PLAIN, 16));

        scoresPanel.add(scoreTextArea);
        highScoresWindow.add(scoresPanel);

        // create scores window
        highScoresWindow.setVisible(true);
        highScoresWindow.setSize(500, 550);
        highScoresWindow.setTitle("High Scores");

        highscoresBack = new JButton("Back");
        highScoresWindow.add(highscoresBack, BorderLayout.SOUTH);
        highscoresBack.addActionListener(this);
    }

    /**
     * Frame for menu screen
     */
    private void frame() {

        JFrame f = new JFrame();

        // create game window
        f.setVisible(true);
        f.setSize(500, 550);
        f.setTitle("Tetris");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel p = new JPanel();

        // game title
        JLabel title = new JLabel(logo);
        start = new JButton("Start");
        start.setPreferredSize(new Dimension(100, 40));

        help = new JButton("Help");
        help.setPreferredSize(new Dimension(100, 40));

        settings = new JButton("Settings");
        settings.setPreferredSize(new Dimension(100, 40));

        highscores = new JButton("High Scores");
        highscores.setPreferredSize(new Dimension(100, 40));

        loadScores();

        f.add(title, BorderLayout.NORTH);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Arial Black", Font.PLAIN, 80));
        p.add(start);
        p.add(help);
        p.add(settings);
        p.add(highscores);
        f.add(p);

        start.addActionListener(this);
        help.addActionListener(this);
        settings.addActionListener(this);
        highscores.addActionListener(this);
    }

    /**
     * Getter for game speed
     * @return speed of timer
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Setter for game speed
     * @param s speed fo drop
     */
    private void setSpeed(int s) {
        speed = s;
    }

    /**
     * Sets num to select color
     * @param c int for color
     */
    private void setColorNum(int c) {
        color = c;
    }

    /**
     * Returns num to select color
     * @return int for color
     */
    public int getColorNum() {
        return color;
    }

    /**
     * Loads scores from text file to keep track of high scores
     */
    private void loadScores() {

        try {

            // open the text file
            File file = new File("scores.txt");
            int currentLine;
            int i = 0;
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                currentLine = Integer.parseInt(sc.nextLine());
                scores[i] = currentLine;
                i++;
            }
        }
        catch (FileNotFoundException error) {
            System.out.println("File not found");
        }

        Arrays.sort(scores);

        int j = 9;

        for (int score : scores) {
            orderedScores[j] = score;
            j--;
        }
    }

    /**
     * Getter for high scores
     * @return speed of timer
     */
    public int[] getScores() {
        return orderedScores;
    }

    /**
     * Checks if button is clicked
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == start) {
            startGame();
        }
        if (e.getSource() == help) {
            helpMenu();
        }
        if (e.getSource() == settings) {
            settingsMenu();
        }
        if (e.getSource() == highscores) {
            highScoresMenu();
        }
        if (e.getSource() == settingsBack) {
            settingsWindow.dispose();
        }
        if (e.getSource() == helpBack) {
            helpWindow.dispose();
        }
        if (e.getSource() == highscoresBack) {
            highScoresWindow.dispose();
        }
        if (e.getSource() == easyButton) {
            setSpeed(700);
        }
        if (e.getSource() == mediumButton) {
            setSpeed(400);
        }
        if (e.getSource() == hardButton) {
            setSpeed(100);
        }
        if (e.getSource() == defaultColors) {
            setColorNum(0);
        }
        if (e.getSource() == dullColors) {
            setColorNum(1);
        }
        if (e.getSource() == blackAndWhiteColors) {
            setColorNum(2);
        }
        if (e.getSource() == fallColors) {
            setColorNum(3);
        }
        if (e.getSource() == springColors) {
            setColorNum(4);
        }
        if (e.getSource() == blackColors) {
            setColorNum(5);
        }
    }

    /**
     * Main method to run game
     * @param args args
     */
    public static void main(String[] args) {
        new Game();
    }
}
