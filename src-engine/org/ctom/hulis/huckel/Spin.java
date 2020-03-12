package org.ctom.hulis.huckel;

public enum Spin {
	//ALPHA : spin ALPHA
	//BETA : spin BETA
	//NOSPIN : no electron
	//SWITCH : ALPHA=SWITCH*BETA
	ALPHA(1),BETA(-1),SWITCH(-1),NOSPIN_ALPHA(91),NOSPIN_BETA(92);

	private final int val;
	
	Spin(int i) {
		this.val=i;
	}

	public int getval() {
		return this.val;
	}

}
