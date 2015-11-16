
public class Vector2 {
	public Vector2(double x, double y) {
		setAt(0, x);
		setAt(1, y);
	}
	
	public Vector2(Vector3 that) {
		for(int index = 0;index < DIMENSION;index++) {
			setAt(index, that.at(index));
		}
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
	
	public Vector2 add(Vector2 that) {
		Vector2 result = new Vector2();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) + that.at(index));
		}
		return result;
	}
	
	public Vector2 subtract(Vector2 that) {
		Vector2 result = new Vector2();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) - that.at(index));
		}
		return result;
	}
	
	public Vector2 multiply(Vector2 that) {
		Vector2 result = new Vector2();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * that.at(index));
		}
		return result;
	}
	
	public Vector2 divide(Vector2 that) {
		Vector2 result = new Vector2();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) / that.at(index));
		}
		return result;
	}
	
	public Vector2 multiply(double value) {
		Vector2 result = new Vector2();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) * value);
		}
		return result;
	}
	
	public Vector2 divide(double value) {
		Vector2 result = new Vector2();
		for(int index = 0;index < DIMENSION;index++) {
			result.setAt(index, at(index) / value);
		}
		return result;
	}
	
	public double dotProduct(Vector2 that) {
		double result = 0;
		for(int index = 0;index < DIMENSION;index++) {
			result += at(index) * that.at(index);
		}
		return result;
	}
	
	public double length() {
		return Math.sqrt(dotProduct(this));
	}
	
	public static final int DIMENSION = 2;
	
	Vector2() {
	}
	
	void setAt(int index, double value) {
		coordinates[index] = value;
	}

	private final double[] coordinates = new double[DIMENSION];
}
