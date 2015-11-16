package ru.nsu.ccfit.serdyukov.minesweeper;

public interface UI
{
	public void gameOver(boolean win);
	public void createGame(boolean sameArrangment);
	public void updateGameState();
	public void quitGame();
}
