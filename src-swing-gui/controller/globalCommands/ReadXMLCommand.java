package controller.globalCommands;

import org.ctom.hulis.files.xyz.GeometryReader;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.structures.StructureDelocalized;

import util.io.SwingIO;
import views.FrameApp;


/**
 * This command is called when the user opens a xml file
 * It erases the current mesomery
 * It constructs a mesomery with the file
 * It puts the molecule at the center of the molecule viewer
 * @author ngoudard
 *
 */
public class ReadXMLCommand implements ICommand {
	FrameApp app;
	GeometryReader xyz;

	public ReadXMLCommand(FrameApp app, GeometryReader xyz) {
		this.app = app;
		this.xyz = xyz;
	}

	public void execute() {
		// erase mesomery
		SwingIO.log(this.getClass().getName(), "execute", "read xml");

		try {
			this.app.getStateManager().initNewState();
			
			this.app.getCurrentMesomeryView().getMesomery().deleteAllStructures();
			// constructs structure with xyz file
			this.app.getCurrentMesomeryView().getMesomery().addDelocalizedStruct(
					new StructureDelocalized(this.xyz));
			EnableActionManager.getInstance().updateActionsToEnable(this.app);

		} catch (CoupleException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
		} catch (IMethodException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
		} catch (MesomeryNoStructureLocalizedException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
		} catch (HuckelBondException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
		} catch (BondException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
		} catch (MesomeryDelocalizedStructureExistsException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
		} catch (MoleculeTooManyNeighboursException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
		} catch (MoleculeCoherenceException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
		} catch (MoleculeBondExistsException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
		} catch (Exception e) {
			SwingIO.error(this.getClass().getName(), "execute",
					"cannot create delocalized structure", e);
			SwingIO.reportError(app);
		}	
			

		// center the structure

		
		new Center (this.app.getCurrentMesomeryView()).execute();
		this.app.getStateManager().saveState();

	}

	/**
	 * 
	 * @return
	 */
	public boolean isSucceed() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFailReason(){
		return "";
	}
}
