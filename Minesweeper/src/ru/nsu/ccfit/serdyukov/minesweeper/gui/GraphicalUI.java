package ru.nsu.ccfit.serdyukov.minesweeper.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import ru.nsu.ccfit.serdyukov.minesweeper.*;
import ru.nsu.ccfit.serdyukov.minesweeper.resources.ImageResources;
import ru.nsu.ccfit.serdyukov.minesweeper.resources.InvalidSkinFileException;

public class GraphicalUI implements UI, ActionListener
{
	public static final Color BACKGROUNDCOLOR = new Color(185, 190, 205);

	private GameModel model;
	private GameSettings gameSettings;

	private JFrame jf;
	private GameCanvas canvas;
	private Menu menu;

	public GraphicalUI() throws IOException, InvalidSkinFileException
	{
		ImageResources.initResources();
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		jf = new JFrame("Minesweeper");
		jf.setResizable(false);
		jf.setLocation(50, 50);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.setBackground(BACKGROUNDCOLOR);
		jf.setIconImage(ImageResources.getIcon());

		jf.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowIconified(WindowEvent arg0)
			{
				model.pauseGame();
			}
			@Override
			public void windowDeiconified(WindowEvent arg0)
			{
				model.resumeGame();
			}
			public void windowClosing(WindowEvent e)
			{
				jf.dispose();
			}
			public void windowClosed(WindowEvent e)
			{
				gameSettings.writeSettings();
				System.exit(0);
			}
		});

		gameSettings = new GameSettings();

		menu = new Menu(this);
		menu.setMenuItemStyle(gameSettings.getVisualStyle());

		jf.setJMenuBar(menu.getJMenuBar());

		canvas = null;
		model = null;
		createGame(false);
		jf.pack();
		jf.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if (cmd.equals("Exit"))
			jf.dispose();
		else if (cmd.equals("New game"))
			createGame(false);
		else if (cmd.equals("High scores"))
		{
			JDialog dlg = new HighScoreDialog(gameSettings);
			centerDialog(dlg);
			dlg.setVisible(true);
		}
		else if (cmd.equals("Options"))
		{
			JDialog dlg = new OptionDialog(gameSettings, this);
			centerDialog(dlg);
			dlg.setVisible(true);
		}
		else if (cmd.equals("Seven skin"))
		{
			jf.remove(canvas);
			gameSettings.setVisualStyle(ImageResources.SEVENSTYLE);
			canvas.changeVisualStyle(ImageResources.SEVENSTYLE);
			jf.add(canvas);
			jf.pack();
		}
		else if (cmd.equals("XP skin"))
		{
			jf.remove(canvas);
			gameSettings.setVisualStyle(ImageResources.XPSTYLE);
			canvas.changeVisualStyle(ImageResources.XPSTYLE);
			jf.add(canvas);
			jf.pack();
		}
		else if (cmd.equals("Custom skin..."))
		{
			JFileChooser fileChooser = new JFileChooser();
			File f = null;
			try
			{
				f = new File(new File(".").getCanonicalPath() + "/skins");
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}

			fileChooser.setCurrentDirectory(f);
			fileChooser.setFileFilter(new FileFilter()
			{
				@Override
				public String getDescription()
				{
					return "*.bmp";
				}

				@Override
				public boolean accept(File f)
				{
					if (f.getName().endsWith(".bmp") || f.isDirectory())
						return true;
					return false;
				}
			});
			if (fileChooser.showOpenDialog(jf) == JFileChooser.APPROVE_OPTION)
				if (fileChooser.getSelectedFile().exists())
				{

					gameSettings.setVisualStyle(ImageResources.CUSTOMSTYLE);
					try
					{
						ImageResources.initCustomStyle(new FileInputStream(fileChooser.getSelectedFile().getAbsolutePath()), ImageResources.CUSTOMSTYLE);
						jf.remove(canvas);
						canvas.changeVisualStyle(ImageResources.CUSTOMSTYLE);
						jf.add(canvas);
						jf.pack();
					}
					catch (Exception e1)
					{
						JOptionPane.showMessageDialog(jf, "Can't load selected skin.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			menu.setMenuItemStyle(gameSettings.getVisualStyle());
		}
		else if (cmd.equals("About"))
			JOptionPane.showMessageDialog(jf, "Minesweeper v.1.1\nDeveloped by Serdyukov Sergey\nNSU FIT 0201\n07.03.2012", "About", JOptionPane.INFORMATION_MESSAGE);
	}
	public void centerDialog(JDialog dlg)
	{
		Point point = jf.getLocation();
		Dimension size = dlg.getSize();
		dlg.setLocation((int) (point.getX() + (jf.getWidth() - size.width) / 2), (int) (point.getY() + (jf.getHeight() - size.height) / 2));
		Point p = dlg.getLocation();
		p.x = Math.max(0, p.x);
		p.y = Math.max(0, p.y);
		dlg.setLocation(p);

	}
	public void gameOver(boolean win)
	{
		Record record = gameSettings.registerGame(model);
		if (gameSettings.getVisualStyle() == ImageResources.SEVENSTYLE)
		{
			GameOverDialog dlg = new GameOverDialog(win, gameSettings, record, this, model.getGameType());
			centerDialog(dlg);
			dlg.setVisible(true);
		}
		else if (win == true && record != null)
		{
			JDialog dlg = new NickNameDialog(gameSettings, record);
			centerDialog(dlg);
			dlg.setVisible(true);
		}
	}
	public void createGame(boolean sameArrangment)
	{
		if (model != null && model.isLoss() == false && model.isWin() == false)
		{
			if (model.isGameWasStarted() == true)
				gameSettings.registerGame(model);
			canvas.removeController();
		}
		try
		{
			if (gameSettings.isSuitableModel(model) == false)
			{
				model = new GameModel(gameSettings, this);
				if (canvas != null)
					jf.remove(canvas);
				canvas = new GameCanvas(model, gameSettings.getVisualStyle());
				jf.add(canvas);
				jf.pack();

			}
			else
			{
				model.restartGame(sameArrangment);
				canvas.addController();
			}
		}
		catch (InvalidFieldSettingsException e)
		{
			e.printStackTrace();
		}
		canvas.updateCanvas();
	}
	public void updateGameState()
	{
		canvas.updateBuffer();
		canvas.repaint();
	}
	public GameModel getGameModel()
	{
		return model;
	}
	@Override
	public void quitGame()
	{
		jf.dispose();
	}
}

class Menu
{
	private JMenuBar jbar;
	private JMenuItem jmi7Style;
	private JMenuItem jmiXPStyle;
	private JMenuItem jmiCustom;
	private ActionListener al;

	public Menu(ActionListener al)
	{
		this.al = al;
		jbar = createJMenuBar();
	}
	public JMenuBar getJMenuBar()
	{
		return jbar;
	}
	private JMenuBar createJMenuBar()
	{

		JMenuBar jmb = new JMenuBar();

		JMenu jmGame = new JMenu("Game");

		JMenuItem jmiOpen = new JMenuItem("New game");
		JMenuItem jmiHighScores = new JMenuItem("High scores");
		JMenuItem jmiOptions = new JMenuItem("Options");
		JMenu jmiAppearance = new JMenu("Change Appearance");
		JMenuItem jmiExit = new JMenuItem("Exit");

		jmGame.add(jmiOpen);
		jmGame.addSeparator();
		jmGame.add(jmiOptions);
		jmGame.add(jmiHighScores);
		jmGame.add(jmiAppearance);
		jmGame.addSeparator();
		jmGame.add(jmiExit);

		jmi7Style = new JCheckBoxMenuItem("Seven skin");
		jmiXPStyle = new JCheckBoxMenuItem("XP skin");
		jmiCustom = new JCheckBoxMenuItem("Custom skin...");

		ButtonGroup bg = new ButtonGroup();
		bg.add(jmi7Style);
		bg.add(jmiXPStyle);
		bg.add(jmiCustom);

		jmiAppearance.add(jmi7Style);
		jmiAppearance.add(jmiXPStyle);
		jmiAppearance.addSeparator();
		jmiAppearance.add(jmiCustom);
		jmb.add(jmGame);

		JMenu jmHelp = new JMenu("Help");
		JMenuItem jmiAbout = new JMenuItem("About");

		jmHelp.add(jmiAbout);
		jmb.add(jmHelp);

		jmiOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		jmiOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		jmiHighScores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		jmiAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

		jmiOpen.addActionListener(al);
		jmiHighScores.addActionListener(al);
		jmiOptions.addActionListener(al);
		jmiAppearance.addActionListener(al);
		jmiExit.addActionListener(al);
		jmiAbout.addActionListener(al);
		jmi7Style.addActionListener(al);
		jmiXPStyle.addActionListener(al);
		jmiCustom.addActionListener(al);

		return jmb;
	}
	public void setMenuItemStyle(int style)
	{
		switch (style)
		{
		case ImageResources.SEVENSTYLE:
			jmi7Style.setSelected(true);
			break;
		case ImageResources.XPSTYLE:
			jmiXPStyle.setSelected(true);
			break;
		case ImageResources.CUSTOMSTYLE:
			jmiCustom.setSelected(true);
			break;
		}
	}
}

class GameCanvas extends JComponent
{
	private static final long serialVersionUID = 1L;

	private BufferedImage buffer;

	private int cellLength;
	private int fieldWidth;
	private int fieldHeight;

	private Rectangle fieldRect;
	private int leftOffset;
	private int topOffset;
	private int rightOffset;
	private int bottomOffset;

	private Dimension size;

	private GameModel gameModel;
	private VisualModel visualModel;
	private ControlListener listener;

	public void updateBuffer()
	{
		visualModel.drawGameField(buffer, fieldRect, cellLength);
	}
	public void addController()
	{
		listener = new ControlListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
	public void removeController()
	{
		removeMouseListener(listener);
		removeMouseMotionListener(listener);
	}
	public GameCanvas(GameModel model, int visualStyle)
	{
		this.gameModel = model;
		visualModel = VisualModelFactory.createVisualModel(visualStyle, model, this);

		Insets insets = new Insets(0, 0, 0, 0);

		cellLength = visualModel.validateVisualSizes(cellLength, insets);

		leftOffset = insets.left;
		rightOffset = insets.right;
		topOffset = insets.top;
		bottomOffset = insets.bottom;

		fieldWidth = cellLength * model.getField().getWidth();
		fieldHeight = cellLength * model.getField().getHeight();

		fieldRect = new Rectangle(leftOffset, topOffset, fieldWidth, fieldHeight);

		size = new Dimension(fieldWidth + leftOffset + rightOffset, fieldHeight + topOffset + bottomOffset);
		setPreferredSize(size);

		buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);

		visualModel.prepareBuffer(buffer, fieldRect, cellLength);
		updateCanvas();
		
		addController();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(buffer, 0, 0, this);
	}
	public Rectangle getFieldRect()
	{
		return fieldRect;
	}
	public int getCellLength()
	{
		return cellLength;
	}
	public Coord getCellWhosePoint(Coord coord)
	{
		int x = coord.getX() - leftOffset;
		int y = coord.getY() - topOffset;
		if (x >= 0 && x < fieldWidth && y >= 0 && y < fieldHeight)
			return new Coord(x / cellLength, y / cellLength);
		return null;
	}
	public GameModel getGameModel()
	{
		return gameModel;
	}
	public VisualModel getVisualModel()
	{
		return visualModel;
	}
	public void updateCanvas()
	{
		visualModel.synchronize();
		updateBuffer();
		repaint();
	}
	public void changeVisualStyle(int newStyle)
	{
		if (visualModel.getVisualStyle() != newStyle)
		{
			VisualModel newModel = VisualModelFactory.createVisualModel(newStyle, gameModel, this);
			newModel.initVisualMarks(visualModel);

			visualModel.destroy();
			visualModel = newModel;

			Insets insets = new Insets(topOffset, leftOffset, bottomOffset, rightOffset);

			cellLength = visualModel.validateVisualSizes(cellLength, insets);

			leftOffset = insets.left;
			rightOffset = insets.right;
			topOffset = insets.top;
			bottomOffset = insets.bottom;

			fieldWidth = cellLength * gameModel.getField().getWidth();
			fieldHeight = cellLength * gameModel.getField().getHeight();

			fieldRect = new Rectangle(leftOffset, topOffset, fieldWidth, fieldHeight);

			size = new Dimension(fieldWidth + leftOffset + rightOffset, fieldHeight + topOffset + bottomOffset);
			setPreferredSize(size);

			buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);

			visualModel.prepareBuffer(buffer, fieldRect, cellLength);

			if (listener != null)
				listener.changeVisualModel();
			updateCanvas();

		}
	}
}
