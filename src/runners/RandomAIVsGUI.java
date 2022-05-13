package runners;

import Players.PlayerI;
import Players.console.ConsolePlayer;
import Players.gui.GUIPlayer;
import Players.random.RandomPlayer;
import env.MainGame;

public class RandomAIVsGUI {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];
		
		players[0] = new RandomPlayer("Random Player");
		players[1] = new GUIPlayer("Player 2");
		
		MainGame.playGame(players);
	}

}
