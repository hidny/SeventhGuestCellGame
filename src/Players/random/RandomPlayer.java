package Players.random;

import Players.PlayerI;
import env.PositionCellGame;

public class RandomPlayer implements PlayerI {

	private String playerName = "";
	
	public RandomPlayer(String playerName) {
		this.playerName = playerName;
	}
	@Override
	public int getMove(PositionCellGame pos) {
		
		if(pos.getMoveList().size() > 0) {
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
