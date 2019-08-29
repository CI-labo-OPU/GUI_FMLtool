package fml;

import java.util.ArrayList;

import jfml.knowledgebase.variable.FuzzyVariableType;
import jfml.knowledgebase.variable.KnowledgeBaseVariable;
import jfml.term.FuzzyTermType;

public class Variable {
	//****************************************
	String name;
	int currentShapeType = 0;
	ArrayList<Integer> shapeType = new ArrayList<Integer>();
	ArrayList<String> shapeName = new ArrayList<String>();

	//FML
	float domainLeft;
	float domainRight;
	FuzzyVariableType variable;
	ArrayList<Term> terms = new ArrayList<Term>();
	ArrayList<String> termsNameList = new ArrayList<String>();
	int currentTerm = 0;
	int termNum = 1;
	int termNameCount = 1;

	final int triangular = FuzzyTermType.TYPE_triangularShape;		//3
	final int gaussian = FuzzyTermType.TYPE_gaussianShape;			//4
	final int trapezoid = FuzzyTermType.TYPE_trapezoidShape;		//7
	final int leftLinear = FuzzyTermType.TYPE_leftLinearShape;		//1
	final int rightLinear = FuzzyTermType.TYPE_rightLinearShape;	//0
	final int rectangular = FuzzyTermType.TYPE_rectangularShape;	//9
	final int leftGaussian = FuzzyTermType.TYPE_leftGaussianShape;	//6
	final int rightGaussian = FuzzyTermType.TYPE_rightGaussianShape;//5
	final int zShape = FuzzyTermType.TYPE_zShape;					//10
	final int sShape = FuzzyTermType.TYPE_sShape;					//11
	final int singleton = FuzzyTermType.TYPE_singletonShape;		//8

	//****************************************

	public Variable(String name, float domainLeft, float domainRight) {
		this.name = name;
		this.domainLeft = domainLeft;
		this.domainRight = domainRight;

		shapeType.add( triangular );	//3
		shapeType.add( gaussian );		//4
		shapeType.add( trapezoid );		//7
		shapeType.add( leftLinear );	//1
		shapeType.add( rightLinear );	//0
		shapeType.add( rectangular );	//9
		shapeType.add( leftGaussian );	//6
		shapeType.add( rightGaussian );	//5
		shapeType.add( zShape );		//10
		shapeType.add( sShape );		//11
		shapeType.add( singleton );		//8

		shapeName.add( "Triangular" );
		shapeName.add( "Gaussian" );
		shapeName.add( "Trapezoid" );
		shapeName.add( "LeftLinear" );
		shapeName.add( "RightLinear" );
		shapeName.add( "Rectangular" );
		shapeName.add( "LeftGaussian" );
		shapeName.add( "RightGaussian" );
		shapeName.add( "Z-Shape" );
		shapeName.add( "S-Shape" );
		shapeName.add( "Singleton" );
	}

	//****************************************

	public void init() {
		variable = new FuzzyVariableType(name, domainLeft, domainRight);
		float[] params = new float[] {0f, 0.5f, 1f};
		for(int i = 0; i < termNum; i++) {
			terms.add( new Term(String.valueOf(termNameCount++),
								 shapeType.get(currentShapeType),
								 params) );
			terms.get(i).setShapeName(shapeName.get(currentShapeType));
			variable.addFuzzyTerm(terms.get(i).getTerm());
			termsNameList.add(terms.get(i).getName());
		}
	}

	public void initInhomogeneous(ArrayList<Double> points) {
		termNum = points.size();
		termNameCount = 1;
		variable = new FuzzyVariableType(name, domainLeft, domainRight);

		float[] params = new float[3];
		params[0] = points.get(0).floatValue();
		params[1] = points.get(0).floatValue();
		params[2] = points.get(1).floatValue();
		terms.add( new Term(String.valueOf(termNameCount++),
							FuzzyTermType.TYPE_triangularShape,
							params) );
		terms.get(0).setShapeName(shapeName.get(currentShapeType));
		variable.addFuzzyTerm(terms.get(0).getTerm());
		termsNameList.add(terms.get(0).getName());
		for(int i = 1; i < termNum - 1; i++) {
			params[0] = points.get(i - 1).floatValue();
			params[1] = points.get(i    ).floatValue();
			params[2] = points.get(i + 1).floatValue();
			terms.add( new Term(String.valueOf(termNameCount++),
								FuzzyTermType.TYPE_triangularShape,
								params) );
			terms.get(i).setShapeName(shapeName.get(currentShapeType));
			variable.addFuzzyTerm(terms.get(i).getTerm());
			termsNameList.add(terms.get(i).getName());
		}
		params[0] = points.get(termNum - 2).floatValue();
		params[1] = points.get(termNum - 1).floatValue();
		params[2] = points.get(termNum - 1).floatValue();
		terms.add( new Term(String.valueOf(termNameCount++),
				FuzzyTermType.TYPE_triangularShape,
				params) );
		terms.get(termNum - 1).setShapeName(shapeName.get(currentShapeType));
		variable.addFuzzyTerm(terms.get(termNum - 1).getTerm());
		termsNameList.add(terms.get(termNum - 1).getName());

	}

