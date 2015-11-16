
import java.util.Observable;

public class Parameters extends Observable {
	
	public static final int MIN_ANGLE_OX = 0;
	public static final int MAX_ANGLE_OX = 360;
	public static final int DEFAULT_ANGLE_OX = 0;
	
	public static final int MIN_ANGLE_OY = 0;
	public static final int MAX_ANGLE_OY = 360;
	public static final int DEFAULT_ANGLE_OY = 0;
	
	public static final int MIN_R = 0;
	public static final int MAX_R = 100000;
	public static final int DEFAULT_R = 100;
	
	int angleOX;
	int angleOY;
	int r;
	
	public Parameters() {
		angleOX = DEFAULT_ANGLE_OX;
		angleOY = DEFAULT_ANGLE_OY;
		r = DEFAULT_R;
	}

	public int getAngleOX(){
		return angleOX;
	}
	
	public void setAngleOX(int angleOX) {
		if (angleOX > MAX_ANGLE_OX)
			angleOX -= MAX_ANGLE_OX;
		if (angleOX < MIN_ANGLE_OX )
			angleOX += MAX_ANGLE_OX;
		this.angleOX = angleOX;
		setChanged();
		notifyObservers();
	}
	
	public int getAngleOY(){
		return angleOY;
	}
	
	public void setAngleOY(int angleOY) {
		if (angleOY > MAX_ANGLE_OY)
			angleOY -= MAX_ANGLE_OY;
		if(angleOY < MIN_ANGLE_OY )
			angleOY += MAX_ANGLE_OY;
		this.angleOY = angleOY;
		setChanged();
		notifyObservers();
	}
	
	public int getR() {
		return r;
	}
	public void setR(int r) {
		if (r > MAX_R || r < MIN_R ){
			throw new IllegalArgumentException("Invalid r");
		}
		this.r = r;
		setChanged();
		notifyObservers();
	}
}
