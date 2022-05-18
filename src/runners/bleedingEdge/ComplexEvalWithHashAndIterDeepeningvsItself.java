package runners.bleedingEdge;

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

public class ComplexEvalWithHashAndIterDeepeningvsItself {

	public static void main(String[] args) {
		PlayerI players[] = new PlayerI[2];

		//players[0] = new ComplexEvalIterDeepHashAlphaBeta(6);
		players[0] = new ComplexEvalIterDeepHashAlphaBeta(7);
		players[1] = new ComplexEvalIterDeepHashAlphaBeta(7);
		
		MainGame.playGame(players);
	}

}
