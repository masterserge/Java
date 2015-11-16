
public abstract class Transformations {
	public static Matrix4 identity() {
		return new Matrix4(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);
	}
	public static Matrix4 translation(Vector3 offset) {
		return new Matrix4(
			1, 0, 0, offset.x(),
			0, 1, 0, offset.y(),
			0, 0, 1, offset.z(),
			0, 0, 0, 1
		);
	}
	public static Matrix4 rotation(Vector3 axis, double angle) {
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double x = axis.x();
		double y = axis.y();
		double z = axis.z();
		return new Matrix4(
			c + x*x*(1 - c),      x*y*(1 - c) - z*s,    x*z*(1-c) + y*s,      0,
			y*x*(1 - c) + z*s,    c + y*y*(1 - c),      y*z*(1 - c) - x*s,    0,
			z*x*(1 - c) - y*s,    z*y*(1 - c) + x*s,    c + z*z*(1 - c),      0,
			0,                    0,                    0,                    1
		);
	}
	public static Matrix4 scaling(Vector3 scale) {
		return new Matrix4(
			scale.x(), 0,         0,         0,
			0,         scale.y(), 0,         0,
			0,         0,         scale.z(), 0,
			0,         0,         0,         1
		);
	}
	public static Matrix4 projection(double r) {
		return new Matrix4(
			1, 0, 0,    0,
			0, 1, 0,    0,
			0, 0, 0,    0,
			0, 0, -1/r, -1
		);
	}
}
