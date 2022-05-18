package edu.iastate.cs472.proj2; 

public class ChessTree
{
	ChessNode root;
	int size = 0;

    public ChessTree(ChessNode r) {
        root = r;
        r.expandRandomMove();
    }

    public ChessNode getRoot() {
        return root;
    }

    public int getSize() {
        return size;
    }

    public void incSize() {
        size++;
    }

    public void setRoot(ChessNode e) {
        root = e;
    }

    public ChessMove getBestMove() {
        int highestPlayouts = root.children.get(0).getPlayouts();
        ChessNode bestNode = root.children.get(0);
        for (int i = 1; i < root.children.size(); i++) {
            if (root.children.get(i).getPlayouts() >= highestPlayouts) {
                highestPlayouts = root.children.get(i).getPlayouts();
                bestNode = root.children.get(i);
            }
        }
        return bestNode.prevMove;
    }

    public ChessNode getMaxNode(ChessNode a, ChessNode b) {
        if ((double) a.getWins() / (double) a.getPlayouts() > (double) b.getWins() / (double) b.getPlayouts()) {
            return a;
        }
        return b;
    }

    public void printTree() {
        System.out.println("=================================root===========================");
        printNode(root);
        for (ChessNode node : root.children) {
            printNode(node);
        }
    }

    public void printNode(ChessNode n) {
        System.out.println();
        System.out.println(n.data);
        System.out.println(n.getWins() + "/" + n.getPlayouts());
        System.out.println();
        //if (n.children != null) {
        //    for (ChessNode node : n.children) {
        //        printNode(node);
        //    }
        //}
    }
}
