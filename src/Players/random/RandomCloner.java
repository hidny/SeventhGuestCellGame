package Players.random;

import java.util.ArrayList;

import Players.PlayerI;
import env.PositionCellGame;

public class RandomCloner implements PlayerI {

	private String playerName = "";
	
	public RandomCloner(String playerName) {
		this.playerName = playerName;
	}
	
	@Override
	public int getMove(PositionCellGame pos) {
		
		ArrayList <Integer> moves = pos.getMoveList();
		
		ArrayList <Integer> cloningMoves = new ArrayList <Integer>();
		
		for(int i=0; i<moves.size(); i++) {
			int moveNum = moves.get(i);

			int i1 = moveNum / PositionCellGame.SIDE_LENGTH_CUBE;
			int j1 = (moveNum / PositionCellGame.SIDE_LENGTH_SQUARE) % PositionCellGame.SIDE_LENGTH;
			int i2 = (moveNum / PositionCellGame.SIDE_LENGTH) % PositionCellGame.SIDE_LENGTH;
			int j2 = moveNum% PositionCellGame.SIDE_LENGTH;
			
			if( ! isJump(i1, j1, i2, j2)) {
				cloningMoves.add(moveNum);
			}
		}
		
		if(cloningMoves.size() > 0) {
			return cloningMoves.get( (int)(cloningMoves.size() * Math.random()) );
		
		} else if(pos.getMoveList().size() > 0) {
			//Jump if there's no cloning moves:
			return pos.getMoveList().get( (int)(pos.getMoveList().size() * Math.random()) );

		} else {
			return -1;
		}
		
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}

	@Override
	public void updatePosition(PositionCellGame pos) {
		
	}
	
	//TODO: put in util function?
	public static boolean isJump(int i1, int j1, int i2, int j2) {
		return Math.abs(i1 - i2) >= 2 || Math.abs(j1 - j2) >= 2;
	}

}
