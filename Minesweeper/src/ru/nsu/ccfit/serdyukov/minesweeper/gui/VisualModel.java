package ru.nsu.ccfit.serdyukov.minesweeper.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Vector;
import ru.nsu.ccfit.serdyukov.minesweeper.Cell;
import ru.nsu.ccfit.serdyukov.minesweeper.GameModel;
import ru.nsu.ccfit.serdyukov.minesweeper.resources.ImageResources;
import static ru.nsu.ccfit.serdyukov.minesweeper.gui.VisibleMark.*;

//define dynamic cells state
abstract public class VisualModel
{
	protected GameModel gameModel;
	protected VisibleMark[][] marks;
	protected int width;
	protected int height;
	protected int visualStyle;

	public VisualModel(GameModel model)
	{
		this.gameModel = model;
		this.width = model.getField().getWidth();
		this.height = model.getField().getHeight();
		marks = new VisibleMark[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				marks[i][j] = new VisibleMark();
	}

	public VisibleMark[][] getVisibleMarks()
	{
		return marks;
	}
	public void synchronize()
	{
		Cell[][] cells = gameModel.getField().getCells();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
			{
				marks[i][j].setPressed(VisibleMark.NOTPRESSED);
				marks[i][j].setBackLight(VisibleMark.NOBACKLIGHT);
				marks[i][j].setShadows(VisibleMark.NOSHADOW);

				switch (cells[i][j].getStateMark())
				{
				case OPEN:
					marks[i][j].setMarkType(ZERO + cells[i][j].getNeighboursMineCount());
					marks[i][j].setPressed(PRESSED);
					break;
				case QUESTION:
					marks[i][j].setMarkType(QUESTION);
					break;
				case FLAG:
					marks[i][j].setMarkType(FLAG);
					break;
				case NONE:
					marks[i][j].setMarkType(NONE);
					break;
				case MINE:
					marks[i][j].setMarkType(MINE);
					marks[i][j].setPressed(PRESSED);
					break;
				case MINEFLAG:
					marks[i][j].setMarkType(MINEFLAG);
					marks[i][j].setPressed(PRESSED);
					break;
				case MINEQUEST:
					marks[i][j].setMarkType(MINEQUEST);
					marks[i][j].setPressed(PRESSED);
					break;
				case MINEWRONG:
					marks[i][j].setMarkType(MINEWRONG);
					marks[i][j].setPressed(PRESSED);
					break;
				case MINEEXPLOSIONCAUSE:
					marks[i][j].setMarkType(MINERED);
					marks[i][j].setPressed(PRESSED);
					break;
				case MINENOMARK:
					marks[i][j].setMarkType(MINENOMARK);
					break;
				case MINENOMARKQUEST:
					marks[i][j].setMarkType(MINENOMARKQUEST);
					break;
				}
			}
	}
	public VisibleMark getVisibleMark(int x, int y)
	{
		if (x >= 0 && x < width && y >= 0 && y < height)
			return marks[x][y];
		return null;
	}
	public VisibleMark[] getMarkAndNeighbours(int x, int y)
	{
		Vector<VisibleMark> vmark = new Vector<VisibleMark>();

		vmark.add(getVisibleMark(x, y));
		for (int[] ar : Cell.neighbourOffsets)
		{
			VisibleMark mark;
			if ((mark = getVisibleMark(x + ar[0], y + ar[1])) != null)
				vmark.add(mark);
		}
		return vmark.toArray(new VisibleMark[0]);
	}
	protected int toNormalizedValue(double value)
	{
		if (value > 999)
			return 999;
		return (int) value;
	}
	public void drawGameField(BufferedImage buffer, Rectangle fieldRect, int cellLength)
	{
		Graphics g = buffer.getGraphics();
		for (int i = 0; i < gameModel.getField().getWidth(); i++)
			for (int j = 0; j < gameModel.getField().getHeight(); j++)
				drawCell(g, fieldRect.x + i * cellLength, fieldRect.y + j * cellLength, cellLength, marks[i][j]);
	}
	public int getVisualStyle()
	{
		return visualStyle;
	}
	public void destroy()
	{
	}
	public void initVisualMarks(VisualModel visualModel)
	{
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				marks[i][j] = visualModel.getVisibleMark(i, j);
	}
	protected Image getImage(int id)
	{
		return ImageResources.getImage(visualStyle, id);
	}
	protected Image getImage(VisibleMark mark)
	{
		return ImageResources.getImage(visualStyle, mark);
	}
	abstract public void drawCell(Graphics g, int x, int y, int cellLength, VisibleMark vmark);
	abstract public int validateVisualSizes(int cellLength, Insets insets);
	abstract public void prepareBuffer(BufferedImage buffer, Rectangle fieldRect, int cellLength);
}

class VisualModelFactory
{
	public static VisualModel createVisualModel(int style, GameModel model, GameCanvas canvas)
	{
		if (style == ImageResources.SEVENSTYLE)
			return new SevenStyleVisualModel(model);
		return new CustomStyleVisualModel(model, style, canvas);
	}
}