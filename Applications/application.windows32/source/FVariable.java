import java.util.ArrayList;

import jfml.knowledgebase.KnowledgeBaseType;
import jfml.knowledgebase.variable.FuzzyVariableType;
import jfml.term.FuzzyTermType;

public class FVariable {

	String name;

	KnowledgeBaseType kb;
	float domainLeft;
	float domainRight;

	float param1 = 0.5f;
	float param2 = 0.5f;
	float param3 = 0.5f;
	float param4 = 0.5f;

	FuzzyVariableType inputVariable;
	FuzzyTermType fuzzyTerm;
	float[] y;

	int currentTermType = 0;
	ArrayList<Integer> shapeType = new ArrayList<Integer>();
	ArrayList<String> termName = new ArrayList<String>();


	public FVariable(String _name, float _domainLeft, float _domainRight) {
		this.name = _name;
		this.domainLeft = _domainLeft;
		this.domainRight = _domainRight;

		shapeType.add( FuzzyTermType.TYPE_triangularShape );
		shapeType.add( FuzzyTermType.TYPE_gaussianShape );
		shapeType.add( FuzzyTermType.TYPE_trapezoidShape );
		shapeType.add( FuzzyTermType.TYPE_leftLinearShape );
		shapeType.add( FuzzyTermType.TYPE_rightLinearShape );
		shapeType.add( FuzzyTermType.TYPE_rectangularShape );
		shapeType.add( FuzzyTermType.TYPE_leftGaussianShape );
		shapeType.add( FuzzyTermType.TYPE_rightGaussianShape );
		shapeType.add( FuzzyTermType.TYPE_zShape );
		shapeType.add( FuzzyTermType.TYPE_sShape );
		shapeType.add( FuzzyTermType.TYPE_singletonShape );
	}

	public void make() {

		float[] params;
		if(this.currentTermType == 10) {
			params = new float[] {this.param1};
		}
		else if(this.currentTermType == 1 ||
				this.currentTermType == 3 ||
				this.currentTermType == 4 ||
				this.currentTermType == 5 ||
				this.currentTermType == 6 ||
				this.currentTermType == 7 ||
				this.currentTermType == 8 ||
				this.currentTermType == 9) {
			params = new float[] {this.param1, this.param2};
		}
		else if(this.currentTermType == 0) {
			params = new float[] {this.param1, this.param2, this.param3};
		}
		else {
			params = new float[] {this.param1, this.param2, this.param3, this.param4};
		}

		this.kb = new KnowledgeBaseType();
		this.inputVariable = new FuzzyVariableType(this.name, this.domainLeft, this.domainRight);
		this.fuzzyTerm = new FuzzyTermType( this.termName.get(this.currentTermType),
											this.shapeType.get(this.currentTermType),
											params);
		this.inputVariable.addFuzzyTerm(this.fuzzyTerm);
		this.kb.addVariable(this.inputVariable);

		params = null;
		System.gc();
	}

	public float[] calcY(float[] _x) {
		this.y = new float[_x.length];

		if(this.termName.get(this.currentTermType) == "Singleton") {
			Boolean flg = true;
			this.y[0] = kb.getVariable(this.name).getTerm(this.termName.get(this.currentTermType)).getMembershipValue(_x[0]);
			for(int i = 1; i < this.y.length; i++) {
				if(this.param1 <= _x[i] && flg) {
					this.y[i] = 1f;
					flg = false;
				} else {
					this.y[i] = 0;
				}
			}
		} else {
			for(int i = 0; i < this.y.length; i++) {
				this.y[i] = kb.getVariable(this.name).getTerm(this.termName.get(this.currentTermType)).getMembershipValue(_x[i]);
			}
		}
		return this.y;
	}


	//GET and SET methods
	public void setName(String _name) {
		this.name = _name;
	}
	public String getName() {
		return this.name;
	}
	public void setCurrentTermType(int _currentTermType) {
		this.currentTermType = _currentTermType;
	}
	public int getCurrentTermType() {
		return this.currentTermType;
	}
	public void setDomainLeft(float _domainLeft) {
		this.domainLeft = _domainLeft;
	}
	public float getDomainLeft() {
		return this.domainLeft;
	}
	public void setDomainRight(float _domainRight) {
		this.domainRight = _domainRight;
	}
	public float getDomainRight() {
		return this.domainRight;
	}
	public void setTermName(ArrayList<String> _termName) {
		this.termName = _termName;
	}
	public ArrayList<String> getTermNameList(){
		return this.termName;
	}
	public void setParam1(float _p1) {
		this.param1 = _p1;
	}
	public float getParam1() {
		return this.param1;
	}
	public void setParam2(float _p2) {
		this.param2 = _p2;
	}
	public float getParam2() {
		return this.param2;
	}
	public void setParam3(float _p3) {
		this.param3 = _p3;
	}
	public float getParam3() {
		return this.param3;
	}
	public void setParam4(float _p4) {
		this.param4 = _p4;
	}
	public float getParam4() {
		return this.param4;
	}
	public float getParam(int _idx) {
		switch(_idx) {
		case 1:
			return this.param1;
		case 2:
			return this.param2;
		case 3:
			return this.param3;
		case 4:
			return this.param4;
		}
		return this.param1;
	}
	public float[] getY() {
		return this.y;
	}
	public float getY(int _idx) {
		return this.y[_idx];
	}


}
