import java.io.File;
import java.util.ArrayList;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.DropdownList;
import controlP5.Slider;
import controlP5.Textarea;
import controlP5.Textfield;
import jfml.term.FuzzyTermType;
import processing.core.PApplet;
import processing.core.PFont;

public class GUI_main {

	//****************************************
	Main main;
	ControlP5 cp5;

	//GUI Area
	int width;
	int height;

	int protMarginX;
	int protMarginY;
	int protWidth;
	int protHeight;
	int originX;
	int originY;

	int toolOriginX;
	int toolOriginY;
	int toolWidth;
	int toolHeight;

	int[] colors;
	PFont font;

	float[] x;
	int h = 500;
	KnowledgeBase kb;
	Classifier classifier;

	ArrayList<DataSetInfo> datasets = new ArrayList<DataSetInfo>();
	ArrayList<String> datasetNames = new ArrayList<String>();
	DropdownList datasetList;
	int currentDataSet = 0;
	int datasetNum = 0;

	int K = 5;

	//Tools
	Slider[] sliders = new Slider[4];
	float[] sliderValues = new float[4];
	Textfield[] numbers = new Textfield[4];
	Textfield fileName;
	Textfield xmlName;

	int paramNum;

	String nn = System.getProperty("line.separator");
	Textarea textArea;
	String information = "> Welcome!" + nn;

	DropdownList shapeList;
	int currentShapeType = 0;

	DropdownList variableList;
	int currentVariable = 0;

	DropdownList dimList;
	int currentDim = 0;

	//****************************************
	public GUI_main(Main _main) {
		this.main = _main;
		this.width = _main.width;
		this.height = _main.height;
		this.setup();
	}

	public void setup() {
		cp5 = main.cp5;
//		font = main.createFont("Arial-Bold", 12);
//		font = main.createFont("Arial Bold", 12);
    font = main.loadFont("Arial-BoldMT-12.vlw");
		x = new float[h + 1];
		for(int i = 0; i < h; i++) {
			x[i] = (float)i / (float)h;
		}
		x[h] = 1f;
		this.coordinateSetup();
		this.colorSetup();
		this.toolSetup();
		this.initKB();
		this.paramNum = kb.getVariable(currentDim).getTerm(currentVariable).getParams().length;
		defaultParams();
		makeTools();

		//TODO 消す Default Dataset
		String datasetFile = "dataset/example_newthyroid_tra.dat";
		this.fileName.setText(datasetFile);
//		loadDataset(newthyroid);
	}

