package ru.nsu.ccfit.serdyukov.plotter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private static final int DEFAULT_HEIGHT = 500;
	private static final int DEFAULT_WIDTH = 500;
	Parameters parameters;
	JMenuBar menuBar;
	
	public MainFrame(Parameters parameters) {

		menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		final JMenuItem openItem = new JMenuItem("Open");
		final JMenuItem saveItem = new JMenuItem("Save");

		file.add(openItem);
		file.add(saveItem);

		openItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					try {
						new ReaderWriter().read(MainFrame.this.parameters, fileChooser.getSelectedFile());
					} catch (IOException e) {
						JOptionPane.showMessageDialog(MainFrame.this, "Can't open the file: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		});

		saveItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					try {
						new ReaderWriter().write(MainFrame.this.parameters, fileChooser.getSelectedFile());
					} catch (IOException e) {
						JOptionPane.showMessageDialog(MainFrame.this, "Can't save the file: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		});

		menuBar.add(file);
		setJMenuBar(menuBar);

		this.parameters = parameters;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel sinPanel = new JPanel();
		sinPanel.setLayout(new BoxLayout(sinPanel, BoxLayout.LINE_AXIS));
		final CassiniOvalPanel sin = new CassiniOvalPanel(parameters);

		sinPanel.setBorder(BorderFactory.createTitledBorder("Draw Panel"));
		sinPanel.add(sin);
		mainPanel.add(sinPanel);

		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
		controls.setBorder(BorderFactory.createTitledBorder("Controls"));

		final ParameterPanel x1 = new ParameterPanel("x1", Parameters.MIN_X1, Parameters.MAX_X1,
				parameters.getPoint1X());
		controls.add(x1);
		x1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				MainFrame.this.parameters.setPoint1X(x1.getValue());
			}
		});

		final ParameterPanel y1 = new ParameterPanel("y1", Parameters.MIN_Y1, Parameters.MAX_Y1,
				parameters.getPoint1Y());
		controls.add(y1);
		y1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				MainFrame.this.parameters.setPoint1Y(y1.getValue());
			}
		});

		final ParameterPanel x2 = new ParameterPanel("x2", Parameters.MIN_X2, Parameters.MAX_X2,
				parameters.getPoint2X());
		controls.add(x2);
		x2.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				MainFrame.this.parameters.setPoint2X(x2.getValue());
			}
		});

		final ParameterPanel y2 = new ParameterPanel("y2", Parameters.MIN_Y2, Parameters.MAX_Y2,
				parameters.getPoint2Y());
		controls.add(y2);
		y2.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				MainFrame.this.parameters.setPoint2Y(y2.getValue());
			}
		});

		final ParameterPanel r = new ParameterPanel("r", Parameters.MIN_R, Parameters.MAX_R,
				parameters.getR());
		controls.add(r);
		r.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				MainFrame.this.parameters.setR(r.getValue());
			}
		});

		parameters.addObserver(new Observer() {

			@Override
			public void update(Observable arg0, Object arg1) {
				x1.setValue(MainFrame.this.parameters.getPoint1X());
				y1.setValue(MainFrame.this.parameters.getPoint1Y());
				x2.setValue(MainFrame.this.parameters.getPoint2X());
				y2.setValue(MainFrame.this.parameters.getPoint2Y());
				r.setValue(MainFrame.this.parameters.getR());
				sin.repaint();
			}
		});
		mainPanel.add(controls);
		pack();
		setMinimumSize(getSize());
		setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}
}


