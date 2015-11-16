
public class Matrix4 {
	public Matrix4(double e00, double e01, double e02, double e03, double e10, double e11, double e12, double e13, double e20, double e21, double e22, double e23, double e30, double e31, double e32, double e33) {
		setAt(0, 0, e00);
		setAt(0, 1, e01);
		setAt(0, 2, e02);
		setAt(0, 3, e03);
		setAt(1, 0, e10);
		setAt(1, 1, e11);
		setAt(1, 2, e12);
		setAt(1, 3, e13);
		setAt(2, 0, e20);
		setAt(2, 1, e21);
		setAt(2, 2, e22);
		setAt(2, 3, e23);
		setAt(3, 0, e30);
		setAt(3, 1, e31);
		setAt(3, 2, e32);
		setAt(3, 3, e33);
	}
	
	public double at(int row, int column) {
		return elements[row * DIMENSION + column];
	}
	
	public Matrix4 multiply(Matrix4 that) {
		Matrix4 result = new Matrix4();
		for (int i = 0;i < DIMENSION;i++) {
			for (int k = 0;k < DIMENSION;k++) {
				for (int j = 0;j < DIMENSION;j++) {
					result.setAt(i, j, result.at(i, j) + at(i, k) * that.at(k, j));
				}
			}
		}
		return result;
	}
	
	public Vector4 multiply(Vector4 that) {
		Vector4 result = new Vector4();
		for (int i = 0;i < DIMENSION;i++) {
			double value = 0;
			for (int j = 0;j < DIMENSION;j++) {
				value += at(i, j) * that.at(j);
			}
			result.setAt(i, value);
		}
		return result;
	}

	public static final int DIMENSION = 4;
	
	Matrix4() {
	}
	
	void setAt(int row, int column, double value) {
		elements[row * DIMENSION + column] = value;
	}
	
	private final double elements[] = new double[DIMENSION * DIMENSION];
}
