package Players;

import env.PositionCellGame;

//import Position;

public interface PlayerI {

	public int getMove(PositionCellGame pos);
	
	public String getPlayerName();
	
	//Function to update the GUI board position:
	public void updatePosition(PositionCellGame pos);
	
}
