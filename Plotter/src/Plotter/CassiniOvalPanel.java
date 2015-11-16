package Plotter;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;


import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CassiniOvalPanel extends JPanel {
	static final int R = 3;
	static final int CAPTURE_RADIUS_SQUARE = 36;
	static final int SEARCH_STEP = 200;
	int mousePositionX;
	int mousePositionY;
	int start1X;
	int start1Y;
	int start2X;
	int start2Y;
	Painter painter;
	boolean dragStarted = false;
	Parameters parameters;
	public CassiniOvalPanel(final Parameters parameters) {
		super();
		this.parameters = parameters;
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1){
						dragStarted = true;
						mousePositionX = e.getX();
						mousePositionY = e.getY();
						start1X = parameters.getPoint1X();
						start1Y = parameters.getPoint1Y();
						start2X = parameters.getPoint2X();
						start2Y = parameters.getPoint2Y();
						
						if ( Math.pow(e.getX() - (surface.getColumns()/2)-parameters.getPoint1X(),2) + Math.pow(e.getY() - (surface.getRows()/2)+parameters.getPoint1Y(),2) 
								<= Math.pow(e.getX() - (surface.getColumns()/2)-parameters.getPoint2X(),2)	+ Math.pow(e.getY() - (surface.getRows()/2)+parameters.getPoint2Y(),2)){
							if (Math.pow(e.getX() - (surface.getColumns()/2)-parameters.getPoint1X(),2) + Math.pow(e.getY() - (surface.getRows()/2)+parameters.getPoint1Y(),2) < CAPTURE_RADIUS_SQUARE){
								parameters.setMouseMode(Parameters.MouseMode.POINT1);
							}
						}
						if ( Math.pow(e.getX() - (surface.getColumns()/2)-parameters.getPoint1X(),2) + Math.pow(e.getY() - (surface.getRows()/2) + parameters.getPoint1Y(),2) 
								> Math.pow(e.getX() - (surface.getColumns()/2)-parameters.getPoint2X(),2)	+ Math.pow(e.getY() - (surface.getRows()/2) + parameters.getPoint2Y(),2)){
							if (Math.pow(e.getX() - (surface.getColumns()/2)-parameters.getPoint2X(),2) + Math.pow(e.getY() -(surface.getRows()/2)+ parameters.getPoint2Y(), 2) < CAPTURE_RADIUS_SQUARE){
								parameters.setMouseMode(Parameters.MouseMode.POINT2);
							}
						}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1){
					dragStarted = false;
					parameters.setMouseMode(Parameters.MouseMode.NONE);
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
		
			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragStarted) {
					if (parameters.getMouseMode() == Parameters.MouseMode.POINT1){
						parameters.setPoint1X(start1X+e.getX()-mousePositionX);
						parameters.setPoint1Y(start1Y-e.getY()+mousePositionY);
					}
					if (parameters.getMouseMode() == Parameters.MouseMode.POINT2){
						parameters.setPoint2X(start2X+e.getX()-mousePositionX);
						parameters.setPoint2Y(start2Y-e.getY()+mousePositionY);
					}
				}
			}
		
		});

	}
	

	
	Surface surface = new Surface(0, 0);
	private BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	
	@Override
	public void paintComponent(Graphics g) {

		if (getWidth() != surface.getColumns() || getHeight() != surface.getRows()) {
			surface = new Surface(getHeight(), getWidth());
			img = new BufferedImage(surface.getColumns(), surface.getRows(), BufferedImage.TYPE_INT_RGB);
		}

		surface.fill(Color.white.getRGB());
		painter = new Painter(surface);
		painter.printAxes();
		painter.printPoint(surface, (surface.getRows()/2)-parameters.getPoint1Y(),(surface.getColumns()/2)+parameters.getPoint1X(), R);
		painter.printPoint(surface, (surface.getRows()/2)-parameters.getPoint2Y(),(surface.getColumns()/2)+parameters.getPoint2X(), R);
		painter.paintCassiniOval(new Vector2int[]{parameters.getPoint1(), parameters.getPoint2()},parameters.getR());
		super.paintComponent(g);

		img.setRGB(0, 0, surface.getColumns(), surface.getRows(), surface.getData(), 0, surface.getColumns());
		g.drawImage(img, 0, 0, null);
	}

}
