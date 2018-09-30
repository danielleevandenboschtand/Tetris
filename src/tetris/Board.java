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
    private boolean isFallingFinished = false;

    // used to know when game is started
    private boolean isStarted = false;

    // number of lines cleared (score)
    private int numLinesRemoved = 0;

    // current position of falling piece
    private int currentX = 0;
    private int currentY = 0;

    // bar to show score/game message
    private JLabel statusbar;

    // curretn tetromino shape
    private Piece currentPiece;

    // game board
    private Tetris[] board;

    /*
    Default constructor. Sets up game
     */
    public Board(Game parent) {
        setFocusable(true);
        currentPiece = new Piece();
        timer = new Timer(400, this);
        timer.start();

        statusbar = parent.getStatusBar();
        board = new Tetris[BoardWidth * BoardHeight];
        addKeyListener(new TAdapter());
        clearBoard();
    }

    /*
    Checks when to get new piece
     */
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    /*
     Gets square width
     @returns width of square
     */
    private int squareWidth() {
        return (int) getSize().getWidth() / BoardWidth;
    }

    /*
     Gets square height
     @returns height of square
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
        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
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

        if (currentPiece.getPiece() != Tetris.emptyPiece) {
            for (int i = 0; i < 4; ++i) {
                int x = currentX + currentPiece.x(i);
                int y = currentY - currentPiece.y(i);
                fillShape(g, x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(), currentPiece.getPiece());
            }
        }
    }

    /*
     Drops piece into lowest position
     */
    private void dropDown() {
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(currentPiece, currentX, newY - 1)) {
                break;
            }
            --newY;
        }
        pieceDropped();
    }

    /*
     Drops piece one line down
     */
    private void oneLineDown() {
        if (!tryMove(currentPiece, currentX, currentY - 1)) {
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
     Checks if a line is full
     */
    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = currentX + currentPiece.x(i);
            int y = currentY - currentPiece.y(i);
            board[(y * BoardWidth) + x] = currentPiece.getPiece();
        }

        removeFullLines();

        if (!isFallingFinished) {
            newPiece();
        }
    }

    /*
     Picks new piece to drop next
     */
    private void newPiece() {
        currentPiece.pickRandomPiece();
        currentX = BoardWidth / 2 + 1;
        currentY = BoardHeight - 1 + currentPiece.minY();

        if (!tryMove(currentPiece, currentX, currentY)) {
            currentPiece.newPiece(Tetris.emptyPiece);
            timer.stop();
            isStarted = false;
            statusbar.setText("Game Over");
        }
    }

    /*
     Checks if space is already occupied
     @returns boolean
     */
    private boolean tryMove(Piece newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) {
                return false;
            }
            if (shapeAt(x, y) != Tetris.emptyPiece) {
                return false;
            }
        }

        currentPiece = newPiece;
        currentX = newX;
        currentY = newY;
        repaint();
        return true;
    }

    /*
     Removes line if full
     */
    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == Tetris.emptyPiece) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                        board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusbar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            currentPiece.newPiece(Tetris.emptyPiece);
            repaint();
        }
    }

    /*
     Fills color for shapes
     */
    private void fillShape(Graphics g, int x, int y, Tetris shape) {
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

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    /*
     Checks when keys are hit to make moves
     */
    class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (!isStarted || currentPiece.getPiece() == Tetris.emptyPiece) {
                return;
            }

            int keycode = e.getKeyCode();

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    tryMove(currentPiece, currentX - 1, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(currentPiece, currentX + 1, currentY);
                    break;
                case KeyEvent.VK_DOWN:
                    tryMove(currentPiece.rotateRight(), currentX, currentY);
                    break;
                case KeyEvent.VK_UP:
                    tryMove(currentPiece.rotateLeft(), currentX, currentY);
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    break;
                case 'd':
                    oneLineDown();
                    break;
                case 'D':
                    oneLineDown();
                    break;
            }
        }
    }
}