package Plotter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class ReaderWriter {
	public void write(Parameters parameters, File file) throws IOException {
		Properties properties = new Properties();
		properties.setProperty("x1", Integer.toString(parameters.getPoint1X()));
		properties.setProperty("y1", Integer.toString(parameters.getPoint1Y()));
		properties.setProperty("x2", Integer.toString(parameters.getPoint2X()));
		properties.setProperty("y2", Integer.toString(parameters.getPoint2Y()));
		properties.setProperty("r", Integer.toString(parameters.getR()));

		FileOutputStream stream = new FileOutputStream(file);
		try {
			properties.storeToXML(stream, "Plotter properties");
		} finally {
			stream.close();
		}

		
	}

	public void read(Parameters parameters, File file) throws InvalidPropertiesFormatException, IOException {
		Properties properties = new Properties();
		FileInputStream stream = new FileInputStream(file);
		try {
			properties.loadFromXML(stream);
		} finally {
			stream.close();
		}
		try {
			parameters.setPoint1X(Integer.parseInt(properties.getProperty("x1")));
		} catch (RuntimeException e) {
			parameters.setPoint1X(Parameters.DEFAULT_X1);
		}
		try {
			parameters.setPoint1Y(Integer.parseInt(properties.getProperty("y1")));
		} catch (RuntimeException e) {
			parameters.setPoint1Y(Parameters.DEFAULT_Y1);
		}
		try {
			parameters.setPoint2X(Integer.parseInt(properties.getProperty("x2")));
		} catch (RuntimeException e) {
			parameters.setPoint2X(Parameters.DEFAULT_X2);
		}
		try {
			parameters.setPoint2Y(Integer.parseInt(properties.getProperty("y2")));
		} catch (RuntimeException e) {
			parameters.setPoint2Y(Parameters.DEFAULT_Y2);
		}
		try {
			parameters.setR(Integer.parseInt(properties.getProperty("r")));
		} catch (RuntimeException e) {
			parameters.setR(Parameters.DEFAULT_R);
		}
	}
}
