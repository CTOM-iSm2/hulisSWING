package util.gaussian;

import org.ctom.hulis.files.gaussian.WriteGaussian;
import org.ctom.hulis.files.gaussian.WriteGaussianException;
import org.ctom.hulis.huckel.Mesomery;

public class IOWriteGaussian extends WriteGaussian {

	
	public IOWriteGaussian(Mesomery m) throws WriteGaussianException  {
		super(m);
	}

}
