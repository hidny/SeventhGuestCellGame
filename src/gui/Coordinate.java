package gui;

//This code was copied from hidny/CheckersAISept2013Revision

//Simple abstraction of a coordinate:

public class Coordinate {
	private int row;
	private int col;
	
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
		
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public boolean isEqual(Coordinate other) {
		if(other.getRow() == this.row && other.getCol() == this.col) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "(col , row): ( " + col + ", " + row + ")";
	}
}
