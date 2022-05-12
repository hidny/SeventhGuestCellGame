package Players.console;

import java.util.ArrayList;
import java.util.Scanner;

import Players.PlayerI;
import env.PositionCellGame;
import env.SanityTestEnv;

public class ConsolePlayer implements PlayerI {

	private String playerName = "";
	public ConsolePlayer(String name) {
		this.playerName = name;
	}
	
	private Scanner in = new Scanner(System.in);
	
	@Override
	public int getMove(PositionCellGame pos) {
		

		boolean chosenMoveIsLegal = false;
		int moveNumber = -1;
		
		do {
			System.out.println("---------------");
			printMovesNoPos(pos);
	
			System.out.println(pos);
			
			System.out.println("Please make a move from the above choices:");
			String line = in.nextLine();
			

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

	
	@Override
	public String getPlayerName() {
		return this.playerName;
	}

	

	public static void printMovesNoPos(PositionCellGame pos) {
		ArrayList<Integer> moves = pos.getMoveList(pos.isP1turn());
		
		System.out.println("Printing moves for the following position:");
		System.out.println(pos);
		System.out.println("Moves");
		for(int i=0; i<moves.size(); i++) {
			System.out.println(SanityTestEnv.convertMoveNumberToString(moves.get(i)));
			
		}
	}


	@Override
	public void updatePosition(PositionCellGame pos) {
		// TODO Auto-generated method stub
		System.out.println(pos);
		System.out.println("---------------");
	}
}
