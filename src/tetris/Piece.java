package tetris;

import java.util.Random;
import java.lang.Math;

public class Piece {

    // tetrominos shapes
    enum Tetris {emptyPiece, sPiece, zPiece, tPiece, lPiece, linePiece, squarePiece, mlPiece};

    // tetrominos shape object
    private Tetris pieces;

    // coordinates to make tetromino piece
    private int pieceCoord[][];

    // array of coordinates of created piece shapes
    private int createCoord[][][];

    // default constructor sets pieceCoors with placeholder empty shape
    public Piece() {
        pieceCoord = new int[4][2];
        newPiece(Tetris.emptyPiece);
    }

    /*
    Creates new tetromino shape
     */
    public void newPiece(Tetris shape) {

        createCoord = new int[][][] {

                // empty shape
                { {0, 0}, {0, 0}, {0, 0}, {0, 0} },

                // S shape
                { {0, -1}, {0, 0}, {1, 0}, {1, 1} },

                // Z shape
                { {0, -1}, {0, 0}, {-1, 0}, {-1, 1} },

                // T shape
                { {-1, 0}, {0, 0}, {1, 0}, {0, 1} },

                // L shape
                { {-1, -1}, {0, -1}, {0, 0}, {0, 1} },

                // Line shape
                { {0, -1}, {0, 0}, {0, 1}, {0, 2} },

                // Square shape
                { {0, 0}, {1, 0}, {0, 1}, {1, 1} },

                // Mirrored L shape
                { {1, -1}, {0, -1}, {0, 0}, {0, 1} }
        };

        // loop to set coordinates and build shapes
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                pieceCoord[i][j] = createCoord[shape.ordinal()][i][j];
            }
        }

        // set piece
        pieces = shape;
    }

    /*
    Returns tetris piece
    @returns pieces tetris piece object
     */
    public Tetris getPiece() {
        return pieces;
    }

    /*
    Chooses a random tetromino piece
     */
    public void pickRandomPiece() {
        Random rand = new Random();
        int i = Math.abs(rand.nextInt()) % 7 + 1;
        Tetris[] num = Tetris.values();
        newPiece(num[i]);
    }

    /*
    Used to adjust x coordinates when piece is rotated
     */
    private void changeX(int i, int x) {
        pieceCoord[i][0] = x;
    }

    /*
    Used to adjust y coordinates when piece is rotated
     */
    private void changeY(int i, int y) {
        pieceCoord[i][0] = y;
    }

    /*
    Used to adjust x coordinates when piece is rotated
    @returns pieceCoord the new x coordinates
     */
    public int x(int i) {
        return pieceCoord[i][0];
    }

    /*
    Used to adjust y coordinates when piece is rotated
    @returns pieceCoord the new x coordinates
     */
    public int y(int i) {
        return pieceCoord[i][1];
    }

    /*
    Rotates tetromino to the right
    @returns rotPiece the new piece coordinates
     */
    public Piece rotateRight() {

        // square piece doesn't change when rotated
        if (pieces == Tetris.squarePiece) {
            return this;
        }

        Piece rotPiece = new Piece();
        rotPiece.pieces = pieces;

        // change coordinates of shape from rotation
        for (int i = 0; i < 4; i++) {
            rotPiece.changeX(i, -y(i));
            rotPiece.changeY(i, x(i));
        }

        return rotPiece;
    }

    /*
    Rotates tetromino to the left
    @returns rotPiece the new piece coordinates
     */
    public Piece rotateLeft() {

        // square piece doesn't change when rotated
        if (pieces == Tetris.squarePiece) {
            return this;
        }

        Piece rotPiece = new Piece();
        rotPiece.pieces = pieces;

        // change coordinates of shape from rotation
        for (int i = 0; i < 4; i++) {
            rotPiece.changeX(i, y(i));
            rotPiece.changeY(i, -x(i));
        }

        return rotPiece;
    }
}