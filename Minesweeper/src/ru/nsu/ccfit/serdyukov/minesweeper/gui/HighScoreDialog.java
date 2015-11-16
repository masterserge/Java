package ru.nsu.ccfit.serdyukov.minesweeper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingConstants;
import javax.swing.JList;
import ru.nsu.ccfit.serdyukov.minesweeper.GameSettings;
import ru.nsu.ccfit.serdyukov.minesweeper.RecordList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;

public class HighScoreDialog extends JDialog implements ListSelectionListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	private JList difficultyList;
	private JPanel difficultyPanel;
	private JPanel bestTimesPanel;
	private JPanel statPanel;
	private JLabel[] nickLabels;
	private JLabel[] timeLabels;
	private JLabel[] statLabels;
	private GameSettings gameSettings;

	public HighScoreDialog(GameSettings gameSettings)
	{
		this.gameSettings = gameSettings;
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton closeButton = new JButton("Close");
				closeButton.setHorizontalTextPosition(SwingConstants.CENTER);
				closeButton.setActionCommand("Close");
				buttonPane.add(closeButton);
				getRootPane().setDefaultButton(closeButton);
				closeButton.setPreferredSize(new Dimension(120, 25));
				closeButton.addActionListener(this);
			}
			{
				JButton resetButton = new JButton("Reset");
				resetButton.setHorizontalTextPosition(SwingConstants.CENTER);
				buttonPane.add(resetButton);
				resetButton.setPreferredSize(new Dimension(120, 25));
				resetButton.addActionListener(this);
			}
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.NORTH);
			{
				difficultyList = new JList(GameSettings.difficulties);
				difficultyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				Dimension dim = difficultyList.getPreferredSize();
				difficultyList.setPreferredSize(new Dimension((int) (dim.width * 2), (int) (dim.height * 1.3)));
				difficultyList.setBorder(BorderFactory.createLineBorder(new Color(130, 135, 144)));
				difficultyList.addListSelectionListener(this);

				difficultyPanel = new JPanel();
				difficultyPanel.add(difficultyList);
			}
			String longNick = "";
			String longTime = "9999.000";
			for (int i = 0; i < GameSettings.MAXNICKLENGTH; i++)
				longNick += "W";
			{
				bestTimesPanel = new JPanel();
				bestTimesPanel.setBorder(BorderFactory.createTitledBorder("Best Times"));
				bestTimesPanel.setLayout(new BorderLayout(0, 0));

				nickLabels = new JLabel[5];
				timeLabels = new JLabel[5];

				for (int i = 0; i < 5; i++)
				{
					nickLabels[i] = new JLabel(longNick);
					timeLabels[i] = new JLabel(longTime);

				}

				Box nickBox = Box.createVerticalBox();
				bestTimesPanel.add(nickBox, BorderLayout.WEST);

				Box timeBox = Box.createVerticalBox();
				bestTimesPanel.add(timeBox, BorderLayout.EAST);

				final int containerGap = 10;
				for (int i = 0; i < 5; i++)
				{
					nickLabels[i].setBorder(BorderFactory.createEmptyBorder(0, containerGap, 0, 0));
					timeLabels[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 0, containerGap));
					timeLabels[i].setAlignmentX(Component.RIGHT_ALIGNMENT);
					nickBox.add(nickLabels[i]);
					timeBox.add(timeLabels[i]);

				}

				Dimension dim = bestTimesPanel.getPreferredSize();
				bestTimesPanel.setPreferredSize(new Dimension((int) (dim.width * 1.2), (int) (dim.height)));
			}
			{
				statPanel = new JPanel();
				statPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				statLabels = new JLabel[4];
				Box statBox = Box.createVerticalBox();
				for (int i = 0; i < 4; i++)
				{
					statLabels[i] = new JLabel(longNick);
					statBox.add(statLabels[i]);
					statBox.add(Box.createVerticalStrut(4));
				}
				statPanel.add(statBox);
				Dimension dim = statPanel.getPreferredSize();
				statPanel.setPreferredSize(new Dimension((int) (dim.width * 1.2), (int) (dim.height)));
			}

			Box panelBox = Box.createHorizontalBox();
			panelBox.add(difficultyPanel);
			panelBox.add(Box.createHorizontalStrut(5));
			panelBox.add(bestTimesPanel);
			panelBox.add(Box.createHorizontalStrut(10));
			panelBox.add(statPanel);
			panelBox.add(Box.createHorizontalStrut(15));
			panel.add(panelBox);
		}

		difficultyList.setSelectedIndex(0);
		setTitle("Minesweeper Statistics - " + System.getProperty("user.name"));
		pack();
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		int index = difficultyList.getSelectedIndex();
		if (index != -1)
		{
			statLabels[0].setText("Games played: " + gameSettings.getTotalGamePlayed(index));
			statLabels[1].setText("Games won: " + gameSettings.getGameWon(index));
			statLabels[2].setText("Win percentage: " + gameSettings.getWinPercentage(index) + " %");
			statLabels[3].setText("Current streak: " + gameSettings.getStreak(index));

			RecordList list = gameSettings.getRecords(index);
			for (int i = 0; i < 5; i++)
			{
				nickLabels[i].setText(list.get(i).getNick());
				String time = String.format(Locale.ENGLISH, "%.3f", list.get(i).getTime());
				timeLabels[i].setText(time + "");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if (cmd.equals("Reset"))
		{
			switch (JOptionPane.showConfirmDialog(this, "Are you sure?", "Reseting statistic", JOptionPane.YES_NO_OPTION))
			{
			case JOptionPane.YES_OPTION:
				gameSettings.resetStatistic();
				int index = difficultyList.getSelectedIndex();
				
				//for updating statistic
				difficultyList.setSelectedIndex((index + 1) % 3);
				difficultyList.setSelectedIndex(index);
				break;
			}
		}
		else if (cmd.equals("Close"))
			this.dispose();
	}
}
