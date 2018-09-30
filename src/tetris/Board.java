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
    final int BoardWidth = 10;
    final int BoardHeight = 22;

    Timer timer;
    boolean isFallingFinished = false;
    boolean isStarted = false;
    boolean isPaused = false;
    int numLinesRemoved = 0;

    // current position of falling piece
    int currentX = 0;
    int currentY = 0;
    JLabel statusbar;
    Shape currentPiece;
    Tetris[] board;

    public Board(Tetris parent) {
        setFocusable(true);
        currentPiece = new Shape();
        timer = new Timer(400, this);
        timer.start();

        statusbar = parent.getStatusBar();
        board = new Tetris[BoardWidth * BoardHeight];
        addKeyListener(new TAdapter());
        clearBoard();
    }

    public void actionPerformed(ActionEvent e) {
        if(isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        }
        else {
            oneLineDown();
        }
    }

    int squareWidth() {
        return (int) getSize().getWidth() / BoardWidth;
    }

    int squareHeight() {
        return (int) getSize().getWidth() / BoardHeight;
    }

    Tetris shapeAt(int x, int y) {
        return board[(y * BoardWidth) + x];
    }

    public void start() {
        if (ifPaused) {
            return;
        }

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
    }

    private void pause() {
        if (!(isStarted)) {
            return;
        }

        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            statusbar.setText("Paused");
        }
        else {
            timer.start();
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);

        Dimension size = getSize();

        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

        for (int i = 0; i < BoardHeight; i++) {
            for(int j = 0; j < BoardWidth; j++) {
                Tetris shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != Tetris.NoShape) {
                    drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(), currentPiece.getShape());
                }
            }
        }
    }
}