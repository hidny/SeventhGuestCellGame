package runners;

import Players.PlayerI;
import Players.alphabetaComplexEval.ComplexEvalAlphaBeta;
import Players.alphabetaComplexEval.ComplexEvalHashAlphaBeta;
import Players.alphabetaComplexEval.ComplexEvalIterDeepHashAlphaBeta;
import Players.alphabetapruning.HashAlphaBeta;
import Players.alphabetapruning.IterDeepHashAlphaBeta;
import Players.alphabetapruning.SimpleAlphaBeta;
import Players.console.ConsolePlayer;
import Players.gui.GUIPlayer;
import env.MainGame;

public class ComplexEvalWithHashAlphaBetavComplexHashAlphaBeta {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];

		players[0] = new ComplexEvalIterDeepHashAlphaBeta(4);
		players[1] = new ComplexEvalAlphaBeta(6);
		
		MainGame.playGame(players);
	}

}
