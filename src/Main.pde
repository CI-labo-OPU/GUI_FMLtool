import controlP5.*;

import jfml.*;
import jfml.jaxb.*;
import jfml.aggregated.*;
import jfml.operator.*;
import jfml.defuzzifier.*;
import jfml.compatibility.*;
import jfml.knowledgebase.*;
import jfml.parameter.*;
import jfml.rule.*;
import jfml.enumeration.*;
import jfml.test.*;
import jfml.membershipfunction.*;
import jfml.rulebase.*;
import jfml.knowledgebase.variable.*;
import jfml.term.*;

  int width = 1650;
  int height = 700;

  int protMargin = 150;
  int protWidth = 700;
  int protHeight = 400;
  int protOriginX = 150;
  int protOriginY = 150;

  int originX = 150;
  int originY = height - 150;

  int toolOriginX = 950;
  int toolOriginY = 0;
  int toolWidth = 700;
  int toolHeight = height;

  ControlP5 cp5;

  Slider slider1;  //for Param1
  Slider slider2;  //for Param2
  Slider slider3;  //for Param3
  Slider slider4;  //for Param4
  float slider1Value;  //for Param1
  float slider2Value;  //for Param2
  float slider3Value;  //for Param3
  float slider4Value;  //for Param4
  int paramNum = 0;

  float[] x;
  int h = 500;

  PFont listFont;
  PFont numberFont;

  DropdownList attributeList;
  DropdownList variableList;
  DropdownList termList;

  int currentAttribute = 0;
  int currentVariable = 0;
  int currentTermType = 0;

  ArrayList<String> attributeName = new ArrayList<String>();
  ArrayList<ArrayList<String>> variableNameList = new ArrayList<ArrayList<String>>();
  ArrayList<String> termName = new ArrayList<String>();

  int Ndim = 2;
  int[] variableNum;
  int[] variableNameCount;
  int[] colors;
  int variableMax = 10;

  ArrayList<ArrayList<FVariable>> variables = new ArrayList<ArrayList<FVariable>>();

  String fileName;
  DataSetInfo dataset;

  boolean hasDataset = false;
  boolean isShowHistogram = true;
  int histogramDivide = 20;
  float[][][] patternRate;
  float[][] patternRateAllClass;
  int[][] patternNumAllClass;

  boolean showColor = true;

  void setup(){
    size(1651, 701);
    background(255);

    cp5 = new ControlP5(this);

    this.x = new float[this.h + 1];
    for(int i = 0; i < this.h; i++) {
      this.x[i] = (float)i / (float)this.h;
    }
    x[this.h] = 1f;

    variableMax = 10;
    this.colors = new int[variableMax];
    this.colors[0] = color(255, 204, 255);
    this.colors[1] = color(102, 255, 153);
    this.colors[2] = color(102, 255, 255);
    this.colors[3] = color(244, 164, 96);
    this.colors[4] = color(147, 112, 219);
    this.colors[5] = color(255, 255, 0);
    this.colors[6] = color(34, 139, 34);
    this.colors[7] = color(255, 0, 0);
    this.colors[8] = color(0, 0, 255);
    this.colors[9] = color(119, 136, 153);

    //listFont = loadFont("AppleSDGothicNeo-Heavy-20.vlw");
    //numberFont = loadFont("AppleSDGothicNeo-Heavy-20.vlw");

    termName.add( "Triangular" );
    termName.add( "Gaussian" );
    termName.add( "Trapezoid" );
    termName.add( "LeftLinear" );
    termName.add( "RightLinear" );
    termName.add( "Rectangular" );
    termName.add( "LeftGaussian" );
    termName.add( "RightGaussian" );
    termName.add( "Z-Shape" );
    termName.add( "S-Shape" );
    termName.add( "Singleton" );

    //Button for Add Variable
    cp5.addButton("Add")
      .setPosition(toolOriginX + 250, toolOriginY + 200)
      .setSize(50, 50)
      //.setFont(listFont)
      .setLabel("Add");

    //Button for Delete Variable
    cp5.addButton("Delete")
      .setPosition(toolOriginX + 350, toolOriginY + 200)
      .setSize(50, 50)
      //.setFont(listFont)
      .setLabel("Del");

    //Is Show the Histogram
    cp5.addToggle("isShowHistogram")
      .setLabel("Histogram")
      .setPosition(toolOriginX + 475, toolOriginY + 225)
      .setSize(100, 25)
      .setValue(false)
      .setMode(ControlP5.SWITCH);

    //List for TermType
    this.termList =
    cp5.addDropdownList("TermTypeList")
      .setPosition(toolOriginX + 450, toolOriginY + 100)
      .setSize(150, 200)
      .setLabel("Term")
      .setBarHeight(50)
      .setItemHeight(40)
      .setItems(this.termName)
      .setDefaultValue(0)
      //.setFont(listFont)
      .close();

    //List for Variable
    this.variableList =
    cp5.addDropdownList("VariableList")
      .setPosition(toolOriginX + 250, toolOriginY + 100)
      .setSize(150, 200)
      .setLabel("Variable")
      .setBarHeight(50)
      .setItemHeight(40)
      .setDefaultValue(0)
      //.setFont(listFont)
      .close();

    //List for Attribute
    this.attributeList =
    cp5.addDropdownList("AttributeList")
      .setPosition(toolOriginX + 50, toolOriginY + 100)
      .setSize(150, 200)
      .setLabel("Attribute")
      .setBarHeight(50)
      .setItemHeight(40)
      .setItems(this.attributeName)
      .setDefaultValue(-1)
      //.setFont(listFont)
      .close();
    
    //Dataset Button
    cp5.addButton("dataset")
    .setPosition(toolOriginX + 50, toolOriginY + 20)
    .setSize(100, 40)
    //.setFont(listFont)
    .setLabel("DataSet");

    loadFiles(null);
    initAttribute(this.Ndim);
  }

  void loadFiles(File selection){
    if(selection == null){
      //Default Input File
      if(hasDataset){
        return;
      } else {
        this.Ndim = 1;
      }
      return;
    } else{
      this.fileName = selection.getAbsolutePath();
      this.hasDataset = true;
    }
    
    this.dataset = new DataSetInfo();
    
    String[] lines = loadStrings(fileName);
    for(int i = 0; i < lines.length; i++) {
      if(i == 0){
        dataset.setDataSize(Integer.parseInt(lines[i].split(",")[0]));
        dataset.setNdim(Integer.parseInt(lines[i].split(",")[1]));
        dataset.setCnum(Integer.parseInt(lines[i].split(",")[2]));
      } else{
        dataset.addPattern(lines[i].split(","));
      }
    }
    
    this.Ndim = this.dataset.getNdim();
    patternRate = new float[Ndim][dataset.getCnum()][histogramDivide];
    patternRateAllClass = new float[Ndim][histogramDivide];
    calcHistogram();
  }

  public void initAttribute(int _Ndim) {
    this.variableNum = new int[_Ndim];
    this.variableNameCount = new int[_Ndim];
    this.variables = new ArrayList<ArrayList<FVariable>>();
    this.attributeName = new ArrayList<String>();
    this.variableNameList = new ArrayList<ArrayList<String>>();;
    this.currentAttribute = 0;
    this.currentVariable = 0;
    this.currentTermType = 0;
    
    for(int i = 0; i < _Ndim; i++) {
      this.variables.add( new ArrayList<FVariable>() );
      this.attributeName.add(String.valueOf(i));
      this.variableNameList.add( new ArrayList<String>() );

      variableNum[i] = 1;
      variableNameCount[i] = 1;
      variables.get(i).add( new FVariable(String.valueOf(variableNameCount[i]), 0f, 1f) );
      variables.get(i).get(0).setTermName(termName);
      variableNameList.get(i).add(String.valueOf(variableNameCount[i]));
    }
    
    this.paramNum = 3;
    variables.get(currentAttribute).get(currentVariable).setCurrentTermType(currentTermType);
    defaultParams();
    makeTools();
    variables.get(currentAttribute).get(currentVariable).make();
    variables.get(currentAttribute).get(currentVariable).calcY(this.x);
    this.attributeList.setItems(this.attributeName);
    this.variableList.setItems(variableNameList.get(currentAttribute));
    this.termList.setItems(termName);
  }
  
  public void draw() {
    background(255);
    stroke(0);

    rectMode(CORNER);

    //Prot area
    fill(250);
    rect(0, 0, protWidth + 2*protMargin, protHeight + 2*protMargin);
    fill(250);
    noStroke();
    rect(protOriginX, protOriginY, protWidth, protHeight);
    stroke(0);
    strokeWeight(4);
    line(protOriginX, protOriginY, protOriginX, protOriginY + protHeight);  //TODO
    line(protOriginX+protWidth, protOriginY, protOriginX+protWidth, protOriginY + protHeight);  //TODO
    line(protOriginX, protOriginY+protHeight, protOriginX + protWidth, protOriginY+protHeight);  //TODO
    
    //tool area
    fill(255);
    rect(toolOriginX, toolOriginY, toolWidth, height);

    //Current Dataset File
    if(this.hasDataset){
      String sep = File.separator;
      String[] s = split(fileName, sep);
      String currentFile = s[s.length - 1];
      //textFont(listFont, 24);
      fill(0);
      textAlign(LEFT, CENTER);
      text(currentFile, toolOriginX + 170, toolOriginY + 40);
    }

    //Current Status
    //textFont(numberFont, 24);
    textSize(24);
    fill(0);
    textAlign(RIGHT, BASELINE);
    text(String.valueOf(currentAttribute), toolOriginX + 200, toolOriginY + 90);
    text(this.variableNameList.get(currentAttribute).get(currentVariable), toolOriginX + 400, toolOriginY + 90);
    textAlign(LEFT, BASELINE);
    text(termName.get(currentTermType), toolOriginX + 450, toolOriginY + 90);

    //Param Label
    //textFont(numberFont, 24);
    textSize(24);
    fill(0);
    for(int i = 0; i < paramNum; i++) {
      text( "Param" + String.valueOf(i+1)
        + " = "
        + String.format("%.2f", variables.get(currentAttribute).get(currentVariable).getParam(i+1)),
        toolOriginX + 20, toolOriginY + 300 + i*100);
    }

    //Draw Legends
    if(hasDataset){
      for(int c = 0; c < dataset.getCnum(); c++) {
        rectMode(CORNER);
        fill(colors[c]);
        strokeWeight(2);
        rect(toolOriginX + 450, toolOriginY + 300 + c*50, 50, 16);
      }
      //textFont(listFont, 16);
      textSize(16);
      fill(0);
      for(int c = 0; c < dataset.getCnum(); c++) {
        textAlign(LEFT, TOP);
        text("Class " + String.valueOf(c), toolOriginX + 510, toolOriginY + 300 + c*50);
      }
    }


    //Draw Ruler
    if(showColor) {
      drawRuler();
    }

    //Draw Histgram of Dataset
    textAlign(CENTER, BASELINE);
    //textFont(listFont, 24);
    textSize(24);
    fill(0);
    text("Histogram", toolOriginX + 525, toolOriginY + 200);
    //textFont(listFont, 20);
    textSize(20);
    text("show", toolOriginX + 475, toolOriginY + 223);
    text("hide", toolOriginX + 575, toolOriginY + 223);

    if(isShowHistogram && hasDataset) {
      drawHistogram();
      if(showColor) {
        drawPatternNum();
      }
    }

    //Membership Function Values
    variables.get(currentAttribute).get(currentVariable).make();
    variables.get(currentAttribute).get(currentVariable).calcY(this.x);
    if(currentTermType == 10) {
      for(int i = 0; i < variableNum[currentAttribute]; i++) {
        if(currentVariable == i) {
          if(this.showColor) {
            fill(255, 0, 0);
          } else {
            fill(0);
          }
        } else {
          fill(0);
        }
        for(int j = 0; j < h+1; j++) {
          ellipse( (x[j]*protWidth) + originX,
               originY - (variables.get(currentAttribute).get(i).getY(j) * protHeight),
               4, 4);
        }
      }
    }
    else {
      for(int i = 0; i < variableNum[currentAttribute]; i++) {
        if(currentVariable == i) {
          if(this.showColor) {
            stroke(255, 0, 0);
          } else {
            stroke(0);
          }
        } else {
          stroke(0);
        }
        strokeWeight(4);
        for(int j = 0; j < h; j++) {
          line(  (x[j] * protWidth) + originX, originY - (variables.get(currentAttribute).get(i).getY(j) * protHeight),
              (x[j+1] * protWidth) + originX, originY - (variables.get(currentAttribute).get(i).getY(j+1) * protHeight) );
        }
      }
      strokeWeight(2);
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

    textAlign(CENTER, CENTER);
    //textFont(numberFont, 16);
    textSize(16);

    for(int i = 0; i < divideX; i++) {
      float num = (float)i * (maxX - minX) / divideX;
      text(String.format("%.1f", num), protOriginX + (hX * i), protOriginY + protHeight + 15);
    }
    text(String.format("%.1f", maxX), protOriginX + protWidth, protOriginY + protHeight + 15);

    for(int i = 0; i < divideY; i++) {
      float num = (float)i * (maxY - minY) / divideY;
      text(String.format("%.1f", num), protOriginX - 20, protOriginY + protHeight - (hY * i));
    }
    text(String.format("%.1f", maxY), protOriginX - 20, protOriginY);

    textAlign(CENTER, CENTER);
    //textFont(listFont, 24);
    textSize(24);
    text("Attribute Value", protOriginX + protWidth/2, protOriginY + protHeight + 45);

    translate(protOriginX - 50, protOriginY + protHeight/2);
    rotate(-(PI/2));
    text("Membership Value", 0, 0);

    rotate(PI/2);
    translate(-(protOriginX - 50), -(protOriginY + protHeight/2));
  }

  public void drawHistogram() {
    int h = protWidth / histogramDivide;
    int stroke = 1;

    rectMode(CORNERS);
    for(int i = 0; i < histogramDivide; i++) {
      float bias = 0;
      for(int c = 0; c < dataset.getCnum(); c++) {
        fill(colors[c]);
        stroke(0);
        strokeWeight(stroke);
        rect(  originX + (i*h), originY - bias,
            originX + (i+1)*h, originY - bias - patternRate[currentAttribute][c][i] * protHeight);
        bias += patternRate[currentAttribute][c][i] * protHeight;
      }
    }
    stroke(0);
  }

  public void drawPatternNum() {
    int h = protWidth / histogramDivide;
    textAlign(CENTER, BASELINE);
    //textFont(listFont, 24);
    textSize(24);
    fill(0);
    text("Patterns", protOriginX + protWidth/2, protOriginY - 70);

    textAlign(LEFT, BOTTOM);
    //textFont(numberFont, 16);
    textSize(16);
    fill(0);
    for(int i = 0; i < histogramDivide; i++) {
      translate(protOriginX + (h*i) + h/2, protOriginY-20);
      rotate(-(PI/4));
      text(String.valueOf(patternNumAllClass[currentAttribute][i]), 0, 0);
      rotate(PI/4);
      translate(-(protOriginX + (h*i) + h/2), -(protOriginY-20));
    }
  }

  public void calcHistogram() {
    int[][] patternNum = new int[dataset.getCnum()][histogramDivide];
    patternNumAllClass = new int[Ndim][histogramDivide];

    for(int i = 0; i < Ndim; i++) {


      for(int j = 0; j < histogramDivide; j++) {
        int currentAttribute = i;
        double min = ((double)j) / (double)histogramDivide;
        double max = ((double)j + 1.0) / (double)histogramDivide;
        
        int count = 0;
        for(int p = 0; p < dataset.getDataSize(); p++){
          if( dataset.getPattern().get(p).getDimValue(currentAttribute) <= max &&
              dataset.getPattern().get(p).getDimValue(currentAttribute) > min ){
                count++;
          }
        }
        patternNumAllClass[i][j] = count;
      }
      int maxNum = patternNumAllClass[i][0];
      for(int j = 1; j < histogramDivide; j++) {
        if(maxNum < patternNumAllClass[i][j]) {
          maxNum = patternNumAllClass[i][j];
        }
      }
      for(int j = 0; j < histogramDivide; j++) {
        this.patternRateAllClass[i][j] = (float)patternNumAllClass[i][j] / (float)maxNum;
      }


      for(int j = 0; j < histogramDivide; j++) {
        int currentAttribute = i;
        double min = ((double)j) / (double)histogramDivide;
        double max = ((double)j + 1.0) / (double)histogramDivide;
        for(int c = 0; c < dataset.getCnum(); c++) {
          int nowClass = c;
          int count = 0;
          for(int p = 0; p < dataset.getDataSize(); p++){
            if( dataset.getPattern().get(p).getDimValue(currentAttribute) <= max &&
                dataset.getPattern().get(p).getDimValue(currentAttribute) > min &&
                dataset.getPattern().get(p).getConClass() == nowClass){
                  count++;
            }
          }
          patternNum[nowClass][j] = count;
        }
      }

      for(int c = 0; c < dataset.getCnum(); c++) {
        for(int j = 0; j < histogramDivide; j++) {
          this.patternRate[i][c][j] = (float)patternNum[c][j] / (float)maxNum;
        }
      }

    }
  }

  public void controlEvent(ControlEvent theEvent) {

    //Attribute List
    if(theEvent.isFrom("AttributeList")) {
      int n = (int)theEvent.getController().getValue();
      this.currentAttribute = n;
      this.currentVariable = 0;
      setNowParams();
      makeTools(); 
      this.variableList.setItems(variableNameList.get(currentAttribute));
      this.termList.setItems(termName);      
    }

    //Variable List
    else if(theEvent.isFrom("VariableList")) {
      int n = (int)theEvent.getController().getValue();
      this.currentVariable = n;
      this.currentTermType = this.variables.get(currentAttribute).get(currentVariable).getCurrentTermType();
      if(currentTermType == 10) {
        this.paramNum = 1;
      }
      else if(currentTermType == 1 ||
          currentTermType == 3 ||
          currentTermType == 4 ||
          currentTermType == 5 ||
          currentTermType == 6 ||
          currentTermType == 7 ||
          currentTermType == 8 ||
          currentTermType == 9) {
        this.paramNum = 2;
      }
      else if(currentTermType == 0) {
        this.paramNum = 3;
      }
      else if(currentTermType == 2) {
        this.paramNum = 4;
      }
      setNowParams();
      makeTools();
      this.variableList.setItems(variableNameList.get(currentAttribute));
      this.termList.setItems(termName);
    }

    //TermType List
    else if(theEvent.isFrom("TermTypeList")) {
      int n = (int)theEvent.getController().getValue();
      this.currentTermType = n;
      if(n == 10) {
        this.paramNum = 1;
      }
      else if(n == 1 ||
          n == 3 ||
          n == 4 ||
          n == 5 ||
          n == 6 ||
          n == 7 ||
          n == 8 ||
          n == 9) {
        this.paramNum = 2;
      }
      else if(n == 0) {
        this.paramNum = 3;
      }
      else if(n == 2) {
        this.paramNum = 4;
      }
      variables.get(currentAttribute).get(currentVariable).setCurrentTermType(n);
      defaultParams();
      makeTools();
      variables.get(currentAttribute).get(currentVariable).make();
      variables.get(currentAttribute).get(currentVariable).calcY(this.x);
      this.variableList.setItems(variableNameList.get(currentAttribute));
      this.termList.setItems(termName);
    }

    //Add Button
    else if(theEvent.isFrom("Add")) {
      this.currentVariable = this.variableNum[currentAttribute];
      this.variableNum[currentAttribute]++;
      this.variableNameCount[currentAttribute]++;
      this.variableNameList.get(currentAttribute).add( String.valueOf(variableNameCount[currentAttribute]) );
      variables.get(currentAttribute).add( new FVariable(String.valueOf(variableNameCount[currentAttribute]), 0f, 1f) );
      variables.get(currentAttribute).get(currentVariable).setCurrentTermType(currentTermType);
      variables.get(currentAttribute).get(currentVariable).setTermName(termName);
      variables.get(currentAttribute).get(currentVariable).make();
      variables.get(currentAttribute).get(currentVariable).calcY(this.x);
      defaultParams();
      makeTools();
      this.variableList.setItems(this.variableNameList.get(currentAttribute));;
      this.termList.setItems(termName);
    }

    //Delete Button
    else if(theEvent.isFrom("Delete")) {
      if(variableNum[currentAttribute] > 1) {
        variables.get(currentAttribute).remove(currentVariable);
        variableNameList.get(currentAttribute).remove(currentVariable);
        variableNum[currentAttribute]--;

        if(currentVariable != 0) {
          currentVariable--;
        }
        currentTermType = variables.get(currentAttribute).get(currentVariable).getCurrentTermType();
        if(currentTermType == 10) {
          this.paramNum = 1;
        }
        else if(currentTermType == 1 ||
            currentTermType == 3 ||
            currentTermType == 4 ||
            currentTermType == 5 ||
            currentTermType == 6 ||
            currentTermType == 7 ||
            currentTermType == 8 ||
            currentTermType == 9) {
          this.paramNum = 2;
        }
        else if(currentTermType == 0) {
          this.paramNum = 3;
        }
        else if(currentTermType == 2) {
          this.paramNum = 4;
        }
        setNowParams();
        makeTools();
        this.variableList.setItems(this.variableNameList.get(currentAttribute));
        this.termList.setItems(termName);
      }
    }
    
    //Select Dataset
    else if(theEvent.isFrom("dataset")){
     selectInput("Please Select Dataset File.", "loadFiles");
     initAttribute(this.Ndim);
    }
  }

  public void setNowParams() {
    switch(this.paramNum) {
    case 1:
      slider1Value = variables.get(currentAttribute).get(currentVariable).getParam1();
      slider2Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      slider3Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      slider4Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      break;
    case 2:
      slider1Value = variables.get(currentAttribute).get(currentVariable).getParam1();
      slider2Value = variables.get(currentAttribute).get(currentVariable).getParam2();
      slider3Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      slider4Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      break;
    case 3:
      slider1Value = variables.get(currentAttribute).get(currentVariable).getParam1();
      slider2Value = variables.get(currentAttribute).get(currentVariable).getParam2();
      slider3Value = variables.get(currentAttribute).get(currentVariable).getParam3();
      slider4Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      break;
    case 4:
      slider1Value = variables.get(currentAttribute).get(currentVariable).getParam1();
      slider2Value = variables.get(currentAttribute).get(currentVariable).getParam2();
      slider3Value = variables.get(currentAttribute).get(currentVariable).getParam3();
      slider4Value = variables.get(currentAttribute).get(currentVariable).getParam4();
      break;
    }
  }

  public void defaultParams() {
    switch(this.paramNum) {
    case 1:
      slider1Value = 0.5f;  //default
      variables.get(currentAttribute).get(currentVariable).setParam1(slider1Value);
      slider2Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      slider3Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      slider4Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      break;
    case 2:
      if( currentTermType == 1 ||
        currentTermType == 6 ||
        currentTermType == 7) {  //be gaussian
        slider1Value = 0.5f;  //default
        slider2Value = 0.1f;  //default
      }else {
        slider1Value = 0.3f;  //default
        slider2Value = 0.7f;  //default
      }
      variables.get(currentAttribute).get(currentVariable).setParam1(slider1Value);
      variables.get(currentAttribute).get(currentVariable).setParam2(slider2Value);
      slider3Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      slider4Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      break;
    case 3:
      slider1Value = 0f;  //default
      slider2Value = 0.5f;  //default
      slider3Value = 1f;  //default
      variables.get(currentAttribute).get(currentVariable).setParam1(slider1Value);
      variables.get(currentAttribute).get(currentVariable).setParam2(slider2Value);
      variables.get(currentAttribute).get(currentVariable).setParam3(slider3Value);
      slider4Value = variables.get(currentAttribute).get(currentVariable).getDomainRight();
      break;
    case 4:
      slider1Value = 0f;  //default
      slider2Value = 0.3f;  //default
      slider3Value = 0.7f;  //default
      slider4Value = 1f;  //default
      variables.get(currentAttribute).get(currentVariable).setParam1(slider1Value);
      variables.get(currentAttribute).get(currentVariable).setParam2(slider2Value);
      variables.get(currentAttribute).get(currentVariable).setParam3(slider3Value);
      variables.get(currentAttribute).get(currentVariable).setParam4(slider4Value);
      break;
    }
  }

  public void makeTools() {
    cp5.remove("sliderFunc1");
    cp5.remove("sliderFunc2");
    cp5.remove("sliderFunc3");
    cp5.remove("sliderFunc4");
    cp5.remove("number1");
    cp5.remove("number2");
    cp5.remove("number3");
    cp5.remove("number4");

    switch(this.paramNum) {
    case 1:
      toolParam1();
      break;
    case 2:
      toolParam1();
      toolParam2();
      break;
    case 3:
      toolParam1();
      toolParam2();
      toolParam3();
      break;
    case 4:
      toolParam1();
      toolParam2();
      toolParam3();
      toolParam4();
      break;
    }
  }

  public void toolParam1() {
    cp5.addSlider("sliderFunc1")
    .setValue(slider1Value)
    .setRange(variables.get(currentAttribute).get(currentVariable).getDomainLeft(), variables.get(currentAttribute).get(currentVariable).getDomainRight())
    .setPosition(toolOriginX + 50, toolOriginY + 310)
    .setSize(250, 50);
    //.setFont(numberFont);

    cp5.addTextfield("number1")
    .setAutoClear(false)
    //.setFont(numberFont)
    .setValue(slider1Value)
    .setPosition(toolOriginX + 350, toolOriginY + 310)
    .setSize(50, 50);
  }

  public void toolParam2() {
    cp5.addSlider("sliderFunc2")
    .setValue(slider2Value)
    .setRange(variables.get(currentAttribute).get(currentVariable).getDomainLeft(), variables.get(currentAttribute).get(currentVariable).getDomainRight())
    .setPosition(toolOriginX + 50, toolOriginY + 410)
    .setSize(250, 50);
    //.setFont(numberFont);

    cp5.addTextfield("number2")
    .setAutoClear(false)
    //.setFont(numberFont)
    .setValue(slider2Value)
    .setPosition(toolOriginX + 350, toolOriginY + 410)
    .setSize(50, 50);
  }

  public void toolParam3() {
    cp5.addSlider("sliderFunc3")
    .setValue(slider3Value)
    .setRange(variables.get(currentAttribute).get(currentVariable).getDomainLeft(), variables.get(currentAttribute).get(currentVariable).getDomainRight())
    .setPosition(toolOriginX + 50, toolOriginY + 510)
    .setSize(250, 50);
    //.setFont(numberFont);

    cp5.addTextfield("number3")
    .setAutoClear(false)
    //.setFont(numberFont)
    .setValue(slider3Value)
    .setPosition(toolOriginX + 350, toolOriginY + 510)
    .setSize(50, 50);
  }

  public void toolParam4() {
    cp5.addSlider("sliderFunc4")
    .setValue(slider4Value)
    .setRange(variables.get(currentAttribute).get(currentVariable).getDomainLeft(), variables.get(currentAttribute).get(currentVariable).getDomainRight())
    .setPosition(toolOriginX + 50, toolOriginY + 610)
    .setSize(250, 50);
    //.setFont(numberFont);

    cp5.addTextfield("number4")
    .setAutoClear(false)
    //.setFont(numberFont)
    .setValue(slider4Value)
    .setPosition(toolOriginX + 350, toolOriginY + 610)
    .setSize(50, 50);
  }

  public void sliderFunc1(float value) {
    float sliderFuncValue;
    if( currentTermType == 1 ||
      currentTermType == 6 ||
      currentTermType == 7) {  //be gaussian
      sliderFuncValue = value;
    } else {
      if(value > slider2Value) {
        sliderFuncValue = slider2Value;
        cp5.getController("sliderFunc1").setValue(sliderFuncValue);
      } else {
        sliderFuncValue = value;
      }
    }
    slider1Value = sliderFuncValue;
    variables.get(currentAttribute).get(currentVariable).setParam1(sliderFuncValue);
    variables.get(currentAttribute).get(currentVariable).make();
    variables.get(currentAttribute).get(currentVariable).calcY(this.x);
  }

  public void sliderFunc2(float value) {
    float sliderFuncValue;
    if( currentTermType == 1 ||
      currentTermType == 6 ||
      currentTermType == 7) {  //be gaussian
      sliderFuncValue = value;
    } else {
      if(value < slider1Value) {
        sliderFuncValue = slider1Value;
        cp5.getController("sliderFunc2").setValue(sliderFuncValue);
      } else if(value > slider3Value) {
        sliderFuncValue = slider3Value;
        cp5.getController("sliderFunc2").setValue(sliderFuncValue);
      } else {
        sliderFuncValue = value;
      }
    }
    slider2Value = sliderFuncValue;
    variables.get(currentAttribute).get(currentVariable).setParam2(sliderFuncValue);
    variables.get(currentAttribute).get(currentVariable).make();
    variables.get(currentAttribute).get(currentVariable).calcY(this.x);
  }

  public void sliderFunc3(float value) {
    float sliderFuncValue;
    if( currentTermType == 1 ||
      currentTermType == 6 ||
      currentTermType == 7) {  //be gaussian
      sliderFuncValue = value;
    } else {
      if(value < slider2Value) {
        sliderFuncValue = slider2Value;
        cp5.getController("sliderFunc3").setValue(sliderFuncValue);
      } else if(value > slider4Value) {
        sliderFuncValue = slider4Value;
        cp5.getController("sliderFunc3").setValue(sliderFuncValue);
      } else {
        sliderFuncValue = value;
      }
    }
    slider3Value = sliderFuncValue;
    variables.get(currentAttribute).get(currentVariable).setParam3(sliderFuncValue);
    variables.get(currentAttribute).get(currentVariable).make();
    variables.get(currentAttribute).get(currentVariable).calcY(this.x);
  }

  public void sliderFunc4(float value) {
    float sliderFuncValue;
    if( currentTermType == 1 ||
      currentTermType == 6 ||
      currentTermType == 7) {  //be gaussian
      sliderFuncValue = value;
    } else {
      if(value < slider3Value) {
        sliderFuncValue = slider3Value;
        cp5.getController("sliderFunc4").setValue(sliderFuncValue);
      } else {
        sliderFuncValue = value;
      }
    }
    slider4Value = sliderFuncValue;
    variables.get(currentAttribute).get(currentVariable).setParam4(sliderFuncValue);
    variables.get(currentAttribute).get(currentVariable).make();
    variables.get(currentAttribute).get(currentVariable).calcY(this.x);
  }

  public void number1(String text) {
    float value = Float.parseFloat(text);
    cp5.getController("sliderFunc1").setValue(value);
    sliderFunc1(value);
  }

  public void number2(String text) {
    float value = Float.parseFloat(text);
    cp5.getController("sliderFunc2").setValue(value);
    sliderFunc2(value);
  }

  public void number3(String text) {
    float value = Float.parseFloat(text);
    cp5.getController("sliderFunc3").setValue(value);
    sliderFunc3(value);
  }

  public void number4(String text) {
    float value = Float.parseFloat(text);
    cp5.getController("sliderFunc4").setValue(value);
    sliderFunc4(value);
  }
