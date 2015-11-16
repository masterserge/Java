package ru.nsu.ccfit.serdyukov.knot;

public class Painter {

	public Painter(Surface surface) {
		this.surface = surface;
	}
	
	public void setMatrix(Matrix4 matrix) {
		this.matrix = matrix;
	}
	
	public void paintLine(Vector3 vertex0, Vector3 vertex1, int color) {
		Vector2 v0 = new Vector2(matrix.multiply(new Vector4(vertex0, 1)).toCartesian());
		Vector2 v1 = new Vector2(matrix.multiply(new Vector4(vertex1, 1)).toCartesian());
		double t0;
		double t1;
		if (Math.abs(v0.y() - v1.y()) > Math.abs(v0.x() - v1.x())) {
			t0 = v0.y();
			t1 = v1.y();
		} else {
			t0 = v0.x();
			t1 = v1.x();
		}
		int startT = (int) Math.min(t0, t1);
		int endT = (int) Math.max(t0, t1);
		Vector2Interpolator position = new Vector2Interpolator(startT, t0, t1, v0, v1);
		for (int t = startT;t <= endT;t++) {
			int row = (int) position.get().y();
			int column = (int) position.get().x();
			if (row >= 0 && column >= 0 && row < surface.getRows() && column < surface.getColumns()) {
				surface.putPixel(row, column, color);
			}
			position.step();
		}
	}
	
	public void paintSpline(Vector3[] vertices, int color) {
		int[] binomialCoefficients = new int[vertices.length];
		{
			int[] nextBinomialCoefficients = new int[vertices.length];
			for (int i = 0;i <= vertices.length;i++) {
				nextBinomialCoefficients[0] = 1;
				for (int j = 1;j < i;j++) {
					nextBinomialCoefficients[j] = binomialCoefficients[j - 1] + binomialCoefficients[j];
				}
				{
					int[] tmp = binomialCoefficients;
					binomialCoefficients = nextBinomialCoefficients;
					nextBinomialCoefficients = tmp;
				}
			}
		}
		Vector3 previousPoint = vertices[0];
		for (int segment = 0;segment < SPLINE_SEGMENTS;segment++) {
			double t = (double)(segment + 1)/SPLINE_SEGMENTS;
			double coefficients[] = new double[vertices.length];
			for (int i = 0;i < vertices.length;i++) {
				coefficients[i] = binomialCoefficients[i];
			}
			{
				double tPower = 1;
				double oneMinusTPower = 1;
				for (int i = 0;i < vertices.length;i++) {
					coefficients[i] *= tPower;
					coefficients[vertices.length - 1 - i] *= oneMinusTPower;
					tPower *= t;
					oneMinusTPower *= (1 - t);
				}
			}
			Vector3 point = new Vector3(0, 0, 0);
			for (int i = 0;i < vertices.length;i++) {
				point = point.add(vertices[i].multiply(coefficients[i]));
			}
			paintLine(previousPoint, point, color);
			previousPoint = point;
		}
	}

	private static final int SPLINE_SEGMENTS = 32;
	
	private Surface surface;
	private Matrix4 matrix = Transformations.identity();
}
