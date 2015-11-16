package ru.nsu.ccfit.serdyukov.minesweeper;

import java.util.Vector;

public class Cell
{
	public static int[][] neighbourOffsets = new int[][] { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

	private boolean isMine;
	private int neighboursMineCount;

	private StateMark mark;

	private GameField field;
	private int x;
	private int y;

	public Cell(GameField field, int x, int y)
	{
		this.field = field;
		this.x = x;
		this.y = y;
		this.isMine = false;
		this.mark = StateMark.NONE;
	}
	public void setNeighboursMineCount(int mineCount)
	{
		neighboursMineCount = mineCount;
	}
	public int getNeighboursMineCount()
	{
		return neighboursMineCount;
	}
	public void setMine(boolean isMine)
	{
		this.isMine = isMine;
	}
	public boolean getMine()
	{
		return isMine;
	}
	public Cell[] getNeighbours()
	{
		Vector<Cell> vcell = new Vector<Cell>();

		for (int[] offset : neighbourOffsets)
		{
			Cell cell = field.getCell(x + offset[0], y + offset[1]);
			if (cell != null)
				vcell.add(cell);
		}
		return vcell.toArray(new Cell[0]);
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setStateMark(StateMark mark)
	{
		this.mark = mark;
	}
	public StateMark getStateMark()
	{
		return mark;
	}
}