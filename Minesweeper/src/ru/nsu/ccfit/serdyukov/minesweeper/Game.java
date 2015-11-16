package ru.nsu.ccfit.serdyukov.minesweeper;

import java.io.IOException;
import ru.nsu.ccfit.serdyukov.minesweeper.gui.GraphicalUI;
import ru.nsu.ccfit.serdyukov.minesweeper.resources.InvalidSkinFileException;
import ru.nsu.ccfit.serdyukov.minesweeper.text.TextUI;

public class Game
{
	public static final int TEXT = 0;
	public static final int GRAPHIC = 1;
	
	private UI ui = null;
	public Game(int type) throws IOException, InvalidSkinFileException
	{
		if (type == TEXT)
			ui = new TextUI(); 
		else
			ui = new GraphicalUI();
	}
	public UI getUI()
	{
		return ui;
	}

}