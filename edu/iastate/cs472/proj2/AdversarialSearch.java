package edu.iastate.cs472.proj2;

/**
 * 
 * @author
 *
 */

/**
 * This class is to be extended by the classes AlphaBetaSearch and MonteCarloTreeSearch.
 */
public abstract class AdversarialSearch {
    protected ChessData board;

    // An instance of this class will be created in the Chess.Board
    // It would be better to keep the default constructor.

    protected void setChessData(ChessData board) {
        this.board = board;
    }
    
    /** 
     * 
     * @return an array of valid moves
     */
    protected ChessMove[] legalMoves() {
    	// TODO
    	return null; 
    }
	
    /**
     * Return a move returned from either the alpha-beta search or the Monte Carlo tree search.
     * 
     * @param legalMoves
     * @return ChessMove 
     */
    public abstract ChessMove makeMove(ChessMove[] legalMoves);
}
