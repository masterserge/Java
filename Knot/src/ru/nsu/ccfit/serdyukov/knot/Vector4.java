package ru.nsu.ccfit.serdyukov.knot;

public class Vector4 {
	public Vector4(double x, double y, double z, double w) {
		setAt(0, x);
		setAt(1, y);
		setAt(2, z);
		setAt(3, w);
	}
	
	public Vector4(Vector3 that, double w) {
		for (int index = 0;index < DIMENSION - 1;index++) {
			setAt(index, that.at(index));
		}
		setAt(DIMENSION -1, w);
	}
	
	public double at(int index) {
		return coordinates[index];
	}
	
	public double x() {
		return at(0);
	}
	
	public double y() {
		return at(1);
	}
	
	public double z() {
		return at(2);
	}
	
	public double w() {
		return at(3);
	}

	public Vector4 add(Vector4 that) {
		Vector4 result = new Vector4();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) + that.at(index));
		}
		return result;
	}
	
	public Vector4 subtract(Vector4 that) {
		Vector4 result = new Vector4();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) - that.at(index));
		}
		return result;
	}
	
	public Vector4 multiply(Vector4 that) {
		Vector4 result = new Vector4();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * that.at(index));
		}
		return result;
	}
	
	public Vector4 divide(Vector4 that) {
		Vector4 result = new Vector4();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) / that.at(index));
		}
		return result;
	}
	
	public Vector4 multiply(double value) {
		Vector4 result = new Vector4();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * value);
		}
		return result;
	}
	
	public Vector4 divide(double value) {
		Vector4 result = new Vector4();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) / value);
		}
		return result;
	}
	
	public double dotProduct(Vector4 that) {
		double result = 0;
		for(int index = 0;index < DIMENSION;index++) {
			result += at(index) * that.at(index);
		}
		return result;
	}
	
	public double length() {
		return Math.sqrt(dotProduct(this));
	}
	
	public Vector3 toCartesian() {
		Vector3 result = new Vector3();
		for (int index = 0;index < DIMENSION - 1;index++) {
			result.setAt(index, at(index) / w());
		}
		return result;
	}
	
	public static final int DIMENSION = 4;
	
	Vector4() {
	}
	
	void setAt(int index, double value) {
		coordinates[index] = value;
	}

	private final double[] coordinates = new double[DIMENSION];
}
