package Players.alphabetapruning;

import java.util.ArrayList;
import java.util.HashMap;

import Players.PlayerI;
import alphaBetaHash.SimpleHashObject;
import env.PositionCellGame;
import env.SanityTestEnv;

public class HashAlphaBeta implements PlayerI {

	private String playerName = "";
	
	public static HashMap<Long, SimpleHashObject> refutationTable = new HashMap<Long, SimpleHashObject>();

	public static int DEBUG_MULT = 100000;
	public int depth;
	
	public HashAlphaBeta(int depth) {
		this.depth = depth;
	}

	public static int debugNumMatches = 0;
	public static int debugNumElements = 0;
	
	@Override
	public int getMove(PositionCellGame pos) {
		
		refutationTable = new HashMap<Long, SimpleHashObject>();
		
		int tmp = getBestMove(pos, this.depth, this.getPlayerName());
		
		System.out.println("Debug:");
		System.out.println("Number of matches: " + debugNumMatches);
		System.out.println("Number of elements added to hash: " + debugNumElements);
		
		debugNumMatches = 0;
		debugNumElements = 0;
		
		
		return tmp;
	}

	@Override
	public String getPlayerName() {
		return "Simple Hash Alpha Beta with depth " + this.depth;
	}

	@Override
	public void updatePosition(PositionCellGame pos) {
		//pass
		
	}
	

	public static int getBestMove(PositionCellGame pos, int depth, String name) {
		
		System.out.println("Trying to get the best move for " + name);
		
		long choices[] = getUtilityOfAllMoves(pos, depth);
		
		if(choices.length == 0) {
			return PositionCellGame.NO_MOVE_PASS_THE_TURN;
		}
		
		int bestIndex = 0;
		
		if(pos.isP1turn()) {
			//get max utility choice:
			bestIndex = getMaxIndex(choices);
		
		} else {
			
			//get min utility choice:
			bestIndex = getMinIndex(choices);
		}
		
		
		
		return pos.getMoveListReduced(pos.isP1turn()).get(bestIndex);
	}
	
	
	public static int getMaxIndex(long choices[]) {
		int bestIndex = 0;
		
		for(int i=0; i<choices.length; i++) {
			if(choices[i] > choices[bestIndex]) {
				bestIndex = i;
			}
		}
		
		return bestIndex;
	}
	
	public static int getMinIndex(long choices[]) {
		int bestIndex = 0;
		
		for(int i=0; i<choices.length; i++) {
			if(choices[i] < choices[bestIndex]) {
				bestIndex = i;
			}
		}
		
		return bestIndex;
	}
	
	public static final int REALLY_HIGH_NUMBER = 100000;
	public static final String SPACE = "         ";
	public static final int REASONABLY_HIGH_NUMBER = 100000;
	