	public void initKB() {
		this.kb = new KnowledgeBase();
		this.kb.init();
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < kb.getShapeName().size(); i++) {
			list.add(kb.getShapeName(i));
		}
		this.shapeList.setItems(list);

	}

	public void coordinateSetup() {
		protMarginX = 100;
		protMarginY = 125;
		protWidth = 600;
		protHeight = 450;
		originX = protMarginX;
		originY = height - protMarginY;

		toolOriginX = protWidth + 2*protMarginX;
		toolOriginY = 0;
		toolWidth = 500;
		toolHeight = height;

	}

	public void colorSetup() {
		int colorMax = 10;
		colors = new int[colorMax];
	    this.colors[0] = main.color(255, 204, 255);
	    this.colors[1] = main.color(102, 255, 153);
	    this.colors[2] = main.color(102, 255, 255);
	    this.colors[3] = main.color(244, 164, 96);
	    this.colors[4] = main.color(147, 112, 219);
	    this.colors[5] = main.color(255, 255, 0);
	    this.colors[6] = main.color(34, 139, 34);
	    this.colors[7] = main.color(255, 0, 0);
	    this.colors[8] = main.color(0, 0, 255);
	    this.colors[9] = main.color(119, 136, 153);
	}

	public void toolSetup() {
		//Information Area
		textArea =
		cp5.addTextarea("Information")
			.setPosition(toolOriginX + 52, toolOriginY + 107)
			.setSize(400 - 4, 96)
			.setColorBackground(main.color(250))
			.setColor(main.color(0))
			.setFont(font);

		//Button for Add Variable
		cp5.addButton("Add")
			.setPosition(toolOriginX + 50, toolOriginY + 220)
			.setSize(50, 30)
			.setLabel("Add");

		//Button for Delete Variable
		cp5.addButton("Delete")
			.setPosition(toolOriginX + 125, toolOriginY + 220)
			.setSize(50, 30)
			.setLabel("Del");

		//Button for Input XML file
		cp5.addButton("Input")
			.setPosition(toolOriginX + 50, toolOriginY + 260)
			.setSize(50, 40)
			.setLabel("Input");

		//Button for Output XML file
		cp5.addButton("Output")
			.setPosition(toolOriginX + 125, toolOriginY + 260)
			.setSize(50, 40)
			.setLabel("Output");

		//XML file name
		xmlName =
		cp5.addTextfield("xmlName")
			.setPosition(toolOriginX + 200, toolOriginY + 260)
			.setSize(250, 40)
			.setAutoClear(false)
			.setLabel("")
      .setFont(font);

		//Toggle Switch for isShow the Histogram
		cp5.addToggle("isShowHistogram")
			.setLabel("Histogram")
			.setPosition(toolOriginX + 40, toolOriginY + 335)
			.setSize(70, 20)
			.setMode(ControlP5.SWITCH)
			.setLabelVisible(false);

		//Dataset Button
		cp5.addButton("dataset")
			.setPosition(toolOriginX + 125, toolOriginY + 315)
			.setSize(50, 40)
			.setLabel("Read");

		//File Name
		fileName =
		cp5.addTextfield("fileName")
			.setPosition(toolOriginX + 200, toolOriginY + 315)
			.setSize(250, 40)
			.setAutoClear(false)
			.setLabel("")
      .setFont(font);

		//List for Dataset
		datasetList =
		cp5.addDropdownList("DatasetList")
			.setPosition(toolOriginX + 300, toolOriginY + 410)
			.setSize(150, 80)
			.setLabel("Dataset")
			.setBarHeight(30)
			.setItemHeight(20)
			.setDefaultValue(0)
			.close();

		//Fuzzy Partitioning Button
		cp5.addButton("partition")
			.setPosition(toolOriginX + 200, toolOriginY + 220)
			.setSize(50, 30)
			.setLabel("Partition");

		//Button for Classify
		cp5.addButton("Classify")
			.setPosition(toolOriginX + 275, toolOriginY + 220)
			.setSize(50, 30)
			.setLabel("Classify");

		//Button for Reasoning
		cp5.addButton("Reasoning")
			.setPosition(toolOriginX + 350, toolOriginY + 220)
			.setSize(50, 30)
			.setLabel("Reasoning");

		//List for TermType
		this.shapeList =
		cp5.addDropdownList("TermTypeList")
			.setPosition(toolOriginX + 350, toolOriginY + 50)
			.setSize(100, 160)
			.setLabel("Term")
			.setBarHeight(40)
			.setItemHeight(20)
			.setDefaultValue(0)
      .setFont(font)
			.close();

		//List for Variable
		this.variableList =
		cp5.addDropdownList("VariableList")
			.setPosition(toolOriginX + 200, toolOriginY + 50)
			.setSize(100, 160)
			.setLabel("Variable")
			.setBarHeight(40)
			.setItemHeight(20)
			.setDefaultValue(0)
      .setFont(font)
			.close();

		//List for Dimension
		this.dimList =
		cp5.addDropdownList("DimList")
			.setPosition(toolOriginX + 50, toolOriginY + 50)
			.setSize(100, 160)
			.setLabel("Attribute")
			.setBarHeight(40)
			.setItemHeight(20)
			.setDefaultValue(0)
      .setFont(font)
			.close();

	}

	//****************************************
	public void draw() {
		main.background(200);
		//main.background(255);//TODO

		main.rectMode(PApplet.CORNER);

		//Prot Area
		main.stroke(0);
		main.fill(255);
		main.rect(originX, height - originY, protWidth, protHeight);
		//main.strokeWeight(4);//TODO
		//main.line(originX, originY, originX, originY - protHeight);
		//main.line(originX, originY, originX + protWidth, originY);
		//main.line(originX + protWidth, originY, originX + protWidth, originY - protHeight);


		//Tool Area
		main.fill(255);
		main.rect(toolOriginX, toolOriginY, toolWidth, height);

		//Status
		drawStatus();

		//Draw Ruler
		drawRuler();

		//Draw Histogram
		if(main.isShowHistogram && datasets.size() != 0) {
			drawHistogram();
			drawPatternNum();
			drawLegend();
		}

		//Membership Function
		drawMembershipFunction();

		//Information
		drawInformation();

	}

	public void controlEvent(ControlEvent event) {
		//DataSet Button
		if(event.isFrom("dataset")) {
			String file = fileName.getText();
			loadDataset(file);
		}

		//Dataset List
		else if(event.isFrom("DatasetList")) {
			int n = (int)event.getController().getValue();
			currentDataSet = n;
			currentDim = 0;
		}

		//XML Input Button
		else if(event.isFrom("Input")) {
			String file = xmlName.getText();
			inputXML(file);
		}

		//XML Output Button
		else if(event.isFrom("Output")) {
			String file = xmlName.getText();
			outputXML(file);
		}

		//Fuzzy Partition Button
		else if(event.isFrom("partition")) {
			if(datasets.get(currentDataSet).getPattern().size() == 0) {
				print("The file doesn't exist.");
				return;
			}

			double F = 1.0;	//TODO to be given
			kb.initInhomogeneous2( FuzzyPartitioning.startPartition(datasets.get(currentDataSet), K, F) );
			currentShapeType = kb.getVariable(currentDim).getCurrentShapeType();

			//update K
			if(K != 5) {
				K++;
			} else {
				K = 2;
			}
			print("partitioning...");
//			kb.initInhomogeneous( new FuzzyPartitioning().startPartition(datasets.get(currentDataSet)));
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0; i < kb.getVariable(currentDim).getTermNum(); i++) {
				list.add(kb.getVariable(currentDim).getTerm(i).getName());
			}
			variableList.setItems(list);
			setNowParams();

			print("Fuzzy partitioning is successed.");
			print("");
		}

		//Ndim List
		else if(event.isFrom("DimList")) {
			int n = (int)event.getController().getValue();
			currentDim = n;
			currentVariable = kb.getVariable(currentDim).getCurrentTerm();
			currentShapeType = kb.getVariable(currentDim).getCurrentShapeType();
			setNowParams();
			variableList.setItems(kb.getVariable(currentDim).getTermsNameList());
			shapeList.setItems(kb.getVariable(currentDim).getShapeName());
		}

		//Variable List
		else if(event.isFrom("VariableList")) {
			int n = (int)event.getController().getValue();
			currentVariable = n;
			currentShapeType = kb.getVariable(currentDim).getCurrentShapeType();
			setNowParams();
			shapeList.setItems(kb.getVariable(currentDim).getShapeName());
		}

		//TermTypeType List
		else if(event.isFrom("TermTypeList")) {
			int n = (int)event.getController().getValue();
			currentShapeType = n;
			kb.getVariable(currentDim).getTerm(currentVariable).setShapeName(kb.getShapeName(n));
			kb.getVariable(currentDim).getTerm(currentVariable).setShapeType(kb.getShapeType(n).intValue());
			kb.getVariable(currentDim).setCurrentShapeType(n);
			defaultParams();
			makeTools();
		}

		//Add Term Button
		else if(event.isFrom("Add")) {
			kb.getVariable(currentDim).addTermButton();
			currentVariable = kb.getVariable(currentDim).getCurrentTerm();
			currentShapeType = kb.getVariable(currentDim).getCurrentShapeType();
			kb.make();
			kb.getVariable(currentDim).getTerm(currentVariable).calcY(x);
			setNowParams();
			variableList.setItems(kb.getVariable(currentDim).getTermsNameList());
		}

		//Delete Term Button
		else if(event.isFrom("Delete")) {
			kb.getVariable(currentDim).setCurrentTerm(currentVariable);
			kb.getVariable(currentDim).deleteTermButton();
			currentVariable = kb.getVariable(currentDim).getCurrentTerm();
			currentShapeType = kb.getVariable(currentDim).getTerm(currentVariable).getShapeType();
			variableList.setItems(kb.getVariable(currentDim).getTermsNameList());
			kb.make();
			setNowParams();
		}

		//Classify Button
		else if(event.isFrom("Classify")) {
			classify();
		}

		//Reasoning Button
		else if(event.isFrom("Reasoning")) {
			reasoning();
		}

	}

	public void classify() {
		if(datasets.get(currentDataSet).getPattern().size() == 0) {
			print("Dataset not found.");
			return;
		}

		StaticClassifierFunc.setStaticKB(kb);

		classifier = new Classifier(kb.getNdim());
		print("making classifier...");
		classifier.makeClassifier(kb, datasets.get(currentDataSet));

		print("classifing...");
		double acc = classifier.calcErrorRate(datasets.get(currentDataSet));

		print("finished!");
		print("    " + "File: " + datasetNames.get(currentDataSet));
		print("    " + "Error: " + String.format("%.3f", acc*100) + "%");
		print("    " + "Rules: " + String.valueOf(classifier.getRuleNum()));
		print("");

	}

	public void reasoning() {
		if(datasets.get(currentDataSet).getPattern().size() == 0) {
			print("Dataset not found.");
			return;
		}

		if(classifier == null) {
			print("Classifier is not made.");
			return;
		}

		StaticClassifierFunc.setStaticKB(kb);

		print("reasoning...");
		double acc = classifier.calcErrorRate(datasets.get(currentDataSet));

		print("finished!");
		print("    " + "File: " + datasetNames.get(currentDataSet));
		print("    " + "Error: " + String.format("%.3f", acc*100) + "%");
		print("    " + "Rules: " + String.valueOf(classifier.getRuleNum()));
		print("");
	}

	public void makeParamNum() {
		if(kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_singletonShape) {
			paramNum = 1;
		}
		else if(kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_gaussianShape ||
				kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_leftLinearShape ||
				kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_rightLinearShape ||
				kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_rectangularShape ||
				kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_leftGaussianShape ||
				kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_rightGaussianShape ||
				kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_zShape ||
				kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_sShape) {
			paramNum = 2;
		}
		else if(kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_triangularShape) {
			paramNum = 3;
		}
		else if(kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_trapezoidShape) {
			paramNum = 4;
		}
	}

	public void slider0(float value) {
		float sliderFuncValue;
		if( kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_gaussianShape ||
			kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_leftGaussianShape ||
			kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_rightGaussianShape) {
			sliderFuncValue = value;
		} else {
			if(value > sliderValues[1]) {
				sliderFuncValue = sliderValues[1];
				cp5.getController("slider0").setValue(sliderFuncValue);
			} else {
				sliderFuncValue = value;
			}
		}
		sliderValues[0] = sliderFuncValue;
		numbers[0].setText(String.format("%.2f", sliderValues[0]));

		makeParamNum();
		float[] params = new float[paramNum];
		for(int i = 0; i < paramNum; i++) {
			params[i] = sliderValues[i];
		}
		kb.getVariable(currentDim).getTerm(currentVariable).setParams(params);
		kb.getVariable(currentDim).getTerm(currentVariable).make();
		kb.getVariable(currentDim).getTerm(currentVariable).calcY(x);
	}

	public void slider1(float value) {
		float sliderFuncValue;
		if( kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_gaussianShape ||
			kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_leftGaussianShape ||
			kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_rightGaussianShape) {
			sliderFuncValue = value;
		} else {
			if(value < sliderValues[0]) {
				sliderFuncValue = sliderValues[0];
				cp5.getController("slider1").setValue(sliderFuncValue);
			} else if(value > sliderValues[2]) {
				sliderFuncValue = sliderValues[2];
				cp5.getController("slider1").setValue(sliderFuncValue);
			} else {
				sliderFuncValue = value;
			}
		}

		sliderValues[1] = sliderFuncValue;
		numbers[1].setText(String.format("%.2f", sliderValues[1]));

		makeParamNum();
		float[] params = new float[paramNum];
		for(int i = 0; i < paramNum; i++) {
			params[i] = sliderValues[i];
		}
		kb.getVariable(currentDim).getTerm(currentVariable).setParams(params);
		kb.getVariable(currentDim).getTerm(currentVariable).make();
		kb.getVariable(currentDim).getTerm(currentVariable).calcY(x);
	}

	public void slider2(float value) {
		float sliderFuncValue;
		if( kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_gaussianShape ||
			kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_leftGaussianShape ||
			kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_rightGaussianShape) {
			sliderFuncValue = value;
		} else {
			if(value < sliderValues[1]) {
				sliderFuncValue = sliderValues[1];
				cp5.getController("slider2").setValue(sliderFuncValue);
			} else if(value > sliderValues[3]) {
				sliderFuncValue = sliderValues[3];
				cp5.getController("slider2").setValue(sliderFuncValue);
			} else {
				sliderFuncValue = value;
			}
		}

		sliderValues[2] = sliderFuncValue;
		numbers[2].setText(String.format("%.2f", sliderValues[2]));

		makeParamNum();
		float[] params = new float[paramNum];
		for(int i = 0; i < paramNum; i++) {
			params[i] = sliderValues[i];
		}
		kb.getVariable(currentDim).getTerm(currentVariable).setParams(params);
		kb.getVariable(currentDim).getTerm(currentVariable).make();
		kb.getVariable(currentDim).getTerm(currentVariable).calcY(x);
	}

	public void slider3(float value) {
		float sliderFuncValue;
		if( kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_gaussianShape ||
			kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_leftGaussianShape ||
			kb.getVariable(currentDim).getTerm(currentVariable).getShapeType() == FuzzyTermType.TYPE_rightGaussianShape) {
			sliderFuncValue = value;
		} else {
			if(value < sliderValues[2]) {
				sliderFuncValue = sliderValues[2];
				cp5.getController("slider3").setValue(sliderFuncValue);
			} else {
				sliderFuncValue = value;
			}
		}
		sliderValues[3] = sliderFuncValue;
		numbers[3].setText(String.format("%.2f", sliderValues[3]));

		makeParamNum();
		float[] params = new float[paramNum];
		for(int i = 0; i < paramNum; i++) {
			params[i] = sliderValues[i];
		}
		kb.getVariable(currentDim).getTerm(currentVariable).setParams(params);
		kb.getVariable(currentDim).getTerm(currentVariable).make();
		kb.getVariable(currentDim).getTerm(currentVariable).calcY(x);
	}

	public void number0(String text) {
		float value = Float.parseFloat(text);
		sliders[0].setValue(value);
		slider0(value);
	}

	public void number1(String text) {
		float value = Float.parseFloat(text);
		sliders[1].setValue(value);
		slider1(value);
	}

	public void number2(String text) {
		float value = Float.parseFloat(text);
		sliders[2].setValue(value);
		slider2(value);
	}

	public void number3(String text) {
		float value = Float.parseFloat(text);
		sliders[3].setValue(value);
		slider3(value);
	}

	public void fileName(String text) {
		loadDataset(text);
	}

	public void makeTools() {
		for(int i = 0; i < 4; i++) {
			cp5.remove("slider" + String.valueOf(i));
			cp5.remove("number" + String.valueOf(i));
		}

		for(int i = 0; i < paramNum; i++) {
			toolParam(i);
		}
	}

	public void toolParam(int idx) {
		sliders[idx] =
		cp5.addSlider(	"slider" + String.valueOf(idx),
						kb.getDomainLeft(), kb.getDomainRight(),
						sliderValues[idx],
						toolOriginX + 50, toolOriginY + 410 + (80*idx),
						150, 30)
            .setFont(font);

		numbers[idx] =
		cp5.addTextfield("number" + String.valueOf(idx),
						 toolOriginX + 225, toolOriginY + 410 + (80*idx),
						 50, 30)
						.setAutoClear(false)
						.setText(String.format("%.2f", sliderValues[idx]))
            .setFont(font);
	}

	public void setNowParams() {
		makeParamNum();
		for(int i = 0; i < paramNum; i++) {
			sliderValues[i] = kb.getVariable(currentDim).getTerm(currentVariable).getParams(i);
		}
		kb.make();
		makeTools();
	}


	public void defaultParams() {
		makeParamNum();
		switch(paramNum) {
		case 1:
			sliderValues[0] = 0.5f;
			sliderValues[1] = kb.getVariable(currentDim).getDomainRight();
			sliderValues[2] = kb.getVariable(currentDim).getDomainRight();
			sliderValues[3] = kb.getVariable(currentDim).getDomainRight();
			break;
		case 2:
			if( currentShapeType == 1 ||
				currentShapeType == 6 ||
				currentShapeType == 7) {	//be gaussian type
				sliderValues[0] = 0.5f;
				sliderValues[1] = 0.1f;
			} else {
				sliderValues[0] = 0.3f;
				sliderValues[1] = 0.7f;
			}
			sliderValues[2] = kb.getVariable(currentDim).getDomainRight();
			sliderValues[3] = kb.getVariable(currentDim).getDomainRight();
			break;
		case 3:
			sliderValues[0] = 0f;
			sliderValues[1] = 0.5f;
			sliderValues[2] = 1f;
			sliderValues[3] = kb.getVariable(currentDim).getDomainRight();
			break;
		case 4:
			sliderValues[0] = 0f;
			sliderValues[1] = 0.3f;
			sliderValues[2] = 0.7f;
			sliderValues[3] = 1f;
			break;
		}

		float[] params = new float[paramNum];
		for(int i = 0; i < paramNum; i++) {
			params[i] = sliderValues[i];
		}
		kb.getVariable(currentDim).getTerm(currentVariable).setParams(params);

		kb.make();
	}

	public void drawStatus() {

		//Current Status
		main.textAlign(ControlP5.LEFT, ControlP5.BASELINE);
		main.textSize(20);
		main.fill(0);
		main.text(String.valueOf(currentDim), toolOriginX + 50, toolOriginY + 40);	//Ndim
		main.text(kb.getVariable(currentDim).getTerm(currentVariable).getName(), toolOriginX + 200, toolOriginY + 40);	//Variable
		main.text(kb.getVariable(currentDim).getTerm(currentVariable).getShapeName(), toolOriginX + 350, toolOriginY + 40);

		//Parameter Values
		this.paramNum = kb.getVariable(currentDim).getTerm(currentVariable).getParams().length;
		main.textAlign(ControlP5.LEFT, ControlP5.BASELINE);
		main.textSize(24);
		main.fill(0);
		for(int i = 0; i < paramNum; i++) {
			main.text("Param" + String.valueOf(i+1) + " = "
						+ String.format("%.2f", kb.getVariable(currentDim).getTerm(currentVariable).getParams()[i]),
						toolOriginX + 20, toolOriginY + 400 + i*80);
		}

		//Histogram Switch
		main.textAlign(ControlP5.CENTER, ControlP5.BASELINE);
		main.textSize(14);
		main.fill(0);
		main.text("show", toolOriginX + 40, toolOriginY + 335);
		main.text("hide", toolOriginX + 110, toolOriginY + 335);


	}

	public void drawPatternNum() {
		if(datasets.get(currentDataSet).getPattern().size() == 0) {
			return;
		}
		int h = protWidth / datasets.get(currentDataSet).getHistogramDivide();
		main.textAlign(ControlP5.CENTER, ControlP5.BASELINE);
		main.textSize(24);
		main.fill(0);
		main.text("Patterns", originX + protWidth/2, originY - protHeight - 70);

		main.textAlign(ControlP5.LEFT, ControlP5.BOTTOM);
		main.textSize(16);
		main.fill(0);
		for(int i = 0; i < datasets.get(currentDataSet).getHistogramDivide(); i++) {
			main.translate(originX + (h*i) + h/2, originY - protHeight - 20);
			main.rotate(-(ControlP5.PI/4));
			main.text(String.valueOf(datasets.get(currentDataSet).getPatternNumAllClass()[currentDim][i]), 0, 0);
			main.rotate(ControlP5.PI/4);
			main.translate(-(originX + (h*i) + h/2), -(originY - protHeight - 20));
		}

	}

	public void drawHistogram() {
		if(datasets.get(currentDataSet).getPattern().size() == 0) {
			return;
		}
		int h = protWidth / datasets.get(currentDataSet).getHistogramDivide();
		int stroke = 1;

		main.rectMode(PApplet.CORNERS);
		for(int i = 0; i < datasets.get(currentDataSet).getHistogramDivide(); i++) {
			float bias = 0;
			for(int c = 0; c < datasets.get(currentDataSet).getCnum(); c++) {
				main.fill(colors[c]);
				main.stroke(0);
				main.strokeWeight(stroke);
				main.rect(	originX + (i*h), originY - bias,
							originX + (i+1)*h, originY - bias - datasets.get(currentDataSet).getPatternRate()[currentDim][c][i] * protHeight);
				bias += datasets.get(currentDataSet).getPatternRate()[currentDim][c][i] * protHeight;
			}
		}
		main.stroke(0);
	}

	public void drawLegend() {
		int x = 320;
		int y = 450;
		for(int c = 0; c < datasets.get(currentDataSet).getCnum(); c++) {
			main.rectMode(PApplet.CORNER);
			main.fill(colors[c]);
			main.strokeWeight(2);
			main.rect(toolOriginX + x, toolOriginY + y + c*25, 25, 16);
		}
		main.textSize(16);
		main.fill(0);
		for(int c = 0; c < datasets.get(currentDataSet).getCnum(); c++) {
			main.textAlign(ControlP5.LEFT, ControlP5.CENTER);
			main.text("Class " + String.valueOf(c), toolOriginX + x + 40, toolOriginY + y + 6 + c*25);
		}
	}

	public void drawMembershipFunction() {
		for(int i = 0; i < kb.getVariable(currentDim).getTermNum(); i++) {
			if(kb.getVariable(currentDim).getTerm(i).getShapeType() == FuzzyTermType.TYPE_singletonShape) {
				kb.getVariable(currentDim).getTerm(i).calcY(x);
				//color
				if(currentVariable == i) {
					main.fill(ControlP5.RED);
				} else {
					main.fill(0);
				}
				main.noStroke();
				for(int j = 0; j < h+1; j++) {
					main.ellipse((x[j]*protWidth) + originX,
							originY - (kb.getVariable(currentDim).getTerm(i).getY(j) * protHeight),
							6, 6);
				}
			}
			else {
				kb.getVariable(currentDim).getTerm(i).calcY(x);
				//color
				//main.stroke(0);//TODO
				if(currentVariable == i) {
					main.stroke(ControlP5.RED);
				} else {
					main.stroke(0);
				}
				main.strokeWeight(4);
				for(int j = 0; j < h; j++) {
					main.line((x[j] * protWidth) + originX,
						 originY - (kb.getVariable(currentDim).getTerm(i).getY(j) * protHeight),
						 (x[j+1] * protWidth) + originX,
						 originY - (kb.getVariable(currentDim).getTerm(i).getY(j+1) * protHeight));
				}
				main.strokeWeight(2);
			}
		}
	}

	public void drawRuler() {
		float maxX = 1f;
		float minX = 0f;
		float maxY = 1f;
		float minY = 0f;

		int divideX = 10;
		int divideY = 5;

		int hX = protWidth / divideX;
		int hY = protHeight / divideY;

		main.fill(0);
		main.textAlign(ControlP5.CENTER, ControlP5.CENTER);
		main.textSize(16);
//		main.textFont(main.createFont("Times New Roman", 16));

		//X ticks
		for(int i = 0; i < divideX; i++) {
			float num = (float)i * (maxX - minX) / divideX;
			main.text(String.format("%.1f", num), originX + (hX * i), originY + 15);
		}
		main.text(String.format("%.1f", maxX), originX + protWidth, originY + 15);

		//Y ticks
		for(int i = 0; i < divideY; i++) {
			float num = (float)i * (maxY - minY) / divideY;
			main.text(String.format("%.1f", num), originX - 20, originY - (hY * i));
		}
		main.text(String.format("%.1f", maxY), originX - 20, originY - protHeight);

		//X Label
		main.textAlign(ControlP5.CENTER, ControlP5.CENTER);
		main.textSize(24);
		main.text("Attribute Value", originX + protWidth/2, originY + 45);

		//Y Label
		main.translate(originX - 55, originY - protHeight/2);
		main.rotate(-(ControlP5.PI/2));
		main.text("Membership Value", 0, 0);
		main.rotate(ControlP5.PI/2);
		main.translate(-(originX - 55), -(originY - protHeight/2));
	}

	public void drawInformation() {
		main.rectMode(PApplet.CORNER);
		main.stroke(0);
		main.fill(250);
		main.rect(toolOriginX + 50, toolOriginY + 105, 400, 100);
		textArea.setText(information);
	}

	public void loadDataset(String fileName) {
		//Check File Exists
		File file = new File(fileName);
		if(!file.exists()) {
			print("The file doesn't exist.");
			return;
		}

		currentDim = 0;
		currentVariable = 0;
		currentShapeType = 0;

		currentDataSet = datasetNum;
		datasetNum++;
		datasets.add(new DataSetInfo());
		DataLoader.inputFile(datasets.get(currentDataSet), fileName);

		this.fileName.setText(fileName);
		String[] str = fileName.split("/");
		datasetNames.add( str[str.length - 1] );
		datasetList.setItems(datasetNames);

		datasets.get(currentDataSet) .calcHistogram();
		kb.initDataset(datasets.get(currentDataSet));

		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < datasets.get(currentDataSet).getNdim(); i++) {
			list.add(String.valueOf(i));
		}
		dimList.setItems(list);

		defaultParams();
		makeTools();

		print("File Readed.");
		print("    " + "file: " + fileName);
		print("    " + "Patterns: " + String.valueOf(datasets.get(currentDataSet).getDataSize()));
		print("    " + "Attributes: " + String.valueOf(datasets.get(currentDataSet).getNdim()));
		print("    " + "Classes: " + String.valueOf(datasets.get(currentDataSet).getCnum()));
		print("");
	}

	public void inputXML(String fileName) {
		//Check File Exists
		File file = new File(fileName);
		if(!file.exists()) {
			print("The file doesn't exist.");
			return;
		}
		kb.inputFML(fileName);
		print("XML file is loaded.");
		print("    " + "file: " + fileName);
		print("");
	}

	public void outputXML(String fileName) {
		kb.outputFML(fileName);
		print("XML file is written out.");
		print("    " + "file: " + fileName);
		print("");
	}

	public void print(String log) {
		information += "> " + log + nn;
		textArea.setText(information);
	}

}
