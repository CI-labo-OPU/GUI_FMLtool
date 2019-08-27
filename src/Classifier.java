
public class Classifier {
	//****************************************
	int[][] rule;
	int[] conClass;
	double[] cf;
	boolean[] flg;

	int Ndim;
	int ruleNum;

	//****************************************
	public Classifier(int Ndim) {
		this.Ndim = Ndim;
	}

	//****************************************
	public void makeClassifier(KnowledgeBase kb, DataSetInfo Dtra) {
		int[] Fdiv = new int[Ndim];
		ruleNum = 1;
		for(int i = 0; i < Ndim; i++) {
			Fdiv[i] = kb.getVariable(i).getTermNum();
			ruleNum *= Fdiv[i];
		}

		//Antecedent Part
		rule = new int[ruleNum][Ndim];
		for(int i = 0; i < Ndim; i++) {
			int rule_i = 0;
			int repNum = 1;
			int interval = 1;
			int count = 0;
			for(int j = 0; j < i; j++) {
				repNum *= Fdiv[j];
			}
			for(int j = i+1; j < Ndim; j++) {
				interval *= Fdiv[j];
			}
			for(int j = 0; j < repNum; j++) {
				count = 0;
				for(int k = 0; k < Fdiv[i]; k++) {
					for(int l = 0; l < interval; l++) {
						rule[rule_i][i] = count;
						rule_i++;
					}
					count++;
				}
			}
		}

		//Consequent Part
		conClass = new int[ruleNum];
		cf = new double[ruleNum];
		for(int i = 0; i < ruleNum; i++) {
			//1. calculation trust
			double[] trust = StaticClassifierFunc.calcTrust(Dtra, rule[i], Dtra.getCnum());
			//2. decision consequent class
			conClass[i] = StaticClassifierFunc.calcConclusion(trust, Dtra.getCnum());
			//3. calculation rule weight
			cf[i] = StaticClassifierFunc.calcCf(conClass[i], trust, Dtra.getCnum());
		}

		//using flag
		flg = new boolean[ruleNum];
		for(int i = 0; i < ruleNum; i++) {
			if(conClass[i] == -1 || cf[i] <= 0) {
				flg[i] = false;
//				ruleNum--;
			} else {
				flg[i] = true;
			}
		}
	}

	public double calcErrorRate(DataSetInfo dataset) {
		double missPatternNum = 0;
		double error;

		for(int p = 0; p < dataset.getDataSize(); p++) {
			int ans = classify(dataset.getPattern(p));
			if(ans != dataset.getPattern(p).getConClass()) {
				missPatternNum++;
			}
		}

		error = missPatternNum / dataset.getDataSize();
		return error;
	}

	public int classify(Pattern line) {
		int ans;
		int winRuleIdx = 0;

		boolean canClassify = true;
		double maxMul = 0.0;
		for(int i = 0; i < ruleNum; i++) {
//			if(!flg[i]) {
//				continue;
//			}

			double multiValue = cf[i] * StaticClassifierFunc.memberMulPure(line, rule[i]);

			if(maxMul < multiValue) {
				maxMul = multiValue;
				winRuleIdx = i;
				canClassify = true;
			} else if(maxMul == multiValue && conClass[i] != conClass[winRuleIdx]) {
				canClassify = false;
			}
		}
		if(canClassify && maxMul > 0.0) {
			ans = conClass[winRuleIdx];
		} else {
			ans = -1;
		}

		return ans;
	}

	public int getRuleNum() {
		return this.ruleNum;
	}
}
