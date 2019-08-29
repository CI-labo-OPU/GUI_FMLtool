package fml;

import jfml.term.FuzzyTermType;

public class Term {
	//****************************************
	String name;
	String shapeName;
	int shapeType;

	FuzzyTermType term;
	float[] params;

	float[] y;

	//****************************************
	public Term(String name, int shapeType, float[] params) {
		this.name = name;
		this.shapeType = shapeType;
		this.params = new float[params.length];
		for(int i = 0; i < params.length; i++) {
			this.params[i] = params[i];
		}
		make();
	}


	//****************************************

	public void make() {
		term = new FuzzyTermType(name, shapeType, params);
	}

	public void calcY(float[] x) {
		y = new float[x.length];
		if(shapeType == FuzzyTermType.TYPE_singletonShape) {
			Boolean flg = true;
			y[0] = term.getMembershipValue(x[0]);
			for(int i = 1; i < y.length; i++) {
				if(params[0] <= x[i] && flg) {
					y[i] = 1f;
					flg = false;
				} else {
					y[i] = 0f;
				}
			}
		}
		else {
			for(int i = 0; i < y.length; i++) {
				y[i] = term.getMembershipValue(x[i]);
			}
		}
	}

	public void setShapeName(String name) {
		this.shapeName = name;
	}
	public String getShapeName() {
		return this.shapeName;
	}

	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
	}

	public int getShapeType() {
		return this.shapeType;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void setParam(int idx, float param) {
		this.params[idx] = param;
	}
	public float getParams(int idx) {
		return this.params[idx];
	}
	//Deep copy
	public void setParams(float[] params) {
		this.params = new float[params.length];
		for(int i = 0; i < params.length; i++) {
			this.params[i] = params[i];
		}
	}
	public float[] getParams() {
		return this.params;
	}

	public FuzzyTermType getTerm() {
		return this.term;
	}

	public float[] getY() {
		return this.y;
	}
	public float getY(int idx) {
		return this.y[idx];
	}
}
