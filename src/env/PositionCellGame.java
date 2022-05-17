package env;
import java.util.ArrayList;


public class PositionCellGame {

	public static final int SIDE_LENGTH = 7;
	public static final int SIDE_LENGTH_SQUARE = SIDE_LENGTH * SIDE_LENGTH;
	public static final int SIDE_LENGTH_CUBE = SIDE_LENGTH * SIDE_LENGTH_SQUARE;
	public static final int SIDE_LENGTH_FORTH = SIDE_LENGTH * SIDE_LENGTH_CUBE;

	public static final long EMPTY = 0;
	public static final long P1_CELL = 1;
	public static final long P2_CELL = 2;
	private static final long HASH_LIMIT = (long)Math.pow(10,  18);
	
	public static final int NO_MOVE_PASS_THE_TURN = -1;

	private static boolean hashMultSet = false;
	private static long hashMult[][] = new long[SIDE_LENGTH][SIDE_LENGTH];
	private static long backupHashMult[][] = new long[SIDE_LENGTH][SIDE_LENGTH];
	
	
	
	public static void main(String[] args) {
		
		PositionCellGame start = new PositionCellGame(true);
		
		System.out.println(start);
	}
	
	public PositionCellGame() {
		
	}

	public PositionCellGame(boolean startPos) {
		
		if(startPos) {
			
			if(hashMultSet == false) {
				setupHashMultiples();
			}
			insertCell(0, 0, P1_CELL);
			insertCell(SIDE_LENGTH-1, SIDE_LENGTH-1, P1_CELL);
			
			insertCell(0, SIDE_LENGTH-1, P2_CELL);
			insertCell(SIDE_LENGTH-1, 0, P2_CELL);
			
		} else {
			//Do nothing
		}
	}
	
	
	public PositionCellGame(PositionCellGame origPos) {
		
		for(int i=0; i<SIDE_LENGTH; i++) {
			for(int j=0; j<SIDE_LENGTH; j++) {
				this.board[i][j] = origPos.board[i][j];
			}
		}
		
		this.curHash = origPos.curHash;
		this.curBackupHash = origPos.curHash;
		this.p1turn = origPos.p1turn;
		this.curUtil = origPos.curUtil;
		
	}
	
	public String toString() {
		String ret = "Position:\n";
		
		for(int i=0; i<SIDE_LENGTH; i++) {
			for(int j=0; j<SIDE_LENGTH; j++) {
				if(board[i][j] == EMPTY) {
					ret += " _";
					
				} else if(board[i][j] == P1_CELL) {
					ret += " 1";
					
				} else if(board[i][j] == P2_CELL) {
					ret += " 2";
					
				} else {
					ret += " ?";
				}
					
			}
			ret +="\n";
		}
		ret +="\n";
		if(this.p1turn) {
			ret+="It\'s Player 1's turn.";
		} else {
			ret+="It\'s Player 2's turn.";
		}
		ret +="\n";

		ret += "Current basic util:    " +  this.curUtil;
		ret +="\n";
		
		String space = "                       ";
		ret +="\n";
		ret += "Cur hash:    " + space.substring((this.curHash + "").length()) + this.curHash;
		ret +="\n";
		ret += "Backup hash: " + space.substring((this.curBackupHash + "").length()) + this.curBackupHash;
		ret +="\n";
		
		return ret;
	}

	private static long turnMult = -1;
	private static long backupTurnMult = -1;
	
	public static void setupHashMultiples() {
		
		long cur = 1;
		long curBack = 1;
		
		long mult = 3;
		long multBack = 7;
		
		for(int i=0; i<SIDE_LENGTH; i++) {
			for(int j=0; j<SIDE_LENGTH; j++) {
				hashMult[i][j] = cur;
				cur = (mult * cur) % HASH_LIMIT;
				
				backupHashMult[i][j] = curBack;
				curBack = (multBack * curBack) % HASH_LIMIT;
			}
		}
		
		turnMult = (mult * cur) % HASH_LIMIT;
		backupTurnMult = (mult * cur) % HASH_LIMIT;
		
	}

	
	public long getCurHash() {
		return curHash;
	}


	public long getCurBackupHash() {
		return curBackupHash;
	}


	public long getCurUtil() {
		return curUtil;
	}
	
	private long board[][] = new long[SIDE_LENGTH][SIDE_LENGTH];
	
	public long[][] getBoard() {
		return board;
	}

	private long curHash = 0L;
	

	private long curBackupHash = 0L;
	
	private long curUtil = 0L;
	
	private boolean p1turn = true;
	
