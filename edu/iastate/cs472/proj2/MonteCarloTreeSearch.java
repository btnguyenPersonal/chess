package edu.iastate.cs472.proj2;

public class MonteCarloTreeSearch extends AdversarialSearch {

	/**
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     *
     * Update 03/18: each legalMove in the input now contains a single move
     * or a sequence of jumps: (rows[0], cols[0]) -> (rows[1], cols[1]) ->
     * (rows[2], cols[2]).
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    public ChessMove makeMove(ChessMove[] legalMoves) {
        System.out.println(board);
        System.out.println();

        ChessNode root = new ChessNode(board, ChessData.BLACK_PLAYER);
        ChessTree gameTree = new ChessTree(root);
        for (int playouts = 0; playouts < 100; playouts++) {
            ChessNode leaf = select(gameTree);
            ChessNode child = leaf;
            if (!child.isGameOver(child.player)) {
                expand(child);
            }
            if (child.children != null) {
                child = child.getRandomMove();
            }
            double result = simulate(child);
            //gameTree.printTree();
            backpropogate(result, child);
        }
        //gameTree.printTree();
        // return best move
        return gameTree.getBestMove();
    }
    
    // start at root, use UCB to select most promising node
    public ChessNode select(ChessTree tree) {
        ChessNode leaf = tree.getRoot();
        while (!leaf.isLeaf()) {
            leaf = leaf.getBestUCB();
        }
        return leaf;
    }

    // choose random move from all possible moves from leaf position
    public void expand(ChessNode leaf) {
        leaf.expandNode();
    }

    // get evaluation afer playing random simulated game
    public double simulate(ChessNode expandedNode) {
        ChessNode temp = expandedNode;
        if (temp.getGameScore(temp.player) == 1) {
            temp.getParent().setWins(-9999);
            return 1;
        }
        while (!temp.isGameOver(temp.player)) {
            temp = temp.simulateRandomMove();
        }
        System.out.println(temp.data);
        return temp.getGameScore(temp.player);
    }

    // back propogation to bring the result back up the tree
    public void backpropogate(double result, ChessNode node) {
        while (node != null) {
            if (result == 0) {
                node.incHalfWins();
            }
            if (result == -1 && node.player == ChessData.BLACK_PLAYER) {
                node.incWins();
            }
            if (result == 1 && node.player == ChessData.WHITE_PLAYER) {
                node.incWins();
            }
            node.incPlayouts();
            node = node.getParent();
        }
    }
}
