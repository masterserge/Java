package ru.nsu.ccfit.serdyukov.knot;

public class Surface {

	private int rows;
	private int columns;
	private int array[];

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	Surface(int rows, int columns) {
		array = new int[rows * columns];
		this.rows = rows;
		this.columns = columns;
	}

	public int[] getData() {
		return array;
	}

	private int getAddress(int row, int column) {
		if (row < 0 || row >= rows) {
			throw new IndexOutOfBoundsException("Row index is out of bounds");
		}
		if (column < 0 || column >= columns) {
			throw new IndexOutOfBoundsException("Column index is out of bounds");
		}
		return row * columns + column;
	}
	
	
	public void putPixel(int row, int column, int colorRGB) {
		array[getAddress(row, column)] = colorRGB;
	}

	public void fill(int colorRGB) {
		for (int i = 0; i < array.length; i++) {
			array[i] = colorRGB;
		}
	}
	
	public int getColor(int row, int column) {
		return array[getAddress(row, column)];
	}
	
 

}
