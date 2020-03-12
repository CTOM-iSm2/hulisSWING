package util.polyCalculator.view.main;


import java.awt.Container;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.IllegalFormatConversionException;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.ctom.hulis.polynomes.Polynomial;
import org.ctom.hulis.util.math.Complex;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import util.polyCalculator.PolyCalculatorListener;
import util.polyCalculator.view.elements.PolyType;
import util.polyCalculator.view.elements.PolyView;
import util.polyCalculator.view.elements.PolyViewListener;

public class PolyCalculatorView implements PolyCalculatorListener, PolyViewListener {

	public static final DecimalFormat decimalFormat = new DecimalFormat("0.0000");
	public static final ComplexFormat complexFormat = new ComplexFormat(decimalFormat);
	private Box col;
	private Box lig_poly;
	private Box lig_result;
	private Box lig_TRE;
	private ArrayList<Boolean> plot_tab=new ArrayList<Boolean>();
	private ArrayList<Polynomial> poly_tab = new ArrayList<Polynomial>();

	private ArrayList<PolyCalculatorViewListener> listener=new ArrayList<PolyCalculatorViewListener>();

	private JFrame mainFrame;
	private JFrame plotFrame;
	private JTextField treLabel;

	private XYSeriesCollection xyDataset;
	private PolyView polyGuiResult;
	private boolean PLOT_RESULT=false;

	/**
	 * a view of the polycalculator it is associated to
	 */
	public PolyCalculatorView() {
		//fenetre pincipale
		mainFrame = new JFrame("Poly Calculator");
		init();
		//fenetre de graphes
		plotFrame = new JFrame("Plot");
//		plotFrame.setVisible(true);
		setplotframe();
		plotFrame.pack();
		this.add(new Polynomial(new double[] {0}), new Complex[] {new Complex(0.0, 0.0)}, PolyType.RESULT);
		mainFrame.setVisible(true);
	}

	/**
	 * adds a polynomial function to the gui. The function can be NORMAL or the RESULT function
	 * @param pf
	 * @param pt
	 */
	public void add(Polynomial pf, Complex[] roots, PolyType pt) {
		PolyView pg = new PolyView(pf, roots,  pt);
		if (pt==PolyType.RESULT) {
			polyGuiResult = pg;
			lig_result.add(pg);
		} else {
			lig_poly.add(pg);
			plot_tab.add(false);
			poly_tab.add(pf);
		}
		pg.addlistener(this);
		col.setPreferredSize(new Dimension(PolyView.POLY_WIDTH+PolyView.ROOT_WIDTH+150,PolyView.STANDARD_HEIGHT*(poly_tab.size()+1)+50));
		mainFrame.pack();
	}
	
	/**
	 * removes a polynomial from the gui.
	 * @param pf
	 */
	public void remove(Polynomial pf) {
		int i=0;
		for (Object o : lig_poly.getComponents()) {
			if (o instanceof PolyView) {
				PolyView pv = (PolyView)o;
				if (pv.getPolynomial()==pf) {
					lig_poly.remove(pv);
					poly_tab.remove(pf);
					plot_tab.remove(i);
					pv=null;
					updateData2Plot();
					mainFrame.pack();
				}
				i++;
			}
		}
		for (PolyCalculatorViewListener l : this.listener) {
			l.removePoly(pf);
		}
	}

	public void addlistener(PolyCalculatorViewListener gl) {
		this.listener.add(gl);
	}

	public void CoeffModified(Polynomial pf, double coeff) {
		for (PolyCalculatorViewListener gl : listener) {
			gl.CoeffModifiedinGUI(pf, coeff);
		}
	}

	public void switchPlot(Polynomial pf) {
		int i=poly_tab.indexOf(pf);
		if (i==-1) {//result case
			PLOT_RESULT=!PLOT_RESULT;
		} else {
			plot_tab.set(i, !plot_tab.get(i));
		}
		updateData2Plot();
	}

	/**
	 * updates the plot window
	 */
	private void updateData2Plot() {
		//clean data to plot
		xyDataset.removeAllSeries();
		plotFrame.setVisible(false);
		//put data to plot
		for (int i=0; i<poly_tab.size(); i++) {
			if (plot_tab.get(i)) {
				plotFrame.setVisible(true);
				XYSeries series = new XYSeries(i);
				xyDataset.addSeries(series);
				for (double x=-10;x<=10; x+=0.01) {
					series.add(x, poly_tab.get(i).eval(x));
				}
			}
		}
		if (PLOT_RESULT) {
			plotFrame.setVisible(true);
			Polynomial pf = polyGuiResult.getPolynomial();
			XYSeries series = new XYSeries(poly_tab.size());
			xyDataset.addSeries(series);
			for (double x=-10;x<=10; x+=0.01) {
				series.add(x, pf.eval(x));
			}
		}
	}

	/**
	 * initialize the object
	 */
	private void init() {
		Container conteneur = mainFrame.getContentPane();
		conteneur.removeAll();
		//3 lignes
		lig_poly = Box.createVerticalBox();
		lig_result = Box.createVerticalBox();
		lig_TRE = Box.createVerticalBox();
		//add the label to the last line
		treLabel = new JTextField("");
		treLabel.setEditable(false);
		treLabel.setBorder(null);
		lig_TRE.add(treLabel);
		//1 colonne
		col = Box.createVerticalBox();
		col.setPreferredSize(new Dimension(1000, 400));
		conteneur.add(col);
		col.add(lig_poly);
		col.add(lig_result);
		col.add(lig_TRE);
		mainFrame.pack();
	}


	/**
	 * initialize the plot window
	 */
	private void setplotframe() {
		NumberAxis xaxis = new NumberAxis("x");
		NumberAxis yaxis = new NumberAxis("P(x)");
		StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator("({1} ; {2})",NumberFormat.getInstance(),NumberFormat.getInstance());
		StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.LINES, ttg, null);
		xyDataset = new XYSeriesCollection();
		XYPlot plot = new XYPlot(xyDataset, xaxis, yaxis, renderer);
		xaxis.setRange(-5, 5);
		yaxis.setRange(-10, 10);
		ChartPanel chartpanel = new ChartPanel(new JFreeChart("Polynomes", plot));
		chartpanel.setPreferredSize(new Dimension(600, 400));
		plotFrame.add(chartpanel);
	}

	public void resultModified(Polynomial pf, Complex[] roots) {
		polyGuiResult.setPoly(pf, roots);
		updateData2Plot();
	}

	public void treModified(int nElec, double tre) {
		try {
			DecimalFormat formatter = new DecimalFormat(".00");
			treLabel.setText("Energy for "+nElec+" electrons is "+formatter.format(tre));
		} catch (IllegalFormatConversionException e) {
			System.out.println(nElec+" "+tre);
			treLabel.setText("Impossible to compute TRE : no electron or imaginary roots.");
		}
	}

	public void removePoly(Polynomial pf) {
		this.remove(pf);
	}

}