package org.ctom.hulis.huckel.spectrum;

import java.text.NumberFormat;

import javax.swing.JFrame;

import org.ctom.hulis.huckel.structures.Structure;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DensityOfStates {

	private double deltaE=0.5;
	private double energyMIN;
	private double energyMAX;
	private double[] dos;
	private JFrame plotFrame;
	
	// TODO NG : ne devrait pas etre dans le "engine"
	private XYSeries xydata;
	private Structure structure;

	public double[] getDos() {
		return dos;
	}

	public DensityOfStates(Structure struct) {
		this.structure=struct;
		update(struct);
	}
	
	private void update(Structure struct) {
		setValues(struct.getEnergiesArray());
	}
	
	public void update() {
		update(this.getStructure());
	}
	
	private Structure getStructure() {
		return structure;
	}

	private void setValues(double[] energiesArray) {
		for (double e : energiesArray) {
			energyMIN = Math.min(energyMIN, e);
			energyMAX = Math.max(energyMAX, e);
		}
		int nInterval = (int) Math.floor((energyMAX-energyMIN)/deltaE)+1;
		dos = new double[nInterval];
		for (double e : energiesArray) {
			int num = (int) Math.floor((e-energyMIN)/deltaE);
			dos[num]++;
		}
		updatePlot();
	}

	private void updatePlot() {
		if (plotFrame==null) {
			plot();
		} else {
			setData();
		}
	}

	private void plot() {
		if (false) return;
		//fenetre de graphes
		plotFrame = new JFrame("Plot");
		setplotframe(plotFrame);
		plotFrame.pack();
		plotFrame.setVisible(true);
	}

	private void setplotframe(JFrame plotFrame) {
		XYSeriesCollection xyDataset = new XYSeriesCollection();
		xydata = new XYSeries("DOS values");
		setData();
		xyDataset.addSeries(xydata);
		NumberAxis xaxis = new NumberAxis("x:E");
		NumberAxis yaxis = new NumberAxis("y:DOS");
		StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator("({1} ; {2})",NumberFormat.getInstance(),NumberFormat.getInstance());
//		StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.LINES, ttg, null);
		XYSplineRenderer renderer = new XYSplineRenderer();
		XYPlot plot = new XYPlot(xyDataset, xaxis, yaxis, renderer);
		ChartPanel chartpanel = new ChartPanel(new JFreeChart("DOS", plot));
//		chartpanel.setPreferredSize(new Dimension(600, 400));
		plotFrame.add(chartpanel);
	}

	private void setData() {
		xydata.clear();
		for (int i=0; i<dos.length; i++) {
			xydata.add(i, dos[i]);
		}
	}




}
