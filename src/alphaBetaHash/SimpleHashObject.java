package alphaBetaHash;

public class SimpleHashObject {


	public SimpleHashObject(long utilValue, boolean wasPrunedBeforeFullyCalc, int depthUsed) {
		super();
		this.utilValue = utilValue;
		this.wasPrunedBeforeFullyCalc = wasPrunedBeforeFullyCalc;
		this.depthUsed = depthUsed;
	}

	private long utilValue;
	
	private boolean wasPrunedBeforeFullyCalc;
	
	int depthUsed;


	public long getUtilValue() {
		return utilValue;
	}

	public void setUtilValue(long utilValue) {
		this.utilValue = utilValue;
	}

	public boolean isWasPrunedBeforeFullyCalc() {
		return wasPrunedBeforeFullyCalc;
	}

	public void setWasPrunedBeforeFullyCalc(boolean wasPrunedBeforeFullyCalc) {
		this.wasPrunedBeforeFullyCalc = wasPrunedBeforeFullyCalc;
	}

	public int getDepthUsed() {
		return depthUsed;
	}

	public void setDepthUsed(int depthUsed) {
		this.depthUsed = depthUsed;
	}
}
