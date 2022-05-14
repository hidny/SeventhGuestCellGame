package runners.SimpleAlphaBeta;

import Players.PlayerI;
import Players.alphabetapruning.SimpleAlphaBeta;
import Players.console.ConsolePlayer;
import Players.gui.GUIPlayer;
import env.MainGame;

public class GUIVsSimpleAlphaBetaDepth6 {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];

		players[0] = new SimpleAlphaBeta(6);
		players[1] = new GUIPlayer("Player 2");
		
		//Good enough to win vs 7th guest by 9 pegs
		MainGame.playGame(players);
	}

}
