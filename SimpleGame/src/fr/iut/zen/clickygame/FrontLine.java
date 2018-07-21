package fr.iut.zen.clickygame;

public class FrontLine {
	private int red;
	private int blue;
	
	public FrontLine(int red, int blue) {
		this.red = red;
		this.blue = blue;
	}
	public FrontLine(int red) {
		this.red = red;
	}
	public FrontLine() {
		red = 1;
		blue = 4;
	}
	
	public int getBlue() {
		return blue;
	}
	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}
	public void resetRedLine() {
		red = 1;
	}
	public void resetBlueLine() {
		blue = 4;
	}
}