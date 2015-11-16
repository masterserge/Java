package ru.nsu.ccfit.serdyukov.knot;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private static final int DEFAULT_HEIGHT = 500;
	private static final int DEFAULT_WIDTH = 500;
	Parameters parameters;
	
	private static final Vector3[][] knot1Splines = new Vector3[][] {
        new Vector3[] {
                new Vector3(-60, -75, 50),
                new Vector3(-35, -75, 50),
                new Vector3(9.47756, -53.5844, 25),
                new Vector3(30.3109, -17.5, 0),
        },
        new Vector3[] {
                new Vector3(30.3109, -17.5, 0),
                new Vector3(51.1442, 18.5844, -25),
                new Vector3(47.4519, 67.8109, -50),
                new Vector3(34.9519, 89.4615, -50),
        },
        new Vector3[] {
                new Vector3(34.9519, 89.4615, -50),
                new Vector3(22.4519, 111.112, -50),
                new Vector3(8.33333, 115, -25),
                new Vector3(0, 115, 0),
        },
        new Vector3[] {
                new Vector3(0, 115, 0),
                new Vector3(-8.33333, 115, 25),
                new Vector3(-22.4519, 111.112, 50),
                new Vector3(-34.9519, 89.4615, 50),
        },
        new Vector3[] {
                new Vector3(-34.9519, 89.4615, 50),
                new Vector3(-47.4519, 67.8109, 50),
                new Vector3(-51.1442, 18.5844, 25),
                new Vector3(-30.3109, -17.5, 0),
        },
        new Vector3[] {
                new Vector3(-30.3109, -17.5, 0),
                new Vector3(-9.47756, -53.5844, -25),
                new Vector3(35, -75, -50),
                new Vector3(60, -75, -50),
        },
        new Vector3[] {
                new Vector3(60, -75, -50),
                new Vector3(85, -75, -50),
                new Vector3(95.4263, -64.7169, -25),
                new Vector3(99.5929, -57.5, 0),
        },
        new Vector3[] {
                new Vector3(99.5929, -57.5, 0),
                new Vector3(103.76, -50.2831, 25),
                new Vector3(107.452, -36.1122, 50),
                new Vector3(94.9519, -14.4615, 50),
        },
        new Vector3[] {
                new Vector3(94.9519, -14.4615, 50),
                new Vector3(82.4519, 7.18911, 50),
                new Vector3(41.6667, 35, 25),
                new Vector3(0, 35, 0),
        },
        new Vector3[] {
                new Vector3(0, 35, 0),
                new Vector3(-41.6667, 35, -25),
                new Vector3(-82.4519, 7.18911, -50),
                new Vector3(-94.9519, -14.4615, -50),
        },
        new Vector3[] {
                new Vector3(-94.9519, -14.4615, -50),
                new Vector3(-107.452, -36.1122, -50),
                new Vector3(-103.76, -50.2831, -25),
                new Vector3(-99.5929, -57.5, 0),
        },
        new Vector3[] {
                new Vector3(-99.5929, -57.5, 0),
                new Vector3(-95.4263, -64.7169, 25),
                new Vector3(-85, -75, 50),
                new Vector3(-60, -75, 50),
        },
	};

	private static final Vector3[][] knot2Splines = new Vector3[][] {
		new Vector3[] {
			new Vector3(10, 70, 0),
			new Vector3(15, 40, 2),
			new Vector3(30, 50, -10),
			new Vector3(30, 55, 20),
			new Vector3(15, 40, -5),
			new Vector3(20, -30, 30),
		},
		
		new Vector3[] {
			new Vector3(20, -30, 30),
			new Vector3(50, 30, -2),
			new Vector3(20, 20, -10),
			new Vector3(12, 35, 20),
			new Vector3(40, 50, -5),
			new Vector3(20, -20, 20),
			},
	
		new Vector3[] {
			new Vector3(20, -20, 20),
			new Vector3(17, 38, -15),
			new Vector3(20, 40, 0),
			new Vector3(16, 60, 5),
			new Vector3(30, 10, -2),
			new Vector3(40, 30, 30),
		},
		
		new Vector3[] {
			new Vector3(40, 30, 30),
			new Vector3(10, 20, -2),
			new Vector3(10, 50, -10),
			new Vector3(2, 45, 20),
			new Vector3(0, 30, -5),
			new Vector3(12, 60, 8),
		},
	
		
	};

	
	public MainFrame(Parameters parameters) {

		this.parameters = parameters;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel knotPanel = new JPanel();
		knotPanel.setLayout(new BoxLayout(knotPanel, BoxLayout.X_AXIS));
		mainPanel.add(knotPanel);

		JPanel knot1Panel = new JPanel();
		knot1Panel.setLayout(new BoxLayout(knot1Panel, BoxLayout.LINE_AXIS));
		knot1Panel.setBorder(BorderFactory.createTitledBorder("Knot 1"));
		knotPanel.add(knot1Panel);
		final KnotPanel knot1 = new KnotPanel(parameters, knot1Splines);
		knot1Panel.add(knot1);
		
		JPanel knot2Panel = new JPanel();
		knot2Panel.setLayout(new BoxLayout(knot2Panel, BoxLayout.LINE_AXIS));
		knot2Panel.setBorder(BorderFactory.createTitledBorder("Knot 2"));
		knotPanel.add(knot2Panel);
		final KnotPanel knot2 = new KnotPanel(parameters, knot2Splines);
		knot2Panel.add(knot2);

		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
		controls.setBorder(BorderFactory.createTitledBorder("Controls"));

		final ParameterPanel angleOX = new ParameterPanel("Angle OX", Parameters.MIN_ANGLE_OX, Parameters.MAX_ANGLE_OX,
				parameters.getAngleOX());
		controls.add(angleOX);
		angleOX.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				MainFrame.this.parameters.setAngleOX(angleOX.getValue());
			}
		});

		final ParameterPanel angleOY = new ParameterPanel("Angle OY", Parameters.MIN_ANGLE_OY, Parameters.MAX_ANGLE_OY,
				parameters.getAngleOY());
		controls.add(angleOY);
		angleOY.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				MainFrame.this.parameters.setAngleOY(angleOY.getValue());
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
				angleOX.setValue(MainFrame.this.parameters.getAngleOX());
				angleOY.setValue(MainFrame.this.parameters.getAngleOY());
				r.setValue(MainFrame.this.parameters.getR());
				knot1.repaint();
				knot2.repaint();
			}
		});
		mainPanel.add(controls);
		pack();
		setMinimumSize(getSize());
		setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}
}


