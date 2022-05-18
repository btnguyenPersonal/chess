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

    ChessData(ChessData input) {
        board = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = input.board[i][j];
            }
        }
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

    public void setUpGame() {
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

    public int pieceAt(int row, int col) {
        return board[row][col];
    }

    private void setPiece(int row, int col, int piece) {
        if ((piece == WHITE_PAWN && row == 0) || (piece == BLACK_PAWN && row == 7)) {
            board[row][col] = (piece == WHITE_PAWN ? WHITE_QUEEN : BLACK_QUEEN);
        } else {
            board[row][col] = piece;
        }
    }

    public void makeMove(ChessMove move) {
        makeMove(move.r1, move.c1, move.r2, move.c2);
    }

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        setPiece(toRow, toCol, pieceAt(fromRow, fromCol));
        setPiece(fromRow, fromCol, EMPTY);
    }

    public ChessMove[] getLegalMoves(int player) {
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
        output = filterInvalidMoves(this, output, player);
        if (output.size() == 0) {
            return null;
        }
        return convertToArray(output);
    }

    public ChessMove[] getLegalMovesUnsafe(int player) {
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

    private ArrayList<ChessMove> filterInvalidMoves(ChessData startBoard, ArrayList<ChessMove> input, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        for (ChessMove move : input) {
            ChessData temp = new ChessData(startBoard);
            temp.makeMove(move);
            System.out.println(temp);
            if (!kingInCheck(temp, player)) {
                output.add(move);
            }
        }
        return output;
    }

    private boolean kingInCheck(ChessData startBoard, int player) {
        ChessData temp = new ChessData(startBoard);
        boolean kingInCheck = false;
        ChessMove[] counterMoves = temp.getLegalMovesUnsafe(swapPlayer(player));
        for (ChessMove tempMove : counterMoves) {
            temp.makeMove(tempMove);
            if (temp.getKingRow(player) == -1) {
                kingInCheck = true;
            }
        }
        return kingInCheck;
    }

    private ChessMove[] convertToArray(ArrayList<ChessMove> output) {
        ChessMove[] legalMoves = new ChessMove[output.size()];
        for (int i = 0; i < output.size(); i++) {
            legalMoves[i] = output.get(i);
        }
        return legalMoves;
    }

    private ChessMove[] getLegalMovesSingle(int row, int col, int player) {
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

    private ChessMove[] getPawnMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        if (player == WHITE_PLAYER) {
            if (row == 6 && canMove(row, col, row - 2, col, player) && isEmpty(row - 1, col) && isEmpty(row - 2, col)) {
                output.add(new ChessMove(row, col, row - 2, col));
            }
            if (canMove(row, col, row - 1, col, player) && isEmpty(row - 1, col)) {
                output.add(new ChessMove(row, col, row - 1, col));
            }
            if (canMove(row, col, row - 1, col - 1, player) && !isEmpty(row - 1, col - 1)) {
                output.add(new ChessMove(row, col, row - 1, col - 1));
            }
            if (canMove(row, col, row - 1, col + 1, player) && !isEmpty(row - 1, col + 1)) {
                output.add(new ChessMove(row, col, row - 1, col + 1));
            }
        } else {
            if (row == 1 && canMove(row, col, row + 2, col, player) && isEmpty(row + 1, col) && isEmpty(row + 2, col)) {
                output.add(new ChessMove(row, col, row + 2, col));
            }
            if (canMove(row, col, row + 1, col, player) && isEmpty(row + 1, col)) {
                output.add(new ChessMove(row, col, row + 1, col));
            }
            if (canMove(row, col, row + 1, col - 1, player) && !isEmpty(row + 1, col - 1)) {
                output.add(new ChessMove(row, col, row + 1, col - 1));
            }
            if (canMove(row, col, row + 1, col + 1, player) && !isEmpty(row + 1, col + 1)) {
                output.add(new ChessMove(row, col, row + 1, col + 1));
            }
        }
        return convertToArray(output);
    }

    private ChessMove[] getRookMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row + i, col) || !canMove(row, col, row + i, col, player)) {
                break;
            } else if (canMove(row, col, row + i, col, player) && !isEmpty(row + i, col)) {
                output.add(new ChessMove(row, col, row + i, col));
                break;
            } else {
                output.add(new ChessMove(row, col, row + i, col));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row - i, col) || !canMove(row, col, row - i, col, player)) {
                break;
            } else if (canMove(row, col, row - i, col, player) && !isEmpty(row - i, col)) {
                output.add(new ChessMove(row, col, row - i, col));
                break;
            } else {
                output.add(new ChessMove(row, col, row - i, col));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row, col + i) || !canMove(row, col, row, col + i, player)) {
                break;
            } else if (canMove(row, col, row, col + i, player) && !isEmpty(row, col + i)) {
                output.add(new ChessMove(row, col, row, col + i));
                break;
            } else {
                output.add(new ChessMove(row, col, row, col + i));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row, col - i) || !canMove(row, col, row, col - i, player)) {
                break;
            } else if (canMove(row, col, row, col - i, player) && !isEmpty(row, col - i)) {
                output.add(new ChessMove(row, col, row, col - i));
                break;
            } else {
                output.add(new ChessMove(row, col, row, col - i));
            }
        }
        return convertToArray(output);
    }

    private ChessMove[] getKnightMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -2; j <= 2; j += 4) {
                if (canMove(row, col, row + i, col + j, player)) {
                    output.add(new ChessMove(row, col, row + i, col + j));
                }
                if (canMove(row, col, row + j, col + i, player)) {
                    output.add(new ChessMove(row, col, row + j, col + i));
                }
            }
        }
        return convertToArray(output);
    }

    private ChessMove[] getBishopMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row + i, col - i) || !canMove(row, col, row + i, col - i, player)) {
                break;
            } else if (canMove(row, col, row + i, col - i, player) && !isEmpty(row + i, col - i)) {
                output.add(new ChessMove(row, col, row + i, col - i));
                break;
            } else {
                output.add(new ChessMove(row, col, row + i, col - i));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row - i, col - i) || !canMove(row, col, row - i, col - i, player)) {
                break;
            } else if (canMove(row, col, row - i, col - i, player) && !isEmpty(row - i, col - i)) {
                output.add(new ChessMove(row, col, row - i, col - i));
                break;
            } else {
                output.add(new ChessMove(row, col, row - i, col - i));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row + i, col + i) || !canMove(row, col, row + i, col + i, player)) {
                break;
            } else if (canMove(row, col, row + i, col + i, player) && !isEmpty(row + i, col + i)) {
                output.add(new ChessMove(row, col, row + i, col + i));
                break;
            } else {
                output.add(new ChessMove(row, col, row + i, col + i));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row - i, col + i) || !canMove(row, col, row - i, col + i, player)) {
                break;
            } else if (canMove(row, col, row - i, col + i, player) && !isEmpty(row - i, col + i)) {
                output.add(new ChessMove(row, col, row - i, col + i));
                break;
            } else {
                output.add(new ChessMove(row, col, row - i, col + i));
            }
        }
        return convertToArray(output);
    }

    private ChessMove[] getKingMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && !isOutOfBounds(row + i, col + j) && canMove(row, col, row + i, col + j, player)) {
                    output.add(new ChessMove(row, col, row + i, col + j));
                }
            }
        }
        return convertToArray(output);
    }

    private ChessMove[] getQueenMoves(int row, int col, int player) {
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row + i, col - i) || !canMove(row, col, row + i, col - i, player)) {
                break;
            } else if (canMove(row, col, row + i, col - i, player) && !isEmpty(row + i, col - i)) {
                output.add(new ChessMove(row, col, row + i, col - i));
                break;
            } else {
                output.add(new ChessMove(row, col, row + i, col - i));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row - i, col - i) || !canMove(row, col, row - i, col - i, player)) {
                break;
            } else if (canMove(row, col, row - i, col - i, player) && !isEmpty(row - i, col - i)) {
                output.add(new ChessMove(row, col, row - i, col - i));
                break;
            } else {
                output.add(new ChessMove(row, col, row - i, col - i));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row + i, col + i) || !canMove(row, col, row + i, col + i, player)) {
                break;
            } else if (canMove(row, col, row + i, col + i, player) && !isEmpty(row + i, col + i)) {
                output.add(new ChessMove(row, col, row + i, col + i));
                break;
            } else {
                output.add(new ChessMove(row, col, row + i, col + i));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row - i, col + i) || !canMove(row, col, row - i, col + i, player)) {
                break;
            } else if (canMove(row, col, row - i, col + i, player) && !isEmpty(row - i, col + i)) {
                output.add(new ChessMove(row, col, row - i, col + i));
                break;
            } else {
                output.add(new ChessMove(row, col, row - i, col + i));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row + i, col) || !canMove(row, col, row + i, col, player)) {
                break;
            } else if (canMove(row, col, row + i, col, player) && !isEmpty(row + i, col)) {
                output.add(new ChessMove(row, col, row + i, col));
                break;
            } else {
                output.add(new ChessMove(row, col, row + i, col));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row - i, col) || !canMove(row, col, row - i, col, player)) {
                break;
            } else if (canMove(row, col, row - i, col, player) && !isEmpty(row - i, col)) {
                output.add(new ChessMove(row, col, row - i, col));
                break;
            } else {
                output.add(new ChessMove(row, col, row - i, col));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row, col + i) || !canMove(row, col, row, col + i, player)) {
                break;
            } else if (canMove(row, col, row, col + i, player) && !isEmpty(row, col + i)) {
                output.add(new ChessMove(row, col, row, col + i));
                break;
            } else {
                output.add(new ChessMove(row, col, row, col + i));
            }
        }
        for (int i = 1; i < 8; i++) {
            if (isOutOfBounds(row, col - i) || !canMove(row, col, row, col - i, player)) {
                break;
            } else if (canMove(row, col, row, col - i, player) && !isEmpty(row, col - i)) {
                output.add(new ChessMove(row, col, row, col - i));
                break;
            } else {
                output.add(new ChessMove(row, col, row, col - i));
            }
        }
        return convertToArray(output);
    }

    private int swapPlayer(int player) {
        return player == WHITE_PLAYER ? BLACK_PLAYER : WHITE_PLAYER;
    }

    private boolean canMove(int row1, int col1, int row2, int col2, int player) {
        // TODO need to add not unprotecting the king from getting taken
        return !isOutOfBounds(row2, col2) && (isEmpty(pieceAt(row2, col2)) || isPlayer(pieceAt(row2, col2), swapPlayer(player)));
    }

    private boolean isEmpty(int piece) {
        return piece == EMPTY;
    }

    private boolean isEmpty(int row, int col) {
        return !isOutOfBounds(row, col) && isEmpty(pieceAt(row, col));
    }

    private boolean isPlayer(int piece, int player) {
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

    private int getKingCol(int player) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((pieceAt(row, col) == WHITE_KING && player == WHITE_PLAYER) || (pieceAt(row, col) == BLACK_KING && player == BLACK_PLAYER)) {
                    return col;
                }
            }
        }
        return -1;
    }

    private int getKingRow(int player) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((pieceAt(row, col) == WHITE_KING && player == WHITE_PLAYER) || (pieceAt(row, col) == BLACK_KING && player == BLACK_PLAYER)) {
                    return row;
                }
            }
        }
        return -1;
    }

    private boolean isOutOfBounds(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return true;
        }
        return false;
    }

    public double getEvaluation(int player) {
        if (getLegalMoves(player) == null) {
            if (kingInCheck(this, player)) {
                return player == WHITE_PLAYER ? 9999 : -9999;
            } else {
                return 0;
            }
        }
        int n;
        double w = 0;
        double b = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                n = pieceAt(row, col);
                if (n == WHITE_PAWN) {
                    w++;
                } else if (n == WHITE_ROOK) {
                    w += 5;
                } else if (n == WHITE_KNIGHT) {
                    w += 3;
                } else if (n == WHITE_BISHOP) {
                    w += 3;
                } else if (n == WHITE_QUEEN) {
                    w += 9;
                } else if (n == WHITE_KING) {
                    w += 1000;
                } else if (n == BLACK_PAWN) {
                    b++;
                } else if (n == BLACK_ROOK) {
                    b += 5;
                } else if (n == BLACK_KNIGHT) {
                    b += 3;
                } else if (n == BLACK_BISHOP) {
                    b += 3;
                } else if (n == BLACK_QUEEN) {
                    b += 9;
                } else if (n == BLACK_KING) {
                    b += 1000;
                }
            }
        }
        return w - b;
    }
}
