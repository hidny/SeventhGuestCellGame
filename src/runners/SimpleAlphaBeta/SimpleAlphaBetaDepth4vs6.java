package runners.SimpleAlphaBeta;

import Players.PlayerI;
import Players.alphabetapruning.SimpleAlphaBeta;
import Players.console.ConsolePlayer;
import Players.gui.GUIPlayer;
import env.MainGame;

public class SimpleAlphaBetaDepth4vs6 {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];

		players[0] = new SimpleAlphaBeta(4);
		players[1] = new SimpleAlphaBeta(6);
		
		MainGame.playGame(players);
	}

}
