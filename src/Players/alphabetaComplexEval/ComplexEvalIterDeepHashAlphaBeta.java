package Players.alphabetaComplexEval;

import java.util.ArrayList;
import java.util.HashMap;

import Players.PlayerI;
import Players.random.RandomCloner;
import alphaBetaHash.HashObjectWithNextBestMoveAndDoubleUtil;
import env.PositionCellGame;
import env.SanityTestEnv;

public class ComplexEvalIterDeepHashAlphaBeta implements PlayerI {

	//TODO: maybe complex eval is just:
	// (eval(prev pos) + eval(current Pos))/2
	// But just 1 depth more
	// Maybe experiment with this?
	//or complicate the eval function
	
	public static HashMap<Long, HashObjectWithNextBestMoveAndDoubleUtil> refutationTable = new HashMap<Long, HashObjectWithNextBestMoveAndDoubleUtil>();

	public static int DEBUG_MULT = 10000;
	public int depth;
	
	public ComplexEvalIterDeepHashAlphaBeta(int depth) {
		this.depth = depth;
	}

	public static int debugNumMatches = 0;
	public static int debugNumElements = 0;
	
	@Override
	public int getMove(PositionCellGame pos) {
		
		refutationTable = new HashMap<Long, HashObjectWithNextBestMoveAndDoubleUtil>();
		
		PositionWithComplexEval pos2 = new PositionWithComplexEval(pos);
		
		int prevBestIndex = -1;
		for(int i=1; i< this.depth; i++) {
			System.out.println("Depth = " + i);
			prevBestIndex = getBestMoveIndex(pos2, i, this.getPlayerName(), prevBestIndex);
		}
		
		System.out.println("Depth = " + this.depth);
		
		int tmpIndex = getBestMoveIndex(pos2, this.depth, this.getPlayerName(), prevBestIndex);
		
		System.out.println("Debug:");
		System.out.println("Number of matches: " + debugNumMatches);
		System.out.println("Number of elements added to hash: " + debugNumElements);
		
		debugNumMatches = 0;
		debugNumElements = 0;
		
		//TODO: copy/paste this into other AIs:
		//Anti-infinite loop measure:
		if(shouldCloneBecauseItsGameOver(pos2, tmpIndex)) {
			return getFirstCloneMoveIndex(pos2);
			
		}
		
		return pos2.getMoveListReduced(pos2.isP1turn()).get(tmpIndex);
	}
	

	@Override
	public String getPlayerName() {
		return "Complex eval with Hashes and Iter Deepening Alpha Beta with depth " + this.depth;
	}

	@Override
	public void updatePosition(PositionCellGame pos) {
		//pass
		
	}
	

	public static int getBestMoveIndex(PositionWithComplexEval pos2, int depth, String name, int prevBestMoveIndex) {
				
		System.out.println("Trying to get the best move for " + name);
		
		double choices[] = getUtilityOfAllMoves(pos2, depth, prevBestMoveIndex);
		
		if(choices.length == 0) {
			return PositionCellGame.NO_MOVE_PASS_THE_TURN;
		}
		
		int bestIndex = 0;
		
		if(pos2.isP1turn()) {
			//get max utility choice:
			bestIndex = getMaxIndex(choices, prevBestMoveIndex);
		
		} else {
			
			//get min utility choice:
			bestIndex = getMinIndex(choices, prevBestMoveIndex);
		}
		
		
		
		return bestIndex;
	}
	
	
	public static int getMaxIndex(double choices[], int prevBestIndex) {
		
		
		int bestIndex = 0;

		//Check prevBestIndex first because that's the first one iterdeepening checked:
		if(prevBestIndex >= 0) {
			bestIndex = prevBestIndex;
		}
		
		
		for(int i=0; i<choices.length; i++) {
			if(choices[i] > choices[bestIndex]) {
				bestIndex = i;
			}
		}
		
		return bestIndex;
	}
	
	public static int getMinIndex(double choices[], int prevBestIndex) {
		int bestIndex = 0;
		
		//Check prevBestIndex first because that's the first one iterdeepening checked:
		if(prevBestIndex >= 0) {
			bestIndex = prevBestIndex;
		}
		
		for(int i=0; i<choices.length; i++) {
			if(choices[i] < choices[bestIndex]) {
				bestIndex = i;
			}
		}
		
		return bestIndex;
	}
	
