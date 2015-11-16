package ru.nsu.ccfit.serdyukov.minesweeper.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import ru.nsu.ccfit.serdyukov.minesweeper.GameSettings;

public class OptionDialog extends JDialog implements ItemListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JRadioButton[] levels;
	private JCheckBox quest;
	private JTextField height;
	private JTextField width;
	private JTextField mines;
	private GameSettings gameSettings;
	private GraphicalUI gui;

	public OptionDialog(GameSettings gameSettings, GraphicalUI gui)
	{
		this.gameSettings = gameSettings;
		this.gui = gui;

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Options");

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setPreferredSize(new Dimension(120, 25));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(this);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setPreferredSize(new Dimension(120, 25));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(this);
			}
		}
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
		{
			JPanel optionPane = new JPanel();
			optionPane.setLayout(new BoxLayout(optionPane, BoxLayout.X_AXIS));
			optionPane.setBorder(BorderFactory.createTitledBorder("Difficulty"));
			{
				levels = new JRadioButton[4];
				levels[0] = new JRadioButton("<html>Beginner<br>10 mines<br>9 x 9 tile grid</html>");
				levels[1] = new JRadioButton("<html>Intermediate<br>40 mines<br>16 x 16 tile grid</html>");
				levels[2] = new JRadioButton("<html>Advanced<br>99 mines<br>16 x 30 tile grid</html>");
				levels[3] = new JRadioButton("Custom");

				ButtonGroup bg = new ButtonGroup();
				for (int i = 0; i < 4; i++)
				{
					bg.add(levels[i]);
					levels[i].addItemListener(this);
				}

				Box box = Box.createVerticalBox();
				box.setAlignmentX(Component.TOP_ALIGNMENT);
				box.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
				box.add(levels[0]);
				box.add(Box.createVerticalStrut(5));
				box.add(levels[1]);
				box.add(Box.createVerticalStrut(5));
				box.add(levels[2]);
				box.add(Box.createVerticalGlue());

				optionPane.add(box);

				JPanel parametersPanel = new JPanel();
				parametersPanel.setAlignmentX(Component.TOP_ALIGNMENT);
				GridLayout gl = new GridLayout(4, 2);
				gl.setHgap(4);
				gl.setVgap(4);

				JLabel heightLabel = new JLabel("Height (" + GameSettings.MINHEIGHT + "-" + GameSettings.MAXHEIGHT + "):");
				JLabel widthLabel = new JLabel("Width (" + GameSettings.MINWIDTH + "-" + GameSettings.MAXWIDTH + "):");
				JLabel minesLabel = new JLabel("Mines (" + GameSettings.MINMINES+ "-" + GameSettings.MAXMINES + "):");

				heightLabel.setVerticalAlignment(JLabel.TOP);
				widthLabel.setVerticalAlignment(JLabel.TOP);
				minesLabel.setVerticalAlignment(JLabel.TOP);

				heightLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
				widthLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
				minesLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

				height = new JTextField();
				width = new JTextField();
				mines = new JTextField();
				
				height.setActionCommand("OK");
				width.setActionCommand("OK");
				mines.setActionCommand("OK");
				height.addActionListener(this);
				width.addActionListener(this);
				mines.addActionListener(this);

				height.setDocument(new JTextFieldOnlyDigits(GameSettings.MAXHEIGHT));
				width.setDocument(new JTextFieldOnlyDigits(GameSettings.MAXWIDTH));
				mines.setDocument(new JTextFieldOnlyDigits(GameSettings.MAXMINES));

				Dimension dim = height.getPreferredSize();
				dim.width = 100;

				height.setPreferredSize(dim);
				width.setPreferredSize(dim);
				mines.setPreferredSize(dim);

				parametersPanel.setLayout(gl);
				parametersPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
				parametersPanel.add(levels[3]);
				parametersPanel.add(new JPanel());

				JPanel panelHeight = new JPanel();
				JPanel panelWidth = new JPanel();
				JPanel panelMines = new JPanel();

				panelHeight.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
				panelWidth.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
				panelMines.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

				panelHeight.add(height);
				panelWidth.add(width);
				panelMines.add(mines);

				parametersPanel.add(heightLabel);
				parametersPanel.add(panelHeight);

				parametersPanel.add(widthLabel);
				parametersPanel.add(panelWidth);

				parametersPanel.add(minesLabel);
				parametersPanel.add(panelMines);

				JPanel tmpPanel = new JPanel();
				tmpPanel.add(parametersPanel);
				optionPane.add(tmpPanel);

				width.setText(gameSettings.getWidth() + "");
				height.setText(gameSettings.getHeight() + "");
				mines.setText(gameSettings.getMineCount() + "");
			}

			Box box1 = Box.createVerticalBox();
			box1.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
			box1.add(optionPane);

			Box box2 = Box.createHorizontalBox();

			quest = new JCheckBox("Allow question marks (on double right-click)");
			quest.setSelected(gameSettings.getUseQuestionMark());
			quest.setHorizontalAlignment(JCheckBox.LEFT);
			box2.add(quest);

			box1.setAlignmentX(Component.LEFT_ALIGNMENT);
			box2.setAlignmentX(Component.LEFT_ALIGNMENT);

			contentPanel.add(box1);
			contentPanel.add(box2);
		}
		levels[gameSettings.getGameType()].setSelected(true);
		pack();
		Dimension dim = getPreferredSize();
		setPreferredSize(new Dimension((int) (1.4 * dim.width), (int) (1.5 * dim.height)));
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		JRadioButton cb = (JRadioButton) e.getItem();
		if (cb != levels[3])
		{
			height.setEnabled(false);
			width.setEnabled(false);
			mines.setEnabled(false);
		}
		else
		{
			height.setEnabled(true);
			width.setEnabled(true);
			mines.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if (cmd.equals("OK"))
		{
			int index = -1;
			for (int i = 0; i < 4; i++)
				if (levels[i].isSelected() == true)
					index = i;

			int widthValue = 0;
			int heightValue = 0;
			int minesValue = 0;

			switch (index)
			{
			case GameSettings.BEGINNER:
				widthValue = 9;
				heightValue = 9;
				minesValue = 10;
				break;
			case GameSettings.INTERMEDIATE:
				widthValue = 16;
				heightValue = 16;
				minesValue = 40;
				break;
			case GameSettings.ADVANCED:
				widthValue = 30;
				heightValue = 16;
				minesValue = 99;
				break;
			case GameSettings.CUSTOM:
				widthValue = Math.max(Integer.parseInt(width.getText()), GameSettings.MINWIDTH);
				heightValue = Math.max(Integer.parseInt(height.getText()), GameSettings.MINHEIGHT);
				minesValue = Math.min(Math.max(Integer.parseInt(mines.getText()), GameSettings.MINMINES), widthValue * heightValue - 1);
				break;
			}
			gameSettings.setWidth(widthValue);
			gameSettings.setHeight(heightValue);
			gameSettings.setMineCount(minesValue);
			gameSettings.setUseQuestionMark(quest.isSelected());
			if (gameSettings.isSuitableModel(gui.getGameModel()) == false)
				gui.createGame(false);
			gui.getGameModel().setUseQuestionMark(gameSettings.getUseQuestionMark());
			dispose();

		}
		else if (cmd.equals("Cancel"))
		{
			dispose();
		}
	}
}

class JTextFieldLimit extends PlainDocument
{
	private static final long serialVersionUID = 1L;

	private int limit;

	public JTextFieldLimit(int limit)
	{
		super();
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
	{
		if (str == null)
			return;

		if ((getLength() + str.length()) <= limit)
			super.insertString(offset, str, attr);
	}
}

class JTextFieldOnlyDigits extends PlainDocument
{

	private static final long serialVersionUID = 1L;

	private int maxValue;

	public JTextFieldOnlyDigits(int maxValue)
	{
		super();
		this.maxValue = maxValue;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
	{
		if (str == null)
			return;

		String newStr = getText(0, getLength()) + str;
		int value = 0;
		try
		{
			value = Integer.parseInt(newStr);
		}
		catch (NumberFormatException e)
		{
			return;
		}
		if (value <= maxValue && value >= 1)
			super.insertString(offset, str, attr);

	}
}