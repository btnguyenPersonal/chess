package edu.iastate.cs472.proj2;
import java.util.Random;
import java.util.ArrayList;

public class ChessNode
{
    public ChessNode parent;
    public ArrayList<ChessNode> children;
    public ChessData data;
    public int player;
    public ChessMove prevMove;
    int playouts = 0;
    double wins = 0;

    public ChessNode(){}

    public ChessNode(ChessData data, int player)
    {
        this(data, null, null, player);
    }

    public ChessNode(ChessData data, ChessNode parent, ArrayList<ChessNode> children, int player)
    {
        this.parent = parent;
        this.children = children;
        this.data = data;
        this.player = player;
    }


    public boolean isLeaf()
    {
        return children == null;
    }

    public void addChild(ChessNode c)
    {
        children.add(c);
    }

    public ArrayList<ChessNode> getChildren()
    {
        return children;
    }

    public void incHalfWins() {
        wins += 0.5;
    }

    public void incWins() {
        wins++;
    }

    public double getWins() {
        return wins;
    }

    public void setWins(int w) {
        wins = w;
    }

    public void incPlayouts() {
        playouts++;
    }

    public int getPlayouts() {
        return playouts;
    }

    public void setChildren(ArrayList<ChessNode> children)
    {
        this.children = children;
    }

    public ChessNode getParent()
    {
        return parent;
    }

    public ChessData getData()
    {
        return data;
    }

    public void setData(ChessData data)
    {
        this.data = data;
    }

    public void printData() {
        System.out.println(data);
    }

    public void expandNode() {
        ChessMove[] moves = data.getLegalMoves(player);
        ArrayList<ChessNode> nodes = new ArrayList<ChessNode>();
        for (int i = 0; i < moves.length; i++) {
            ChessData temp_board = new ChessData(data);
            temp_board.makeMove(moves[i]);
            ChessNode node = new ChessNode(temp_board, this, null, swapPlayer(player));
            node.prevMove = moves[i];
            nodes.add(node);
        }
        setChildren(nodes);
    }

    public ChessNode getRandomMove() {
        Random r = new Random();
        int index = r.nextInt(children.size());
        return children.get(index);
    }

    public ChessNode expandRandomMove() {
        ChessMove[] moves = data.getLegalMoves(player);
        ArrayList<ChessNode> nodes = new ArrayList<ChessNode>();
        for (int i = 0; i < moves.length; i++) {
            ChessData temp_board = new ChessData(data);
            temp_board.makeMove(moves[i]);
            ChessNode node = new ChessNode(temp_board, this, null, swapPlayer(player));
            node.prevMove = moves[i];
            nodes.add(node);
        }
        setChildren(nodes);
        Random r = new Random();
        int index = r.nextInt(moves.length);
        return nodes.get(index);
    }

    public ChessNode simulateRandomMove() {
        ChessMove[] moves = data.getLegalMoves(player);
        int randindex = 0;
        for (ChessMove move : moves) {
            if (move.isCapture(data)) {
                randindex += 3;
            } else {
                randindex++;
            }
        }
        Random r = new Random();
        int index = r.nextInt(randindex);
        ChessData temp_board = new ChessData(data);
        for (int i = 0; i < moves.length; i++) {
            if (moves[i].isCapture(data)) {
                randindex -= 3;
            } else {
                randindex--;
            }
            if (randindex <= index) {
                temp_board.makeMove(moves[i]);
                return new ChessNode(temp_board, null, null, swapPlayer(player));
            }
        }
        temp_board.makeMove(moves[-1]);
        return new ChessNode(temp_board, null, null, swapPlayer(player));
    }

    public int swapPlayer(int player) {
        return (player == ChessData.WHITE_PLAYER ? ChessData.BLACK_PLAYER : ChessData.WHITE_PLAYER);
    }

    public double getUCB() {
        if (playouts == 0) {
            return 99999;
        }
        return ((double) wins / (double) playouts) + 1.41 * Math.sqrt(Math.log((double) parent.getPlayouts()) / (double) playouts);
    }

    public ChessNode getBestUCB() {
        double max = children.get(0).getUCB();
        int index = 0;
        for (int i = 1; i < children.size(); i++) {
            if (max < children.get(i).getUCB()) {
                max = children.get(i).getUCB();
                index = i;
            }
        }
        return children.get(index);
    }

    public boolean isGameOver(int player) {
        return data.getLegalMoves(player) == null || data.isTwoKings();
    }

    public double getGameScore(int player) {
        if (data.getEvaluation(player) == 9999) {
            return 1;
        } else if (data.getEvaluation(player) == -9999) {
            return -1;
        } else {
            return 0;
        }
    }

}

