package ru.nsu.ccfit.serdyukov.plotter;


public class Vector2int {
	public Vector2int(int x, int y) {
		setAt(0, x);
		setAt(1, y);
	}
	
	public int at(int index) {
		return coordinates[index];
	}
	
	public int x() {
		return at(0);
	}
	
	public int y() {
		return at(1);
	}
	
	public Vector2int add(Vector2int that) {
		Vector2int result = new Vector2int();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) + that.at(index));
		}
		return result;
	}
	
	public Vector2int subtract(Vector2int that) {
		Vector2int result = new Vector2int();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) - that.at(index));
		}
		return result;
	}
	
	public Vector2int multiply(Vector2int that) {
		Vector2int result = new Vector2int();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * that.at(index));
		}
		return result;
	}
	
	public Vector2int multiply(int value) {
		Vector2int result = new Vector2int();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * value);
		}
		return result;
	}
	
	public int dotProduct(Vector2int that) {
		int result = 0;
		for(int index = 0;index < DIMENSION;index++) {
			result += at(index) * that.at(index);
		}
		return result;
	}
	
	public int lengthSquare() {
		return dotProduct(this);
	}
	
	public Vector2int perpendicular() {
		return new Vector2int(-y(), x());
	}
	
	public static final int DIMENSION = 2;
	
	Vector2int() {
	}
	
	void setAt(int index, int value) {
		coordinates[index] = value;
	}

	private final int[] coordinates = new int[DIMENSION];
}
