package ru.nsu.ccfit.serdyukov.plotter;

import java.awt.Color;



public class Painter {

	public Painter(Surface surface) {
		this.surface = surface;
	}

	public void printAxes() {
		int columns = surface.getColumns();
		int rows = surface.getRows();

		for (int i = 0; i < rows; i++)
			surface.putPixel(i, columns / 2, Color.black.getRGB());

		for (int i = 0; i < columns; i++)
			surface.putPixel(rows / 2, i, Color.black.getRGB());

		for (int i = 0; i < (int)Math.sqrt(rows*columns) / 50; i++) {
			surface.putPixel(i, columns / 2 + i, Color.black.getRGB());
			surface.putPixel(i, columns / 2 - i, Color.black.getRGB());
			surface.putPixel(rows / 2 - i, columns - 1 - i, Color.black.getRGB());
			surface.putPixel(rows / 2 + i, columns - 1 - i, Color.black.getRGB());
		}

	}
	
	public void printPoint(Surface surface, int y, int x, int r){
		int startRow = Math.max(0,Math.min(surface.getRows(),(y - r)));
		int endRow = Math.max(0,Math.min(surface.getRows(),(y + r)));
		
		for (int row = startRow; row < endRow; row++)
		{
			int startColumn = Math.max(0,Math.min(surface.getColumns(),(int)(x +0.5- Math.sqrt(r*r  - (row-y)*(row-y)))));
			int endColumn = Math.max(0,Math.min(surface.getColumns(),(int)(x +0.5+ Math.sqrt(r*r  - (row-y)*(row-y)))));
			for(int column = startColumn; column < endColumn; column++){
				surface.putPixel(row, column,Color.red.getRGB());
			}
		}
	}
	public Vector2int findStartPoint(Vector2int[] points, int r) {
		if (0 == r) {
			return points[1];
		}
		CassiniOval oval = new CassiniOval(points, r);
		int y = points[1].y();
		int startX = points[1].x();
		int endX = startX;
		{
			int deltaEndX = (points[1].x() > points[0].x())? r : -r;
			while(oval.getFunction(new Vector2int(endX, y)) < 0) {
				endX += deltaEndX;
			}
		}
		while(Math.abs(startX - endX) > 1) {
			int x = (startX + endX > 0)? (startX + endX + 1)/2 : (startX + endX - 1)/2;
			if (oval.getFunction(new Vector2int(x, y)) > 0) {
				endX = x;
			} else {
				startX = x;
			}
		}
		if (Math.abs(oval.getFunction(new Vector2int(startX, y))) < Math.abs(oval.getFunction(new Vector2int(endX, y)))) {
			return new Vector2int(startX, y);
		} else {
			return new Vector2int(endX, y);
		}
	}
	
	public void paintCassiniOval(Vector2int[] points, int r) {
		{
			CassiniOval oval = new CassiniOval(points, r);
			Vector2int startPoint = findStartPoint(points, r);
			Vector2int point = startPoint;
			boolean ccw = true;
			while(true) {
				Vector2int reflection = points[0].add(points[1]).subtract(point);
				if (Math.abs(point.y()) < surface.getRows()/2 && Math.abs(point.x()) < surface.getColumns()/2) {
					surface.putPixel(surface.getRows()/2-point.y(), surface.getColumns()/2+point.x(), 0x00ff00);
				}
				if (Math.abs(reflection.y()) < surface.getRows()/2 && Math.abs(reflection.x()) < surface.getColumns()/2) {
					surface.putPixel(surface.getRows()/2-reflection.y(), surface.getColumns()/2+reflection.x(), 0xff00);
				}
				Vector2int point0;
				Vector2int point1;
				Vector2int point2;
				Vector2long direction = oval.getGradient(new Vector2long(point.x(), point.y())).perpendicular().multiply(ccw? 1 : -1);
				if (direction.x() == 0 && direction.y() == 0) {
					if (ccw) {
						point = startPoint;
						ccw = false;
						continue;
					} else {
						break;
					}
				}
				if (Math.abs(direction.x()) > Math.abs(direction.y())) {
					if (direction.x() > 0) {
						point0 = new Vector2int(point.x() + 1, point.y());
						point1 = new Vector2int(point.x() + 1, point.y() - 1);
						point2 = new Vector2int(point.x() + 1, point.y() + 1);
					} else {
						point0 = new Vector2int(point.x() - 1, point.y());
						point1 = new Vector2int(point.x() - 1, point.y() - 1);
						point2 = new Vector2int(point.x() - 1, point.y() + 1);
					}
				} else {
					if (direction.y() > 0) {
						point0 = new Vector2int(point.x(), point.y() + 1);
						point1 = new Vector2int(point.x() - 1, point.y() + 1);
						point2 = new Vector2int(point.x() + 1, point.y() + 1);
					} else {
						point0 = new Vector2int(point.x(), point.y() - 1);
						point1 = new Vector2int(point.x() - 1, point.y() - 1);
						point2 = new Vector2int(point.x() + 1, point.y() - 1);
					}
				}
				if (Math.abs(oval.getFunction(point0)) > Math.abs(oval.getFunction(point1))) {
					if (Math.abs(oval.getFunction(point1)) > Math.abs(oval.getFunction(point2))) {
						point = point2;
					} else {
						point = point1;
					}
				} else {
					if (Math.abs(oval.getFunction(point0)) > Math.abs(oval.getFunction(point2))) {
						point = point2;
					} else {
						point = point0;
					}
				}
				if (point.x() == startPoint.x() && point.y() == startPoint.y()) {
					break;
				}
				if (reflection.x() == startPoint.x() && reflection.y() == startPoint.y()) {
					break;
				}
			}
		}
	}
	
	private Surface surface;
}
