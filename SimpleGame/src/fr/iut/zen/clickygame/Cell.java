package fr.iut.zen.clickygame;

import java.awt.Color;
//import java.util.Objects;
import java.util.Random;

public class Cell {
	private Color color;
	private int value;
	private final static Color[] colors = { Color.RED, Color.BLUE, Color.WHITE };
	private final static Random random = new Random();
	public static final int valueMax = 8;
	private String type;
	public static String[] types = {null, "Unit", "Structure"};

	public Cell(int i, int value, int j) {
		this.color = colors[i]; 
		this.value = value;
		this.type = types[j];
	}

	public static Cell randomGameCell() {
		return new Cell(random.nextInt(colors.length), random.nextInt(valueMax + 1), random.nextInt(types.length));
	}

	public static Cell emptyGameCell() {
		return new Cell(2,0,0);
	}

	public Color getColor() {
		return color;
	}
	
	public String getType() {
		return type;
	}
	
	public int getNumberColor() {
		if (color.equals(colors[0])) {
			return 0;
		}
		else if(color.equals(colors[1])) {
			return 1;
		}
		return 2;
	}
	
	public int getNumberType() {
		if (type.equals(types[1])) {
			return 1;
		}
		else if(type.equals(types[2])) {
			return 2;
		}
		return 0;
	}
	
	public boolean isEmpty() {
		if (value == 0) {
			return true;
		}
		return false;
	}

	public int getValue() {
		return value; 
	}

	@Override
	public String toString() {
		return color.toString();
	}
}