	public static final double REALLY_HIGH_NUMBER = 100000.0;
	public static final String SPACE = "                          ";
	public static final double REASONABLY_HIGH_NUMBER = 10000.0;
	
	public static double[] getUtilityOfAllMoves(PositionWithComplexEval pos2, int depth, int prevBestMoveIndex) {
			
		ArrayList<Integer> choices = pos2.getMoveListReduced(pos2.isP1turn());
		
		
		boolean maximizingPlayer = true;
		if(! pos2.isP1turn()) {
			maximizingPlayer = false;
		}
		
		//make sure depth doesn't go beyond the game:
		if(choices.size() == 0) {
				//Pass:
				return new double[0];
		}
		
		double ret[] = new double[choices.size()];
		
		double curBest = -666.0;

		boolean isFirstTrial = true;
		
		//Try prev best move: (TODO: later: try prev best 3 moves?)
		if(prevBestMoveIndex >= 0) {
			System.out.println("Trying prev best move: " + SanityTestEnv.convertMoveNumberToString(choices.get(prevBestMoveIndex)));
			ret[prevBestMoveIndex] = getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(prevBestMoveIndex)), depth-1, -REALLY_HIGH_NUMBER, REALLY_HIGH_NUMBER);
			
			curBest = ret[prevBestMoveIndex];
			
			isFirstTrial = false;
			
