package org.ctom.hulis.huckel.warning;

import org.ctom.hulis.huckel.methods.IMethod;

public class IMethodWarning {
	
	private String message ="";
	public HulisWarningType type=HulisWarningType.EMPTY;
	private IMethod parent;
	
	public IMethodWarning (IMethod p, String msg, HulisWarningType type) {
		this.message=msg;
		this.type=type;
		this.parent=p;
	}
	
	public IMethod getParent() {
		return parent;
	}
	public String getMessage() {
		return message;
	}

	public HulisWarningType getType() {
		return type;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IMethodWarning)) return false;
		return super.equals(obj);
	}

	public enum HulisWarningType {
		EMPTY,
		REDUNDANCY,
		UNAPPROPRIATE_METHOD;
	}

}
