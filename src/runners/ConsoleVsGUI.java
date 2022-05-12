package runners;

import Players.PlayerI;
import Players.console.ConsolePlayer;
import Players.gui.GUIPlayer;
import env.MainGame;

public class ConsoleVsGUI {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];
		
		players[0] = new ConsolePlayer("Player 1");
		players[1] = new GUIPlayer("Player 2");
		
		MainGame.playGame(players);
	}

}
