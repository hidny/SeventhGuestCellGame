package Players.alphabetaComplexEval;

import java.util.ArrayList;

import Players.PlayerI;
import env.PositionCellGame;
import env.SanityTestEnv;

//TODO: make another one that hashes prev positions!
// I think it will make a big diff...

public class ComplexEvalAlphaBeta implements PlayerI {

	private String playerName = "";
	
	public int depth;
	
	public ComplexEvalAlphaBeta(int depth) {
		this.depth = depth;
	}

	@Override
	public int getMove(PositionCellGame pos) {
		
		PositionWithComplexEval pos2 = new PositionWithComplexEval(pos);
		return getBestMove(pos2, this.depth, this.getPlayerName()) ;
	}

	@Override
	public String getPlayerName() {
		return "Simple Alpha Beta with depth " + this.depth;
	}

	@Override
	public void updatePosition(PositionCellGame pos) {
		//pass
		
	}
	

	public static int getBestMove(PositionWithComplexEval pos2, int depth, String name) {
		
		System.out.println("Trying to get the best move for " + name);
		
		double choices[] = getUtilityOfAllMoves(pos2, depth);
		
		if(choices.length == 0) {
			return PositionCellGame.NO_MOVE_PASS_THE_TURN;
		}
		
		int bestIndex = 0;
		
		if(pos2.isP1turn()) {
			//get max utility choice:
			bestIndex = getMaxIndex(choices);
		
		} else {
			
			//get min utility choice:
			bestIndex = getMinIndex(choices);
		}
		
		
		
		return pos2.getMoveListReduced(pos2.isP1turn()).get(bestIndex);
	}
	
	
	public static int getMaxIndex(double choices[]) {
		int bestIndex = 0;
		
		for(int i=0; i<choices.length; i++) {
			if(choices[i] > choices[bestIndex]) {
				bestIndex = i;
			}
		}
		
		return bestIndex;
	}
	
	public static int getMinIndex(double choices[]) {
		int bestIndex = 0;
		
		for(int i=0; i<choices.length; i++) {
			if(choices[i] < choices[bestIndex]) {
				bestIndex = i;
			}
		}
		
		return bestIndex;
	}
	
	public static final double REALLY_HIGH_NUMBER = 100000.0;
	public static final String SPACE = "         ";
	public static final double REASONABLY_HIGH_NUMBER = 100000.0;
	
