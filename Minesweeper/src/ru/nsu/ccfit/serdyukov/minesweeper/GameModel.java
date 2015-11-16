package ru.nsu.ccfit.serdyukov.minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GameModel
{
	public static final int OPEN = 0;
	public static final int MARKCHANGE = 1;
	public static final int REVEAL = 2;

	public static final int DELAYMILLS = 500;

	private int gameType;
	private GameField field;
	private UI ui;
	private StopWatch stopWatch;

	private boolean useQuestionMark;
	private boolean loss;
	private boolean win;

	public GameModel(GameSettings settings, UI userInterface) throws InvalidFieldSettingsException
	{
		this.field = new GameField(settings.getWidth(), settings.getHeight(), settings.getMineCount(), this);
		this.ui = userInterface;

		this.loss = false;
		this.win = false;
		this.useQuestionMark = settings.getUseQuestionMark();
		this.gameType = GameSettings.getGameType(field.getWidth(), field.getHeight(), field.getMineCount());
		stopWatch = new StopWatch();
	}
	public GameField getField()
	{
		return field;
	}
	public int getGameType()
	{
		return gameType;
	}
	public void restartGame(boolean sameArrangement)
	{
		loss = false;
		win = false;
		field.resetField(sameArrangement);
		stopWatch.reset();
	}
	public void markCell(int mark, int x, int y)
	{
		Cell cell = field.getCell(x, y);
		StateMark cellMark = cell.getStateMark();
		switch (mark)
		{
		case OPEN:
			if (field.open(cell) == false)
				mineWasRevealed(cell);
			break;
		case MARKCHANGE:
			switch (cellMark)
			{
			case NONE:
				cell.setStateMark(StateMark.FLAG);
				break;
			case FLAG:
				if (useQuestionMark == true)
					cell.setStateMark(StateMark.QUESTION);
				else
					cell.setStateMark(StateMark.NONE);
				break;
			case QUESTION:
				cell.setStateMark(StateMark.NONE);
				break;
			}
			break;
		case REVEAL:
			if (cell.getStateMark() == StateMark.OPEN)
			{
				Cell[] cells = cell.getNeighbours();
				int mineCount = 0;
				for (Cell c : cells)
					if (c.getStateMark() == StateMark.FLAG)
						mineCount++;
				if (mineCount == cell.getNeighboursMineCount())
				{
					for (Cell c : cells)
						if (c.getMine() == true && c.getStateMark() != StateMark.FLAG)
						{
							mineWasRevealed(c);
							break;
						}
					if (loss == false)
						for (Cell c : cells)
							field.open(c);
				}
			}
			break;
		}
		checkWin();
	}
	public void mineWasRevealed(Cell cell)
	{
		stopWatch.stop();

		loss = true;

		Cell[][] cells = field.getCells();
		for (int i = 0; i < field.getWidth(); i++)
			for (int j = 0; j < field.getHeight(); j++)
				if (cells[i][j].getMine() == true && cells[i][j].getStateMark() == StateMark.FLAG)
					cells[i][j].setStateMark(StateMark.MINEFLAG);
				else if (cells[i][j].getMine() == true && cells[i][j].getStateMark() == StateMark.QUESTION)
					cells[i][j].setStateMark(StateMark.MINEQUEST);
				else if (cells[i][j].getMine() == true && cells[i][j].getStateMark() != StateMark.FLAG)
					cells[i][j].setStateMark(StateMark.MINE);
				else if (cells[i][j].getMine() == false && cells[i][j].getStateMark() == StateMark.FLAG)
					cells[i][j].setStateMark(StateMark.MINEWRONG);
		cell.setStateMark(StateMark.MINEEXPLOSIONCAUSE);
	}
	public void checkWin()
	{
		if (loss == true)
			return;
		win = true;
		Cell[][] cells = field.getCells();
		for (int i = 0; i < field.getWidth(); i++)
			for (int j = 0; j < field.getHeight(); j++)
				if (cells[i][j].getMine() == false)
					if (cells[i][j].getStateMark() != StateMark.OPEN)
						win = false;

		if (win == true)
		{
			stopWatch.stop();
			for (int i = 0; i < field.getWidth(); i++)
				for (int j = 0; j < field.getHeight(); j++)
					if (cells[i][j].getMine() == true)
						if (cells[i][j].getStateMark() == StateMark.QUESTION)
							cells[i][j].setStateMark(StateMark.MINENOMARKQUEST);
						else if (cells[i][j].getStateMark() != StateMark.FLAG)
							cells[i][j].setStateMark(StateMark.MINENOMARK);
		}
	}
	public boolean isLoss()
	{
		return loss;
	}
	public boolean isWin()
	{
		return win;
	}
	public UI getUserInterface()
	{
		return ui;
	}
	public void gameIsStarted()
	{
		stopWatch.start();
		Timer timer = new Timer(DELAYMILLS, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ui.updateGameState();
			}
		});
		timer.start();
	}
	public double getElapsedTime()
	{
		return stopWatch.elapsedTime();
	}
	public void pauseGame()
	{
		stopWatch.pause();
	}
	public void resumeGame()
	{
		stopWatch.resume();
	}
	public boolean isGameWasStarted()
	{
		return field.getIsFirstOpen() == false;
	}
	public void setUseQuestionMark(boolean useQuestionMark)
	{
		this.useQuestionMark = useQuestionMark;
	}
}
