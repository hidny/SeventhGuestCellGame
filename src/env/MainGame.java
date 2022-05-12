package env;

import Players.PlayerI;
import Players.console.ConsolePlayer;

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
		sendPositionUpdate(curPos, players);

		for(int j=0; j<2; j++) {
			players[j].updatePosition(curPos);
		}
		
		int curPlayerIndex = -1;
		
		boolean isPlayer1Turn = true;

		do {
			if(isPlayer1Turn) {
				curPlayerIndex = 0;
			} else {
				curPlayerIndex = 1;
				
			}
			
			if(curPos.getMoveList().size() == 0) {
				
				//TODO: this is ugly. Rearrange it somehow.
				System.out.println(players[curPlayerIndex].getPlayerName() + " has no moves to make.");
				curPos = curPos.move(PositionCellGame.NO_MOVE_PASS_THE_TURN);
				isPlayer1Turn = !isPlayer1Turn;
				continue;
			}
			
			System.out.println(players[curPlayerIndex].getPlayerName() + ", please make a move:");
			System.out.println(curPos);
			
			
			int move = players[curPlayerIndex].getMove(curPos);
			
			curPos = curPos.move(move);
			
			System.out.println(players[curPlayerIndex] + " move: ");
			
			System.out.println(SanityTestEnv.convertMoveNumberToString(move));

			sendPositionUpdate(curPos, players);
			
			isPlayer1Turn = !isPlayer1Turn;
			
		} while( ! curPos.isGameOver());
		
		System.out.println("Final position:");
		System.out.println(curPos);
		
		sendPositionUpdate(curPos, players);
		
		if(curPos.isPlayer1Winning()) {
			System.out.println(players[curPlayerIndex] + " (i.e: Player 1) wins!");
		} else {
			System.out.println(players[curPlayerIndex] + " (i.e: Player 2) wins!");
		}
		
		System.out.println("The end!");
	}
	
	public static void sendPositionUpdate(PositionCellGame curPos, PlayerI players[]) {
		for(int i=0; i<players.length; i++) {
			players[i].updatePosition(curPos);
		}
	}

}
