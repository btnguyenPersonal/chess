package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.Arrays;

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

    void setUpGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = EMPTY;
            }
        }
        board[0][0] = BLACK_ROOK;
        board[0][1] = BLACK_KNIGHT;
        board[0][2] = BLACK_BISHOP;
        board[0][3] = BLACK_QUEEN;
        board[0][4] = BLACK_KING;
        board[0][5] = BLACK_BISHOP;
        board[0][6] = BLACK_KNIGHT;
        board[0][7] = BLACK_ROOK;
        for (int i = 0; i < 8; i++) {
            board[1][i] = BLACK_PAWN;
        }
        for (int i = 0; i < 8; i++) {
            board[6][i] = WHITE_PAWN;
        }
        board[7][0] = WHITE_ROOK;
        board[7][1] = WHITE_KNIGHT;
        board[7][2] = WHITE_BISHOP;
        board[7][3] = WHITE_QUEEN;
        board[7][4] = WHITE_KING;
        board[7][5] = WHITE_BISHOP;
        board[7][6] = WHITE_KNIGHT;
        board[7][7] = WHITE_ROOK;
    }

    int pieceAt(int row, int col) {
        return board[row][col];
    }

    void setPiece(int row, int col, int piece) {
        board[row][col] = piece;
    }

    void makeMove(ChessMove move) {
        makeMove(move.r1, move.c2, move.r2, move.c2);
    }

    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        setPiece(toRow, toCol, pieceAt(fromRow, fromCol));
        setPiece(fromRow, fromCol, EMPTY);
    }

    ChessMove[] getLegalMoves(int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        ChessMove[] temp;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                temp = getLegalMovesSingle(row, col, player);
                for (int i = 0; i < temp.length; i++) {
                    output.add(temp[i]);
                }
            }
        }
        return convertToArray(output);
    }

    ChessMove[] convertToArray(ArrayList<ChessMove> output) {
        ChessMove[] legalMoves = new ChessMove[output.size()];
        for (int i = 0; i < output.size(); i++) {
            legalMoves[i] = output.get(i);
        }
        return legalMoves;
    }

    ChessMove[] getLegalMovesSingle(int row, int col, int player) {
        if (player == WHITE_PLAYER && !isEmpty(pieceAt(row, col))) {
            switch (pieceAt(row, col)) {
                case WHITE_PAWN:
                    return getPawnMoves(row, col, player);
                case WHITE_ROOK:
                    return getRookMoves(row, col, player);
                case WHITE_KNIGHT:
                    return getKnightMoves(row, col, player);
                case WHITE_BISHOP:
                    return getBishopMoves(row, col, player);
                case WHITE_QUEEN:
                    return getQueenMoves(row, col, player);
                case WHITE_KING:
                    return getKingMoves(row, col, player);
            }
        } else if (player == BLACK_PLAYER && !isEmpty(pieceAt(row, col))) {
            switch (pieceAt(row, col)) {
                case BLACK_PAWN:
                    return getPawnMoves(row, col, player);
                case BLACK_ROOK:
                    return getRookMoves(row, col, player);
                case BLACK_KNIGHT:
                    return getKnightMoves(row, col, player);
                case BLACK_BISHOP:
                    return getBishopMoves(row, col, player);
                case BLACK_QUEEN:
                    return getQueenMoves(row, col, player);
                case BLACK_KING:
                    return getKingMoves(row, col, player);
            }
        }
        return new ChessMove[0];
    }

    ChessMove[] getPawnMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        return convertToArray(output);
    }

    ChessMove[] getRookMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        return convertToArray(output);
    }

    ChessMove[] getKnightMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        return convertToArray(output);
    }

    ChessMove[] getBishopMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        return convertToArray(output);
    }

    ChessMove[] getKingMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        return convertToArray(output);
    }

    ChessMove[] getQueenMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        return convertToArray(output);
    }

    int swapPlayer(int player) {
        return player == WHITE_PLAYER ? BLACK_PLAYER : WHITE_PLAYER;
    }

    boolean canMove(int row1, int col1, int row2, int col2, int player) {
        return !isOutOfBounds(row2, col2) && (isEmpty(pieceAt(row2, col2)) || isPlayer(pieceAt(row2, col2), player));
    }

    boolean isEmpty(int piece) {
        return piece == EMPTY;
    }

    boolean isEmpty(int row, int col) {
        return !isOutOfBounds(row, col) && isEmpty(pieceAt(row, col));
    }

    boolean isPlayer(int piece, int player) {
        if (player == WHITE_PLAYER) {
            return piece == WHITE_PAWN
                || piece == WHITE_ROOK
                || piece == WHITE_KNIGHT
                || piece == WHITE_BISHOP
                || piece == WHITE_KING
                || piece == WHITE_QUEEN;
        } else {
            return piece == BLACK_PAWN
                || piece == BLACK_ROOK
                || piece == BLACK_KNIGHT
                || piece == BLACK_BISHOP
                || piece == BLACK_KING
                || piece == BLACK_QUEEN;
        }
    }

    boolean isOutOfBounds(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return true;
        }
        return false;
    }

    boolean checkIf(int row, int col, int piece) {
        return !isOutOfBounds(row, col) && board[row][col] == piece;
    }

}
