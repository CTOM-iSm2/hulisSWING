package org.ctom.hulis.files.gaussian.test;

import org.ctom.hulis.files.SectionReader;
import org.ctom.hulis.files.gaussian.NRTSTRreader;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.HuckelAtomException;

public class TestNRTSTRreader extends NRTSTRreader {

	public TestNRTSTRreader(Mesomery mesomery, SectionReader sectionReader) {
		super(mesomery, sectionReader);
	}
	
	@Override
	protected void setRadR(HuckelAtom a, int npair) throws HuckelAtomException {
		a.setRadR(npair);
	}

	
	
}
