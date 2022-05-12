package gui;

import java.awt.*;

import javax.swing.JFrame;

//This code was copied from hidny/CheckersAISept2013Revision
//It was also lightly modified.


public class Board
{
  // Some colour presets
  public static final Color YELLOW = Color.YELLOW;
  public static final Color BLUE = Color.BLUE;
  public static final Color CYAN = Color.CYAN;
  public static final Color GREEN = Color.GREEN;
  public static final Color PINK = Color.PINK;
  public static final Color BLACK = Color.BLACK;
  public static final Color WHITE = Color.WHITE;
  public static final Color RED = Color.RED;
  public static final Color ORANGE = Color.ORANGE;
  
  // Instance variables
  Frame frame;
  BoardPanel panel;
  
  public static int TITLE_BAR_HEIGHT = 25;/*The approximate Title bar size*/

	
  // Other instance variables
  int rows; /*****Maybe I could refer to the Board Panels rows and cols... (maybe)*/
  int columns;
  
  public static void main(String args[]) {
	  

   Board b = new Board(10, 10);
   b.displayMessage("Hello World! I wish I was a different font...");
   b.putPeg(CYAN, 7, 1);
   b.putPeg(GREEN, 1, 2);
   b.removePeg(1, 2);
   for(int i=0; i<2; i++) {
     for(int j=0; j<5; j++) {
       b.putPeg(RED, i, j);
     }
   }
   
   boolean imNotBored = true;
   
   while(imNotBored == true) {
		
        Coordinate c =  b.getNextCoordinateClicked();
         
        System.out.println("You have clicked on ("+ c.getRow() + ", "+ c.getCol() + ")");
   }
  }
  
  public Coordinate getNextCoordinateClicked() {
	  Coordinate ret = this.panel.getNextCoordinateClicked();
	  
	  if( ret == null) {
		//Sanity check:
		System.out.println("ERROR: The coordinate is equal to null!"); 
	    panel.CoordinateUsed();
	    return new Coordinate(-1, -1);
	  }
	  
	  return ret;
  }
  
  
  public Board(int rows, int columns)
  {
    this.rows = rows;
    this.columns = columns;
    
	frame = new JFrame("Michael's Cell Game AI");
	
	panel = new BoardPanel(rows, columns);
	frame.add(panel);
	
	
	/*sets the size of the window.*/
	frame.setSize(2 * BoardPanel.X_OFFSET + columns * BoardPanel.X_DIM, 2 * BoardPanel.Y_OFFSET + rows * BoardPanel.Y_DIM + TITLE_BAR_HEIGHT);
    
	//2013: exit on close buttom for the win!
	((JFrame) frame).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	frame.setResizable(false);
	frame.setVisible(true);
	
	
  }
  
  public void displayMessage(String theMessage)
  {
    panel.displayMessage(theMessage);
  }
  
  public void putPeg(Color theColour, int row, int col)
  {
	  panel.putPeg(theColour, row, col);
  }
  
  
  public void removePeg(int row, int col)
  {
	  panel.removePeg(row, col);
  }
  
  public void highlight(int row, int col) {
	  panel.highlight(YELLOW, row, col);
  }
  
  public void removeHighlight(int row, int col) {
	  panel.removeHighlight(row, col);
  }
  
  public int getColumns()
  {
    return this.columns;
  }
  
  public int getRows()
  {
    return this.rows;
  }
  
  
  
  
  private static final Color P1Color = RED; 
  private static final Color P2Color = BLUE; 
  
  public void displayBoard(env.PositionCellGame position) {
	  
	  long board[][] = position.getBoard();
	  
	  boolean isP1Turn = position.isP1turn();
	  
	  for(int i=0; i<board.length; i++) {
		  for(int j=0; j<board[0].length; j++) {
			  
			  if(board[i][j] == env.PositionCellGame.EMPTY) {
				  this.removePeg(i, j);
				  
			  } else if(board[i][j] == env.PositionCellGame.P1_CELL) {
				  this.putPeg(P1Color, i, j);
				  
			  } else if(board[i][j] == env.PositionCellGame.P2_CELL) {
				  this.putPeg(P2Color, i, j);
				  
			  } else {
				  System.out.println("Error: unknown number is table");
			  }
		  }
	  }
	  
	  if(isP1Turn) {
		  this.displayMessage("Player 1's turn");
	  } else {
		  this.displayMessage("Player 2's turn");
	  }
  }
  
  
  
  public void closeWindow() {
	  frame.dispose();
  }
  
}

