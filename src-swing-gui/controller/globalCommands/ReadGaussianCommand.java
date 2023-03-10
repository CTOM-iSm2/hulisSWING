package controller.globalCommands;

import java.awt.Window;
import java.util.List;

import javax.swing.JFrame;

import org.ctom.hulis.files.gaussian.ReadGaussian;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.exception.SelectionException;
import org.ctom.hulis.huckel.methods.IMethod;

import util.io.SwingIO;
import views.FrameApp;
import views.IMesomeryReference;
import views.IStructureReference;

/**
 * This command is called when the user opens or pastes a gaussian file
 * It constructs a mesomery with the gaussian file
 * It gives the choosen method to the mesomery
 * It updates the views with the new mesomery reference
 * It selects all the structures
 * It rebuilds the views and centers the molecules
 * @author ngoudard
 *
 */
public class ReadGaussianCommand implements ICommand {
	FrameApp app;
	ReadGaussian readGaussian;
	private boolean succeed;
	private String failReason;

	public ReadGaussianCommand(FrameApp app, ReadGaussian readGaussian) {
		this.app = app;
		this.readGaussian = readGaussian;
		this.succeed = false;
		this.failReason = "";
	}

	public void execute() {
		try {
			SwingIO.log(this.getClass().getName(), "execute", "read gaussian");
	
			this.app.getStateManager().initNewState();
	
			final Mesomery mesomery = new Mesomery(this.readGaussian); 
			mesomery.setCloneResultsCacheWhenCloneMesomery(FrameApp.CLONE_RESULTS_WHEN_CLONE_MESOMERY);
			// a ce stade la mesomerie ne comporte pas de methodes donc aucun poids n'est renvoye.
			
			// on sauvegarde dans une liste les methodes de calcul selectionnees car l'instruction suivante
			// car on ne veut pas que de calculs soient lances avant la fin
			// de l'affichage des structures pour eviter tout plantage d'affichage en cas d'erreur dans le calcul
			List<IMethod> lstCurrentSelectedMethods = this.app.getListSelectedMethods();
		
			// deselectionne toutes les methodes
			app.deselectAllMethods();
			mesomery.setStructureSelector(app.getCurrentMesomery().getStructureSelector());		
			
			// updates references :pointe sur la bonne mesomerie
			// Attention  mesomeryview.setMesomery(m) appelle mesomeryview.rebuild() 
			// qui rafrachait les fenetres selectionne. Cette action appelle donc le listener JtabbedPane.setChanged() 
			// qui elle meme appelle EnableActionManager.getInstance().updateActionsToEnable(app).
			// Cette methode rafraichit notamment les options de menu "nom de methodes" en fonction des methodes
			// fournies par la mesomerie. 
			// donc a ce stade elle deselectionne toute les options puisqu'on est sur une nouvelle mesomerie Gaussian qui ne coporte aucune methode.
			this.app.getCurrentMesomeryView().setMesomery(mesomery);						

			
			mesomery.addListener(this.app.getToolBarHuckel());
			mesomery.addListener(this.app.getStateManager());

			this.app.getToolBarMesomery().setMesomery(mesomery);
			
			// pointe sur la bonne mesomerie dans les fenetres se referent a la mesomerie
			for (Window  r : JFrame.getWindows()) {
				if (r instanceof IMesomeryReference) {
					IMesomeryReference mr = (IMesomeryReference) r;
					mr.setMesomery(mesomery);

				}
				else // ferme les fenetre relatives aux structures
					if (r instanceof IStructureReference)
						r.dispose();
			}

			// update views and center molecules
			this.app.getCurrentMesomeryView().rebuild();

			EnableActionManager.getInstance().updateActionsToEnable(app);

			this.app.getToolBarHuckel().refresh();
			this.app.getToolBarMesomery().refresh();
		


			this.succeed = true; // on considere que la commande a reussi car le fichier gaussian a ete lu meme si une erreur de calcul survient apres  
			// put the methods to the new mesomery : on rajoute cela a la fin de la lecture du fichier gaussian
			// car si une erreur de calcul survient sur une methode, cela fait echouer la lecture du fichier
	
			// on restaure les methodes qu'on avait sauvegardees au debut del a commande
			for (IMethod method : lstCurrentSelectedMethods) 
				this.app.selectMethod(method.getName(), true);

			// select the structures with the greatest value		
			mesomery.setSelectionTreshold(mesomery.getStructureSelector().getMaximalValue());
	
			// on le fait une deuxieme fois car la premiere fois aucune structure n'est selectionne donc le treshold etait a zero
			mesomery.setSelectionTreshold(mesomery.getStructureSelector().getMaximalValue());
			
			new Center (this.app.getCurrentMesomeryView()).execute();
			
			SwingIO.log(this.getClass().getName(), "execute", "reading gaussian was successful");

			this.app.getStateManager().saveState();
			
		} catch (CoupleException | SelectionException | IMethodException | MesomeryNoStructureLocalizedException e) {
			this.failReason = e.getMessage();
			e.printStackTrace();
		} catch (Exception e) {
			this.failReason = e.getMessage();
			e.printStackTrace();
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
	public boolean isSucceed() {
		return this.succeed;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFailReason(){
		return this.failReason;
	}
}
