package runners;

import Players.PlayerI;
import Players.console.ConsolePlayer;
import Players.gui.GUIPlayer;
import Players.random.RandomCloner;
import Players.random.RandomPlayer;
import env.MainGame;

public class RandomClonerVsGUI {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];
		
		players[0] = new RandomCloner("Random Cloner");
		players[1] = new GUIPlayer("Player 2");
		
		MainGame.playGame(players);
	}

}
