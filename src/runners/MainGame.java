package runners;

import Players.ConsolePlayer;
import Players.PlayerI;
import env.PositionCellGame;
import env.SanityTestEnv;

public class MainGame {

	//TODO: make more main functions (Like in mellow)
	//That way, you don't need to change the code.

	//TODO: make a console player and a random player...
	//TODO: make a basic A* AI.
	//TODO: make the basic A* AI do iterative deepening.
	
	//TODO: make a basic A* AI that also uses hashes
			// And make sure the only diff is speed!
			// Also make sure there's no hash collisions.

	//TODO: make the A* AI prioritize moving to the from location of where the opponent just
	//      jumped from. (That's usually a good move)
	//TODO: make the A* AI prioritize the prev depth's favorite 3?
	
	
	public static void main(String[] args) {
//
		playerVsPlayer();

	}
	
	public static void playerVsPlayer() {
		PlayerI players[] = new PlayerI[2];
		
		players[0] = new ConsolePlayer("Player 1");
		players[1] = new ConsolePlayer("Player 2");
		
		playGame(players);
	}
	
	
	public static void playGame(PlayerI players[]) {
		
		PositionCellGame start = new PositionCellGame(true);
		
		PositionCellGame curPos = start;
		
		int curPlayerIndex = -1;
		
		boolean isPlayer1Turn = true;

		do {
			if(isPlayer1Turn) {
				curPlayerIndex = 0;
			} else {
				curPlayerIndex = 1;
				
			}
			System.out.println(players[curPlayerIndex].getPlayerName() + ", please make a move:");
			System.out.println(curPos);
			
			
			int move = players[curPlayerIndex].getMove(curPos);
			
			curPos = curPos.move(move);
			
			System.out.println(players[curPlayerIndex] + " move: ");
			
			System.out.println(SanityTestEnv.convertMoveNumberToString(move));
			
			isPlayer1Turn = !isPlayer1Turn;
			
		} while( ! curPos.isGameOver());
		
		System.out.println("Final position:");
		System.out.println(curPos);
		
		if(curPos.isPlayer1Winning()) {
			System.out.println(players[curPlayerIndex] + " (i.e: Player 1) wins!");
		} else {
			System.out.println(players[curPlayerIndex] + " (i.e: Player 2) wins!");
		}
		
		System.out.println("The end!");
	}

}
