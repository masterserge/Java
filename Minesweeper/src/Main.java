import javax.swing.SwingUtilities;
import ru.nsu.ccfit.serdyukov.minesweeper.Game;

public class Main
{
	public static void main(String[] args)
	{
		final int gameType;
		if (args.length == 0 || args[0].equals("-t") == false)
			gameType = Game.GRAPHIC;
		else
			gameType = Game.TEXT;

		final ExceptionListener el = new ExceptionListener()
		{
			@Override
			public void exceptionCatched(Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		};
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					new Game(gameType);
				}
				catch (Exception e)
				{
					el.exceptionCatched(e);
				}
			}
		});
	}
}
interface ExceptionListener
{
	void exceptionCatched(Exception e);
}
