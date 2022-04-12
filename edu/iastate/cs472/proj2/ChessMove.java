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
    
    ArrayList<Integer> rows = new ArrayList<Integer>();
    ArrayList<Integer> cols = new ArrayList<Integer>();
    
    ChessMove(int r1, int c1, int r2, int c2) {
        // Constructor, a single move from
        //(r1, c1) to (r2, c2)
        
        // move's start
        rows.add(r1);
        cols.add(c1);
        
        // move's destination
        rows.add(r2);
        cols.add(c2);
    }
    
    ChessMove() {
        // Constructor, create an empty move
    }

    int getLastCol() {
        return cols.get(cols.size() - 1);
    }

    int getLastRow() {
        return rows.get(rows.size() - 1);
    }
    
    boolean isJump() {
        // Test whether this move is a jump.  It is assumed that
        // the move is legal.  In a jump, the piece moves two
        // rows.  (In a regular move, it only moves one row.)
        return (rows.get(0) - rows.get(1) == 2 || rows.get(0) - rows.get(1) == -2);
    }
    
    
    void addMove(int r, int c){
        // add another move (continuous jump), which goes from
        // (last ele in rows, last ele in cols) to (r, c)
        rows.add(r);
        cols.add(c);
    }
    
    //get a copy of this move
    @Override
    public ChessMove clone() {
        ChessMove move = new ChessMove();
        
        move.rows.addAll(this.rows);
        move.cols.addAll(this.cols);
        
        return move;
        
    }

    //get a copy of this move
    @Override
    public String toString() {
        return getCoors();
    }

    public String getCoors() {
        String output = "\nMove:\n";
        for (int i = 0; i < rows.size(); i++) {
            output += "    (" + rows.get(i) + ", " + cols.get(i) + ")";
            output += "\n";
        }
        return output;
    }

}  // end class ChessMove.
