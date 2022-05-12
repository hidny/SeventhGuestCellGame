package env;
import java.util.ArrayList;

public class SanityTestEnv {

	public static void main(String[] args) {

		
		PositionCellGame start = new PositionCellGame(true);
		
		
		debugPrintMoves(start);
	}

	
	public static void debugPrintMoves(PositionCellGame pos) {
		ArrayList<Integer> moves = pos.getMoveList(true);
		
		System.out.println("Printing moves for the following position:");
		System.out.println(pos);
		System.out.println("Moves");
		for(int i=0; i<moves.size(); i++) {
			System.out.println(convertMoveNumberToString(moves.get(i)));
			System.out.println(pos.move(moves.get(i)));
			
		}
	}
	
	
	public static String convertMoveNumberToString(int move) {
		
		String ret = "";
		
		int i1 = move / PositionCellGame.SIDE_LENGTH_CUBE;
		int j1 = (move / PositionCellGame.SIDE_LENGTH_SQUARE) % PositionCellGame.SIDE_LENGTH;
		
		int i2 = (move / PositionCellGame.SIDE_LENGTH) % PositionCellGame.SIDE_LENGTH;
		int j2 = move% PositionCellGame.SIDE_LENGTH;
		
		if(Math.abs(i1 - i2) >= 2 || Math.abs(j1 - j2) >= 2) {
			//It's a jump!
			ret += "Jump ";
		} else {
			ret += "Move ";
		}
		
		ret += "from (" + i1 +", " + j1 + ") to (" + i2 + ", " + j2 + ") (i.e. " + move + ")";
		
		
		return ret;
	}
}
