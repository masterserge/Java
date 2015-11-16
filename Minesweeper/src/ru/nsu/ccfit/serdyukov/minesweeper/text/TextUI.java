package ru.nsu.ccfit.serdyukov.minesweeper.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import ru.nsu.ccfit.serdyukov.minesweeper.GameModel;
import ru.nsu.ccfit.serdyukov.minesweeper.GameSettings;
import ru.nsu.ccfit.serdyukov.minesweeper.InvalidFieldSettingsException;
import ru.nsu.ccfit.serdyukov.minesweeper.Record;
import ru.nsu.ccfit.serdyukov.minesweeper.UI;

public class TextUI implements UI
{
	private GameModel model;
	private GameSettings gameSettings;
	private BufferedReader br;

	public TextUI()
	{
		gameSettings = new GameSettings();
		try
		{
			model = new GameModel(gameSettings, this);
		}
		catch (InvalidFieldSettingsException e)
		{
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				//
			}
		});
		br = new BufferedReader(new InputStreamReader(System.in));
		createGame(false);
	}
	@Override
	public void gameOver(boolean win)
	{
		if (win == true)
		{
			System.out.println("Congratulations, you won the game!");
			System.out.println("Time: " + model.getElapsedTime());
			Record record = null;
			if ((record = gameSettings.registerGame(model)) != null)
			{
				String[] placeStrings = { "", "second ", "third ", "fourth ", "fifth " };
				System.out.println("You have the " + placeStrings[record.getPlace()] + "fastest time for this difficulty level!");
				System.out.println("Please type your name:");
				String nick = "";
				try
				{
					nick = br.readLine();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				if (nick.equals(""))
					nick = "Anonymous";
				if (nick.length() > GameSettings.MAXNICKLENGTH)
					nick = nick.substring(0, GameSettings.MAXNICKLENGTH);
				record.setNick(nick);
			}
		}
		else
		{
			System.out.println("Sorry, you lost this game. Better luck next time!");
			System.out.println("Time: " + model.getElapsedTime());
		}
	}
	@Override
	public void createGame(boolean sameArrangment)
	{
		for (;;)
		{
			readGameParameters();

			if (model.isLoss() == false && model.isWin() == false)
				if (model.isGameWasStarted())
					gameSettings.registerGame(model);
			try
			{
				if (gameSettings.isSuitableModel(model) == false)
					model = new GameModel(gameSettings, this);
				else
					model.restartGame(sameArrangment);
			}
			catch (InvalidFieldSettingsException e)
			{
				e.printStackTrace();
			}

			printField();

			if (beginGame() == false)
				continue;

			System.out.println("Do you wish to play again(Y/N)");
			String str = null;
			try
			{
				str = br.readLine();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			str = str.trim();
			if (str.equals("y") == false && str.equals("Y") == false)
				break;
		}
		quitGame();

	}
	public void readGameParameters()
	{
		String str = null;
		System.out.println("Enter field parameters in format [width(" + GameSettings.MINWIDTH + "-" + GameSettings.MAXWIDTH + 
				")] [height(" + GameSettings.MINHEIGHT + "-" + GameSettings.MAXHEIGHT + ")] [mines(" + GameSettings.MINMINES+ "-" + GameSettings.MAXMINES + ")]");

		try
		{
			str = br.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		str = str.trim();

		String[] parts = str.split("\\s+");

		int width = 0, height = 0, mines = 0;
		if (parts.length == 3)
			try
			{
				width = Integer.parseInt(parts[0]);
				height = Integer.parseInt(parts[1]);
				mines = Integer.parseInt(parts[2]);

			}
			catch (NumberFormatException ex)
			{
				System.out.println("Field parameters is invalid. Will be used the previous parameters");

			}
		if (GameSettings.isValidParameters(width, height, mines) == true)
		{
			gameSettings.setWidth(width);
			gameSettings.setHeight(height);
			gameSettings.setMineCount(mines);
		}
		else
		{
			System.out.println("Field parameters is invalid. Will be used the previous parameters");
		}
	}
	private boolean beginGame()
	{
		String str = null;
		try
		{
			while ((str = br.readLine()) != null)
			{
				str = str.trim();
				if (processCommand(str) == true)
				{
					if (str.equals("New game"))
						return false;
					continue;
				}

				String[] words = str.split("\\s+");
				if (words.length != 3)
				{
					System.out.println("invalid command");
					continue;
				}
				int x;
				int y;
				try
				{
					x = Integer.parseInt(words[1]);
					y = Integer.parseInt(words[2]);
				}
				catch (NumberFormatException e)
				{
					System.out.println("invalid command");
					continue;
				}
				if (x < 0 || x >= model.getField().getWidth() || y < 0 || y >= model.getField().getHeight())
				{
					System.out.println("invalid coordinates");
					continue;
				}
				if (words[0].equals("o"))
					model.markCell(GameModel.OPEN, x, y);
				else if (words[0].equals("f"))
					model.markCell(GameModel.MARKCHANGE, x, y);
				else if (words[0].equals("r"))
					model.markCell(GameModel.REVEAL, x, y);
				else
				{
					System.out.println("invalid command");
					continue;
				}
				printField();
				if (model.isWin() == true || model.isLoss() == true)
				{
					gameOver(model.isWin());
					return true;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	@Override
	public void updateGameState()
	{
	}
	public void printField()
	{
		System.out.print("  ");
		for (int i = 0; i < model.getField().getWidth(); i++)
			System.out.print(i % 10 + " ");
		System.out.println();
		for (int j = 0; j < model.getField().getHeight(); j++)
		{
			System.out.print(j % 10 + " ");
			for (int i = 0; i < model.getField().getWidth(); i++)
			{
				switch (model.getField().getCells()[i][j].getStateMark())
				{
				case NONE:
					System.out.print("-");
					break;
				case OPEN:
					int mineCount = model.getField().getCells()[i][j].getNeighboursMineCount();
					if (mineCount == 0)
						System.out.print(' ');
					else
						System.out.print(mineCount);
					break;
				case FLAG:
					System.out.print('F');
					break;
				case QUESTION:
					System.out.print('?');
					break;
				case MINE:
				case MINEFLAG:
				case MINEQUEST:
				case MINEWRONG:
				case MINEEXPLOSIONCAUSE:
				case MINENOMARK:
				case MINENOMARKQUEST:
					System.out.print("*");
					break;
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	boolean processCommand(String str)
	{
		if (str.equals("about"))
		{
			System.out.println("Minesweeper v.1.1\nDeveloped by Serdyukov Sergey\nNSU FIT 0201\n07.03.2012");
		}
		else if (str.equals("highscores"))
		{
			for (int i = GameSettings.BEGINNER; i <= GameSettings.ADVANCED; i++)
			{
				System.out.println(GameSettings.difficulties[i]);
				for (Record r : gameSettings.getRecords(i))
					System.out.println(r.getNick() + " " + r.getTime());
				System.out.println();
			}
		}
		else if (str.equals("exit"))
		{
			quitGame();
		}
		else if (str.equals("new game"))
		{
			createGame(false);
		}
		else if (str.equals("help"))
			System.out.println("command format: o(open) | f(change mark none-flag-question) | r(reveal)  x y\nexit - quit from game" + 
		"\nabout - info about program\nnew game - begin new game\nhighscores - know best results");
		else
			return false;
		return true;
	}
	@Override
	public void quitGame()
	{
		gameSettings.writeSettings();
		System.exit(0);
	}
}
