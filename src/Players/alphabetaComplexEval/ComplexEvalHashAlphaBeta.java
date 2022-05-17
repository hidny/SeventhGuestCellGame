package Players.alphabetaComplexEval;

import java.util.ArrayList;
import java.util.HashMap;

import Players.PlayerI;
import alphaBetaHash.SimpleHashObject;
import alphaBetaHash.SimpleHashObjectWithDoubleUtil;
import env.PositionCellGame;
import env.SanityTestEnv;

public class ComplexEvalHashAlphaBeta implements PlayerI {

	
	public static HashMap<Long, SimpleHashObjectWithDoubleUtil> refutationTable = new HashMap<Long, SimpleHashObjectWithDoubleUtil>();

	public static int DEBUG_MULT = 10000;
	public int depth;
	
	public ComplexEvalHashAlphaBeta(int depth) {
		this.depth = depth;
	}

	public static int debugNumMatches = 0;
	public static int debugNumElements = 0;
	
	@Override
	public int getMove(PositionCellGame pos) {
		
		refutationTable = new HashMap<Long, SimpleHashObjectWithDoubleUtil>();
		
		PositionWithComplexEval pos2 = new PositionWithComplexEval(pos);
		
		int tmp = getBestMove(pos2, this.depth, this.getPlayerName());
		
		System.out.println("Debug:");
		System.out.println("Number of matches: " + debugNumMatches);
		System.out.println("Number of elements added to hash: " + debugNumElements);
		
		debugNumMatches = 0;
		debugNumElements = 0;
		
		
		return tmp;
	}

	@Override
	public String getPlayerName() {
		return "Complex eval and Simple Hash Alpha Beta with depth " + this.depth;
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
	public static double getMoveUtil(PositionWithComplexEval pos2, int depth, double alpha, double beta) {
		if(depth == 0) {
			return pos2.getComplexUtil();
		}
		
		//Check if we already did the calculation somehow:
		if(refutationTable.containsKey(pos2.getCurHash())) {
			
			SimpleHashObjectWithDoubleUtil simpleObject = refutationTable.get(pos2.getCurHash());
			
			if(simpleObject.getDepthUsed() >= depth) {
				
				debugNumMatches++;
				
				if(debugNumMatches % DEBUG_MULT == 0) {
					System.out.println("debugNumMatches: " + debugNumMatches);
				}
				
				if( ! simpleObject.isWasPrunedBeforeFullyCalc()) {
					return simpleObject.getUtilValue();
				} else {
					if(pos2.isP1turn()) {
						alpha = Math.max(simpleObject.getUtilValue(), alpha);
					} else {
						beta = Math.min(simpleObject.getUtilValue(), beta);
					}
				}
				
				if(alpha >= beta) {
					return simpleObject.getUtilValue();
				}
				
			}
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
				
				alpha = Math.max(getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(i)), depth-1, alpha, beta), alpha);
				
				if(alpha >= beta) {
					break;
				}
				
				
			}
			
			ret = alpha;
			
		} else {
			
			//get min util for next turn:
			for(int i=0; i<choices.size(); i++) {
				beta = Math.min(getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(i)), depth-1, alpha, beta), beta);
					
				if(beta <= alpha) {
					break;
				}
				
			}
			
			ret = beta;
		}
		

		// TODO: make this non-static.
		//Add to the refutationTable:
		boolean wasPrunedBeforeFullyCalc = (beta <= alpha);
		
		
		SimpleHashObjectWithDoubleUtil simpleObject = new SimpleHashObjectWithDoubleUtil(ret, wasPrunedBeforeFullyCalc, depth);
		
		if(refutationTable.containsKey(pos2.getCurHash())) {
			refutationTable.remove(pos2.getCurHash());
			debugNumElements--;
		}
		refutationTable.put(pos2.getCurHash(), simpleObject);
		debugNumElements++;
		
		if(debugNumElements % DEBUG_MULT == 0) {
			System.out.println("debugNumElements: " + debugNumElements);
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
