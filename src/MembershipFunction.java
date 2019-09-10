import java.io.File;
import java.util.ArrayList;

import jfml.FuzzyInferenceSystem;
import jfml.JFML;
import jfml.term.FuzzyTermType;

public class MembershipFunction {

	FuzzyInferenceSystem fs;
	ArrayList<String> variableName = new ArrayList<String>();
	ArrayList<ArrayList<String>> termName = new ArrayList<ArrayList<String>>();

	int attributeNum;
	int[] termNum;

	public MembershipFunction(String fileName) {
		this.init(fileName);
	}

	public void init(String fileName) {
		File fml = new File(fileName);
		fs = JFML.load(fml);

		for(int i = 0; i < fs.getVariables().size(); i++) {
			variableName.add(fs.getVariables().get(i).getName());
			termName.add(new ArrayList<String>());

			for(int j = 0; j < fs.getVariable(variableName.get(i)).getTerms().size(); j++) {
				FuzzyTermType term = (FuzzyTermType)fs.getVariable(variableName.get(i)).getTerms().get(j);
				String name = term.getName();
				termName.get(i).add(name);
			}
		}

		attributeNum = variableName.size();
		termNum = new int[attributeNum];
		for(int i = 0; i < attributeNum; i++) {
			termNum[i] = termName.get(i).size();
		}
	}

	public float getMembershipValue(int attribute, int fuzzySet, float x) {
		return fs.getVariable(variableName.get(attribute)).getTerm(termName.get(attribute).get(fuzzySet)).getMembershipValue(x);
	}

	public int getAttributeNum() {
		return this.attributeNum;
	}

	public int getTermNum(int index) {
		return this.termNum[index];
	}

	public ArrayList<String> getVariableName(){
		return this.variableName;
	}

	public ArrayList<ArrayList<String>> getTermName(){
		return this.termName;
	}
}
