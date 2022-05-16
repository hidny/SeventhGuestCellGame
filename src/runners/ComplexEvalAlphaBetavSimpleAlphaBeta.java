package runners;

import Players.PlayerI;
import Players.alphabetaComplexEval.ComplexEvalAlphaBeta;
import Players.alphabetapruning.HashAlphaBeta;
import Players.alphabetapruning.IterDeepHashAlphaBeta;
import Players.alphabetapruning.SimpleAlphaBeta;
import Players.console.ConsolePlayer;
import Players.gui.GUIPlayer;
import env.MainGame;

public class ComplexEvalAlphaBetavSimpleAlphaBeta {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];

		players[0] = new SimpleAlphaBeta(6);
		players[1] = new ComplexEvalAlphaBeta(6);
		
		MainGame.playGame(players);
	}

}
