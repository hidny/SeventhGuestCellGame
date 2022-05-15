package alphaBetaHash;

public class HashObjectWithNextBestMove {


	public HashObjectWithNextBestMove(long utilValue, boolean wasPrunedBeforeFullyCalc, int depthUsed, int prevBestMove) {
		super();
		this.utilValue = utilValue;
		this.wasPrunedBeforeFullyCalc = wasPrunedBeforeFullyCalc;
		this.depthUsed = depthUsed;
		this.prevBestMove = prevBestMove;
	}

	private long utilValue;
	
	private boolean wasPrunedBeforeFullyCalc;
	
	int depthUsed;
	
	private int prevBestMove;


	public int getPrevBestMove() {
		return prevBestMove;
	}

	public long getUtilValue() {
		return utilValue;
	}

	public boolean isWasPrunedBeforeFullyCalc() {
		return wasPrunedBeforeFullyCalc;
	}


	public int getDepthUsed() {
		return depthUsed;
	}

}
