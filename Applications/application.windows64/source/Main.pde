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

GUI_main gui;
ControlP5 cp5;
boolean isShowHistogram = false;

void setup(){
  size(1301, 701);
  
  background(255);
  this.cp5 = new ControlP5(this);
  this.gui = new GUI_main(this);
}

void draw() {
  this.gui.draw();
}

void controlEvent(ControlEvent event) {
  this.gui.controlEvent(event);
}

void slider0(float value) {
  gui.slider0(value);
}

void slider1(float value) {
  gui.slider1(value);
}

void slider2(float value) {
  gui.slider2(value);
}

void slider3(float value) {
  gui.slider3(value);
}

void number0(String text) {
  gui.number0(text);
}

void number1(String text) {
  gui.number1(text);
}

void number2(String text) {
  gui.number2(text);
}

void number3(String text) {
  gui.number3(text);
}

void fileName(String text) {
  gui.fileName(text);
}
