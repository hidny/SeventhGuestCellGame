package runners.SimpleAlphaBeta;

import Players.PlayerI;
import Players.alphabetapruning.SimpleAlphaBeta;
import Players.console.ConsolePlayer;
import Players.gui.GUIPlayer;
import env.MainGame;

public class GUIVsSimpleAlphaBeta {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];
		
		players[0] = new GUIPlayer("Player 1");
		players[1] = new SimpleAlphaBeta(10);
		
		MainGame.playGame(players);
	}

}
