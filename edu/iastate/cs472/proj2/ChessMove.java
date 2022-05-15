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
    
    ChessMove(int r1, int c1, int r2, int c2) {
        this.r1 = r1;
        this.c1 = c1;
        this.r2 = r2;
        this.c2 = c2;
    }
    
    ChessMove() {
    }

    @Override
    public ChessMove clone() {
        return new ChessMove(r1, c1, r2, c2);
    }

    @Override
    public String toString() {
        return getCoors();
    }

    public String getCoors() {
        String output = "\nMove:\n";
        output += "    (" + r1 + ", " + c1 + ")";
        output += "\n";
        output += "    (" + r2 + ", " + c2 + ")";
        output += "\n";
        return output;
    }

}  // end class ChessMove.
