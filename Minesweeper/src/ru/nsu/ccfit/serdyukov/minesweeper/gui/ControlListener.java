package ru.nsu.ccfit.serdyukov.minesweeper.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import ru.nsu.ccfit.serdyukov.minesweeper.Coord;
import ru.nsu.ccfit.serdyukov.minesweeper.GameModel;

public class ControlListener implements MouseMotionListener, MouseListener
{
	private VisualModel visualModel;
	private GameModel gameModel;
	private GameCanvas canvas;
	private boolean lastPressedLeftForReveal;

	private boolean lastLeftPressedInsideField;
	private boolean lastRightPressedInsideField;

	private boolean leftPressed;
	private boolean rightPressed;

	private boolean cellsRevealed;

	public ControlListener(GameCanvas canvas)
	{
		this.visualModel = canvas.getVisualModel();
		this.canvas = canvas;

		gameModel = canvas.getGameModel();
		leftPressed = false;
		rightPressed = false;

		lastLeftPressedInsideField = false;
		lastRightPressedInsideField = false;

		cellsRevealed = false;
		lastPressedLeftForReveal = false;
	}
	public void changeVisualModel()
	{
		this.visualModel = canvas.getVisualModel();
	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2)
		{
			Coord coord = new Coord(e.getX(), e.getY());
			Coord cellCoord = canvas.getCellWhosePoint(coord);
			if (cellCoord != null)
				gameModel.markCell(GameModel.REVEAL, cellCoord.getX(), cellCoord.getY());
			updateCanvas(e);
		}
	}
	@Override
	public void mouseEntered(MouseEvent e)
	{
		processMoveOnPoint(new Coord(e.getX(),e.getY()));
	}
	@Override
	public void mouseExited(MouseEvent e)
	{
		canvas.updateCanvas();
	}
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			leftPressed = true;
			lastLeftPressedInsideField = new Coord(e.getX(), e.getY()).isInsideRect(canvas.getFieldRect());

			processMoveOnPoint(new Coord(e.getX(), e.getY()));
		}
		else if (e.getButton() == MouseEvent.BUTTON3)
		{
			rightPressed = true;
			lastRightPressedInsideField = new Coord(e.getX(), e.getY()).isInsideRect(canvas.getFieldRect());

			Coord coord = new Coord(e.getX(), e.getY());
			Coord cellCoord = canvas.getCellWhosePoint(coord);
			if (leftPressed == false)
				if (cellCoord != null)
					gameModel.markCell(GameModel.MARKCHANGE, cellCoord.getX(), cellCoord.getY());
			processMoveOnPoint(coord);
		}

		if (leftPressed && rightPressed)
			lastPressedLeftForReveal = true;
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
			leftPressed = false;
		else if (e.getButton() == MouseEvent.BUTTON3)
			rightPressed = false;

		Coord coord = new Coord(e.getX(), e.getY());
		Coord cellCoord = canvas.getCellWhosePoint(coord);

		if (cellCoord != null && cellsRevealed && (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3))
		{
			gameModel.markCell(GameModel.REVEAL, cellCoord.getX(), cellCoord.getY());
			cellsRevealed = false;
		}

		if (cellCoord != null && lastPressedLeftForReveal == false && e.getButton() == MouseEvent.BUTTON1 && rightPressed == false && lastLeftPressedInsideField)
			gameModel.markCell(GameModel.OPEN, cellCoord.getX(), cellCoord.getY());

		processMoveOnPoint(coord);
		if (e.getButton() == MouseEvent.BUTTON1)
			lastPressedLeftForReveal = false;
	}
	@Override
	public void mouseDragged(MouseEvent e)
	{
		updateCanvas(e);
	}
	@Override
	public void mouseMoved(MouseEvent e)
	{
		updateCanvas(e);
	}
	void updateCanvas(MouseEvent e)
	{
		if (new Coord(e.getX(), e.getY()).isInsideRect(canvas.getFieldRect()) == false)
			canvas.updateCanvas();
		else
			processMoveOnPoint(new Coord(e.getX(), e.getY()));
	}
	public void processMoveOnPoint(Coord coord)
	{
		Coord cellCoord = canvas.getCellWhosePoint(coord);
		if (cellCoord == null)
			return;

		int cx = cellCoord.getX();
		int cy = cellCoord.getY();
		visualModel.synchronize();

		if ((lastLeftPressedInsideField || lastRightPressedInsideField) && (leftPressed && rightPressed))
		{
			cellsRevealed = true;
			VisibleMark[] marks = visualModel.getMarkAndNeighbours(cx, cy);
			for (VisibleMark mark : marks)
				mark.setPressed(VisibleMark.PRESSED);

		}
		else if (lastLeftPressedInsideField && leftPressed && lastPressedLeftForReveal == false)
			visualModel.getVisibleMark(cx, cy).setPressed(VisibleMark.PRESSED);
		else
			visualModel.getVisibleMark(cx, cy).setBackLight(VisibleMark.BACKLIGHT);

		if (gameModel.isLoss() == true || gameModel.isWin() == true)
		{
			canvas.removeController();
			canvas.updateCanvas();
			gameModel.getUserInterface().gameOver(gameModel.isWin());
		}
		else
		{
			canvas.updateBuffer();
			canvas.repaint();
		}
	}
}