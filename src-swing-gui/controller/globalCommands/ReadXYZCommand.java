package controller.globalCommands;

import org.ctom.hulis.files.xyz.GeometryReader;
import org.ctom.hulis.files.xyz.ReadxyzException;
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
 * This command is called when the user opens or pastes a xyz file
 * It erases the current mesomery
 * It constructs a mesomery with the xyz file
 * It puts the molecule at the center of the molecule viewer
 * @author ngoudard
 *
 */
public class ReadXYZCommand implements ICommand {
	FrameApp app;
	GeometryReader xyz;
	boolean succeed;
	private String failReason;
	
	public ReadXYZCommand(FrameApp app, GeometryReader xyz) {
		this.app = app;
		this.xyz = xyz;
		this.succeed = false;
		this.failReason = "";
	}

	public void execute() {
		// erase mesomery
		SwingIO.log(this.getClass().getName(), "execute", "try to read xyz");

		try {
		
			this.app.getStateManager().initNewState();
			
			// constructs structure with xyz file
			StructureDelocalized structureDelocalized=new StructureDelocalized(this.xyz);

			
			SwingIO.PrintIf("delete structures  in read XYZ Command");

			
			this.app.getCurrentMesomeryView().getMesomery().deleteAllStructures();
			this.app.getCurrentMesomeryView().getMesomery().addDelocalizedStruct(structureDelocalized);
			EnableActionManager.getInstance().updateActionsToEnable(this.app);
			
			this.app.getToolBarHuckel().refresh();
			
			//center the mesomery
			new Center (this.app.getCurrentMesomeryView()).execute();
			this.app.getStateManager().saveState();
			this.succeed = true;
			SwingIO.log(this.getClass().getName(), "execute", "reading xyz was successful");

		} catch (CoupleException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			this.failReason = e.getMessage();
		} catch (IMethodException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			this.failReason = e.getMessage();
		} catch (MesomeryNoStructureLocalizedException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			this.failReason = e.getMessage();
		} catch (HuckelBondException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			this.failReason = e.getMessage();
		} catch (BondException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			this.failReason = e.getMessage();
		} catch (MesomeryDelocalizedStructureExistsException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			this.failReason = e.getMessage();
		} catch (MoleculeTooManyNeighboursException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			this.failReason = e.getMessage();
		} catch (MoleculeCoherenceException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			this.failReason = e.getMessage();
		} catch (MoleculeBondExistsException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			this.failReason = e.getMessage();
		} catch (ReadxyzException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.failReason = e.getMessage();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.failReason = e.getMessage();
			try {
				new Undo(this.app).execute();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}




	}
	

	/**
	 * 
	 * @return
	 */
	public String getFailReason(){
		return this.failReason;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSucceed() {
		return this.succeed;
	}
	
	

}
