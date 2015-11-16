package ru.nsu.ccfit.serdyukov.plotter;



public class Vector2long {
	public Vector2long(long x, long y) {
		setAt(0, x);
		setAt(1, y);
	}
	
	public long at(int index) {
		return coordinates[index];
	}
	
	public long x() {
		return at(0);
	}
	
	public long y() {
		return at(1);
	}
	
	public Vector2long add(Vector2long that) {
		Vector2long result = new Vector2long();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) + that.at(index));
		}
		return result;
	}
	
	public Vector2long subtract(Vector2int that) {
		Vector2long result = new Vector2long();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) - that.at(index));
		}
		return result;
	}
	
	public Vector2long multiply(Vector2long that) {
		Vector2long result = new Vector2long();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * that.at(index));
		}
		return result;
	}
	
	public Vector2long multiply(long value) {
		Vector2long result = new Vector2long();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * value);
		}
		return result;
	}
	
	public long dotProduct(Vector2long that) {
		long result = 0;
		for(int index = 0;index < DIMENSION;index++) {
			result += at(index) * that.at(index);
		}
		return result;
	}
	
	public long lengthSquare() {
		return dotProduct(this);
	}
	
	public Vector2long perpendicular() {
		return new Vector2long(-y(), x());
	}
	
	public static final int DIMENSION = 2;
	
	Vector2long() {
	}
	
	void setAt(int index, long value) {
		coordinates[index] = value;
	}

	private final long[] coordinates = new long[DIMENSION];
}