	public static double[] getUtilityOfAllMoves(PositionWithComplexEval pos2, int depth) {
		ArrayList<Integer> choices = pos2.getMoveListReduced(pos2.isP1turn());
		
		boolean maximizingPlayer = true;
		if(! pos2.isP1turn()) {
			maximizingPlayer = false;
		}
		
		if(choices.size() == 0) {
				//Pass:
				return new double[0];
		}
		
		double ret[] = new double[choices.size()];
		
		double curBest = -1;
		
		boolean isFirstTrial = true;
		
		for(int i=0; i<choices.size(); i++) {
			
			if( i > 0) {
				isFirstTrial = false;
			}
			
			System.out.println("Trying move: " + SanityTestEnv.convertMoveNumberToString(choices.get(i)));
			
			if(isFirstTrial) {
				ret[i] = getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(i)), depth-1, -REALLY_HIGH_NUMBER, REALLY_HIGH_NUMBER);
				
			} else {
				if(maximizingPlayer) {
					ret[i] = getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(i)), depth-1, curBest, REALLY_HIGH_NUMBER);
				} else {
					ret[i] = getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(i)), depth-1, -REALLY_HIGH_NUMBER, curBest);
				}
			}
			
			if( isFirstTrial
			 ||	(maximizingPlayer && ret[i] > curBest)
			 || (! maximizingPlayer && ret[i] < curBest)) {
				
				System.out.println("Got " +  SPACE.substring((ret[i] + "").length()) + ret[i]);
				
				curBest = ret[i];
				
			} else {
				System.out.print("Got worse than (or equal to)" +  SPACE.substring((ret[i] + "").length()) + ret[i]);
				
				if(maximizingPlayer) {
					System.out.println(" (higher number is better)");
				} else {
					System.out.println(" (lower number is better)");
				}
			}
			
			System.out.println();
			
		}
		
		return ret;
		
	}

	
	//pre: there will always be a peg to place until depth = 0.
	
	//once depth =0, just return some rule of thumb.
	public static double getMoveUtil(PositionWithComplexEval pos2, int depth, double reallyHighNumber, double reallyHighNumber2) {
		if(depth == 0) {
			return pos2.getComplexUtil();
		}
		
		ArrayList<Integer> choices = pos2.getMoveListReduced(pos2.isP1turn());
		
		if(choices.size() == 0
				&& pos2.isGameOver()) {

			return pos2.getComplexUtil();

		} else if(choices.size() == 0) {
			
			double edgeCaseUtil = handleNoMoveEdgeCase(pos2, depth);
			
			if(edgeCaseUtil == INCONCLUSIVE ) {
				
				// Can't figure out who's winning, so keep looking:
				System.out.println("Keep looking.");
				choices.add(new Integer(PositionCellGame.NO_MOVE_PASS_THE_TURN));
				
			} else {
				//The edge case function figured out who's winning, so just
				// return the relevant util.
				return edgeCaseUtil;
			}
			
		}
		
		
		double ret;
		
		if(pos2.isP1turn()) {
			
			//get max util for next turn:
			for(int i=0; i<choices.size(); i++) {
				
				reallyHighNumber = Math.max(getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(i)), depth-1, reallyHighNumber, reallyHighNumber2), reallyHighNumber);
				
				if(reallyHighNumber >= reallyHighNumber2) {
					break;
				}
				
				
			}
			
			ret = reallyHighNumber;
			
		} else {
			
			//get min util for next turn:
			for(int i=0; i<choices.size(); i++) {
				reallyHighNumber2 = Math.min(getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(i)), depth-1, reallyHighNumber, reallyHighNumber2), reallyHighNumber2);
					
				if(reallyHighNumber2 <= reallyHighNumber) {
					break;
				}
				
			}
			
			ret = reallyHighNumber2;
		}
		
		
		return ret;
	}
	

	public static final double INCONCLUSIVE = -1234567.0; 
	
	public static double handleNoMoveEdgeCase(PositionWithComplexEval pos2, int depth) {
		
		//Edge case where player ran out of moves:

		
		long target = -1;
		double mult = 1.0;
		
		if(pos2.isP1turn()) {
			target = PositionCellGame.P1_CELL;
			
			if(pos2.getCurUtil() < 0) {
				//Quick return because current player obviously lost:
				return  -2 * REASONABLY_HIGH_NUMBER;
			}
		} else {
			target = PositionCellGame.P2_CELL;
			

			if(pos2.getCurUtil() > 0) {
				//Quick return because current player obviously lost:
				return  2 * REASONABLY_HIGH_NUMBER;
			}
			
			mult = -1.0;
		}
		
		long board[][] = pos2.getBoard();
		
		int sum = 0;
		
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[0].length; j++) {
				if(board[i][j] == target) {
					sum++;
				}
			}
		}
		
		boolean keepTrying = false;
		
		double tmp = -1.0;
		if(sum > PositionCellGame.SIDE_LENGTH_SQUARE / 2) {

			//TODO: maybe improve on this latter?
			
			System.out.println();
			System.out.println("Exceptional pos:");
			System.out.println(pos2);
			
			System.out.println("I don't know. Player that cannot move is currently winning");
			System.out.println("But I don't know if it's a forced win...");
			tmp = pos2.getCurUtil();
			keepTrying = true;
			

			System.out.println("Util in edge case:");
			System.out.println(tmp);
			System.out.println();
			System.out.println("Complex util in edge case:");
			System.out.println(pos2.getComplexUtil());
			System.out.println();
			
			//Util if it was a forced win: (Do not use yet)
			//tmp = mult * ( REASONABLY_HIGH_NUMBER + sum + depth);
		} else {

			//System.out.println("Loss for the current player's turn");
			tmp = mult * ( - REASONABLY_HIGH_NUMBER + sum + depth);
		}
	
		
		if(keepTrying) {
			return INCONCLUSIVE;
		} else {
			return tmp;
		}
		
	}
	
}
