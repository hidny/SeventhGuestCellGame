package Players.alphabetapruning;

import java.util.ArrayList;

import Players.PlayerI;
import env.PositionCellGame;
import env.SanityTestEnv;

public class SimpleAlphaBeta implements PlayerI {

	private String playerName = "";
	
	public int depth;
	
	public SimpleAlphaBeta(int depth) {
		this.depth = depth;
	}

	@Override
	public int getMove(PositionCellGame pos) {
		
		return getBestMove(pos, this.depth, this.getPlayerName()) ;
	}

	@Override
	public String getPlayerName() {
		return "Simple Alpha Beta with depth " + this.depth;
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
	public static final String SPACE = "      ";
	
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
					//TODO: did I get it the right way around?
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
		
		ArrayList<Integer> choices = pos.getMoveListReduced(pos.isP1turn());
		
		if(choices.size() == 0
				&& pos.isGameOver()) {

			return pos.getCurUtil();
		} else if(choices.size() == 0) {
			
			System.out.println("TODO: FIX ME UP!");
			//TODO: this should be just a function that checks who's winning
			// because only 1 player can't move!
			return pos.getCurUtil();
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
		
		
		return ret;
	}
	
}
