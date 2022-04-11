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
public class CheckersData {

    /*
     * The following constants represent the possible contents of a square
     * on the board. The constants RED and BLACK also represent players
     * in the game.
     */

    static final int EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;

    int[][] board; // board[r][c] is the contents of row r, column c.

    /**
     * Constructor. Create the board and set it up for a new game.
     */
    CheckersData() {
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
                if (n == 0) {
                    sb.append(" ");
                } else if (n == 1) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == 2) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == 3) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == 4) {
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
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == col % 2 && row < 3) {
                    board[row][col] = BLACK;
                }
                if (row % 2 == col % 2 && row > 4) {
                    board[row][col] = RED;
                }
            }
        }
    }

    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        return board[row][col];
    }

    /**
     * Make the specified move. It is assumed that move
     * is non-null and that the move it represents is legal.
     *
     * Update 03/18: make a single move or a sequence of jumps
     * recorded in rows and cols.
     *
     */
    void makeMove(CheckersMove move) {
        int l = move.rows.size();
        for (int i = 0; i < l - 1; i++)
            makeMove(move.rows.get(i), move.cols.get(i), move.rows.get(i + 1), move.cols.get(i + 1));
    }

    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol). It is
     * assumed that this move is legal. If the move is a jump, the
     * jumped piece is removed from the board. If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     */
    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Update the board for the given move. You need to take care of the following
        // situations:
        // 1. move the piece from (fromRow,fromCol) to (toRow,toCol)
        // 2. if this move is a jump, remove the captured piece
        // 3. if the piece moves into the kings row on the opponent's side of the board,
        // crowned it as a king
        if (isType(RED, board[fromRow][fromCol]) && toRow == 0) {
            board[toRow][toCol] = RED_KING;
        } else if (isType(BLACK, board[fromRow][fromCol]) && toRow == 7) {
            board[toRow][toCol] = BLACK_KING;
        } else {
            board[toRow][toCol] = board[fromRow][fromCol];
        }
        board[fromRow][fromCol] = EMPTY;
        CheckersMove move = new CheckersMove(fromRow, fromCol, toRow, toCol);
        if (move.isJump()) {
            removeJumpedPiece(fromRow, fromCol, toRow, toCol);
        }
    }

    void removeJumpedPiece(int fromRow, int fromCol, int toRow, int toCol) {
        int row = (fromRow + toRow) / 2;
        int col = (fromCol + toCol) / 2;
        board[row][col] = EMPTY;
    }

    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board. If the player
     * has no legal moves, null is returned. The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned. If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    CheckersMove[] getLegalMoves(int player) {
        ArrayList<CheckersMove> legalMoves = new ArrayList<CheckersMove>();
        legalMoves = getLegalMovesHelper(player, boardContainsJumps(player));
        if (legalMoves.size() == 0) {
            return null;
        }
        CheckersMove[] output = new CheckersMove[legalMoves.size()];
        for (int i = 0; i < legalMoves.size(); i++) {
            output[i] = legalMoves.get(i);
        }
        return output;
    }

    ArrayList<CheckersMove> getLegalMovesHelper(int player, boolean hasJumps) {
        ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
        ArrayList<CheckersMove> output = new ArrayList<CheckersMove>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (isType(player, board[row][col])) {
                    if (hasJumps) {
                        moves = getHelperLegalJumpsFrom(player, row, col);
                    } else {
                        moves = getLegalMovesSingle(player, row, col);
                    }
                }
                for (int i = 0; i < moves.size(); i++) {
                    output.add(moves.get(i));
                }
            }
        }
        return output;
    }

    boolean isType(int player, int boardSquare) {
        if (player == RED) {
            return boardSquare == RED || boardSquare == RED_KING;
        } else if (player == BLACK) {
            return boardSquare == BLACK || boardSquare == BLACK_KING;
        }
        return false;
    }

    ArrayList<CheckersMove> getLegalMovesSingle(int player, int row, int col) {
        ArrayList<CheckersMove> legalMoves = new ArrayList<CheckersMove>();
        switch (player) {
            case RED:
                if (checkIf(row, col, RED_KING)) {
                    if (checkIf(row + 1, col + 1, EMPTY)) {
                        legalMoves.add(new CheckersMove(row, col, row + 1, col + 1));
                    }
                    if (checkIf(row + 1, col - 1, EMPTY)) {
                        legalMoves.add(new CheckersMove(row, col, row + 1, col - 1));
                    }
                }
                if (checkIf(row - 1, col + 1, EMPTY)) {
                    legalMoves.add(new CheckersMove(row, col, row - 1, col + 1));
                }
                if (checkIf(row - 1, col - 1, EMPTY)) {
                    legalMoves.add(new CheckersMove(row, col, row - 1, col - 1));
                }
                break;
            case BLACK:
                if (checkIf(row, col, BLACK_KING)) {
                    if (checkIf(row - 1, col + 1, EMPTY)) {
                        legalMoves.add(new CheckersMove(row, col, row - 1, col + 1));
                    }
                    if (checkIf(row - 1, col - 1, EMPTY)) {
                        legalMoves.add(new CheckersMove(row, col, row - 1, col - 1));
                    }
                }
                if (checkIf(row + 1, col + 1, EMPTY)) {
                    legalMoves.add(new CheckersMove(row, col, row + 1, col + 1));
                }
                if (checkIf(row + 1, col - 1, EMPTY)) {
                    legalMoves.add(new CheckersMove(row, col, row + 1, col - 1));
                }
                break;
            default:
                break;
        }
        return legalMoves;
    }

    boolean checkIf(int row, int col, int color) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return false;
        } else {
            return board[row][col] == color;
        }
    }

    boolean boardContainsJumps(int player) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (isType(player, board[row][col])) {
                    ArrayList<CheckersMove> jumps = getHelperLegalJumpsFrom(player, row, col);
                    if (jumps.size() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    int getNumJumps(int player) {
        int i = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == BLACK || board[row][col] == BLACK_KING) {
                    ArrayList<CheckersMove> jumps = getHelperLegalJumpsFrom(player, row, col);
                    if (jumps.size() > 0) {
                        i++;
                    }
                }
            }
        }
        return i;
    }

    ArrayList<CheckersMove> getDoubleJumpsFrom(CheckersMove previousMove, boolean isKing, int player, int row, int col) {
        ArrayList<CheckersMove> doubleJumps = new ArrayList<CheckersMove>();
        switch (player) {
            case RED:
                if (isKing) {
                    if (
                        (checkIf(row + 1, col + 1, BLACK) || checkIf(row + 1, col + 1, BLACK_KING))
                        && checkIf(row + 2, col + 2, EMPTY)
                        ) {
                        CheckersMove newMove = previousMove.clone();
                        newMove.addMove(row + 2, col + 2);
                        doubleJumps.add(newMove);
                    }
                    if (
                        (checkIf(row + 1, col - 1, BLACK) || checkIf(row + 1, col - 1, BLACK_KING))
                        && checkIf(row + 2, col - 2, EMPTY)
                        ) {
                        CheckersMove newMove = previousMove.clone();
                        newMove.addMove(row + 2, col - 2);
                        doubleJumps.add(newMove);
                    }
                }
                if (
                    (checkIf(row - 1, col + 1, BLACK) || checkIf(row - 1, col + 1, BLACK_KING))
                    && checkIf(row - 2, col + 2, EMPTY)
                    ) {
                    CheckersMove newMove = previousMove.clone();
                    newMove.addMove(row - 2, col + 2);
                    doubleJumps.add(newMove);
                }
                if (
                    (checkIf(row - 1, col - 1, BLACK) || checkIf(row - 1, col - 1, BLACK_KING))
                    && checkIf(row - 2, col - 2, EMPTY)
                    ) {
                    CheckersMove newMove = previousMove.clone();
                    newMove.addMove(row - 2, col - 2);
                    doubleJumps.add(newMove);
                }
                break;
            case BLACK:
                if (isKing) {
                    if (
                    (checkIf(row - 1, col + 1, RED) || checkIf(row - 1, col + 1, RED_KING))
                    && checkIf(row - 2, col + 2, EMPTY)
                    ) {
                        CheckersMove newMove = previousMove.clone();
                        newMove.addMove(row - 2, col + 2);
                        doubleJumps.add(newMove);
                    }
                    if (
                    (checkIf(row - 1, col - 1, RED) || checkIf(row - 1, col - 1, RED_KING))
                    && checkIf(row - 2, col - 2, EMPTY)
                    ) {
                        CheckersMove newMove = previousMove.clone();
                        newMove.addMove(row - 2, col - 2);
                        doubleJumps.add(newMove);
                    }
                }
                if (
                    (checkIf(row + 1, col + 1, RED) || checkIf(row + 1, col + 1, RED_KING))
                    && checkIf(row + 2, col + 2, EMPTY)
                    ) {
                    CheckersMove newMove = previousMove.clone();
                    newMove.addMove(row + 2, col + 2);
                    doubleJumps.add(newMove);
                }
                if (
                    (checkIf(row + 1, col - 1, RED) || checkIf(row + 1, col - 1, RED_KING))
                    && checkIf(row + 2, col - 2, EMPTY)
                    ) {
                    CheckersMove newMove = previousMove.clone();
                    newMove.addMove(row + 2, col - 2);
                    doubleJumps.add(newMove);
                }
                break;
            default:
                break;
        }
        return doubleJumps;
    }


    ArrayList<CheckersMove> getHelperLegalJumpsFrom(int player, int row, int col) {
        ArrayList<CheckersMove> legalJumps = new ArrayList<CheckersMove>();
        switch (player) {
            case RED:
                if (checkIf(row, col, RED_KING)) {
                    if (
                        (checkIf(row, col, RED) || checkIf(row, col, RED_KING))
                        && (checkIf(row + 1, col + 1, BLACK) || checkIf(row + 1, col + 1, BLACK_KING))
                        && checkIf(row + 2, col + 2, EMPTY)
                        ) {
                        legalJumps.add(new CheckersMove(row, col, row + 2, col + 2));
                    }
                    if (
                        (checkIf(row, col, RED) || checkIf(row, col, RED_KING))
                        && (checkIf(row + 1, col - 1, BLACK) || checkIf(row + 1, col - 1, BLACK_KING))
                        && checkIf(row + 2, col - 2, EMPTY)
                        ) {
                        legalJumps.add(new CheckersMove(row, col, row + 2, col - 2));
                    }
                }
                if (
                    (checkIf(row, col, RED) || checkIf(row, col, RED_KING))
                    && (checkIf(row - 1, col + 1, BLACK) || checkIf(row - 1, col + 1, BLACK_KING))
                    && checkIf(row - 2, col + 2, EMPTY)
                    ) {
                    legalJumps.add(new CheckersMove(row, col, row - 2, col + 2));
                }
                if (
                    (checkIf(row, col, RED) || checkIf(row, col, RED_KING))
                    && (checkIf(row - 1, col - 1, BLACK) || checkIf(row - 1, col - 1, BLACK_KING))
                    && checkIf(row - 2, col - 2, EMPTY)
                    ) {
                    legalJumps.add(new CheckersMove(row, col, row - 2, col - 2));
                }
                break;
            case BLACK:
                if (checkIf(row, col, BLACK_KING)) {
                    if (
                    (checkIf(row, col, BLACK) || checkIf(row, col, BLACK_KING))
                    && (checkIf(row - 1, col + 1, RED) || checkIf(row - 1, col + 1, RED_KING))
                    && checkIf(row - 2, col + 2, EMPTY)
                    ) {
                        legalJumps.add(new CheckersMove(row, col, row - 2, col + 2));
                    }
                    if (
                    (checkIf(row, col, BLACK) || checkIf(row, col, BLACK_KING))
                    && (checkIf(row - 1, col - 1, RED) || checkIf(row - 1, col - 1, RED_KING))
                    && checkIf(row - 2, col - 2, EMPTY)
                    ) {
                        legalJumps.add(new CheckersMove(row, col, row - 2, col - 2));
                    }
                }
                if (
                    (checkIf(row, col, BLACK) || checkIf(row, col, BLACK_KING))
                    && (checkIf(row + 1, col + 1, RED) || checkIf(row + 1, col + 1, RED_KING))
                    && checkIf(row + 2, col + 2, EMPTY)
                    ) {
                    legalJumps.add(new CheckersMove(row, col, row + 2, col + 2));
                }
                if (
                    (checkIf(row, col, BLACK) || checkIf(row, col, BLACK_KING))
                    && (checkIf(row + 1, col - 1, RED) || checkIf(row + 1, col - 1, RED_KING))
                    && checkIf(row + 2, col - 2, EMPTY)
                    ) {
                    legalJumps.add(new CheckersMove(row, col, row + 2, col - 2));
                }
                break;
            default:
                break;
        }
        ArrayList<CheckersMove> doubleJumps = new ArrayList<CheckersMove>();
        ArrayList<CheckersMove> temp = new ArrayList<CheckersMove>();
        for(CheckersMove move : legalJumps) {
            temp = getDoubleJumpsFrom(move, isKing(move), player, move.getLastRow(), move.getLastCol());
            for(int i = 0; i < temp.size(); i++) {
                doubleJumps.add(temp.get(i));
            }
        }
        for (int i = 0; i < doubleJumps.size(); i++) {
            legalJumps.add(doubleJumps.get(i));
        }
        return legalJumps;
    }

    boolean isKing(CheckersMove m) {
        return board[m.rows.get(0)][m.cols.get(0)] == RED_KING
            || board[m.rows.get(0)][m.cols.get(0)] == BLACK_KING;
    }

    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column. If no such
     * jumps are possible, null is returned. The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * Update 03/18: Note that each CheckerMove may contain multiple jumps.
     * Each move returned in the array represents a sequence of jumps
     * until no further jump is allowed.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param row    row index of the start square.
     * @param col    col index of the start square.
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        ArrayList<CheckersMove> legalJumps = getHelperLegalJumpsFrom(player, row, col);
        CheckersMove[] output = new CheckersMove[legalJumps.size()];
        for (int i = 0; i < legalJumps.size(); i++) {
            output[i] = legalJumps.get(i);
        }
        return output;
    }
}