	public PositionCellGame hardCopy() {
		
		PositionCellGame newPos = new PositionCellGame();
		
		for(int i=0; i<SIDE_LENGTH; i++) {
			for(int j=0; j<SIDE_LENGTH; j++) {
				newPos.board[i][j] = this.board[i][j];
			}
		}
		
		newPos.curHash = this.curHash;
		newPos.curBackupHash = this.curBackupHash;
		newPos.curUtil = this.curUtil;
		newPos.p1turn = this.p1turn;
		
		return newPos;
	}
	
	
	public ArrayList<Integer> getMoveList() {
		return getMoveList(this.isP1turn());
	}
	
	public ArrayList<Integer> getMoveList(boolean isPlayer1Turn) {
		
		long curColour = -1;
		if(isPlayer1Turn) {
			curColour = P1_CELL;
		} else {
			curColour = P2_CELL;
		}
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		for(int isrc=0; isrc<SIDE_LENGTH; isrc++) {
			for(int jsrc=0; jsrc<SIDE_LENGTH; jsrc++) {
				
				if(board[isrc][jsrc] == curColour) {
					
					int firstPartMoveNumber = SIDE_LENGTH_SQUARE * (isrc * SIDE_LENGTH + jsrc);
					
					for(int idest=Math.max(isrc-2, 0); idest <= Math.min(isrc + 2, SIDE_LENGTH-1); idest++ ) {
						for(int jdest=Math.max(jsrc-2, 0); jdest <= Math.min(jsrc + +2, SIDE_LENGTH-1); jdest++) {
							
							if(board[idest][jdest] == EMPTY) {
								ret.add(idest * SIDE_LENGTH + jdest + firstPartMoveNumber);
							}
							
						}
					}
				}
			}
		}
		
		return ret;
		
	}
	
	//Use this function for alpha beta pruning:
	// I think the results match the other getMoveList function expect it's less redundant...
	// It was good enough to win, so I guess it's ok...
	public ArrayList<Integer> getMoveListReduced(boolean isPlayer1Turn) {
		long curColour = -1;
		if(isPlayer1Turn) {
			curColour = P1_CELL;
		} else {
			curColour = P2_CELL;
		}
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		boolean cloneMoveLandingsFound[][] = new boolean[SIDE_LENGTH][SIDE_LENGTH];
		
		for(int isrc=0; isrc<SIDE_LENGTH; isrc++) {
			for(int jsrc=0; jsrc<SIDE_LENGTH; jsrc++) {
				
				if(board[isrc][jsrc] == curColour) {
					
					int firstPartMoveNumber = SIDE_LENGTH_SQUARE * (isrc * SIDE_LENGTH + jsrc);
					
					for(int idest=Math.max(isrc-2, 0); idest <= Math.min(isrc + 2, SIDE_LENGTH-1); idest++ ) {
						for(int jdest=Math.max(jsrc-2, 0); jdest <= Math.min(jsrc + +2, SIDE_LENGTH-1); jdest++) {
							
							if(board[idest][jdest] == EMPTY) {
								
								if(Math.abs(idest - isrc) >= 2 
										|| Math.abs(jdest - jsrc) >= 2) {
									
									//Jumps are always unique (Not much room for reduction...)
									ret.add(idest * SIDE_LENGTH + jdest + firstPartMoveNumber);
									
									
								} else if( ! cloneMoveLandingsFound[idest][jdest]) {
									
									//Idea: after a clone move, only the destination matter,
									// so remove clone moves where the destination is the same.
									ret.add(idest * SIDE_LENGTH + jdest + firstPartMoveNumber);
									cloneMoveLandingsFound[idest][jdest] = true;
								}
							}
							
						}
					}
				}
			}
		}
		
		return ret;
	}

	public PositionCellGame move(int move) {
		return move(
			move / SIDE_LENGTH_CUBE,
			(move / SIDE_LENGTH_SQUARE) % SIDE_LENGTH,
			(move / SIDE_LENGTH) % SIDE_LENGTH,
			move% SIDE_LENGTH
		);
		
	}
	
	public PositionCellGame move(int ifrom, int jfrom, int ito, int jto) {
		
		PositionCellGame pos = this.hardCopy();
		return pos.moveSoft(ifrom, jfrom, ito, jto);
	}
	
