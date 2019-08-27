import java.util.ArrayList;

public class FuzzyPartitioning2 {

	public int K = 0;	//分割数

	public FuzzyPartitioning2() {}

	public ArrayList<ArrayList<double[]>> startPartition(DataSetInfo tra, int K, double F){

		ArrayList<ArrayList<double[]>> trapezoids = new ArrayList<ArrayList<double[]>>();

		for(int dim_i = 0; dim_i < tra.getNdim(); dim_i++) {
			this.K = K;

			//1. 入力パターンをdim_i属性についてソート
			double[] x = new double[tra.getDataSize()];
			double[] index = new double[tra.getDataSize()];
			for(int p = 0; p < tra.getDataSize(); p++) {
				x[p] = tra.getPattern(p).getDimValue(dim_i);	//Deep Copy
				index[p] = p;
			}
			StaticGeneralFunc.multiQuickSort(x, index);

			//2. (K - 1)個の分割点を獲得する (optimal splitting method)
			ArrayList<Double> dividePoints = optimalSplitting(tra, x, index);

			//3. 分割点をファジィ化する
			trapezoids.add(makeTrapezoids(dividePoints, F));
		}

		return trapezoids;
	}

	public ArrayList<Double> optimalSplitting(DataSetInfo tra, double[] x, double[] index){
		ArrayList<Double> dividePoints = new ArrayList<Double>();

		//1. 分割点の候補(= クラスが切り替わる点)
		ArrayList<Double> candidate = new ArrayList<Double>();
		double point;
		for(int p = 1; p < tra.getDataSize(); p++) {
			if(x[p-1] == x[p]) {
				point = x[p-1];
			} else {
				point = (x[p-1] + x[p])/2;
			}
			if(tra.getPattern((int)index[p-1]).getConClass() != tra.getPattern((int)index[p]).getConClass()) {
				if(!candidate.contains(point)) {
					if(candidate.size() > 0) {
						double a = Math.abs(candidate.get(candidate.size() - 1) - point);
						double threshould = 1.0 / 50.0;	//しきい値を設けないと，組合せ数が膨大となるため
						if( a > threshould) {
							candidate.add(point);
						}
					} else {
						candidate.add(point);
					}
				}
			}
		}

		//2.. 分割点の調整
		if(candidate.size() < K-1) {
			this.K = candidate.size() + 1;
		}

		//3. 分割点候補から(K - 1)個選択する全組み合わせのクラスエントロピーを計算する．
		int combination = combinationNum(candidate.size(), K - 1);
		double[] entropy = new double[combination];
		double[][] pointSet = new double[combination][K - 1];
		ArrayList<ArrayList<Double>> pointSetList = combinationList(candidate, K - 1);
		for(int i = 0; i < combination; i++) {
			for(int j = 0; j < K - 1; j++) {
				pointSet[i][j] = pointSetList.get(i).get(j);
			}
			entropy[i] = calcClassEntropy(tra, pointSet[i], x, index);
		}

		//4. 最小のエントロピーを得る組み合わせを探す．
		double min = entropy[0];
		int minIndex = 0;
		for(int i = 1; i < combination; i++) {
			if(entropy[i] < min) {
				min = entropy[i];
				minIndex = i;
			}
		}

		//5. 3.で最小のエントロピーを得る組み合わせをreturnする．
		for(int i = 0; i < K - 1; i++) {
			dividePoints.add(pointSet[minIndex][i]);
		}

		return dividePoints;
	}

