
public class Vector3 {
	public Vector3(double x, double y, double z) {
		setAt(0, x);
		setAt(1, y);
		setAt(2, z);
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
	
	public Vector3 add(Vector3 that) {
		Vector3 result = new Vector3();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) + that.at(index));
		}
		return result;
	}
	
	public Vector3 subtract(Vector3 that) {
		Vector3 result = new Vector3();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) - that.at(index));
		}
		return result;
	}
	
	public Vector3 multiply(Vector3 that) {
		Vector3 result = new Vector3();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * that.at(index));
		}
		return result;
	}
	
	public Vector3 divide(Vector3 that) {
		Vector3 result = new Vector3();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) / that.at(index));
		}
		return result;
	}
	
	public Vector3 multiply(double value) {
		Vector3 result = new Vector3();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * value);
		}
		return result;
	}
	
	public Vector3 divide(double value) {
		Vector3 result = new Vector3();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) / value);
		}
		return result;
	}
	
	public double dotProduct(Vector3 that) {
		double result = 0;
		for(int index = 0;index < DIMENSION;index++) {
			result += at(index) * that.at(index);
		}
		return result;
	}
	
	public double length() {
		return Math.sqrt(dotProduct(this));
	}
	
	public static final int DIMENSION = 3;
	
	Vector3() {
	}
	
	void setAt(int index, double value) {
		coordinates[index] = value;
	}

	private final double[] coordinates = new double[DIMENSION];
}
