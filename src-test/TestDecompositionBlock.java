import org.ctom.hulis.util.VectorssHuckel;
import org.ctom.hulis.util.io.HuckelIO;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class TestDecompositionBlock {
	public static void main(String args[]) {
		double[][] hamiltonien={{0.00,1.00,1.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00},
				{1.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,0.00,0.00},
				{1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00},
				{0.00,1.00,0.00,0.00,1.00,0.00,0.00,1.00,0.00,0.00},
				{0.00,0.00,0.00,1.00,0.00,1.00,1.00,0.00,0.00,0.00},
				{0.00,0.00,1.00,0.00,1.00,0.00,0.00,0.00,0.00,0.00},
				{0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00},
				{0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00,0.00},
				{0.00,0.00,0.00,0.00,0.00,0.00,0.00,1.00,0.00,1.00},
				{0.00,0.00,0.00,0.00,0.00,0.00,1.00,0.00,1.00,0.00}};
		Matrix mHamiltionein= new Matrix(hamiltonien);
		EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecomposition(mHamiltionein);
		HuckelIO.PrintVector(VectorssHuckel.sort(eigenvalueDecomposition.getRealEigenvalues()), "calcOrbitalsEnergies petit bloc");
		HuckelIO.PrintMatrix(new Matrix(VectorssHuckel.sort(eigenvalueDecomposition.getV().getArray())), "orbitales petit bloc");
		

	}

}