	public void initInhomogeneous2(ArrayList<double[]> trapezoids) {
		currentShapeType = 2;
		termNum = trapezoids.size();
		termNameCount = 1;
		variable = new FuzzyVariableType(name, domainLeft, domainRight);

		float[] params = new float[4];

		for(int i = 0; i < termNum; i++) {
			for(int j = 0; j < 4; j++) {
				params[j] = (float)trapezoids.get(i)[j];
			}
			terms.add( new Term(String.valueOf(termNameCount++),
								FuzzyTermType.TYPE_trapezoidShape,
								params) );
			terms.get(i).setShapeName(shapeName.get(currentShapeType));
			variable.addFuzzyTerm(terms.get(i).getTerm());
			termsNameList.add(terms.get(i).getName());
		}
	}

	public void inputFML(KnowledgeBaseVariable variable) {
		termNum = ((FuzzyVariableType)variable).getTerms().size();
		termNameCount = 1;
		this.variable = new FuzzyVariableType(name, domainLeft, domainRight);

		float[] params;
		for(int i = 0; i < termNum; i++) {
			FuzzyTermType term = ((FuzzyVariableType)variable).getTerms().get(i);
			this.termsNameList.add(term.getName() + ".");
			termNameCount++;
			params = new float[term.getParam().length];
			for(int j = 0; j < params.length; j++) {
				params[j] = term.getParam()[j];
			}

			//The author edited a code in "FuzzyTerm.java".
			//The author added "getType()" method.
			//2019.08.06. Yuichi Omozaki.
			int termType = term.getType();
			terms.add( new Term(String.valueOf(termsNameList.get(i)),
								termType,
								params));
			terms.get(i).setShapeName(type2shapeName(termType));
			this.variable.addFuzzyTerm(terms.get(i).getTerm());
		}
	}


	public void make() {
		variable = new FuzzyVariableType(name, domainLeft, domainRight);
		for(int i = 0; i < termNum; i++) {
			terms.get(i).make();
			variable.addFuzzyTerm(terms.get(i).getTerm());
		}

	}

	public String type2shapeName(int type) {
		String shapeName = "";
		switch(type) {
		case 0:
			shapeName = "RightLinear";
			break;
		case 1:
			shapeName = "LeftLinear";
			break;
		case 3:
			shapeName = "Triangular";
			break;
		case 4:
			shapeName = "Gaussian";
			break;
		case 5:
			shapeName = "RightGaussian";
			break;
		case 6:
			shapeName = "LeftGaussian";
			break;
		case 7:
			shapeName = "Trapezoid";
			break;
		case 8:
			shapeName = "Singleton";
			break;
		case 9:
			shapeName = "Rectangular";
			break;
		case 10:
			shapeName = "zShape";
			break;
		case 11:
			shapeName = "sShape";
			break;
		}

		return shapeName;
	}

	public void addTermButton() {
		currentTerm = termNum;
		termsNameList.add( String.valueOf(termNameCount) );
		terms.add( new Term(String.valueOf(termNameCount), FuzzyTermType.TYPE_triangularShape, new float[] {0f, 0.5f, 1f}) );
		terms.get(currentTerm).setShapeName(shapeName.get(0));
		terms.get(currentTerm).make();
		termNum++;
		termNameCount++;
	}

	public void deleteTermButton() {
		if(termNum <= 1) {
			return;
		}

		terms.remove(currentTerm);
		termsNameList.remove(currentTerm);
		termNum--;
		if(currentTerm != 0) {
			currentTerm--;
		}
	}

	//GET and SET methods
	public void setName(String _name) {
		this.name = _name;
	}
	public String getName() {
		return this.name;
	}

	public void setCurrentShapeType(int _currentTermType) {
		this.currentShapeType = _currentTermType;
	}
	public int getCurrentShapeType() {
		return this.currentShapeType;
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

	public void setShapeName(ArrayList<String> _shapeName) {
		this.shapeName = _shapeName;
	}
	public ArrayList<String> getShapeName(){
		return this.shapeName;
	}

	public ArrayList<String> getTermsNameList(){
		return this.termsNameList;
	}


	public void setTermNum(int termNum) {
		this.termNum = termNum;
	}
	public int getTermNum() {
		return this.termNum;
	}

	public FuzzyVariableType getVariable() {
		return this.variable;
	}

	public void setCurrentTerm(int idx) {
		this.currentTerm = idx;
	}

	public int getCurrentTerm() {
		return this.currentTerm;
	}

	public Term getTerm(int idx) {
		return this.terms.get(idx);
	}

}
