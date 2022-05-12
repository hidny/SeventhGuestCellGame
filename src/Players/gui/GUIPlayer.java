package Players.gui;

import java.util.ArrayList;
import java.util.Scanner;

import Players.PlayerI;
import Players.console.ConsolePlayer;
import env.PositionCellGame;
import gui.Board;
import gui.Coordinate;


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
		
		ArrayList<Integer> moveList = pos.getMoveList();
		
		
		int moveNumber = -1;
		//Testing:
		//int moveNumber = sanityGetMoveConsole(pos);
		
		
		Coordinate from = null;
		Coordinate dest = null;
		
		while(true) {
			
			from = personalBoard.getNextCoordinateClicked();
		


			//Is the start location valid?
			if(pos.isP1turn() && pos.getBoard()[from.getRow()][from.getCol()] == PositionCellGame.P1_CELL) {
				//pass
				
			} else if(! pos.isP1turn() && pos.getBoard()[from.getRow()][from.getCol()] == PositionCellGame.P2_CELL) {
				//pass
				
			} else {
				personalBoard.displayMessage("Please select one of your pegs");
				continue;
			}

			personalBoard.highlight(from.getRow(), from.getCol());
			
			dest = personalBoard.getNextCoordinateClicked();
			
			personalBoard.removeHighlight(from.getRow(), from.getCol());
			
			int i1 = from.getRow();
			int j1 = from.getCol();
			
			int i2 = dest.getRow();
			int j2 = dest.getCol();
			
			moveNumber = i1 * PositionCellGame.SIDE_LENGTH_CUBE
					       + j1 * PositionCellGame.SIDE_LENGTH_SQUARE
					       + i2 * PositionCellGame.SIDE_LENGTH
					       + j2;
			
			//TODO: copy/paste code:
			Integer moveNumObj = new Integer(moveNumber);
			
			//Check if moveNum is legal:
			boolean chosenMoveIsLegal = false;
			for(int i=0; i<moveList.size(); i++) {
				if(moveNumObj.equals(moveList.get(i))) {
					chosenMoveIsLegal = true;
					System.out.println("True!!!");
					break;
				}
			}
			
			if(chosenMoveIsLegal == false) {
				personalBoard.displayMessage("Please make a legal move");
				continue;
			}
			
			//Made it to the end! We're playing the user's move!
			break;
		}

		
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
