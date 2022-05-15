package edu.iastate.cs472.proj2;

/**
 * This class implements the Monte Carlo tree search method to find the best
 * move at the current state.
 */
public class MonteCarloTreeSearch extends AdversarialSearch {

    public ChessMove makeMove(ChessMove[] legalMoves) {
        System.out.println(board);
        System.out.println();
        return legalMoves[0];
    }
}