	public static long[] getUtilityOfAllMoves(PositionCellGame pos, int depth) {
		ArrayList<Integer> choices = pos.getMoveListReduced(pos.isP1turn());
		
		
		boolean maximizingPlayer = true;
		if(! pos.isP1turn()) {
			maximizingPlayer = false;
		}
		
		//make sure depth doesn't go beyond the game:
		if(choices.size() == 0) {
				//Pass:
				return new long[0];
		}
		
		long ret[] = new long[choices.size()];
		
		long curBest = -1;
		
		boolean isFirstTrial = true;
		
		for(int i=0; i<choices.size(); i++) {
			
			if( i > 0) {
				isFirstTrial = false;
			}
			
			System.out.println("Trying move: " + SanityTestEnv.convertMoveNumberToString(choices.get(i)));
			
			if(isFirstTrial) {
				ret[i] = getMoveUtil(pos.move(choices.get(i)), depth-1, -REALLY_HIGH_NUMBER, REALLY_HIGH_NUMBER);
				
			} else {
				if(maximizingPlayer) {
					ret[i] = getMoveUtil(pos.move(choices.get(i)), depth-1, curBest, REALLY_HIGH_NUMBER);
				} else {
					ret[i] = getMoveUtil(pos.move(choices.get(i)), depth-1, -REALLY_HIGH_NUMBER, curBest);
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
	public static long getMoveUtil(PositionCellGame pos, int depth, long alpha, long beta) {
		if(depth == 0) {
			return pos.getCurUtil();
		}
		
		if(refutationTable.containsKey(pos.getCurHash())) {
			
			SimpleHashObject simpleObject = refutationTable.get(pos.getCurHash());
			
			if(simpleObject.getDepthUsed() >= depth) {
				
				debugNumMatches++;
				
				if(debugNumMatches % DEBUG_MULT == 0) {
					System.out.println("debugNumMatches: " + debugNumMatches);
				}
				
				if( ! simpleObject.isWasPrunedBeforeFullyCalc()) {
					return simpleObject.getUtilValue();
				} else {
					if(pos.isP1turn()) {
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
		
		ArrayList<Integer> choices = pos.getMoveListReduced(pos.isP1turn());
		
		if(choices.size() == 0
				&& pos.isGameOver()) {

			return pos.getCurUtil();

		} else if(choices.size() == 0) {
			
			long edgeCaseUtil = handleNoMoveEdgeCase(pos, depth);
			
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
		
		
		long ret;
		
		if(pos.isP1turn()) {
			
			//get max util for next turn:
			for(int i=0; i<choices.size(); i++) {
				
				alpha = Math.max(getMoveUtil(pos.move(choices.get(i)), depth-1, alpha, beta), alpha);
				
				if(alpha >= beta) {
					break;
				}
				
				
			}
			
			ret = alpha;
			
		} else {
			
			//get min util for next turn:
			for(int i=0; i<choices.size(); i++) {
				beta = Math.min(getMoveUtil(pos.move(choices.get(i)), depth-1, alpha, beta), beta);
					
				if(beta <= alpha) {
					break;
				}
				
			}
			
			ret = beta;
		}
		
		//refutationTable.
		
		boolean wasPrunedBeforeFullyCalc = (beta <= alpha);
		
		
		SimpleHashObject simpleObject = new SimpleHashObject(ret, wasPrunedBeforeFullyCalc, depth);
		//TODO: add the hashDepth as a var
		if(depth > 2) {
			// TODO: make this non-static.
			if(refutationTable.containsKey(pos.getCurHash())) {
				refutationTable.remove(pos.getCurHash());
				debugNumElements--;
			}
			refutationTable.put(pos.getCurHash(), simpleObject);
			debugNumElements++;
			
			if(debugNumElements % DEBUG_MULT == 0) {
				System.out.println("debugNumElements: " + debugNumElements);
			}
		}
		
		return ret;
	}
	

	public static final long INCONCLUSIVE = -1234567; 
	
	public static long handleNoMoveEdgeCase(PositionCellGame pos, int depth) {
		
		//Edge case where player ran out of moves:

		
		long target = -1;
		int mult = 1;
		
		if(pos.isP1turn()) {
			target = PositionCellGame.P1_CELL;
			
			if(pos.getCurUtil() < 0) {
				//Quick return because current player obviously lost:
				return  -2 * REASONABLY_HIGH_NUMBER;
			}
		} else {
			target = PositionCellGame.P2_CELL;
			

			if(pos.getCurUtil() > 0) {
				//Quick return because current player obviously lost:
				return  2 * REASONABLY_HIGH_NUMBER;
			}
			
			mult = -1;
		}
		
		long board[][] = pos.getBoard();
		
		int sum = 0;
		
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[0].length; j++) {
				if(board[i][j] == target) {
					sum++;
				}
			}
		}
		
		boolean keepTrying = false;
		
		long tmp = -1;
		if(sum > PositionCellGame.SIDE_LENGTH_SQUARE / 2) {

			//TODO: maybe improve on this latter?
			
			System.out.println();
			System.out.println("Exceptional pos:");
			System.out.println(pos);
			
			System.out.println("I don't know. Player that cannot move is currently winning");
			System.out.println("But I don't know if it's a forced win...");
			tmp = pos.getCurUtil();
			keepTrying = true;
			

			System.out.println("Util in edge case:");
			System.out.println(tmp);
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
