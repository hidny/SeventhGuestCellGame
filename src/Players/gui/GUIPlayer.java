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
			
			//Check if moveNum is legal: (Maybe make a util function?)
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
		
		if(pos.isGameOver()) {
			if(pos.isPlayer1Winning()) {
				personalBoard.displayMessage("Player 1 (Red) wins!");
			} else {
				personalBoard.displayMessage("Player 2 (Blue) wins!");
			}
		}
	}
	
	//TODO: maybe update Position with move, so I can make an animation...
	// I'll do it later!
	
	

}
