

import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;

public class Main extends PApplet{

	int width = 1300;
	int height = 700;
	GUI_main gui;
	ControlP5 cp5;

	boolean isShowHistogram = false;

//	@Override
	public void settings() {
		size(width + 1, height + 1);
	}

//	@Override
	public void setup() {
		background(255);
		this.cp5 = new ControlP5(this);
		this.gui = new GUI_main(this);

	}

//	@Override
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

	public static void main(String[] args) {
//		String[] a = PFont.list();
		PApplet.main("Main");
	}

}