	/* Reference Paper:
	 * H. Ishibuchi and Y. Nojima,
	 * "Comparison between Fuzzy and Interval Partitions in Evolutionary Multiobjective Design of Rule-Based Classification Systems"
	 */
	public double calcClassEntropy(DataSetInfo tra, double[] combination, double[] x, double[] index) {
		double entropy = 0;

		double D = tra.getDataSize();
		double[] Dj = new double[combination.length + 1];
		double[][] Djh = new double[combination.length + 1][tra.getCnum()];

		for(int p = 0; p < tra.getDataSize(); p++) {
			for(int j = 0; j < combination.length; j++) {
				if(x[p] == combination[j]) {
					Dj[j] += 0.5;
					Dj[j+1] += 0.5;

					Djh[j][tra.getPattern((int)index[p]).getConClass()] += 0.5;
					Djh[j+1][tra.getPattern((int)index[p]).getConClass()] += 0.5;
					break;
				}
				else if(x[p] < combination[j]) {
					Dj[j] += 1;

					Djh[j][tra.getPattern((int)index[p]).getConClass()] += 1;
					break;
				}
			}
			if(x[p] > combination[combination.length - 1]) {
				Dj[combination.length] += 1;

				Djh[combination.length][tra.getPattern((int)index[p]).getConClass()] += 1;
			}
		}

		double sum = 0;
		for(int j = 0; j < Dj.length; j++) {
			double subsum = 0;
			for(int h = 0; h < tra.getCnum(); h++) {
				if(Djh[j][h] == 0) {
					subsum += 0;
				}
				else {
					subsum += (Djh[j][h] / Dj[j]) * StaticGeneralFunc.log( (Djh[j][h] / Dj[j]) , 2.0);
				}
			}
			sum += (Dj[j] / D) * subsum;
		}
		entropy = -sum;
		return entropy;
	}

	public int combinationNum(int n, int r) {
		int num = 1;
		for(int i = 1; i <= r; i++) {
			num = num * (n - i + 1) / i;
		}
		return num;
	}

	public static ArrayList<ArrayList<Double>> combinationList(ArrayList<Double> candidate, int r) {
		if(candidate.size() < r || candidate.size() <= 0 || r <= 0) {
			ArrayList<ArrayList<Double>> empty = new ArrayList<ArrayList<Double>>();
			empty.add(new ArrayList<Double>());
			return empty;
		}

		ArrayList<ArrayList<Double>> combination = new ArrayList<>();
		for (int i = 0; i <= candidate.size() - r; i++) {
			Double picked = candidate.get(i);
			ArrayList<Double> copy = new ArrayList<>(candidate);
			for(int j = 0; j <= i; j++) {
				copy.remove(0);
			}
			ArrayList<Double> rest = new ArrayList<>(copy);

			// 再帰呼び出しし、得られたリストの全ての先頭に取り出したものを結合する
			//TODO Processing用
			ArrayList<ArrayList<Double>> list = combinationList(rest, r - 1);
			for(int j = 0; j < list.size(); j++) {
				list.get(j).add(0, picked);
			}
			combination.addAll(list);

			//TODO Processingではラムダ式を使用できないため．
//			combination.addAll(make(rest, r - 1).stream().map(list -> {
//				list.add(0, picked);
//				return list;
//			}).collect(Collectors.toList()));
		}
		return combination;
		}

	public ArrayList<double[]> makeTrapezoids(ArrayList<Double> points, double F){
		ArrayList<double[]> trapezoids = new ArrayList<double[]>();

		ArrayList<Double> newPoints = new ArrayList<Double>();

		//1. 両端を追加
		points.add(0, 0.0);
		points.add(1.0);

		double l;
		double point;
		double u;
		//2. 各分割点をファジィ化（両端を含まない）
		for(int i = 1; i < points.size() - 1; i++) {
			l = points.get(i - 1);
			point = points.get(i);
			u = points.get(i + 1);
			newPoints.addAll(fuzzify(l, u, point, F));
		}

		newPoints.add(0, 0.0);
		newPoints.add(0, 0.0);
		newPoints.add(1.0);
		newPoints.add(1.0);

		//3. 新しい点集合からK個の台形型を作成する．
		int index = 0;
		for(int i = 0; i < K; i ++) {
			trapezoids.add(new double[4]);
			for(int j = 0; j < 4; j++) {
				trapezoids.get(i)[j] = newPoints.get(index + j);
			}
			index += 2;
		}

		return trapezoids;
	}

	//[l, u]内の点pointを重なり度合いFでファジィ化する．
	public ArrayList<Double> fuzzify(double l, double u, double point, double F) {
		ArrayList<Double> ans = new ArrayList<Double>();

		if((point-l) < (u-point)) {
			u = point + (point-l);
		} else {
			l = point - (u-point);
		}

		double ac_F0 = point;
		double ac_F1 = l;
		double bd_F0 = point;
		double bd_F1 = u;

		double ac_F = ac_F0 + (ac_F1 - ac_F0)*F;
		double bd_F = bd_F0 + (bd_F1 - bd_F0)*F;

		ans.add(ac_F);
		ans.add(bd_F);

		return ans;
	}
}
