import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FuzzyPartitioning {
	public MembershipFunction MF = new MembershipFunction ();
	public double numOfTotalPoints = 0;

	public FuzzyPartitioning(){}


	public ArrayList<ArrayList<Double>> startPartition(DataSetInfo tra){
		ArrayList<ArrayList<Double>> points = new ArrayList<ArrayList<Double>>();

		for(int attri = 0; attri < tra.getNdim(); ++attri) {
			ArrayList<Double> Coodinate = new ArrayList <Double> ();
			ArrayList<Boolean> use = new ArrayList <Boolean> ();
			double[] x = new double [tra.getDataSize()];
			double[] index = new double [tra.getDataSize()];
			for (int p = 0; p < tra.getDataSize(); ++p){
				x[p] = tra.getPattern(p).getDimValue(attri);
				index[p] = p;
			}
			StaticGeneralFunc.multiQuickSort(x, index);
			double u = x[x.length - 1], l = x[0];
			makeFuzzy(tra, true, attri, l, x, u, index, Coodinate);
			Coodinate.add(0, l);
			Coodinate.add(u);
			Collections.sort(Coodinate);
			for (int i = 0; i < Coodinate.size(); ++i){
				use.add(true);
			}
			points.add(Coodinate);
			MF.point.add(Coodinate);
			MF.isUse.add(use);
//			MF.addWFE(attri, tra);
			numOfTotalPoints += Coodinate.size();
		}

		return points;
	}

	void makeFuzzy(DataSetInfo tra, boolean first, int attri, double low, double[] x, double up, double[] index, ArrayList<Double> Coodinate){
		if (x.length >= 0.02 * tra.getDataSize()){
			double min = Double.MAX_VALUE;
			double xmin = -1.0;
			int minp = -1;
			for (int p = 1; p < x.length - 1; ++p){
				double[][] points = {{low, low, low, x[p]}, {low, x[p],x[p], up}, {x[p], up, up, up}};
				double WFEnt = WFEnt(tra, attri, points, index);
				if (min >= WFEnt){
					min = WFEnt;
					xmin = x[p];
					minp = p;
				}
			}
			double[] divIndex1 = Arrays.copyOfRange(index, 0, minp + 1);
			double[] divx1 = Arrays.copyOfRange(x, 0, minp + 1);
			double[] divIndex2 =Arrays.copyOfRange(index, minp + 1, index.length);
			double[] divx2 = Arrays.copyOfRange(x, minp + 1, x.length);
			Coodinate.add(xmin);
			if ((FGain(tra, first, attri, low, up, min, index) >= (StaticGeneralFunc.log((double)index.length - 1.0, 2.0) + delta(tra, first, attri, low, up, xmin, index)) / (double)index.length) && xmin > -1.0){
				makeFuzzy(tra, false, attri, low, divx1, xmin, divIndex1, Coodinate);
				makeFuzzy(tra, false, attri, xmin, divx2, up, divIndex2, Coodinate);
			}
		}
	}

	double WFEnt(DataSetInfo tra, int attri, double[][] points, double[] index){
		double sum = 0;
		for (int i = 0; i < points.length; ++i){
			double cardinality = cardinality(tra, attri, points[i], index);
			sum += (cardinality / index.length) * FEnt(tra, attri, cardinality, points[i], index);
		}
		return sum;
	}

	double cardinality(DataSetInfo tra, int attri, double[] point, double[] index){
		double sum = 0;
		for (int p = 0; p < index.length; ++p){
			sum += StaticFuzzyFunc.calcMenbership(point, tra.getPattern((int)index[p]).getDimValue(attri));
		}
		return sum;
	}

	double FEnt(DataSetInfo tra, int attri, double cardinality, double[] point, double[] index){
		double SUM = 0;
		for (int h = 0; h < tra.getCnum(); ++h){
			double sum = 0;
			for (int p = 0; p < index.length; ++p){
				if (h == tra.getPattern((int)index[p]).getConClass()){
					sum += StaticFuzzyFunc.calcMenbership(point, tra.getPattern((int)index[p]).getDimValue(attri));
				}
			}
			if (sum > 0.0) SUM -= (sum / cardinality) * StaticGeneralFunc.log((sum / cardinality), 2.0);
		}
		return SUM;
	}

	double FGain(DataSetInfo tra, boolean first, int attri, double low, double up, double min, double[] index){
		if (first){
			double[][] before = {{low, low, up, up}};
			return WFEnt(tra, attri, before, index) - min;
		}
		else{
			double[][] before = {{low, low, low, up}, {low, up, up, up}};
			return WFEnt(tra, attri, before, index) - min;
		}
	}

	double delta(DataSetInfo tra, boolean first, int attri, double low, double up, double xmin, double[] index){
		if (first){
			double[][] before = {{low, low, up, up}};
			double[][] points = {{low, low, low, xmin}, {low, xmin, xmin, up}, {xmin, up, up, up}};
			int[][] cla = new int [3][tra.getCnum()];
			for (int p = 0; p < index.length; ++p){
				if (tra.getPattern((int)index[p]).getDimValue(attri) <= xmin)	cla[0][tra.getPattern((int)index[p]).getConClass()]++;
				else cla[2][tra.getPattern((int)index[p]).getConClass()]++;
				cla[1][tra.getPattern((int)index[p]).getConClass()]++;
			}
			double[] M = new double [3];
			for (int h = 0; h < tra.getCnum(); ++h){
				for (int i = 0; i < 3; ++i){
					if (cla[i][h] > 0) M[i]++;
				}
			}
			double[] sum = new double [2];
			for (int t = 0; t < before.length; ++t){
				double cardinality = cardinality(tra, attri, before[t], index);
				sum[0] += M[1] * FEnt(tra, attri, cardinality, before[t], index);
			}
			for (int i = 0; i < points.length; ++i){
				double cardinality = cardinality(tra, attri, points[i], index);
				sum[1] += M[i] * FEnt(tra, attri, cardinality, points[i], index);
			}
			return StaticGeneralFunc.log(Math.pow(3.0, M[1]) - 2.0, 2.0) - (sum[0] - sum[1]) ;
		}
		else{
			double[][] before = {{low, low, low, up}, {low, up, up, up}};
			double[][] points = {{low, low, low, xmin}, {low, xmin, xmin, up}, {xmin, up, up, up}};
			int[][] cla = new int [3][tra.getCnum()];
			for (int p = 0; p < index.length; ++p){
				if (tra.getPattern((int)index[p]).getDimValue(attri) <= xmin)	cla[0][tra.getPattern((int)index[p]).getConClass()]++;
				else cla[2][tra.getPattern((int)index[p]).getConClass()]++;
				cla[1][tra.getPattern((int)index[p]).getConClass()]++;
			}
			double[] M = new double [3];
			for (int h = 0; h < tra.getCnum(); ++h){
				for (int i = 0; i < 3; ++i){
					if (cla[i][h] > 0) M[i]++;
				}
			}
			double[] sum = new double [2];
			for (int t = 0; t < before.length; ++t){
				double cardinality = cardinality(tra, attri, before[t], index);
				sum[0] += M[1] * FEnt(tra, attri, cardinality, before[t], index);
			}
			for (int i = 0; i < points.length; ++i){
				double cardinality = cardinality(tra, attri, points[i], index);
				sum[1] += M[i] * FEnt(tra, attri, cardinality, points[i], index);
			}
			return StaticGeneralFunc.log(Math.pow(3.0, M[1]) - 2.0, 2.0) - (sum[0] - sum[1]) ;
		}
	}
}
