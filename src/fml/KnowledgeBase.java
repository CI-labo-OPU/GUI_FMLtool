package fml;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBElement;

import jfml.FuzzyInferenceSystem;
import jfml.JFML;
import jfml.knowledgebase.KnowledgeBaseType;
import jfml.knowledgebase.variable.FuzzyVariableType;
import jfml.knowledgebase.variable.KnowledgeBaseVariable;
import jfml.term.FuzzyTermType;
import methods.DataSetInfo;

public class KnowledgeBase {
	//****************************************

	//FML
	float domainLeft = 0f;
	float domainRight = 1f;
	KnowledgeBaseType kb;
	Variable[] variables;

	ArrayList<Integer> shapeType = new ArrayList<Integer>();
	ArrayList<String> shapeName = new ArrayList<String>();

	int Ndim = 1;	//Default

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
	public KnowledgeBase() {
		this.listSetup();
	}

	public void listSetup() {
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

		shapeName.add( "Triangular" );		//0
		shapeName.add( "Gaussian" );		//1
		shapeName.add( "Trapezoid" );		//2
		shapeName.add( "LeftLinear" );		//3
		shapeName.add( "RightLinear" );		//4
		shapeName.add( "Rectangular" );		//5
		shapeName.add( "LeftGaussian" );	//6
		shapeName.add( "RightGaussian" );	//7
		shapeName.add( "Z-Shape" );			//8
		shapeName.add( "S-Shape" );			//9
		shapeName.add( "Singleton" );		//10
	}


	//****************************************
	public void init() {
		kb = new KnowledgeBaseType();
		variables = new Variable[Ndim];
		for(int i = 0; i < Ndim; i++) {
			variables[i] = new Variable(String.valueOf(i), domainLeft, domainRight);
			variables[i].init();
			kb.addVariable(variables[i].getVariable());
		}
	}

	public void initDataset(DataSetInfo dataset) {
		kb = null;
		System.gc();
		Ndim = dataset.getNdim();
		kb = new KnowledgeBaseType();
		variables = new Variable[Ndim];
		for(int i = 0; i < Ndim; i++) {
			variables[i] = new Variable(String.valueOf(i), domainLeft, domainRight);
			variables[i].init();
			kb.addVariable(variables[i].getVariable());
		}
	}

	public void initInhomogeneous(ArrayList<ArrayList<Double>> points) {
		kb = new KnowledgeBaseType();
		variables = new Variable[Ndim];

		for(int dim_i = 0; dim_i < Ndim; dim_i++) {
			variables[dim_i] = new Variable(String.valueOf(dim_i), domainLeft, domainRight);
			variables[dim_i].initInhomogeneous(points.get(dim_i));
			kb.addVariable(variables[dim_i].getVariable());
		}

	}

	public void initInhomogeneous2(ArrayList<ArrayList<double[]>> trapezoids) {
		kb = new KnowledgeBaseType();
		variables = new Variable[Ndim];

		for(int dim_i = 0; dim_i < Ndim; dim_i++) {
			variables[dim_i] = new Variable(String.valueOf(dim_i), domainLeft, domainRight);
			variables[dim_i].initInhomogeneous2(trapezoids.get(dim_i));
			kb.addVariable(variables[dim_i].getVariable());
		}
	}


	public void make() {
		kb = new KnowledgeBaseType();
		for(int i = 0; i < Ndim; i++) {
			variables[i].make();
			kb.addVariable(variables[i].getVariable());
		}
	}

	public double calcMembershipValue(int attribute, int fuzzySet, double x) {
		float value = 0f;
		value = variables[attribute].getTerm(fuzzySet).getTerm().getMembershipValue((float)x);
		return (double)value;
	}

	public void inputFML(String fileName) {
		File fml = new File(fileName);
		FuzzyInferenceSystem fs = JFML.load(fml);

		ArrayList<KnowledgeBaseVariable> kbvar = new ArrayList<KnowledgeBaseVariable>();
		this.kb = fs.getKnowledgeBase();
		for(Object o : kb.getVariables()) {
			if(((JAXBElement) o).getValue() instanceof KnowledgeBaseVariable){
				kbvar.add( (KnowledgeBaseVariable) ((JAXBElement) o).getValue() );
			}
		}

		Ndim = kbvar.size();
		domainLeft = ((FuzzyVariableType)kbvar.get(0)).getDomainleft();
		domainRight = ((FuzzyVariableType)kbvar.get(0)).getDomainright();

		variables = new Variable[Ndim];
		for(int dim_i = 0; dim_i < Ndim; dim_i++) {
			variables[dim_i] = new Variable(String.valueOf(dim_i), domainLeft, domainRight);
			variables[dim_i].inputFML(kb.getVariable(String.valueOf(dim_i)));
			kb.addVariable(variables[dim_i].getVariable());
		}

	}

	public void outputFML(String fileName) {
		FuzzyInferenceSystem fs = new FuzzyInferenceSystem();
		make();
		fs.setKnowledgeBase(this.kb);
		File fml = new File(fileName);
		JFML.writeFSTtoXML(fs,  fml);

	}


	public Variable getVariable(int idx) {
		return this.variables[idx];
	}

	public Integer getShapeType(int idx) {
		return this.shapeType.get(idx);
	}

	public ArrayList<String> getShapeName(){
		return this.shapeName;
	}

	public String getShapeName(int idx) {
		return this.shapeName.get(idx);
	}

	public float getDomainLeft() {
		return this.domainLeft;
	}

	public float getDomainRight() {
		return this.domainRight;
	}

	public int getNdim() {
		return this.Ndim;
	}


}
