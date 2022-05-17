package alphaBetaHash;

public class HashObjectWithNextBestMoveAndDoubleUtil {


	public HashObjectWithNextBestMoveAndDoubleUtil(double utilValue, boolean wasPrunedBeforeFullyCalc, int depthUsed, int prevBestMove, long backupHash) {
		super();
		this.utilValue = utilValue;
		this.wasPrunedBeforeFullyCalc = wasPrunedBeforeFullyCalc;
		this.depthUsed = depthUsed;
		this.prevBestMove = prevBestMove;
		this.backupHash = backupHash;
	}

	private double utilValue;
	
	private boolean wasPrunedBeforeFullyCalc;
	
	int depthUsed;
	
	private int prevBestMove;
	
	private long backupHash;


	public int getPrevBestMove() {
		return prevBestMove;
	}

	public double getUtilValue() {
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
