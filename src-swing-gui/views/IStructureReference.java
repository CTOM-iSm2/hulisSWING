package views;

import org.ctom.hulis.huckel.listeners.IStructureListener;
import org.ctom.hulis.huckel.structures.Structure;

public interface IStructureReference extends IStructureListener {
	public Structure getStructure();
	
	public void setStructure(Structure structure);


}
