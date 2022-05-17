package Players.alphabetaComplexEval;

import env.PositionCellGame;

public class PositionWithComplexEval extends PositionCellGame {

	public static void main(String args[]) {
		PositionCellGame a = new PositionCellGame(true);
		
	}
	
	
	public PositionWithComplexEval(PositionCellGame origPos) {
		super(origPos);
	}
	
	public PositionCellGame move(int move) {
		return new PositionWithComplexEval(
		
			super.move(
			move / SIDE_LENGTH_CUBE,
			(move / SIDE_LENGTH_SQUARE) % SIDE_LENGTH,
			(move / SIDE_LENGTH) % SIDE_LENGTH,
			move% SIDE_LENGTH)
		);
		
	}
	
	
	//TODO: maybe just add function to object...
	//TODO: make this function more dynamic somehow? Nah!
			// (Have eval values that update after each move)
	
	//Slightly more complex eval:
	public double getComplexUtil() {
		
		double ret = this.getCurUtil();
		
		long curColour = 0;
		long opponentColour = 0;
		
		double mult = 1.0;
		
		if(this.isP1turn()) {
			curColour = PositionCellGame.P1_CELL;
			opponentColour = PositionCellGame.P2_CELL;
			mult = 1.0;
		} else {
			curColour = PositionCellGame.P2_CELL;
			opponentColour = PositionCellGame.P1_CELL;
			mult = -1.0;
			
		}
		
		long board[][] = this.getBoard();
		
		int bestVulnerability = 0;
		
		
		for(int isrc=0; isrc<SIDE_LENGTH; isrc++) {
			for(int jsrc=0; jsrc<SIDE_LENGTH; jsrc++) {
				
				
				if(board[isrc][jsrc] == EMPTY) {

					int curVulnerability = 0;
					boolean hasCloseNeighbour = false;
					boolean hasFarNeighbour = false;
					
					for(int idest=Math.max(isrc-2, 0); idest <= Math.min(isrc + 2, SIDE_LENGTH-1); idest++ ) {
						for(int jdest=Math.max(jsrc-2, 0); jdest <= Math.min(jsrc + +2, SIDE_LENGTH-1); jdest++) {
							
							if(board[idest][jdest] == opponentColour) {
								curVulnerability++;
								
							} else if(board[idest][jdest] == curColour) {
								hasFarNeighbour = true;
								
								if(! PositionCellGame.isJump(isrc, jsrc, idest, jdest)) {
									hasCloseNeighbour = true;
								}
								
							}
							
						}
					}
					
					if(hasCloseNeighbour) {
						//pass
					} else if(hasFarNeighbour) {
						curVulnerability--;
						//TODO: maybe I should make sure this isn't negative?
						//Actually, best Vulnerability starts at 0, so we're good!
						
					} else if(hasFarNeighbour == false) {
						curVulnerability = 0;
					}
					
					if(curVulnerability > bestVulnerability) {
						bestVulnerability = curVulnerability;
					}
					
				}
			}
		}
		
		ret = ret + mult * 0.5 * bestVulnerability;
		
		return ret;
	}
	
}
