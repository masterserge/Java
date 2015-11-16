package ru.nsu.ccfit.serdyukov.minesweeper.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import ru.nsu.ccfit.serdyukov.minesweeper.GameSettings;
import ru.nsu.ccfit.serdyukov.minesweeper.Record;
import javax.swing.BoxLayout;

public class GameOverDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final JPanel titlePanel = new JPanel();
	private JLabel[] labels;
	private JButton exitButton;
	private JButton restartButton;
	private JButton playAgainButton;
	private JTextField nickTextField;
	private Record record;
	private GraphicalUI gui;

	public GameOverDialog(boolean win, GameSettings gameSettings, final Record record, final GraphicalUI gui, int gameType)
	{
		this.gui = gui;
		this.record = record;
		boolean highScore = false;
		if (win == true)
		{
			if (record != null)
				highScore = true;
			setTitle("Game Won");
		}
		else
			setTitle("Game Lost");
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		{
			labels = new JLabel[9];
			for (int i = 0; i < 9; i++)
			{
				labels[i] = new JLabel();
				labels[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			}
			{
				labels[0].setHorizontalAlignment(SwingConstants.CENTER);
				labels[0] = new JLabel("Sorry, you lost this game. Better luck next time!");
				if (win == true)
					labels[0] = new JLabel("Congratulations, you won the game!");
			}

			if (gameType != GameSettings.CUSTOM)
			{
				String[] placeStrings = { "", "second ", "third ", "fourth ", "fifth " };
				if (win == true && highScore == true)
				{
					labels[1].setText("You have the " + placeStrings[record.getPlace()] + "fastest time for this difficulty level!");
					labels[2].setText("Please type your name:");
				}
				labels[1].setHorizontalAlignment(SwingConstants.CENTER);

				labels[3].setText("Time: " + gui.getGameModel().getElapsedTime() + " sec.");

				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String dateNow = formatter.format(Calendar.getInstance().getTime());
				labels[4].setText("Date: " + dateNow);

				labels[5].setText("Best time: " + gameSettings.getBestTime(gameType) + " sec.");
				labels[6].setText("Games played: " + gameSettings.getTotalGamePlayed(gameType));
				labels[7].setText("Games won: " + gameSettings.getGameWon(gameType));
				labels[8].setText("Percentage: " + gameSettings.getWinPercentage(gameType) + " %");
			}
		}
		final int horGap = 15;
		{
			Box box = Box.createVerticalBox();

			JPanel winOrLosePanel = new JPanel();
			winOrLosePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

			JPanel placePanel = new JPanel();
			placePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

			JPanel typeNamePanel = new JPanel();
			typeNamePanel.setLayout(new BorderLayout());
			typeNamePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

			winOrLosePanel.add(labels[0]);
			placePanel.add(labels[1]);
			typeNamePanel.add(labels[2], BorderLayout.CENTER);

			winOrLosePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			placePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			typeNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

			box.add(winOrLosePanel);
			if (gameType != GameSettings.CUSTOM)
			{
				box.add(placePanel);
				box.add(typeNamePanel);

				if (win == true && highScore == true)
				{
					nickTextField = new JTextField();
					nickTextField.setDocument(new JTextFieldLimit(GameSettings.MAXNICKLENGTH));
					nickTextField.setText(gameSettings.getLastNick());
					nickTextField.select(0, GameSettings.MAXNICKLENGTH);
					nickTextField.addActionListener(this);
					box.add(nickTextField);
				}
			}
			titlePanel.add(box);
			if (gameType == GameSettings.CUSTOM)
				titlePanel.setBorder(BorderFactory.createEmptyBorder(25, horGap, 10, horGap));
			else
				titlePanel.setBorder(BorderFactory.createEmptyBorder(10, horGap, 10, horGap));

		}
		{
			exitButton = new JButton("Exit");
			exitButton.setActionCommand("Exit");

			exitButton.setPreferredSize(new Dimension(120, 25));
			exitButton.setMaximumSize(new Dimension(120, 25));
			exitButton.setMinimumSize(new Dimension(120, 25));
			exitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
			exitButton.addActionListener(this);
			getRootPane().setDefaultButton(exitButton);
		}
		{
			restartButton = new JButton("Restart game");
			restartButton.setActionCommand("Restart game");
			restartButton.setPreferredSize(new Dimension(120, 25));
			restartButton.setMaximumSize(new Dimension(120, 25));
			restartButton.setMinimumSize(new Dimension(120, 25));
			restartButton.setAlignmentX(Component.LEFT_ALIGNMENT);
			restartButton.addActionListener(this);
		}
		{
			playAgainButton = new JButton("Play again");
			playAgainButton.setActionCommand("Play again");
			playAgainButton.setPreferredSize(new Dimension(120, 25));
			playAgainButton.setMaximumSize(new Dimension(120, 25));
			playAgainButton.setMinimumSize(new Dimension(120, 25));
			playAgainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
			playAgainButton.addActionListener(this);
		}

		{
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));

			Box col1 = Box.createVerticalBox();

			if (gameType != GameSettings.CUSTOM)
			{
				col1.add(labels[3]);
				col1.add(Box.createVerticalGlue());
				col1.add(labels[5]);
				col1.add(Box.createVerticalGlue());
				col1.add(labels[6]);
				col1.add(Box.createVerticalGlue());
				col1.add(labels[7]);
			}

			col1.add(Box.createVerticalGlue());
			col1.add(exitButton);

			contentPanel.add(col1);

			final int labelHeight = labels[3].getPreferredSize().height;

			Box col2 = Box.createVerticalBox();

			if (gameType != GameSettings.CUSTOM)
			{
				col2.add(labels[4]);
				col2.add(Box.createVerticalGlue());
				col2.add(Box.createRigidArea(new Dimension(0, labelHeight)));
				col2.add(Box.createVerticalGlue());
				col2.add(Box.createRigidArea(new Dimension(0, labelHeight)));
				col2.add(Box.createVerticalGlue());
				col2.add(labels[8]);
			}

			col2.add(Box.createVerticalGlue());

			if (win == false)
				col2.add(restartButton);
			else
				col2.add(playAgainButton);

			contentPanel.add(Box.createHorizontalGlue());
			contentPanel.add(Box.createHorizontalStrut(15));
			contentPanel.add(col2);

			if (win == false)
			{
				Box col3 = Box.createVerticalBox();
				for (int i = 0; i < 4; i++)
				{
					col3.add(Box.createRigidArea(new Dimension(0, labelHeight)));
					col3.add(Box.createVerticalGlue());
				}
				col3.add(playAgainButton);
				contentPanel.add(Box.createHorizontalGlue());
				contentPanel.add(Box.createHorizontalStrut(15));
				contentPanel.add(col3);
			}
			contentPanel.setBorder(BorderFactory.createEmptyBorder(0, horGap, 10, 10));
		}
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if (record != null)
					record.setNick(nickTextField.getText());
				dispose();
				gui.createGame(false);

			}
		});

		pack();
		Dimension dim = getPreferredSize();
		setPreferredSize(new Dimension((int)(dim.width), (int) (1.3 * dim.height)));
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (record != null)
			record.setNick(nickTextField.getText());
		if (e.getActionCommand().equals("Exit"))
			gui.quitGame();
		else if (e.getActionCommand().equals("Play again"))
			gui.createGame(false);
		else if (e.getActionCommand().equals("Restart game"))
			gui.createGame(true);
		this.dispose();
	}

}

