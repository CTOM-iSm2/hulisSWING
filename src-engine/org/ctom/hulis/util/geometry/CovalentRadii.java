package org.ctom.hulis.util.geometry;

public enum CovalentRadii {
	// Table from Wikipedia double bond covalent radius said to come from 
	//P. Pyykkö, M. Atsumi Chemistry: A European Journal (2009) 15 12770-12779.
	// 
	H("H", 0.320), He("He",0.46), Li("Li", 1.24), Be("Be", 0.90), B("B", 0.78),
	C("C",
			0.67), N("N", 0.600),O("O", 0.570),F("F", 0.590), Ne("Ne", 0.67), 
			 Na("Na",1.550), Mg("Mg", 1.390),Al("Al", 1.130), Si(
			"Si", 1.070), P("P", 1.02), S("S", 0.94),Cl("Cl", 0.95),  Ar("Ar", 0.96),    Me("C",0.750);

	private double covRad;
	private String name;

	private CovalentRadii(String name, double covrad) {
		this.name = name;
		this.covRad = covrad;
	}

	public double getCovRad() {
		return this.covRad;
	}

	public String getName() {
		return this.name;
	}

}
