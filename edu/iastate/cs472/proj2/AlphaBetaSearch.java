package edu.iastate.cs472.proj2;

/**
 * This class implements the Alpha-Beta pruning algorithm to find the best 
 * move at current state.
*/
public class AlphaBetaSearch extends AdversarialSearch {

    public ChessMove makeMove(ChessMove[] legalMoves) {
        System.out.println(board);
        System.out.println();

        return legalMoves[0];
    }

}
