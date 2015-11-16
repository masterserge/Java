package ru.nsu.ccfit.serdyukov.minesweeper;

import java.util.Random;

public class GameField
{
	private GameModel gameModel;

	private int width;
	private int height;
	private Cell[][] cells;
	private boolean isFilledField;
	private boolean firstOpen;
	private int mineCount;
	private int bottom3BV;
	private int top3BV;

	public GameField(int width, int height, int mineCount, GameModel gameModel) throws InvalidFieldSettingsException
	{
		if (GameSettings.isValidParameters(width, height, mineCount) == false)
			throw new InvalidFieldSettingsException();
		this.width = width;
		this.height = height;
		this.mineCount = mineCount;
		cells = new Cell[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				cells[i][j] = new Cell(this, i, j);
		isFilledField = false;
		bottom3BV = 0;
		top3BV = Integer.MAX_VALUE;
		this.gameModel = gameModel;
		this.firstOpen = true;
	}
	/**
	 * Method prints field to console
	 */
	public void printField()
	{
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
			{
				if (cells[i][j].getMine())
					System.out.print("X ");
				else
					System.out.print("O ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
			{
				System.out.print(cells[i][j].getNeighboursMineCount() + " ");
			}
			System.out.println();
		}
	}
	public void fillFieldRandomly(int startX, int startY)
	{
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				cells[i][j].setMine(false);

		Random rand = new Random();
		for (int i = 0; i < mineCount; i++)
		{
			int x, y;
			do
			{
				x = rand.nextInt(width);
				y = rand.nextInt(height);
			}
			while ((x == startX && y == startY) || (cells[x][y].getMine() == true));
			cells[x][y].setMine(true);
		}
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
			{
				Cell[] neighbours = cells[i][j].getNeighbours();
				int neighboursMineCount = 0;
				for (Cell cell : neighbours)
					if (cell.getMine())
						neighboursMineCount++;
				cells[i][j].setNeighboursMineCount(neighboursMineCount);
			}
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	public int getMineCount()
	{
		return mineCount;
	}
	public boolean getIsFirstOpen()
	{
		return firstOpen;
	}
	public int getFlagSetted()
	{
		int count = 0;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (cells[i][j].getStateMark() == StateMark.FLAG || cells[i][j].getStateMark() == StateMark.MINEFLAG || cells[i][j].getStateMark() == StateMark.MINEWRONG)
					count++;
		return count;
	}
	public Cell getCell(int x, int y)
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;
		return cells[x][y];
	}
	/**
	 * 
	 * @param cell
	 *            - cell which should open
	 * @return true - if cell was opened normally or cell has something which differs from NONE and QUESTION; false - if cell has a mine.
	 */
	public boolean open(Cell cell)
	{
		if (firstOpen == true)
		{
			gameModel.gameIsStarted();
			firstOpen = false;
			if (isFilledField == false)
			{
				isFilledField = true;
				int value3BV;
				do
				{
					fillFieldRandomly(cell.getX(), cell.getY());
					value3BV = compute3BV();
				}
				while (value3BV < bottom3BV || value3BV > top3BV);
			}
		}

		if (cell.getStateMark() != StateMark.NONE && cell.getStateMark() != StateMark.QUESTION)
			return true;

		if (cell.getMine())
			return false;
		cell.setStateMark(StateMark.OPEN);
		if (cell.getNeighboursMineCount() == 0)
		{
			Cell[] neighbours = cell.getNeighbours();
			for (Cell c : neighbours)
				open(c);
		}
		return true;
	}
	public void open(int x, int y)
	{
		open(cells[x][y]);
	}
	public Cell[][] getCells()
	{
		return cells;
	}
	/**
	 * 
	 * @see <a href="http://www.minesweeper.info/wiki/3BV">3BV - MinesweeperWiki</a> for detailed information
	 */
	public int compute3BV()
	{
		int value3BV = 1;
		int[][] field = new int[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (cells[i][j].getNeighboursMineCount() == 0 && cells[i][j].getMine() == false)
					if (field[i][j] == 0)
						recursiveOpen(i, j, value3BV++, field);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
			{
				if (cells[i][j].getMine() == false)
					if (field[i][j] == 0)
						recursiveOpen(i, j, value3BV++, field);
			}
		return value3BV - 1;
	}
	public void recursiveOpen(int x, int y, int value3BV, int[][] field)
	{
		field[x][y] = value3BV;
		Cell cell = getCell(x, y);
		if (cell.getNeighboursMineCount() == 0)
			for (Cell c : cell.getNeighbours())
				if (field[c.getX()][c.getY()] == 0 && c.getMine() == false)
					recursiveOpen(c.getX(), c.getY(), value3BV, field);

	}
	public void resetField(boolean sameArrangement)
	{
		firstOpen = true;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				cells[i][j].setStateMark(StateMark.NONE);
		
		if (sameArrangement == false)
			isFilledField = false;
	}
}