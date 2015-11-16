package ru.nsu.ccfit.serdyukov.minesweeper.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import ru.nsu.ccfit.serdyukov.minesweeper.GameModel;
import ru.nsu.ccfit.serdyukov.minesweeper.resources.ImageResources;

public class SevenStyleVisualModel extends VisualModel
{
	private int clockSignLength;
	private int mineCounterSignLength;

	private int panelHeight;
	private int panelWidth;

	public SevenStyleVisualModel(GameModel model)
	{
		super(model);
		visualStyle = ImageResources.SEVENSTYLE;
	}
	public void validateShadows()
	{
		VisibleMark mark;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
			{
				boolean left = false;
				boolean top = false;
				mark = getVisibleMark(i - 1, j);
				if (mark == null || mark.isIndented() == false)
				{
					left = true;
					marks[i][j].setShadows(VisibleMark.LEFTSHADOW);
				}
				mark = getVisibleMark(i, j - 1);
				if (mark == null || mark.isIndented() == false)
				{
					top = true;
					marks[i][j].setShadows(VisibleMark.TOPSHADOW);
				}
				if (top && left)
					marks[i][j].setShadows(VisibleMark.BOTHSHADOWS);
				else if (top == false && left == false)
					marks[i][j].setShadows(VisibleMark.NOSHADOW);
			}
	}
	public void drawCell(Graphics g, int x, int y, int cellLength, VisibleMark vmark)
	{
		VisibleMark mark = vmark.clone();
		Image img = getImage(mark);

		boolean extraShadows = false;
		if (img == null)
		{
			extraShadows = true;
			mark.setShadows(VisibleMark.NOSHADOW);
			img = getImage(mark);
		}
		assert (img != null);
		g.drawImage(img, x, y, cellLength, cellLength, null);
		if (extraShadows == true)
		{
			if ((vmark.getMarkType() >= VisibleMark.ZERO && vmark.getMarkType() <= VisibleMark.EIGHT) || (vmark.getMarkType() == VisibleMark.NONE && vmark.getPressed() == VisibleMark.PRESSED))
			{
				if (vmark.getShadows() == VisibleMark.BOTHSHADOWS || vmark.getShadows() == VisibleMark.LEFTSHADOW)
				{
					img = ImageResources.getImage(visualStyle, ImageResources.LEFTSHADOWIMAGE);
					g.drawImage(img, x, y, (int) (img.getWidth(null) * cellLength / (double) img.getHeight(null)), cellLength, null);
				}
				if (vmark.getShadows() == VisibleMark.BOTHSHADOWS || vmark.getShadows() == VisibleMark.TOPSHADOW)
				{
					img = ImageResources.getImage(visualStyle, ImageResources.TOPSHADOWIMAGE);
					g.drawImage(img, x, y, cellLength, (int) (img.getHeight(null) * cellLength / (double) img.getWidth(null)), null);
				}
				return;
			}
			assert (true);
		}
	}
	public void drawGameField(BufferedImage buffer, Rectangle fieldRect, int cellLength)
	{
		validateShadows();
		super.drawGameField(buffer, fieldRect, cellLength);
		Graphics g = buffer.getGraphics();
		g.setColor(Color.white);
		g.setFont(new Font("Dialog", 0, cellLength));
		FontMetrics fm = g.getFontMetrics();
		String time = "" + toNormalizedValue(gameModel.getElapsedTime());

		int textHeight = (int) (fm.getAscent() * 3 / 4.0);

		// relative coordinates inside panel
		int textX = (panelWidth - fm.stringWidth(time)) / 2;
		int textY = (panelHeight - textHeight) / 2 + textHeight;

		// coordinates of time panel
		int beginX = fieldRect.x - cellLength / 3 + clockSignLength;
		int beginY = fieldRect.y + fieldRect.height + cellLength / 3;

		g.drawImage(ImageResources.getImage(visualStyle, ImageResources.PANEL7STYLE), beginX, beginY, panelWidth, panelHeight, null);
		g.drawString(time, textX + beginX, textY + beginY);

		String mines = "" + (gameModel.getField().getMineCount() - gameModel.getField().getFlagSetted());
		if (gameModel.isWin() == true)
			mines = "0";

		textX = (panelWidth - fm.stringWidth(mines)) / 2;

		beginX = fieldRect.x + fieldRect.width + cellLength / 3 - mineCounterSignLength - panelWidth;
		g.drawImage(ImageResources.getImage(visualStyle, ImageResources.PANEL7STYLE), beginX, beginY, panelWidth, panelHeight, null);
		g.drawString(mines, textX + beginX, textY + beginY);

	}
	public int validateVisualSizes(int cellLength, Insets insets)
	{
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int l1 = (d.width - 200) / width;
		int l2 = (d.height - 200) / height;
		cellLength = Math.min(Math.min(l1, l2), 30);

		insets.left = (int) (1 * cellLength);
		insets.right = (int) (1 * cellLength);
		insets.top = (int) (1 * cellLength);
		insets.bottom = (int) (2 * cellLength);

		mineCounterSignLength = (int) (1.5 * cellLength);
		clockSignLength = (int) (1.5 * cellLength);
		panelHeight = (int) (1.5 * cellLength);
		panelWidth = 3 * cellLength;
		return cellLength;
	}
	@Override
	public void prepareBuffer(BufferedImage buffer, Rectangle fieldRect, int cellLength)
	{
		Graphics g = buffer.getGraphics();

		g.setColor(GraphicalUI.BACKGROUNDCOLOR);
		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());

		int beginX = fieldRect.x - cellLength / 3 + clockSignLength;
		int beginY = fieldRect.y + fieldRect.height + cellLength / 3;

		g.drawImage(ImageResources.getImage(visualStyle, ImageResources.CLOCKSIGN), beginX, beginY, clockSignLength, clockSignLength, null);
		g.drawImage(ImageResources.getImage(visualStyle, ImageResources.PANEL7STYLE), beginX, beginY, panelWidth, panelHeight, null);
		beginX = fieldRect.x + fieldRect.width + cellLength / 3 - mineCounterSignLength - panelWidth;
		g.drawImage(ImageResources.getImage(visualStyle, ImageResources.MINESIGN), beginX, beginY, mineCounterSignLength, mineCounterSignLength, null);
		g.drawImage(ImageResources.getImage(visualStyle, ImageResources.PANEL7STYLE), beginX, beginY, panelWidth, panelHeight, null);
	}
}
