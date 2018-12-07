package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Game extends JFrame implements ActionListener {
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

    /** current player name */
    private String playerName;

    /** high scores array **/
    private String[][] highScores = new String[10][2];

    /** color selector */
    private int color;

    /**
     * Default constructor to create game
     */
    public Game() {
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

        // text for controls
        JTextArea helpTextArea = new JTextArea(
                "                            Right:              Up Arrow\n" +
                        "                            Rotate Left:    Down Arrow\n" +
                        "                            Move Right:   Right Arrow\n" +
                        "                            Move Left:      Left Arrow\n" +
                        "                            Fast Drop:      Shift\n" +
                        "                            Instant Drop:  Space\n" +
                        "                            Pause:            p" +
                        "\n\n\n" +
                        "                    Easy Difficulty: 50 points per line cleared" +
                        "                         Normal Difficulty: 100 points per line cleared" +
                        "                          Hard Difficulty: 200 points per line cleared"
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

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

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

        buttonPanel.add(Box.createRigidArea(new Dimension(2,2)));

        ButtonGroup colorGroup = new ButtonGroup();
        colorGroup.add(defaultColors);
        colorGroup.add(dullColors);
        colorGroup.add(blackAndWhiteColors);
        colorGroup.add(fallColors);
        colorGroup.add(springColors);
        colorGroup.add(blackColors);

        JTextField difficultyLabel = new JTextField("Difficulty");
        difficultyLabel.setEditable(false);
        difficultyLabel.setFont(new Font("Arial Black", Font.PLAIN, 16));
        buttonPanel.add(difficultyLabel);

        buttonPanel.add(easyButton);
        buttonPanel.add(mediumButton);
        buttonPanel.add(hardButton);

        JTextField themeLabel = new JTextField("Theme");
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

        scoreTextArea.setFont(new Font("Arial Black", Font.PLAIN, 16));

        for (int j = 0; j < highScores.length; j++) {
            for (int k = 0; k < 2; k++) {
                scoreTextArea.append(highScores[j][k]);
                if ( k==0 ) {
                    scoreTextArea.append(": ");
                } else {
                    scoreTextArea.append("\n");
                }
            }

        }

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

        playerName = JOptionPane.showInputDialog("Please Enter First Name");

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
     * @return int speed of timer
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Getter for current player name
     * @return String playerName
     */
    public String getPlayerName() {
        return playerName;
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
     * Loads scores from file to keep track of high scores
     */
    private void loadScores() {

        String csvFile = "highscores.dat";
        String line = "";
        String csvSplitBy = ",";
        int rowCounter = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] tempScores = line.split(csvSplitBy);
                for (int colCounter = 0; colCounter < tempScores.length; colCounter++) {
                    highScores[rowCounter][colCounter] = tempScores[colCounter];
                }
                rowCounter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        sortScores(highScores);
    }

    private static void sortScores(String[][] arr) {
        int n = arr.length;

        // run bubble sort on this 2d array based on the values in the 2nd column!
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (Integer.parseInt(arr[j][1]) < Integer.parseInt(arr[j + 1][1])) {
                    String[] temp = new String[2];

                    // names and scores
                    for (int k = 0; k < temp.length; k++) {
                        temp[k] = arr[j][k];
                    }
                    for (int k = 0; k < temp.length; k++) {
                        arr[j][k] = arr[j + 1][k];
                    }
                    for (int k = 0; k < temp.length; k++) {
                        arr[j + 1][k] = temp[k];
                    }
                }
            }
        }
    }


    /**
     * Getter for high scores
     * @return speed of timer
     */
    public String[][] getScores(){
        return highScores;
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
