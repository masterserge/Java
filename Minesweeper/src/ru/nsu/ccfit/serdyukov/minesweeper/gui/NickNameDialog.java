package ru.nsu.ccfit.serdyukov.minesweeper.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ru.nsu.ccfit.serdyukov.minesweeper.GameSettings;
import ru.nsu.ccfit.serdyukov.minesweeper.Record;

public class NickNameDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private Record record;
	private JTextField nickTextField;
	private WindowAdapter wa;

	public NickNameDialog(GameSettings gameSettings, final Record record)
	{
		this.record = record;

		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Congratulations");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.setPreferredSize(new Dimension(80, 25));
				okButton.addActionListener(this);
			}
		}
		String[] placeStrings = { "", "second ", "third ", "fourth ", "fifth " };

		JLabel label1 = new JLabel("You have the " + placeStrings[record.getPlace()] + "fastest time for this difficulty level!");
		JLabel label2 = new JLabel("Please type your name:");

		contentPanel.add(label1);
		contentPanel.add(Box.createVerticalStrut(5));
		contentPanel.add(label2);
		contentPanel.add(Box.createVerticalStrut(5));

		nickTextField = new JTextField();

		nickTextField.setDocument(new JTextFieldLimit(GameSettings.MAXNICKLENGTH));
		nickTextField.setText(gameSettings.getLastNick());
		nickTextField.select(0, GameSettings.MAXNICKLENGTH);
		nickTextField.addActionListener(this);

		contentPanel.add(nickTextField);

		wa = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				record.setNick(nickTextField.getText());
			}
		};
		addWindowListener(wa);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		record.setNick(nickTextField.getText());
		removeWindowListener(wa);
		dispose();
	}

}