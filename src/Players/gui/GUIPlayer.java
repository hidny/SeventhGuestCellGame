package Players.gui;

import java.util.ArrayList;
import java.util.Scanner;

import Players.PlayerI;
import Players.console.ConsolePlayer;
import env.PositionCellGame;
import gui.Board;

public class GUIPlayer implements PlayerI {

	private String playerName = "";
	
	private Board personalBoard = null;
	
	
	public GUIPlayer(String playerName) {
		this.playerName = playerName;
		
		
		personalBoard = new Board(env.PositionCellGame.SIDE_LENGTH, env.PositionCellGame.SIDE_LENGTH);
		
		
		personalBoard.displayMessage("Hello World! I wish I was a different font...");
	  		   
	}
	
	
	@Override
	public int getMove(PositionCellGame pos) {
		
		personalBoard.displayBoard(pos);
		
		//TODO: get move from the GUI/JPanel...
		//TODO: don't do spin locks! That's lame! (See what I did for mellow)
		int moveNumber = sanityGetMoveConsole(pos);
		

		ConsolePlayer.printMovesNoPos(pos);
		
		
		
		personalBoard.displayBoard(pos.move(moveNumber));
		
		return moveNumber;
	}

	
	@Override
	public String getPlayerName() {
		return playerName;
	}


	@Override
	public void updatePosition(PositionCellGame pos) {
		personalBoard.displayBoard(pos);
		
		//TODO: check if the game is over and then display a message?
		//You could do this later...
	}
	
	//TODO: maybe update Position with move, so I can make an animation...
	// I'll do it later!
	
	
	
	private Scanner intest = new Scanner(System.in);
	
	// Testing GUI by getting moves from the console.
	// It seems to work.
	private int sanityGetMoveConsole(PositionCellGame pos) {

		int moveNumber = -1;
		
		boolean chosenMoveIsLegal = false;
		
		
		do {
			System.out.println("---------------");
			ConsolePlayer.printMovesNoPos(pos);
	
			System.out.println(pos);
			
			System.out.println("Please make a move from the above choices:");
			String line = intest.nextLine();
			

			//Try to avoid fat-fingering:
			line = line.replace("\\", "");
			line = line.replace("}", "");
			line = line.replace("{", "");
			line = line.replace("]", "");
			line = line.replace("[", "");
			line = line.replace("|", "");
			line = line.replace("\'", "");
			line = line.replace("?", "");
			line = line.replace("\"", "");
			line = line.replace("=", "");
			line = line.replace("+", "");
			
			
			String tokens[] = line.trim().split(" ");
			
			
			if(tokens.length > 3) {
				int i1 = Integer.parseInt(tokens[0]);
				int j1 = Integer.parseInt(tokens[1]);
				
				int i2 = Integer.parseInt(tokens[2]);
				int j2 = Integer.parseInt(tokens[3]);
				
				moveNumber = i1 * PositionCellGame.SIDE_LENGTH_CUBE
						       + j1 * PositionCellGame.SIDE_LENGTH_SQUARE
						       + i2 * PositionCellGame.SIDE_LENGTH
						       + j2;
				
			
			} else {
				moveNumber = Integer.parseInt(tokens[0]);
			}
			ArrayList<Integer> moveList = pos.getMoveList(pos.isP1turn());
			
			Integer moveNumObj = new Integer(moveNumber);
			
			
			for(int i=0; i<moveList.size(); i++) {
				if(moveNumObj.equals(moveList.get(i))) {
					chosenMoveIsLegal = true;
					break;
				}
			}
		
			if( ! chosenMoveIsLegal) {
				System.out.println("That's an illegal move! Try again!");
			}
			
		} while(! chosenMoveIsLegal);
		
		
		return moveNumber;
	}

}
