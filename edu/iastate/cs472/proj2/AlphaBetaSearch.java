package edu.iastate.cs472.proj2;

public class AlphaBetaSearch extends AdversarialSearch {

    public ChessMove makeMove(ChessMove[] legalMoves) {
        System.out.println(board);
        System.out.println();
        return AlphaBetaSearch(legalMoves);
    }

    int swapPlayer(int player) {
        return player == ChessData.WHITE_PLAYER ? ChessData.BLACK_PLAYER : ChessData.WHITE_PLAYER;
    }

    ChessMove AlphaBetaSearch(ChessMove[] legalMoves) {
        int depth = 3;
        double min = 9999;
        double value = 0;
        int lowestIndex = 0;
        for (int i = 0; i < legalMoves.length; i++) {
            ChessData temp_state = new ChessData(board);
            temp_state.makeMove(legalMoves[i]);
            value = MaxValue(temp_state, ChessData.WHITE_PLAYER, depth - 1, -9999, 9999)[0];
            if (min > value) {
                min = value;
                lowestIndex = i;
            }
        }
        return legalMoves[lowestIndex];
    }

    double[] MaxValue(ChessData state, int player, int depth, double alpha, double beta) {
        ChessMove[] moves = state.getLegalMoves(player);
        if (moves == null) {
            double[] value = new double[2];
            value[0] = eval(state, player);
            return value;
        }
        double[] v = {alpha, beta};
        for (int i = 0; i < moves.length; i++) {
            // if reached max depth, set B to eval
            if (depth == 0) {
                v[0] = eval(state, player);
                break;
            } else {
                // get value of child node
                double[] new_value = new double[2];
                ChessData temp_state = new ChessData(state);
                temp_state.makeMove(moves[i]);
                new_value = MinValue(temp_state, swapPlayer(player), depth - 1, v[0], v[1]);
                // if A >= B prune
                if (v[0] >= v[1]) {
                    return v;
                }
                // if A < B_child
                if (v[0] < new_value[1]) {
                    // set A to B_child
                    v[0] = new_value[1];
                }
            }
        }
        return v;
    }

    double[] MinValue(ChessData state, int player, int depth, double alpha, double beta) {
        ChessMove[] moves = state.getLegalMoves(player);
        if (moves == null) {
            double[] value = new double[2];
            value[1] = eval(state, player);
            return value;
        }
        double[] v = {alpha, beta};
        for (int i = 0; i < moves.length; i++) {
            // if reached max depth, set A to eval
            if (depth == 0) {
                v[1] = eval(state, player);
                break;
            } else {
                // get value of child node
                double[] new_value = new double[2];
                ChessData temp_state = new ChessData(state);
                temp_state.makeMove(moves[i]);
                new_value = MaxValue(temp_state, swapPlayer(player), depth - 1, v[0], v[1]);
                // if B <= A prune
                if (v[1] <= v[0]) {
                    return v;
                }
                // if B > A_child
                if (v[1] > new_value[0]) {
                    // set B to A_child
                    v[1] = new_value[0];
                } 
            }
        }
        return v;
    }

    double eval(ChessData state, int player) {
        return state.getEvaluation(player);
    }

}
