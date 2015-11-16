package ru.nsu.ccfit.serdyukov.minesweeper;

public class StopWatch
{
	private long total;
	private long start;
	private boolean running;
	private boolean isPaused;

	public StopWatch()
	{
		reset();
	}
	public void reset()
	{
		total = 0;
		start = 0;
		running = false;
		isPaused = false;
	}
	public void stop()
	{
		total += System.currentTimeMillis() - start;
		running = false;
	}
	public void start()
	{
		start = System.currentTimeMillis();
		running = true;
	}
	public void pause()
	{
		if (running == true)
		{
			total += System.currentTimeMillis() - start;
			isPaused = true;
			running = false;
		}
	}
	public void resume()
	{
		if (isPaused == true)
		{
			start = System.currentTimeMillis();
			running = true;
			isPaused = false;
		}
	}
	public double elapsedTime()
	{
		if (running == true)
			return (System.currentTimeMillis() - start + total) / 1000.0;
		return total / 1000.0;
	}
}