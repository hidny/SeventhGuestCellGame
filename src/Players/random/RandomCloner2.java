package Players.random;

import java.util.ArrayList;

import Players.PlayerI;
import env.PositionCellGame;

public class RandomCloner2 implements PlayerI {

	private String playerName = "";
	
	public RandomCloner2(String playerName) {
		this.playerName = playerName;
	}
	
	@Override
	public int getMove(PositionCellGame pos) {
		
		ArrayList <Integer> moves = pos.getMoveList();
		
		ArrayList <Integer> cloningMoves = new ArrayList <Integer>();
		
		for(int i=0; i<moves.size(); i++) {
			int moveNum = moves.get(i);
			
			if( ! PositionCellGame.isJump(moves.get(i))) {
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

}
