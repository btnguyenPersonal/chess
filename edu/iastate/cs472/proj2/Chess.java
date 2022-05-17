package edu.iastate.cs472.proj2;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
//Scanner for the external input.
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * This panel lets two users play chess against each other.
 */
public class Chess extends JPanel {

    /**
     * Main routine makes it possible to run Chess as a stand-alone
     * application.  Opens a window showing a Chess panel; the program
     * ends when the user closes the window.
     */
	//AIKEY
	static int aiKey = 0;
	//To demonstrate previous board
    static boolean chengeValue = false;
    
	public static void main(String[] args) {
		System.out.println("A Chess-Playing Agent");
		// 1: use alpha-beta pruning throughout the game.
		// 2: use Monte Carlo tree search throughout the game.
		// 3: randomly choose between alpha-beta and MCTS to decide on the next move. 
		System.out.println("keys: 1 (alpha-beta)  2 (MCTS)  3 (random)\n");
        JFrame window = new JFrame("Chess");
        
        
        Chess content = new Chess();
        window.setContentPane(content);
        
        window.pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation( (screensize.width - window.getWidth())/2,
                (screensize.height - window.getHeight())/2 );
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setResizable(false);
        window.setVisible(true);
    }

    private JButton newGameButton;  // Button for starting a new game.
    private JButton resignButton;   // Button that a player can use to end
    // the game by resigning.

    private JLabel message;  // Label for displaying messages to the user.
    private static JLabel premessage;
    
    //Previous display
    static PreBoard previous = new PreBoard(); // Display previous board
    /**
     * The constructor creates the Board (which in turn creates and manages
     * the buttons and message label), adds all the components, and sets
     * the bounds of the components.  A null layout is used.  (This is
     * the only thing that is done in the main Chess class.)
     */
    public Chess() {

        setLayout(null);  // I will do the layout myself.
        //setPreferredSize( new Dimension(350,250) );
        setPreferredSize( new Dimension(550,250) );
        setBackground(new Color(0,150,0));  // Dark green background.

        /* Create the components and add them to the applet. */

        Board board = new Board();  // Note: The constructor for the
        //   board also creates the buttons
        //   and label.
        /*
         * Previous board
         */
        add(board);
        add(previous);
        
        add(newGameButton);
        add(resignButton);
        add(message);
        add(premessage);

      /* Set the position and size of each component by calling
       its setBounds() method. */

        //board.setBounds(20,20,164,164); // Note:  size MUST be 164-by-164 !
        previous.setBounds(20,20,164,164);
        board.setBounds(230,20,164,164);
        
        //previous.setBounds(20,20,164,164);
        newGameButton.setBounds(400, 60, 120, 30);
        resignButton.setBounds(400, 120, 120, 30);
        message.setBounds(140, 200, 350, 30);
        premessage.setBounds(40, 200, 350, 30);

        
        
    } // end constructor
 
    /**
     * This panel displays a 160-by-160 checkerboard pattern with
     * a 2-pixel black border.  It is assumed that the size of the
     * panel is set to exactly 164-by-164 pixels.  This class does
     * the work of letting the users play chess, and it displays
     * the chessboard.
     */
    public static class PreBoard extends JPanel{
    	ChessData preBoard;
    	ChessMove moveAI;
    	//setBackground(Color.BLACK);
    	//boolean gameInProgress=true;
    	PreBoard()
    	{
    		premessage = new JLabel("",JLabel.LEFT);
    		premessage.setFont(new  Font("Serif", Font.BOLD, 14));
    		premessage.setForeground(Color.green);
            premessage.setText("Initialization");
            preBoard = new ChessData();
            preBoard.setUpGame();
            moveAI = new ChessMove();
            repaint();
    	}
    	public void drawBoard(ChessData currentBoard, ChessMove move)
    	{
    		premessage = new JLabel("",JLabel.LEFT);
    		premessage.setFont(new  Font("Serif", Font.BOLD, 14));
    		premessage.setForeground(Color.green);
    		premessage.setText("Agent to Play");
    		preBoard = copyBoard(currentBoard);
    		moveAI = move.clone();
    		repaint();
    	}
    	private ChessData copyBoard(ChessData board)
        {
            this.preBoard = board;
            ChessData new_board = new ChessData();
            for(int i=0; i<board.board.length;i++)
            {
                for(int j=0;j<8;j++)
                {
                    new_board.board[i][j]=board.pieceAt(i, j);
                }
            }
            return new_board;
        }
    	
