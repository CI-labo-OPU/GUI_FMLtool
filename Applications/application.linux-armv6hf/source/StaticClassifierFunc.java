import java.util.ArrayList;

public class StaticClassifierFunc {
	//****************************************
	public static KnowledgeBase kb;

	//****************************************
	public StaticClassifierFunc() {}

	//****************************************
	public static void setStaticKB(KnowledgeBase _kb) {
		kb = _kb;
	}

	//Membership Value
	public static double calcMembership(int attribute, int fuzzySet, double x) {
		return kb.calcMembershipValue(attribute, fuzzySet, x);
	}

	//
	public static double memberMulPure(Pattern line, int[] rule) {
		double ans = 1.0;
		int Ndim = rule.length;
		for(int i = 0; i < Ndim; i++) {
			ans *= calcMembership(i, rule[i], line.getDimValue(i));
			if(ans == 0) {
				return ans;
			}
		}
		return ans;
	}

	//Rule Trust
	public static double[] calcTrust(DataSetInfo dataSetInfo, int[] rule, int Cnum) {
		ArrayList<Double> part = new ArrayList<Double>();	//Sum of membership value for each class
		double partSum;

		for(int c = 0; c < Cnum; c++) {
			final int CLASSNUM = c;
			partSum = 0.0;
			for(int p = 0; p < dataSetInfo.getDataSize(); p++) {
				if(dataSetInfo.getPattern(p).getConClass() == CLASSNUM) {
					partSum += memberMulPure(dataSetInfo.getPattern(p), rule);
				}
			}
			part.add(partSum);
		}

		double all = 0.0;
		for(int c = 0; c < part.size(); c++) {
			all += part.get(c);
		}

		double[] trust = new double[Cnum];
		if(all != 0.0) {
			for(int c = 0; c < Cnum; c++) {
				trust[c] = part.get(c) / all;
			}
		}

		return trust;
	}

	//Decision Class Label of Consequent Part
	public static int calcConclusion(double trust[], int Cnum) {
		int conClass = 0;
		double max = 0.0;
		for(int c = 0; c < Cnum; c++) {
			if(max < trust[c]) {
				max = trust[c];
				conClass = c;
			} else if( max == trust[c] ) {
				conClass = -1;
			}
		}
		return conClass;
	}

	//Rule Weight
	public static double calcCf(int conClass, double[] trust, int Cnum) {
		double cf = 0.0;
		if(conClass == -1 || trust[conClass] <= 0.5) {
			cf = 0;
		} else {
			double sum = 0.0;
			for(int c = 0; c < Cnum; c++) {
				sum += trust[c];
			}
			cf = trust[conClass] - (sum - trust[conClass]);
		}
		return cf;
	}

}
