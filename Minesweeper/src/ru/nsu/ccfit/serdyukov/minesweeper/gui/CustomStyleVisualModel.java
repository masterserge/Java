package ru.nsu.ccfit.serdyukov.minesweeper.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import ru.nsu.ccfit.serdyukov.minesweeper.Coord;
import ru.nsu.ccfit.serdyukov.minesweeper.GameModel;
import ru.nsu.ccfit.serdyukov.minesweeper.resources.ImageResources;

public class CustomStyleVisualModel extends VisualModel implements MouseListener, MouseMotionListener
{
	private final int panelHeight = 33;
	private final int cornerWidth = 12;
	private final int cornerHeight = 11;
	private final int cellLength = 16;
	private final int digitWidth = 11;
	private final int smileLength = 26;

	private int tableWidth;

	private int smileX;
	private int smileY;

	private GameCanvas canvas;
	private boolean pressed;
	private boolean pressedOnSmile;
	private boolean pressedSmile;

	public CustomStyleVisualModel(GameModel model, int style, GameCanvas canvas)
	{
		super(model);
		this.visualStyle = style;
		this.canvas = canvas;
		this.pressed = false;
		pressedOnSmile = false;
		pressedSmile = false;
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}
	public void validateShadows()
	{
	}
	public void drawCell(Graphics g, int x, int y, int cellLength, VisibleMark vmark)
	{
		VisibleMark mark = vmark.clone();
		mark.setBackLight(VisibleMark.NOBACKLIGHT);
		mark.setShadows(VisibleMark.NOSHADOW);
		Image img = getImage(mark);

		assert (img != null);

		g.drawImage(img, x, y, cellLength, cellLength, null);
	}
	public void drawGameField(BufferedImage buffer, Rectangle fieldRect, int cellLength)
	{
		super.drawGameField(buffer, fieldRect, cellLength);
		Graphics g = buffer.getGraphics();
		if (pressed == true && pressedOnSmile == false)
			g.drawImage(getImage(ImageResources.SMILEOH), smileX, smileY, null);
		else
			g.drawImage(getImage(ImageResources.SMILEOK), smileX, smileY, null);

		if (gameModel.isLoss() == true)
			g.drawImage(getImage(ImageResources.SMILEBAD), smileX, smileY, null);
		if (gameModel.isWin() == true)
			g.drawImage(getImage(ImageResources.SMILEWIN), smileX, smileY, null);
		if (pressedSmile == true)
			g.drawImage(getImage(ImageResources.SMILEOKPRESSED), smileX, smileY, null);

		int time = toNormalizedValue(gameModel.getElapsedTime());
		int mines = (gameModel.getField().getMineCount() - gameModel.getField().getFlagSetted());
		drawNumber(g, cornerWidth + 4, cornerHeight + 4, time);
		drawNumber(g, cornerWidth + fieldRect.width - tableWidth - 4, cornerHeight + 4, mines);
	}
	public void drawNumber(Graphics g, int startX, int startY, int number)
	{
		boolean neg = number < 0;
		number = Math.abs(number);
		int n0 = number % 10;
		int n1 = (number / 10) % 10;
		int n2 = (number / 100) % 10;
		g.drawImage(getImage(ImageResources.DIGIT0 + n2), startX + 2, startY + 2, null);
		g.drawImage(getImage(ImageResources.DIGIT0 + n1), startX + digitWidth + 2 * 2, startY + 2, null);
		g.drawImage(getImage(ImageResources.DIGIT0 + n0), startX + 2 * digitWidth + 3 * 2, startY + 2, null);

		if (neg)
			g.drawImage(getImage(ImageResources.DIGITMINUS), startX + 2, startY + 2, null);
	}
	public void validateSmilePressed(Coord coord)
	{
		pressedSmile = false;
		if (pointInsideSmile(coord) && pressedOnSmile == true)
			pressedSmile = true;

	}
	@Override
	public int validateVisualSizes(int cellLength, Insets insets)
	{
		insets.top = panelHeight + 2 * cornerHeight;
		insets.left = insets.right = cornerWidth;
		insets.bottom = cornerHeight;
		return this.cellLength;
	}
	@Override
	public void prepareBuffer(BufferedImage buffer, Rectangle fieldRect, int cellLength)
	{
		Graphics g = buffer.getGraphics();
		g.drawImage(getImage(ImageResources.TOPLEFTCORNER), 0, 0, null);

		for (int i = 0; i < fieldRect.width; i++)
		{
			g.drawImage(getImage(ImageResources.HORTOPFILLER), i + cornerWidth, 0, null);
			g.drawImage(getImage(ImageResources.HORMIDDLEFILLER), i + cornerWidth, cornerHeight + panelHeight, null);
			g.drawImage(getImage(ImageResources.HORBOTTOMFILLER), i + cornerWidth, 2 * cornerHeight + panelHeight + fieldRect.height, null);
		}

		g.drawImage(getImage(ImageResources.TOPRIGHTCORNER), cornerWidth + fieldRect.width, 0, null);

		for (int i = 0; i < panelHeight; i++)
		{
			g.drawImage(getImage(ImageResources.VERTTOPLEFTFILLER), 0, cornerHeight + i, null);
			g.drawImage(getImage(ImageResources.VERTTOPRIGHTFILLER), cornerWidth + fieldRect.width, cornerHeight + i, null);
		}
		g.drawImage(getImage(ImageResources.MIDDLELEFTCORNER), 0, cornerHeight + panelHeight, null);
		g.drawImage(getImage(ImageResources.MIDDLERIGHTCORNER), cornerWidth + fieldRect.width, cornerHeight + panelHeight, null);
		for (int i = 0; i < fieldRect.height; i++)
		{
			g.drawImage(getImage(ImageResources.VERTBOTTOMLEFTFILLER), 0, 2 * cornerHeight + panelHeight + i, null);
			g.drawImage(getImage(ImageResources.VERTBOTTOMRIGHTFILLER), cornerWidth + fieldRect.width, 2 * cornerHeight + panelHeight + i, null);
		}

		g.drawImage(getImage(ImageResources.BOTTOMLEFTCORNER), 0, 2 * cornerHeight + panelHeight + fieldRect.height, null);
		g.drawImage(getImage(ImageResources.BOTTOMRIGHTCORNER), cornerWidth + fieldRect.width, 2 * cornerHeight + panelHeight + fieldRect.height, null);

		g.drawImage(getImage(ImageResources.PIXELFILLER), cornerWidth, cornerHeight, fieldRect.width, panelHeight, null);

		Image img = getImage(ImageResources.PANEL);
		g.drawImage(img, cornerWidth + 4, cornerHeight + 4, null);

		tableWidth = img.getWidth(null);
		g.drawImage(img, cornerWidth + fieldRect.width - tableWidth - 4, cornerHeight + 4, null);

		Image smile = getImage(ImageResources.SMILEOK);
		smileX = (fieldRect.width - smile.getWidth(null)) / 2 + cornerWidth;
		smileY = (panelHeight - smile.getHeight(null)) / 2 + cornerHeight;
		g.drawImage(smile, smileX, smileY, null);
	}
	public boolean pointInsideSmile(Coord coord)
	{
		return coord.isInsideRect(new Rectangle(smileX, smileY, smileLength, smileLength));
	}

	public void destroy()
	{
		canvas.removeMouseListener(this);
		canvas.removeMouseMotionListener(this);
	}
	@Override
	public void mouseDragged(MouseEvent e)
	{
		validateSmilePressed(new Coord(e.getX(), e.getY()));
		canvas.updateBuffer();
		canvas.repaint();
	}
	@Override
	public void mouseMoved(MouseEvent e)
	{
	}
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			pressed = true;
			if (pointInsideSmile(new Coord(e.getX(), e.getY())) == true)
				pressedOnSmile = true;
			validateSmilePressed(new Coord(e.getX(), e.getY()));
			canvas.updateCanvas();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (pointInsideSmile(new Coord(e.getX(), e.getY())) == true && pressedOnSmile == true)
			gameModel.getUserInterface().createGame(false);
		pressedOnSmile = false;
		pressedSmile = false;
		pressed = false;
		canvas.updateCanvas();

	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
	}
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
}
