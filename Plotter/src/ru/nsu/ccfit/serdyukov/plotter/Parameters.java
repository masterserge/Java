package ru.nsu.ccfit.serdyukov.plotter;

import java.util.Observable;


public class Parameters extends Observable {
	
	public static final int MIN_X1 = -1000;
	public static final int MAX_X1 = 1000;
	public static final int DEFAULT_X1 = -10;
	
	public static final int MIN_Y1 = -1000;
	public static final int MAX_Y1 = 1000;
	public static final int DEFAULT_Y1 = 0;
	
	public static final int MIN_X2 = -1000;
	public static final int MAX_X2 = 1000;
	public static final int DEFAULT_X2 = 10;
	
	public static final int MIN_Y2 = -1000;
	public static final int MAX_Y2 = 1000;
	public static final int DEFAULT_Y2 = 0;

	public static final int MIN_R = -1000;
	public static final int MAX_R = 1000;
	public static final int DEFAULT_R = 1;
	

	enum MouseMode{
		POINT1,
		POINT2,
		NONE
	}
	MouseMode mouseMode = MouseMode.NONE;
	
	public MouseMode getMouseMode() {
		return mouseMode;
	}
	public void setMouseMode(MouseMode mouseMode) {
		this.mouseMode = mouseMode;
	}
	public Parameters() {
		point1 = new Vector2int(DEFAULT_X1, DEFAULT_Y1);
		point2 = new Vector2int(DEFAULT_X2, DEFAULT_Y2);
		r = DEFAULT_R;
	}
	
	public Vector2int getPoint1() {
		return point1;
	}
	public void setPoint1(Vector2int point) {
		if (point.x() > MAX_X1 || point.x() < MIN_X1 ){
			throw new IllegalArgumentException("Invalid x1");
		}
		if (point.y() > MAX_Y1 || point.y() < MIN_Y1 ){
			throw new IllegalArgumentException("Invalid y1");
		}
		this.point1 = point;
		setChanged();
		notifyObservers();
	}
	
	public Vector2int getPoint2() {
		return point2;
	}
	public void setPoint2(Vector2int point) {
		if (point.x() > MAX_X2 || point.x() < MIN_X2 ){
			throw new IllegalArgumentException("Invalid x2");
		}
		if (point.y() > MAX_Y2 || point.y() < MIN_Y2 ){
			throw new IllegalArgumentException("Invalid y2");
		}
		this.point2 = point;
		setChanged();
		notifyObservers();
	}

	Vector2int point1;
	Vector2int point2;
	int r;
	
	public int getPoint1X() {
		return point1.x();
	}
	public void setPoint1X(int x1) {
		if (x1 > MAX_X1 || x1 < MIN_X1 ){
			throw new IllegalArgumentException("Invalid x1");
		}
		point1.setAt(0, x1);
		setChanged();
		notifyObservers();
	}
	
	public int getPoint1Y() {
		return point1.y();
	}
	public void setPoint1Y(int y1) {
		if (y1 > MAX_Y1 || y1 < MIN_Y1 ){
			throw new IllegalArgumentException("Invalid y1");
		}
		point1.setAt(1, y1);
		setChanged();
		notifyObservers();
	}
	
	public int getPoint2X() {
		return point2.x();
	}
	public void setPoint2X(int x2) {
		if (x2 > MAX_X2 || x2 < MIN_X2 ){
			throw new IllegalArgumentException("Invalid x2");
		}
		point2.setAt(0, x2);
		setChanged();
		notifyObservers();
	}
	
	public int getPoint2Y() {
		return point2.y();
	}
	public void setPoint2Y(int y2) {
		if (y2 > MAX_Y2 || y2 < MIN_Y2 ){
			throw new IllegalArgumentException("Invalid y2");
		}
		point2.setAt(1, y2);
		setChanged();
		notifyObservers();
	}
	
	public int getR() {
		return r;
	}
	public void setR(int r) {
		if (r > MAX_R || r < MIN_R ){
			throw new IllegalArgumentException("Invalid r");
		}
		this.r = r;
		setChanged();
		notifyObservers();
	}
}
