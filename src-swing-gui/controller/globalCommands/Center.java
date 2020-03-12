package controller.globalCommands;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.ImageIcon;

import org.ctom.hulis.huckel.structures.Structure;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.MesomeryView;
import views.StructureView;
import views.ToolBarHuckel;

public class Center extends ActionCommand implements  ICommand,
IGlobalCommands  {


	private MesomeryView mesomeryView;

	public Center (MesomeryView mesomeryView) {
		super(IGlobalCommands.CENTER);
		updateLanguage();
		this.mesomeryView = mesomeryView;
	}
	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "center the structures");

		final Structure structure = this.mesomeryView.getMesomery().getDelocalizedStructure();
		final StructureView structureView = this.mesomeryView.getStructureView(structure);
		if ( (structure == null) ||  (structureView == null))
			return;
		
		center(structure, structureView);
		
		ComponentListener componentListener = new ComponentListener() {


			public void componentResized(ComponentEvent e) {
				center(structure, structureView);
			}
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}



		};
		
		structureView.addComponentListener(componentListener);
		

	}
	
	
	public void center(Structure structure, StructureView structureView) {
		double moleculeWidth = structure.getWidth();
		double moleculeHeight = structure.getHeight();
		double zoom = mesomeryView.getZoom();

		double xMargin = ((structureView.getWidth()/ 2) )  / zoom - moleculeWidth /2 - structure.getXMin() ;
		double yMargin = ((structureView.getHeight()/ 2) ) / zoom - moleculeHeight /2 - structure.getYMin() ;

		mesomeryView.setXMargin(xMargin);
		mesomeryView.setYMargin(yMargin);
		mesomeryView.refresh();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	
	public void updateLanguage() {
		
		MyResourceBundle rb3 = LanguageManager.getInstance().getResource("ToolBarHuckel");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(
				new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(rb3.getString("kimgcenter"))).getImage().getScaledInstance(ToolBarHuckel.ICON_WIDTH, ToolBarHuckel.ICON_HEIGHT, Image.SCALE_SMOOTH) ), bundle.getString("kcenter1"),  bundle.getString("kcenter2"));
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
