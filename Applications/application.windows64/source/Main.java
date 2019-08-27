import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

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
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main extends PApplet {



















GUI_main gui;
ControlP5 cp5;
boolean isShowHistogram = false;

public void setup(){
  
  
  background(255);
  this.cp5 = new ControlP5(this);
  this.gui = new GUI_main(this);
}

public void draw() {
  this.gui.draw();
}

public void controlEvent(ControlEvent event) {
  this.gui.controlEvent(event);
}

public void slider0(float value) {
  gui.slider0(value);
}

public void slider1(float value) {
  gui.slider1(value);
}

public void slider2(float value) {
  gui.slider2(value);
}

public void slider3(float value) {
  gui.slider3(value);
}

public void number0(String text) {
  gui.number0(text);
}

public void number1(String text) {
  gui.number1(text);
}

public void number2(String text) {
  gui.number2(text);
}

public void number3(String text) {
  gui.number3(text);
}

public void fileName(String text) {
  gui.fileName(text);
}
  public void settings() {  size(1301, 701); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
