package ru.nsu.ccfit.serdyukov.minesweeper;

import java.awt.Rectangle;

public class Coord
{
	private int x;
	private int y;

	public Coord(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public boolean isInsideRect(Rectangle rect)
	{
		return (x >= rect.getX() && x < rect.getWidth() + rect.getX() && y >= rect.getY() && y < rect.getHeight() + rect.getY());
	}
}