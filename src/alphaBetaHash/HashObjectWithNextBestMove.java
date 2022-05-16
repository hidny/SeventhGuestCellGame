package alphaBetaHash;

public class HashObjectWithNextBestMove {


	public HashObjectWithNextBestMove(long utilValue, boolean wasPrunedBeforeFullyCalc, int depthUsed, int prevBestMove, long backupHash) {
		super();
		this.utilValue = utilValue;
		this.wasPrunedBeforeFullyCalc = wasPrunedBeforeFullyCalc;
		this.depthUsed = depthUsed;
		this.prevBestMove = prevBestMove;
		this.backupHash = backupHash;
	}

	private long utilValue;
	
	private boolean wasPrunedBeforeFullyCalc;
	
	int depthUsed;
	
	private int prevBestMove;
	
	private long backupHash;


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

	public long getBackupHash() {
		return backupHash;
	}


}
