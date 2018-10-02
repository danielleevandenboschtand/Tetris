package tetris;

import java.util.Random;
import java.lang.Math;


public class Piece {

    // tetrominos shapes
    enum Tetris {emptyPiece, zPiece, sPiece, linePiece, tPiece, squarePiece, lPiece, mlPiece};

    // tetrominos shape object
    private Tetris pieces;

    // coordinates to make tetromino piece
    private int pieceCoords[][];

    // array of coordinates of created piece shapes
    private int[][][] createCoord;

    /*
    Default constructor sets pieceCoords with placeholder empty shape
     */
    public Piece() {
        pieceCoords = new int[4][2];
        setPiece(Tetris.emptyPiece);
    }

    /*
    Creates new tetromino shape
     */
    public void setPiece(Tetris shape) {

        createCoord = new int[][][] {

                // empty shape
                { {0, 0}, {0, 0}, {0, 0}, {0, 0} },

                // Z shape
                { {0, -1}, {0, 0}, {-1, 0}, {-1, 1} },

                // S shape
                { {0, -1}, {0, 0}, {1, 0}, {1, 1} },

                // Line shape
                { {0, -1}, {0, 0}, {0, 1}, {0, 2} },

                // T shape
                { {-1, 0}, {0, 0}, {1, 0}, {0, 1} },

                // Square shape
                { {0, 0}, {1, 0}, {0, 1}, {1, 1} },

                // L shape
                { {-1, -1}, {0, -1}, {0, 0}, {0, 1} },

                // Mirrored L shape
                { {1, -1}, {0, -1}, {0, 0}, {0, 1} }
        };

        // loop to set coordinates and build shapes
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                pieceCoords[i][j] = createCoord[shape.ordinal()][i][j];
            }
        }

        // set piece
        pieces = shape;

    }

    /*
    Returns tetris piece
    @returns Tetris piece object
     */
    public Tetris getPiece() {
        return pieces;
    }

    /*
    Chooses a random tetromino piece
     */
    public void pickRandomPiece() {
        Random rand = new Random();
        int x = Math.abs(rand.nextInt()) % 7 + 1;
        Tetris[] num = Tetris.values();
        setPiece(num[x]);
    }

    /*
    Used to adjust x coordinates when piece is rotated
     */
    private void changeX(int i, int x) {
        pieceCoords[i][0] = x;
    }

    /*
    Used to adjust y coordinates when piece is rotated
     */
    private void changeY(int i, int y) {
        pieceCoords[i][1] = y;
    }

    /*
    Used to adjust x coordinates when piece is rotated
    @returns int pieceCoords the new x coordinates
     */
    public int x(int i) {
        return pieceCoords[i][0];
    }

    /*
    Used to adjust y coordinates when piece is rotated
    @returns int pieceCoords the new y coordinates
     */
    public int y(int i) {
        return pieceCoords[i][1];
    }

    /*
    Rotates the tetromino to the right
    @returns Tetris rotPiece the new piece coordinates
     */
    public Piece rotateRight() {

        // square piece doesn't change when rotated
        if (pieces == Tetris.squarePiece) {
            return this;
        }

        Piece rotPiece = new Piece();
        rotPiece.pieces = pieces;

        // change coordinates of shape from rotation
        for (int i = 0; i < 4; ++i) {
            rotPiece.changeX(i, -y(i));
            rotPiece.changeY(i, x(i));
        }
        return rotPiece;
    }

    /*
    Rotates the tetromino to the left
    @returns Tetris rotPiece the new piece coordinates
     */
    public Piece rotateLeft() {

        // square piece doesn't change when rotated
        if (pieces == Tetris.squarePiece) {
            return this;
        }

        Piece rotPiece = new Piece();
        rotPiece.pieces = pieces;

        // change coordinates of shape from rotation
        for (int i = 0; i < 4; ++i) {
            rotPiece.changeX(i, y(i));
            rotPiece.changeY(i, -x(i));
        }
        return rotPiece;
    }

    /*
    Sets where the piece starts from the top based on the size of the piece
    @returns int min the minimum top-position
     */
    public int minY()
    {
        int min = pieceCoords[0][1];
        for (int i = 0; i < 4; i++) {
            min = Math.min(min, pieceCoords[i][1]);
        }
        return min;
    }
}