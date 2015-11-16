package ru.nsu.ccfit.serdyukov.knot;

import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ParameterPanel extends JPanel {
	JSpinner spinner;
	JSlider slider;


	public int getValue() {
		return (Integer)spinner.getValue();
	}

	public void setValue(int value) {
		spinner.setValue(value);
	}

	public void addChangeListener(ChangeListener l) {
		slider.addChangeListener(l);
	}

	public ParameterPanel(final String name, final int min, final int max, int value) {
		setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel(name));
		spinner = new JSpinner(new SpinnerNumberModel(value, min, max,1));
		
		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				slider.setValue((Integer)spinner.getValue());
			}
		});
		spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		
		
		((JSpinner.NumberEditor) spinner.getEditor()).getTextField().addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				try {
					spinner.commitEdit();
				} catch (ParseException e) {
					JOptionPane.showMessageDialog(ParameterPanel.this.getRootPane(), name + " must be a number from " + min + " to " + max);
				}
			}
		});
		
		

		add(spinner);

		slider = new JSlider(min, max, value);
		slider.setPreferredSize(slider.getMinimumSize());

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				spinner.setValue((Integer)slider.getValue());
			}
		});
		add(slider);
	}
}















