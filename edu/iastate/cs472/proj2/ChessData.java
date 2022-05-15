package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */
public class ChessData {

    /*
     * The following constants represent the possible contents of a square
     * on the board. The constants RED and BLACK also represent players
     * in the game.
     */

    static final int EMPTY = 0,
            WHITE_PAWN = 1,
            WHITE_ROOK = 2,
            WHITE_KNIGHT = 3,
            WHITE_BISHOP = 4,
            WHITE_QUEEN = 5,
            WHITE_KING = 6,
            BLACK_PAWN = 7,
            BLACK_ROOK = 8,
            BLACK_KNIGHT = 9,
            BLACK_BISHOP = 10,
            BLACK_QUEEN = 11,
            BLACK_KING = 12;

    static final int WHITE_PLAYER = 0,
            BLACK_PLAYER = 1;

    int[][] board; // board[r][c] is the contents of row r, column c.

    /**
     * Constructor. Create the board and set it up for a new game.
     */
    ChessData() {
        board = new int[8][8];
        setUpGame();
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            sb.append(8 - i).append(" ");
            for (int n : row) {
                if (n == EMPTY) {
                    sb.append(" ");
                } else if (n == WHITE_PAWN) {
                    sb.append(ANSI_RED + "P" + ANSI_RESET);
                } else if (n == WHITE_ROOK) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == WHITE_KNIGHT) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == WHITE_BISHOP) {
                    sb.append(ANSI_RED + "B" + ANSI_RESET);
                } else if (n == WHITE_QUEEN) {
                    sb.append(ANSI_RED + "Q" + ANSI_RESET);
                } else if (n == WHITE_KING) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == BLACK_PAWN) {
                    sb.append(ANSI_YELLOW + "P" + ANSI_RESET);
                } else if (n == BLACK_ROOK) {
                    sb.append(ANSI_YELLOW + "R" + ANSI_RESET);
                } else if (n == BLACK_KNIGHT) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                } else if (n == BLACK_BISHOP) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == BLACK_QUEEN) {
                    sb.append(ANSI_YELLOW + "Q" + ANSI_RESET);
                } else if (n == BLACK_KING) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                }
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");

        return sb.toString();
    }

    /**
     * Set up the board with checkers in position for the beginning
     * of a game. Note that checkers can only be found in squares
     * that satisfy row % 2 == col % 2. At the start of the game,
     * all such squares in the first three rows contain black squares
     * and all such squares in the last three rows contain red squares.
     */
    void setUpGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = EMPTY;
            }
        }
        for (int i = 0; i < 8; i++) {
            board[1][i] = WHITE_PAWN;
        }
        for (int i = 0; i < 8; i++) {
            board[6][i] = BLACK_PAWN;
        }
    }

    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        return board[row][col];
    }

    void makeMove(ChessMove move) {
        // TODO
    }

    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).
     */
    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        // TODO
    }

    /**
     * Return an array containing all the legal ChessMoves
     * for the specified player on the current board. If the player
     * has no legal moves, null is returned. The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned. If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    ChessMove[] getLegalMoves(int player) {
        // TODO
        ArrayList<ChessMove> legalMoves = new ArrayList<ChessMove>();
        ChessMove[] output = new ChessMove[10];
        output[0] = new ChessMove(0, 0, 1, 1);
        return output;
    }

    boolean checkIf(int row, int col, int piece) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return false;
        } else {
            return board[row][col] == piece;
        }
    }

}
