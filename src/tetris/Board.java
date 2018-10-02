package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import tetris.Piece.Tetris;

public class Board extends JPanel implements ActionListener {

    // width of game board
    private final int BoardWidth = 10;

    // height of game board
    private final int BoardHeight = 22;

    // timer used to regulate drop speeds
    private Timer timer;

    // used to know when to get next piece
    private boolean atBottom = false;

    // used to know when game is started
    private boolean isStarted = false;

    // used to know if game is paused
    private boolean isPaused = false;

    // number of lines cleared
    private int score = 0;

    // current position of falling piece
    private int curX = 0;
    private int curY = 0;

    // bar to show score/game message
    private JLabel scorebar;

    // current tetromino shape
    private Piece curPiece;

    // game board
    private Tetris[] board;

    /*
    Default constructor. Sets up game
     */
    public Board(Game parent) {
        setFocusable(true);
        curPiece = new Piece();
        timer = new Timer(400, this);
        timer.start();

        scorebar = parent.getStatusBar();
        board = new Tetris[BoardWidth * BoardHeight];
        addKeyListener(new TAdapter());
        clearBoard();
    }

    /*
     Gets square width
     @returns int width of square
     */
    private int squareWidth() {
        return (int) getSize().getWidth() / BoardWidth;
    }

    /*
     Gets square height
     @returns int height of square
     */
    private int squareHeight() {
        return (int) getSize().getWidth() / BoardHeight;
    }

    /*
     Gets piece at coordinate position
     @returns position of shape
     */
    private Tetris shapeAt(int x, int y) {
        return board[(y * BoardWidth) + x];
    }

    /*
     Resets game and starts a new one
     */
    public void start() {

        // check if game is paused
        if (isPaused) {
            return;
        }

        isStarted = true;
        atBottom = false;
        score = 0;
        clearBoard();

        newPiece();
        timer.start();
    }

    /*
     Pauses the game
     */
    private void pause() {

        // check if game is already paused
        if (!isStarted)
            return;

        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            scorebar.setText("Paused");
        } else {
            timer.start();
            scorebar.setText(String.valueOf(score * 100));
        }
        repaint();
    }

    /*
     Fills color for pieces
     */
    private void fillShape(Graphics g, int x, int y, Tetris piece) {
        Color colors[] = {
                new Color(0, 0, 0),
                new Color(255, 0, 0),
                new Color(0, 255, 37),
                new Color(0, 10, 255),
                new Color(240, 238, 0),
                new Color(204, 0, 222),
                new Color(0, 241, 239),
                new Color(255, 154, 0)
        };

        Color color = colors[piece.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
    }

    /*
     Colors game piece
     */
    public void paint(Graphics g) {

        super.paint(g);

        Dimension size = getSize();

        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Tetris shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != Tetris.emptyPiece) {
                    fillShape(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (curPiece.getPiece() != Tetris.emptyPiece) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                fillShape(g, x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(), curPiece.getPiece());
            }
        }
    }

    /*
     Checks if space is already occupied
     @returns boolean
     */
    private boolean move(Piece newPiece, int newX, int newY) {

        // try move
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            // check if piece has room to attempt move
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) {
                return false;
            }
            if (shapeAt(x, y) != Tetris.emptyPiece) {
                return false;
            }
        }
        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    /*
     Drops piece one line down
     */
    private void oneLineDown() {
        if (!move(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    /*
     Clears board of all pieces
     */
    private void clearBoard() {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i) {
            board[i] = Tetris.emptyPiece;
        }
    }

    /*
     Removes line if full
     */
    private void removeFullLines() {
        int lines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == Tetris.emptyPiece) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++lines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                        board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (lines > 0) {
            score += lines;
            scorebar.setText(String.valueOf(score * 100));
            atBottom = true;
            curPiece.setPiece(Tetris.emptyPiece);
            repaint();
        }
    }

    /*
     Checks if a line is full
     */
    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BoardWidth) + x] = curPiece.getPiece();
        }
        removeFullLines();

        if (!atBottom) {
            newPiece();
        }
    }

    /*
     Picks new piece to drop next
     */
    private void newPiece() {
        curPiece.pickRandomPiece();
        curX = BoardWidth / 2 + 1;
        curY = BoardHeight - 1 + curPiece.minY();

        if (!move(curPiece, curX, curY)) {
            curPiece.setPiece(Tetris.emptyPiece);
            timer.stop();
            isStarted = false;
            scorebar.setText("Game Over");
        }
    }

    /*
     Drops piece into lowest position
     */
    private void instantDrop() {
        int newY = curY;
        while (newY > 0) {
            if (!move(curPiece, curX, newY - 1)) {
                break;
            }
            --newY;
        }
        pieceDropped();
    }

    /*
    Checks when to get new piece
     */
    public void actionPerformed(ActionEvent e) {

        // check if piece is at bottom
        if (atBottom) {
            atBottom = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    /*
     Checks when keys are hit to make moves
     */
    class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            if (!isStarted || curPiece.getPiece() == Tetris.emptyPiece) {
                return;
            }

            // code for key pressed
            int keycode = e.getKeyCode();

            // pause game
            if (keycode == 'p' || keycode == 'P') {
                pause();
                return;
            }

            // can't do moves of game is paused
            if (isPaused) {
                return;
            }

            // controls for moves
            switch (keycode) {

                // move left
                case KeyEvent.VK_LEFT:
                    move(curPiece, curX - 1, curY);
                    break;

                // move right
                case KeyEvent.VK_RIGHT:
                    move(curPiece, curX + 1, curY);
                    break;

                // rotate left
                case KeyEvent.VK_DOWN:
                    move(curPiece.rotateLeft(), curX, curY);
                    break;

                // rotate right
                case KeyEvent.VK_UP:
                    move(curPiece.rotateRight(), curX, curY);
                    break;

                // instant drop to bottom
                case KeyEvent.VK_SPACE:
                    instantDrop();
                    break;

                // speed up drop
                case KeyEvent.VK_SHIFT:
                    oneLineDown();
                    break;
            }
        }
    }
}