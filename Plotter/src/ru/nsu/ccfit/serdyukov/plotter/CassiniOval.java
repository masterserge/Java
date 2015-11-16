package ru.nsu.ccfit.serdyukov.plotter;


public class CassiniOval {

	public CassiniOval(Vector2int[] points, int r) {
		for (int index = 0;index < points.length;index++) {
			this.points[index] = points[index];
		}
		this.r = r;
	}
	
	public long getFunction(Vector2int point) {
		return (long)point.subtract(points[0]).lengthSquare()*point.subtract(points[1]).lengthSquare() - (long)r*r*r*r;
	}
	
	public Vector2long getGradient(Vector2long point) {
		long distance0 = point.subtract(points[0]).lengthSquare();
		long distance1 = point.subtract(points[1]).lengthSquare();
		Vector2long result = new Vector2long(0, 0);
		result = result.add(point.subtract(points[0]).multiply(2*distance1));
		result = result.add(point.subtract(points[1]).multiply(2*distance0));
		return result;
	} 
	
	private final Vector2int[] points = new Vector2int[2];
	private final int r;
}
 