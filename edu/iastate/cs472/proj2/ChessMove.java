package edu.iastate.cs472.proj2;

import java.util.ArrayList;

/**
 * A ChessMove object represents a move in the game of Chess.
 * It holds the row and column of the piece that is to be moved
 * and the row and column of the square to which it is to be moved.
 * (This class makes no guarantee that the move is legal.)
 *
 */
public class ChessMove {
    
    public int r1;
    public int c1;
    public int r2;
    public int c2;

    public boolean isEnPassant = false;
    public boolean doubleMove = false;
    
    ChessMove(int r1, int c1, int r2, int c2) {
        this.r1 = r1;
        this.c1 = c1;
        this.r2 = r2;
        this.c2 = c2;
    }
    
    ChessMove() {
    }

    public boolean isEnPassant() {
        return isEnPassant;
    }

    public void setEnPassant() {
        isEnPassant = true;
    }

    @Override
    public ChessMove clone() {
        return new ChessMove(r1, c1, r2, c2);
    }

    @Override
    public String toString() {
        return getCoors();
    }


    public String convert(int input) {
        switch (input) {
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3:
                return "d";
            case 4:
                return "e";
            case 5:
                return "f";
            case 6:
                return "g";
            case 7:
                return "h";
        }
        return "error";
    }

    public boolean isCapture(ChessData state) {
        if (state.board[r2][c2] != ChessData.EMPTY) {
            return true;
        }
        return false;
    }

    public String getCoors() {
        String output = "\nMove:\n";
        output += "     " + convert(c1) + (r1 + 1) + convert(c2) + (r2 + 1);
        return output;
    }

}  // end class ChessMove.