			System.out.println("Got " +  SPACE.substring((ret[prevBestMoveIndex] + "").length()) + ret[prevBestMoveIndex]);
			System.out.println();
		}
		
		
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
		
		int prevBestMoveIndex = -1;
		
		if(refutationTable.containsKey(pos2.getCurHash())) {
			
			if(pos2.getCurBackupHash() == refutationTable.get(pos2.getCurHash()).getBackupHash()) {
			
				HashObjectWithNextBestMoveAndDoubleUtil prevPos = refutationTable.get(pos2.getCurHash());
				
				if(prevPos.getDepthUsed() >= depth) {
					
					debugNumMatches++;
					
					if(debugNumMatches % DEBUG_MULT == 0) {
						System.out.println("debugNumMatches: " + debugNumMatches);
					}
					
					if( ! prevPos.isWasPrunedBeforeFullyCalc()) {
						return prevPos.getUtilValue();
					} else {
						if(pos2.isP1turn()) {
							alpha = Math.max(prevPos.getUtilValue(), alpha);
						} else {
							beta = Math.min(prevPos.getUtilValue(), beta);
						}
						
						prevBestMoveIndex = refutationTable.get(pos2.getCurHash()).getPrevBestMove();
					}
					
					if(alpha >= beta) {
						return prevPos.getUtilValue();
					}
					
				} else {
					// Found a match, but it's from the prev iteration:
					debugNumMatches++;

					if(debugNumMatches % DEBUG_MULT == 0) {
						System.out.println("debugNumMatches: " + debugNumMatches);
					}
					prevBestMoveIndex = refutationTable.get(pos2.getCurHash()).getPrevBestMove();
				}
			} else {
				System.err.println("WARNING: COLLISION!");
				
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
		

		//prevBestMoveIndex
		int nextBestMoveIndex = -1;
		
		if(pos2.isP1turn()) {
			
			if( prevBestMoveIndex >= 0) {
				double tmp = getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(prevBestMoveIndex)), depth-1, alpha, beta);
				
				if(tmp > alpha) {
					nextBestMoveIndex = prevBestMoveIndex;
				}
				alpha = Math.max(tmp, alpha);
				
			}
			
			//get max util for next turn:
			for(int i=0; i<choices.size() && alpha < beta; i++) {
				
				double tmp = getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(i)), depth-1, alpha, beta);
					
				
				if(tmp > alpha) {
					nextBestMoveIndex = i;
				}
				alpha = Math.max(tmp, alpha);
				
				
			}
			
			ret = alpha;
			
		} else {
			
			if( prevBestMoveIndex >= 0) {
				double tmp = getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(prevBestMoveIndex)), depth-1, alpha, beta);
				
				if(tmp < beta) {
					nextBestMoveIndex = prevBestMoveIndex;
				}
				beta = Math.min(tmp, beta);
				
			}
			
			//get min util for next turn:
			for(int i=0; i<choices.size() && alpha < beta; i++) {
				
				double tmp = getMoveUtil((PositionWithComplexEval)pos2.move(choices.get(i)), depth-1, alpha, beta);
				
				if(tmp < beta) {
					nextBestMoveIndex = i;
				}
				
				beta = Math.min(tmp, beta);
				
				
			}
			
			ret = beta;
		}
		
		//Add to the refutationTable:
		boolean wasPrunedBeforeFullyCalc = (beta <= alpha);
		
		
		HashObjectWithNextBestMoveAndDoubleUtil nextBestMoveHash = new HashObjectWithNextBestMoveAndDoubleUtil(ret, wasPrunedBeforeFullyCalc, depth, nextBestMoveIndex, pos2.getCurBackupHash());

		// TODO: make this non-static.
		if(refutationTable.containsKey(pos2.getCurHash())) {
			refutationTable.remove(pos2.getCurHash());
			debugNumElements--;
		}
		refutationTable.put(pos2.getCurHash(), nextBestMoveHash);
		debugNumElements++;
		
		if(debugNumElements % DEBUG_MULT == 0) {
			System.out.println("debugNumElements: " + debugNumElements);
		}

		return ret;
	}
	

	public static final double INCONCLUSIVE = -1234567.0;
	public static final double SMALL_DECIMAL = 0.0001;
	public static final double MARGIN = 1000.0;
	
	

	//TODO: copy/paste this into other AIs
	// I corrected a bug...
	//TODO: Maybe have it be called PositionCellGame
	// The only reason I used the more complex position is because of a print statement.
	public static double handleNoMoveEdgeCase(PositionWithComplexEval pos2, int depth) {
		
		//Edge case where player ran out of moves:

		
		long opponentColour = -1;
		double mult = 1.0;
		
		if(pos2.isP1turn()) {
			opponentColour = PositionCellGame.P2_CELL;
			
			if(pos2.getCurUtil() < 0) {
				//Quick return because current player obviously lost:
				return  -2 * REASONABLY_HIGH_NUMBER;
			}
		} else {
			opponentColour = PositionCellGame.P1_CELL;
			

			if(pos2.getCurUtil() > 0) {
				//Quick return because current player obviously lost:
				return  2 * REASONABLY_HIGH_NUMBER;
			}
			
			mult = -1.0;
		}
		
		long board[][] = pos2.getBoard();
		
		int sumOpponentPegs = 0;
		
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[0].length; j++) {
				if(board[i][j] == opponentColour) {
					sumOpponentPegs++;
				}
			}
		}
		
		boolean keepTrying = false;
		
		double tmp = -1.0;
		if(sumOpponentPegs > PositionCellGame.SIDE_LENGTH_SQUARE / 2) {

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

			//System.out.println("Win for the current player's turn");
			tmp = mult * ( REASONABLY_HIGH_NUMBER + sumOpponentPegs + depth);
		}
	
		
		if(keepTrying) {
			return INCONCLUSIVE;
		} else {
			return tmp;
		}
		
	}

	public static boolean shouldCloneBecauseItsGameOver(PositionWithComplexEval pos2, int curBestIndex) {
		return pos2.getMoveListReduced(! pos2.isP1turn()).size() == 0
				&& PositionCellGame.isJump(pos2.getMoveListReduced(pos2.isP1turn()).get(curBestIndex))
				&& ((pos2.isP1turn() && handleNoMoveEdgeCase(pos2, 0) > REALLY_HIGH_NUMBER - MARGIN)
						||
					(!pos2.isP1turn() && handleNoMoveEdgeCase(pos2, 0) < REALLY_HIGH_NUMBER + MARGIN)
					);
	}
	
	public static int getFirstCloneMoveIndex(PositionCellGame pos) {
		ArrayList<Integer> moves = pos.getMoveListReduced(pos.isP1turn());
		
		System.out.println("Just clone because it's game over:");
		
		//Just clone because it's game over
		for(int i=0; i<moves.size(); i++) {

			if( ! PositionCellGame.isJump(moves.get(i))) {
				return moves.get(i);
			}
		}
		System.out.println("AHH! No clone moves! This shouldn't be possible!");
		return -1;
	}
}
