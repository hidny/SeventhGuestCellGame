package alphaBetaHash;

public class SimpleHashObjectWithDoubleUtil {


	public SimpleHashObjectWithDoubleUtil(double utilValue, boolean wasPrunedBeforeFullyCalc, int depthUsed) {
		super();
		this.utilValue = utilValue;
		this.wasPrunedBeforeFullyCalc = wasPrunedBeforeFullyCalc;
		this.depthUsed = depthUsed;
	}

	private double utilValue;
	
	private boolean wasPrunedBeforeFullyCalc;
	
	int depthUsed;


	public double getUtilValue() {
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
