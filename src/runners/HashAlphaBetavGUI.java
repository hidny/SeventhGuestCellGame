package runners;

import Players.PlayerI;
import Players.alphabetapruning.HashAlphaBeta;
import Players.alphabetapruning.IterDeepHashAlphaBeta;
import Players.alphabetapruning.SimpleAlphaBeta;
import Players.console.ConsolePlayer;
import Players.gui.GUIPlayer;
import env.MainGame;

public class HashAlphaBetavGUI {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];

		players[0] = new GUIPlayer("Michael");
		players[1] = new HashAlphaBeta(8);
		
		MainGame.playGame(players);
	}

}
