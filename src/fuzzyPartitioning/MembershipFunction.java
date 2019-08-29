package fuzzyPartitioning;

import java.util.ArrayList;

public class MembershipFunction {
	public ArrayList <Integer> useNum;
	public ArrayList <ArrayList<Double>> point;
	public ArrayList <ArrayList<Boolean>> isUse;

	public MembershipFunction(){
		this.point = new ArrayList <ArrayList<Double>> ();
		this.isUse = new ArrayList <ArrayList<Boolean>> ();
		this.useNum = new ArrayList <Integer> ();
	}

	public MembershipFunction(MembershipFunction MF){
		this.point = new ArrayList <ArrayList<Double>> ();
		this.isUse = new ArrayList <ArrayList<Boolean>> ();
		this.useNum = new ArrayList <Integer> ();
		for (int i = 0; i < MF.point.size(); ++i){
			ArrayList<Double> temp1 = new ArrayList<Double> ();
			ArrayList<Boolean> temp2 = new ArrayList<Boolean> ();
			for (int j = 0; j < MF.point.get(i).size(); ++j){
				temp1.add(MF.point.get(i).get(j));
				temp2.add(MF.isUse.get(i).get(j));
			}
			this.point.add(temp1);
			this.isUse.add(temp2);
			this.useNum.add(MF.useNum.get(i));
		}
	}

	public void isNotUse(int att, int i, boolean isUse){
		this.isUse.get(att).set(i, isUse);
	}

	public void setUseNum(int attr) {
		int count = 0;
		for (int j = 0; j < isUse.get(attr).size(); ++j){
			if (isUse.get(attr).get(j)){
				count++;
			}
		}
		this.useNum.set(attr, count);
	}

	public int getUseNum(int attr) {
		return useNum.get(attr);
	}

	public double getUseNum() {
		int count = 0;
		for (int i = 0; i < isUse.size(); ++i){
			count += useNum.get(i);
		}
		return count;
	}

	public void mutation(int sign, int attr, int size) {
		if (sign == 1) {
			double max = 0.0;
			int flag = 0;
			for (int i = 1; i < size - 1; ++i){
				if (!isUse.get(attr).get(i)) {
					int k = 0, l = 0;
					while (i - 1 - k >= 0 && !isUse.get(attr).get(i - 1 - k)){
						k++;
						if (i - 1 - k < 0)	break;
					}
					while (i + 1 + l < size && !isUse.get(attr).get(i + 1 + l)){
						l++;
						if (i + 1 + l >= size)	break;
					}
					double dis = point.get(attr).get(i + 1 + l) - point.get(attr).get(i - 1 - k);
					if (max < dis) {
						max = dis;
						flag = i;
					}
					else if (max == dis) {
						double dis1 = (point.get(attr).get(i + 1 + l) - point.get(attr).get(i)) - (point.get(attr).get(i) - point.get(attr).get(i - 1 - k));
						double dis2 = (point.get(attr).get(i + 1 + l) - point.get(attr).get(flag)) - (point.get(attr).get(flag) - point.get(attr).get(i - 1 - k));
						if (Math.abs(dis1) < Math.abs(dis2))	flag = i;
					}
				}
			}
			if (max> 0.0)	isNotUse(attr, flag, true);
		}
		else {
			double min = Double.MAX_VALUE;
			int flag = 0;
			for (int i = 1; i < size - 1; ++i){
				if (isUse.get(attr).get(i)) {
					int k = 0, l = 0;
					while (i - 1 - k >= 0 && !isUse.get(attr).get(i - 1 - k)){
						k++;
						if (i - 1 - k < 0)	break;
					}
					while (i + 1 + l < size && !isUse.get(attr).get(i + 1 + l)){
						l++;
						if (i + 1 + l >= size)	break;
					}
					double dis = point.get(attr).get(i + 1 + l) - point.get(attr).get(i - 1 - k);
					if (min > dis) {
						min = dis;
						flag = i;
					}
				}
			}
			if (min < Double.MAX_VALUE)	isNotUse(attr, flag, false);
		}
		setUseNum(attr);
	}
}
