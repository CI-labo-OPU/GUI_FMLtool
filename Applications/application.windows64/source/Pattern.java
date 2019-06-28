import java.io.Serializable;

public class Pattern {

	//Fields ****************************************************

	double[] x;
	int conClass;

	// **********************************************************

	//Constructor ***********************************************
	public Pattern() {}

	public Pattern(double[] _line) {
		int Ndim = _line.length - 1;
		this.x = _line;
		this.conClass = (int)_line[Ndim];
	}
	// **********************************************************

	//Methods ***************************************************

	public double getDimValue(int i) {
		return this.x[i];
	}

	public int getConClass() {
		return this.conClass;
	}
	// **********************************************************
}