	private PositionCellGame moveSoft(int ifrom, int jfrom, int ito, int jto) {
		
		long curColour2 = board[ifrom][jfrom];
		
		if(jto >= 0) {
			//if jto is a real coord ( -1 means that the player just gives away their turn)
			
			long curColour = -1;
			int numPiecesDiffUsedAsUtil = 0;
	
			long otherColour = -1;
			if(this.p1turn) {
				curColour=P1_CELL;
				otherColour=P2_CELL;
				
			} else {
				curColour=P2_CELL;
				otherColour=P1_CELL;
				
			} 
			
			if(curColour != curColour2){
				System.out.println("Oops! Bad move!");
			}
			
			
			if(Math.abs(ifrom - ito) >= 2 || Math.abs(jfrom - jto) >= 2) {
				//It's a jump!
				removeCell(ifrom, jfrom);

			} else {
				numPiecesDiffUsedAsUtil++;
			}
			
			insertCell(ito, jto, curColour);
			
			for(int i=Math.max(ito-1, 0); i <= Math.min(ito + 1, SIDE_LENGTH-1); i++ ) {
				for(int j=Math.max(jto-1, 0); j <= Math.min(jto + 1, SIDE_LENGTH-1); j++) {
					
					if(board[i][j] == otherColour) {
						removeCell(i, j);
						insertCell(i, j, curColour);
						
						numPiecesDiffUsedAsUtil += 2;
					}
				}
			}
			
			if(curColour == P1_CELL) {
				curUtil += numPiecesDiffUsedAsUtil;
			} else {
				curUtil -= numPiecesDiffUsedAsUtil;
				
			}

		}
		
		p1turn = !p1turn;
		if(p1turn) {
			this.curHash -= turnMult;
			this.curBackupHash -= backupTurnMult;
		} else {
			this.curHash += turnMult;
			this.curBackupHash += backupTurnMult;
		}
		
		readjustHashes();
		
		return this;
	}
	
	private void readjustHashes() {

		//Make sure pos is between 0 and HASH_LIMIT
		this.curHash = ((this.curHash % HASH_LIMIT) + HASH_LIMIT) % HASH_LIMIT;
		
		this.curBackupHash = ((this.curBackupHash % HASH_LIMIT) + HASH_LIMIT) % HASH_LIMIT;
		
		
		
	}
				
	//Dirty insert that also updates hashes
	// (It doesn't do mod hash though, so be careful!)

	private void insertCell(int i, int j, long colour) {
		
		board[i][j] = colour;
		curHash += hashMult[i][j] * colour;
		curBackupHash += backupHashMult[i][j] * colour;
	}


	//Dirty remove that also updates hashes
	// (It doesn't do mod hash though, so be careful!)
	public void removeCell(int i, int j) {
		
		long mult = board[i][j];

		board[i][j] = EMPTY;
		curHash -= hashMult[i][j] * mult;
		curBackupHash -= backupHashMult[i][j] * mult;
	}
	
	public boolean isGameOver() {
	
		//Game is over when no one can move:
		if(this.getMoveList(true).size() == 0
				&& this.getMoveList(false).size() == 0) {

			return true;
		}
			
		int numP1Pegs = 0;
		int numP2Pegs = 0;
		
		for(int i=0; i<SIDE_LENGTH; i++) {
			for(int j=0; j<SIDE_LENGTH; j++) {
				if(this.board[i][j] == P1_CELL) {
					numP1Pegs++;
				} else if(this.board[i][j] == P2_CELL) {
					numP2Pegs++;
				}
			}
		}
		
		if(numP1Pegs == 0
				|| numP2Pegs == 0
				|| numP1Pegs + numP2Pegs == SIDE_LENGTH_SQUARE) {
			return true;
		} else {
			return false;
		}
		
		
	}
	
	
	public boolean isPlayer1Winning() {
		
		int numP1 = 0;
		int numP2 = 0;
		for(int i=0; i<SIDE_LENGTH; i++) {
			for(int j=0; j<SIDE_LENGTH; j++) {
				if(this.board[i][j] == P1_CELL) {
					numP1++;
				} else if(this.board[i][j] == P2_CELL) {
					numP2++;
				}
			}
		}
		
		
		return numP1 > numP2;
	}

	public boolean isP1turn() {
		return p1turn;
	}
	

	public static boolean isJump(int move) {
		return isJump(move / PositionCellGame.SIDE_LENGTH_CUBE,
				(move / PositionCellGame.SIDE_LENGTH_SQUARE) % PositionCellGame.SIDE_LENGTH,
				(move / PositionCellGame.SIDE_LENGTH) % PositionCellGame.SIDE_LENGTH,
				move% PositionCellGame.SIDE_LENGTH);
	}
	
	
	public static boolean isJump(int i1, int j1, int i2, int j2) {
		return Math.abs(i1 - i2) >= 2 || Math.abs(j1 - j2) >= 2;
	}
}
