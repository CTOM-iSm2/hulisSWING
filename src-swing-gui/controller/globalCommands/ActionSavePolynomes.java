package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ctom.hulis.polynomes.CharacteristicPolynomial;
import org.ctom.hulis.polynomes.Polynomial;

import util.io.SwingIO;
import util.polyCalculator.PolyCalculator;
import views.FrameApp;


/**
 * send the polynomial to the calculator
 * 
 * @author ycarissan
 *
 */
public class ActionSavePolynomes implements ICommand, ActionListener {
	FrameApp app;

	public ActionSavePolynomes(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "save polynome");
	
		SwingIO.PrintIfln("Sauve Polynome");
		CharacteristicPolynomial poly = this.app.getCurrentMesomeryView().getCurrentStructure().getCharacteristicPolynomial();
		Polynomial polynomialFunction = poly.getPolynomialFunction();
		PolyCalculator polyCalc = this.app.getPolyCalc();
		polyCalc.add(1, polynomialFunction);
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