    	public void paintComponent(Graphics g) {
    	    //TODO
            /* Draw a two-pixel black border around the edges of the canvas. */
            g.setColor(Color.black);
            g.drawRect(0,0,getSize().width-1,getSize().height-1);
            g.drawRect(1,1,getSize().width-3,getSize().height-3);
            /* Draw the squares of the chessboard and the chess. */
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if ( row % 2 == col % 2 )
                        g.setColor(Color.LIGHT_GRAY);
                    else
                        g.setColor(Color.GRAY);
                    g.fillRect(2 + col*20, 2 + row*20, 20, 20);
                    switch (preBoard.pieceAt(row,col)) {
                        case ChessData.WHITE_PAWN:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("P", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_ROOK:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("R", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_KNIGHT:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_BISHOP:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("B", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_QUEEN:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("Q", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_KING:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_PAWN:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("P", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_ROOK:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("R", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_KNIGHT:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("N", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_BISHOP:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("B", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_QUEEN:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("Q", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_KING:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                    }
                }
            }
            g.setColor(Color.green);
            g.drawRect(2 + moveAI.c1 * 20, 2 + moveAI.r1 * 20, 19, 19);
            g.drawRect(3 + moveAI.c1 * 20, 3 + moveAI.r1 * 20, 17, 17);
            
        }  // end paintComponent()
    }
    
    private class Board extends JPanel implements ActionListener, MouseListener {
        ChessData board;
        boolean gameInProgress;
        int currentPlayer;
        int selectedRow, selectedCol;
        ChessMove[] legalMoves;
        AdversarialSearch player_1; // AI player, Alpha-beta
        AdversarialSearch player_2; // MCTS
        ChessData displayBoard;
        ChessData agentBoard;
        
        Board() {
            setBackground(Color.BLACK);
            addMouseListener(this);
            resignButton = new JButton("Resign");
            resignButton.addActionListener(this);
            newGameButton = new JButton("New Game");
            newGameButton.addActionListener(this);
            message = new JLabel("",JLabel.CENTER);
            message.setFont(new  Font("Serif", Font.BOLD, 14));
            message.setForeground(Color.green);
            board = new ChessData();
            //Display board
            displayBoard = new ChessData();
            agentBoard = new ChessData();
            //Select the AI players.
            decideAIplayer();
            //Start new game
            doNewGame();
        }

        public void decideAIplayer()
        {
        	Scanner stdin = new Scanner(System.in);
    		boolean done = false;
    		player_1 = new AlphaBetaSearch();
        	player_2 = new MonteCarloTreeSearch();
            while (!done) {
                try {
                	int aikey = stdin.nextInt();
                    if (aikey==1) {done = true; aiKey = 1;}
                    else if(aikey==2) 
                    { done =true; aiKey= 2;}
                    else if(aikey==3) 
                    {
                    	done =true; aiKey= 3;}
                    else {
                        System.out.println("\tThe entered number should be (1-3)");
                    }
                }
                catch (InputMismatchException e) {
                    System.out.println("\tInvalid input type (must be an integer)");
                    stdin.nextLine();  // Clear invalid input from scanner buffer.
                }
            }
        }
        /**
         * Respond to user's click on one of the two buttons.
         */
        public void actionPerformed(ActionEvent evt) {
            Object src = evt.getSource();
            if (src == newGameButton)
                doNewGame();
            else if (src == resignButton)
                doResign();
        }


        /**
         * Start a new game
         */
        void doNewGame() {
            if (gameInProgress) {
                // This should not be possible, but it doesn't hurt to check.
                message.setText("Finish the current game first!");
                return;
            }
            board.setUpGame();   // Set up the pieces.
            
            //
            displayBoard.setUpGame(); // S_R, Set up the pieces.
            agentBoard.setUpGame(); // S_L
            //
            currentPlayer = ChessData.WHITE_PLAYER;
            player_1.setChessData(board);
            player_2.setChessData(board);
            legalMoves = board.getLegalMoves(ChessData.WHITE_PLAYER);
            selectedRow = -1;
            message.setText("White:  Make your move.");
            gameInProgress = true;
            newGameButton.setEnabled(false);
            resignButton.setEnabled(true);
            
            ///
            previous.drawBoard(agentBoard, new ChessMove());
            ////
            repaint();
        }


        /**
         * Current player resigns.  Game ends.  Opponent wins.
         */
        void doResign() {
            if (!gameInProgress) {  // Should be impossible.
                message.setText("There is no game in progress!");
                return;
            }
            if (currentPlayer == ChessData.WHITE_PLAYER)
                gameOver("WHITE resigns.  BLACK wins.");
            else
                gameOver("BLACK resigns.  WHITE wins.");
            	//previous.drawBoard();
        }


        /**
         * The game ends.  The parameter, str, is displayed as a message
         * to the user.  The states of the buttons are adjusted so players
         * can start a new game.  This method is called when the game
         * ends at any point in this class.
         */
        void gameOver(String str) {
            message.setText(str);
            newGameButton.setEnabled(true);
            resignButton.setEnabled(false);
            gameInProgress = false;
            //Previous state
            premessage.setText("Game is done");
        }


        /**
         * This is called by mousePressed() when a player clicks on the
         * square in the specified row and col.  It has already been checked
         * that a game is, in fact, in progress.
         */
        void doClickSquare(int row, int col) {

            /* If the player clicked on one of the pieces that the player
               can move, mark this row and col as selected and return.  (This
               might change a previous selection.)  Reset the message, in
               case it was previously displaying an error message. */
            for (ChessMove legalMove : legalMoves) {
                if (legalMove.r1 == row && legalMove.c1 == col) {
                    selectedRow = row;
                    selectedCol = col;
                    if (currentPlayer == ChessData.WHITE_PLAYER)
                        message.setText("WHITE:  Make your move.");
                    else
                        message.setText("BLACK:  Make your move.");
                    repaint();
                    return;
                }
            }

            /* If no piece has been selected to be moved, the user must first
               select a piece.  Show an error message and return. */
            if (selectedRow < 0) {
                message.setText("Click the piece you want to move.");
                return;
            }

            /* If the user clicked on a square where the selected piece can be
               legally moved, then make the move and return. */
            for (ChessMove legalMove : legalMoves) {
                if (legalMove.r1 == selectedRow && legalMove.c1 == selectedCol
                        && legalMove.r2 == row && legalMove.c2 == col) {
                    doMakeMove(legalMove);
                    return;
                }
            }

         /* If we get to this point, there is a piece selected, and the square where
          the user just clicked is not one where that piece can be legally moved.
          Show an error message. */

            message.setText("Click the square you want to move to.");

        }  // end doClickSquare()


        /**
         * This is called when the current player has chosen the specified
         * move.  Make the move, and then either end or continue the game
         * appropriately.
         */
        void doMakeMove(ChessMove move) {	
            board.makeMove(move);
            agentBoard=copyBoard(board);
            
            ChessMove moveAI = new ChessMove();
             /* The current player's turn is ended, so change to the other player.
                Get that player's legal moves.  If the player has no legal moves,
                then the game ends. */
            //Play checkers game on agentboard
            if (currentPlayer == ChessData.WHITE_PLAYER) {
                currentPlayer = ChessData.BLACK_PLAYER;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null) {
                    gameOver("BLACK has no moves.  WHITE wins.");
                    displayBoard = copyBoard(board);
                    previous.drawBoard(board, moveAI);
                    repaint();
                    return;
                } else {
                    message.setText("BLACK:  Now AI's turn.");
                }

                player_1.setChessData(board);
                player_2.setChessData(board);
                
                switch(aiKey){
                case 1: moveAI = player_1.makeMove(legalMoves); break;
                case 2: moveAI = player_2.makeMove(legalMoves); break;
                case 3: Random rand = new Random();
            			if(rand.nextInt(2) == 1)
            				moveAI = player_1.makeMove(legalMoves); 
            			else
            				moveAI = player_2.makeMove(legalMoves); 
                }

                board.makeMove(moveAI);

                displayBoard = copyBoard(board);
                //Add time
                
                repaint();
                
                //timeDelay(1);
                //previous.drawBoard(board);
                
            }
            
            previous.drawBoard(agentBoard, moveAI);

            currentPlayer = ChessData.WHITE_PLAYER;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
                gameOver("WHITE has no moves.  BLACK wins.");
            else
                message.setText("WHITE:  Make your move.");

            /* Set selectedRow = -1 to record that the player has not yet selected
               a piece to move. */
            selectedRow = -1;

            /* As a courtesy to the user, if all legal moves use the same piece, then
               select that piece automatically so the user won't have to click on it
               to select it. */
            if (legalMoves != null) {
                boolean sameStartSquare = true;
                for (int i = 1; i < legalMoves.length; i++)
                    if (legalMoves[i].r1 != legalMoves[0].r1
                            || legalMoves[i].c1 != legalMoves[0].c1) {
                        sameStartSquare = false;
                        break;
                    }
                if (sameStartSquare) {
                    selectedRow = legalMoves[0].r1;
                    selectedCol = legalMoves[0].c1;
                }
            }

            /* Make sure the board is redrawn in its new state. */
            repaint();
        }  // end doMakeMove();
        /**
         * Draw a checkerboard pattern in gray and lightGray.  Draw the
         * checkers.  If a game is in progress, highlight the legal moves.
         */
        @Override
        public void paintComponent(Graphics g) {

            /* Draw a two-pixel black border around the edges of the canvas. */

            g.setColor(Color.black);
            g.drawRect(0,0,getSize().width-1,getSize().height-1);
            g.drawRect(1,1,getSize().width-3,getSize().height-3);

            /* Draw the squares of the checkerboard and the checkers. */
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if ( row % 2 == col % 2 )
                        g.setColor(Color.LIGHT_GRAY);
                    else
                        g.setColor(Color.GRAY);
                    g.fillRect(2 + col*20, 2 + row*20, 20, 20);
                    switch (displayBoard.pieceAt(row,col)) {
                        case ChessData.WHITE_PAWN:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("P", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_ROOK:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("R", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_KNIGHT:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_BISHOP:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("B", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_QUEEN:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("Q", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.WHITE_KING:
                            g.setColor(Color.WHITE);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.BLACK);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_PAWN:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("P", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_ROOK:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("R", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_KNIGHT:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("N", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_BISHOP:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("B", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_QUEEN:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("Q", 7 + col*20, 16 + row*20);
                            break;
                        case ChessData.BLACK_KING:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                    }
                }
            }

         /* If a game is in progress, highlight the legal moves.   Note that legalMoves
          is never null while a game is in progress. */

            if (gameInProgress) {
                /* First, draw a 2-pixel cyan border around the pieces that can be moved. */
                g.setColor(Color.cyan);
                for (ChessMove legalMove : legalMoves) {
                    g.drawRect(2 + legalMove.c1 * 20, 2 + legalMove.r1 * 20, 19, 19);
                    g.drawRect(3 + legalMove.c1 * 20, 3 + legalMove.r1 * 20, 17, 17);
                }
               /* If a piece is selected for moving (i.e. if selectedRow >= 0), then
                draw a 2-pixel white border around that piece and draw green borders
                around each square that that piece can be moved to. */
                if (selectedRow >= 0) {
                    g.setColor(Color.white);
                    g.drawRect(2 + selectedCol*20, 2 + selectedRow*20, 19, 19);
                    g.drawRect(3 + selectedCol*20, 3 + selectedRow*20, 17, 17);
                    g.setColor(Color.green);
                    for (ChessMove legalMove : legalMoves) {
                        if (legalMove.c1 == selectedCol && legalMove.r1 == selectedRow) {
                            g.drawRect(2 + legalMove.c1 * 20, 2 + legalMove.r1 * 20, 19, 19);
                            g.drawRect(3 + legalMove.c1 * 20, 3 + legalMove.r1 * 20, 17, 17);
                        }
                    }
                }
            }

        }  // end paintComponent()

        private ChessData copyBoard(ChessData board)
        {
            this.board = board;
            ChessData new_board = new ChessData();
            for(int i=0; i<board.board.length;i++)
            {
                for(int j=0;j<8;j++)
                {
                    new_board.board[i][j]=board.pieceAt(i, j);
                }
            }
            return new_board;
        }
        /**
         * Respond to a user click on the board.  If no game is in progress, show
         * an error message.  Otherwise, find the row and column that the user
         * clicked and call doClickSquare() to handle it.
         */
        @Override
        public void mousePressed(MouseEvent evt) {
            if (!gameInProgress)
                message.setText("Click \"New Game\" to start a new game.");
            else {
                int col = (evt.getX() - 2) / 20;
                int row = (evt.getY() - 2) / 20;
                if (col >= 0 && col < 8 && row >= 0 && row < 8)
                    doClickSquare(row,col);
            }
        }

        @Override
        public void mouseReleased(MouseEvent evt) { }
        @Override
        public void mouseClicked(MouseEvent evt) { }
        @Override
        public void mouseEntered(MouseEvent evt) { }
        @Override
        public void mouseExited(MouseEvent evt) { }
    }  // end class Board
    public void timeDelay(int t) {
        try {
            
            // delay 5 seconds
            TimeUnit.SECONDS.sleep(t);
            
            // delay 0.5 second
            //TimeUnit.MICROSECONDS.sleep(500);

			// delay 1 minute
            //TimeUnit.MINUTES.sleep(1);
			
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
} // end class Chess
