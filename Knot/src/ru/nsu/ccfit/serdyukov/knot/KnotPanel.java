package ru.nsu.ccfit.serdyukov.knot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class KnotPanel extends JPanel{
	Surface surface = new Surface(0, 0);
	private BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	
	private Parameters parameters;
	private Vector3[][] splines;
	
	boolean dragStarted = false;
	int mousePositionX;
	int mousePositionY;
	int startAngleOX;
	int startAngleOY;
	
	public KnotPanel(final Parameters parameters, Vector3[][] splines) {
	
		this.parameters = parameters;
		this.splines = splines;
	

	
	addMouseListener(new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1){
					dragStarted = true;
					mousePositionX = e.getX();
					mousePositionY = e.getY();
					startAngleOX = parameters.getAngleOX();
					startAngleOY = parameters.getAngleOY();
					
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1){
				dragStarted = false;
			}
		}
	});
	
	addMouseMotionListener(new MouseMotionAdapter() {
	
		@Override
		public void mouseDragged(MouseEvent e) {
			if (dragStarted) {
					parameters.setAngleOY((int)(startAngleOY + (180/Math.PI*Math.atan((e.getX()-mousePositionX)/parameters.getR()))));
					parameters.setAngleOX((int)(startAngleOX + (180/Math.PI*Math.atan((e.getY()-mousePositionY)/parameters.getR()))));
			}
		}
	
	});
	}
	

	@Override
	public void paintComponent(Graphics g) {

		if (getWidth() != surface.getColumns() || getHeight() != surface.getRows()) {
			surface = new Surface(getHeight(), getWidth());
			img = new BufferedImage(surface.getColumns(), surface.getRows(), BufferedImage.TYPE_INT_RGB);
		}

		surface.fill(Color.white.getRGB());
		
		{
			Painter painter = new Painter(surface);
			
			Matrix4 matrix = Transformations.identity();
			matrix = Transformations.rotation(new Vector3(0, 1, 0), parameters.getAngleOY()*Math.PI/180).multiply(matrix);
			matrix = Transformations.rotation(new Vector3(1, 0, 0), parameters.getAngleOX()*Math.PI/180).multiply(matrix);
			matrix = Transformations.projection(parameters.getR()).multiply(matrix);
			matrix = Transformations.scaling(new Vector3(1, -1, 1)).multiply(matrix);
			matrix = Transformations.translation(new Vector3(surface.getColumns() * 0.5, surface.getRows() * 0.5, 0)).multiply(matrix);
			painter.setMatrix(matrix);
			
			for(Vector3[] vertices : splines) {
				painter.paintSpline(vertices, Color.BLACK.getRGB());
			}
		}

		img.setRGB(0, 0, surface.getColumns(), surface.getRows(), surface.getData(), 0, surface.getColumns());
		g.drawImage(img, 0, 0, null);
	}
}
