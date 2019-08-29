package fuzzyPartitioning;

import java.util.ArrayList;
import java.util.Arrays;

import methods.Pattern;

public class StaticFuzzyFunc {

	public StaticFuzzyFunc(){}
	static boolean homo = true;

	/************************************************************************************************************/
	//適合度
	public static double menberMulPure(Pattern line, int rule[], MembershipFunction MF){

		double ans = 1.0;
		int Ndim = rule.length;
		for(int i=0; i<Ndim; i++){
			try{
				ans *= calcMenbership(makeCoodinate(MF, i, rule[i]), line.getDimValue(i));
			}catch (Exception e) {
				//calcMenbership(rule[i], 0.0);
			}
		}

		return ans;
	}

	//メンバシップ値(inhomogeneous)
	public static double calcMenbership(double[] point, double x){
		double[] dontcare = {0, 0, 1, 1};
		if (Arrays.equals(point, dontcare)) {
			return 1.0;
		}
		else if (x >= 0.0) {
			if (x < point[0]) return 0.0;
			if (point[3] < x) return 0.0;
			if (point[0] <= x && x <= point[1]){
				if (point[0] == point[1]) return 1.0;
				else return (x - point[0]) / (point[1] - point[0]);
			}
			if (point[2] <= x && x <= point[3]){
				if (point[2] == point[3]) return 1.0;
				else return (point[3] - x) / (point[3] - point[2]);
			}
			if (point[1] <= x && x <= point[2]){
				return 1.0;
			}
			return 1.0;
		}
		else {
			if (point[0] == x) {
				return 1.0;
			}
			else {
				return 0.0;
			}
		}
	}

	public static double[] makeCoodinate(MembershipFunction MF, int attri, int num){
		if (num < 0) {
			double[] categorical = {num, num, num, num};
			return categorical;
		}
		int size = MF.isUse.get(attri).size();
		ArrayList<Double> Coodinates = MF.point.get(attri);
		double[] dontcare = {0.0, 0.0, 1.0, 1.0};
		if (num == 0){
			return dontcare;
		}
		int k = 0;
		int l = 0;
		while (num - 2 - k >= 0 && !MF.isUse.get(attri).get(num - 2 - k)){
			k++;
			if (num - 2 - k < 0)	break;
		}
		while (num + l < size && !MF.isUse.get(attri).get(num + l)){
			l++;
			if (num + l >= size)	break;
		}
		if (!MF.isUse.get(attri).get(num - 1)){
			if (num - 1 == 0)	num += l + 1;
			else if (num == size)	num -= k + 1;
			else{
				if (num - 2 - k < 0)	num = 1;
				else if (num + l == size)	num = size;
				else	num += (Math.abs(MF.point.get(attri).get(num + l) - MF.point.get(attri).get(num - 1)) < Math.abs(MF.point.get(attri).get(num - 1) - MF.point.get(attri).get(num - 2 - k)))?(l + 1):(-k - 1);
			}
			k = 0; l = 0;
			while (num - 2 - k >= 0 && !MF.isUse.get(attri).get(num - 2 - k)){
				k++;
				if (num - 2 - k < 0)	break;
			}
			while (num + l < size && !MF.isUse.get(attri).get(num + l)){
				l++;
				if (num + l >= size)	break;
			}
		}
		if (num - 2 - k < 0 && num + l >= size){
			return dontcare;
		}
		else if (num - 2 - k >= 0 && num + l >= size){
			double[] points = new double [4];
			for (int j = 0; j < 4; ++j)	points[j] = Coodinates.get((j < 2)?(num - 1 - (1 + k) * (1 - j)):(size - 1));
			return points;
		}
		else if (num - 2 - k < 0 && num + l < size){
			double[] points = new double [4];
			for (int j = 0; j < 4; ++j)	points[j] = Coodinates.get((j < 2)?(0):(num - 1 + (j - 2) * (1 + l)));
			return points;
		}
		else{
			double[] points = {Coodinates.get(num - 2 - k), Coodinates.get(num - 1), Coodinates.get(num - 1), Coodinates.get(num + l)};
			return points;
		}
	}

	/************************************************************************************************************/

}
