package views;

import java.text.DecimalFormat;

import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.Energy.EnergyFormater;
import org.ctom.hulis.huckel.Energy.LetterFormat;

import util.SwingNumberFormater;

/**
 * 
 * @author macbook
 *
 */
public class SwingEnergyFormater implements EnergyFormater {
	
	SwingNumberFormater alphaFormater;
	SwingNumberFormater betaFormater;
	public SwingEnergyFormater(DecimalFormat alphaFormat, DecimalFormat betaFormat) {
		this.alphaFormater = new SwingNumberFormater(alphaFormat);
		this.betaFormater = new SwingNumberFormater(betaFormat);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Energy.EnergyFormater#getFormatLetters()
	 */
	public LetterFormat getFormatLetters() {
		return Energy.LetterFormat.UTF8;
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Energy.EnergyFormater#formatAlpha(double)
	 */
	public String formatAlpha(double number) {
		return this.alphaFormater.formatNumber(number);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Energy.EnergyFormater#formatBeta(double)
	 */
	public String formatBeta(double number) {
		return this.betaFormater.formatNumber(number);
	}

	
}