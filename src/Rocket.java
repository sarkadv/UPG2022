import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Trida reprezentujici raketu.
 */
public class Rocket extends SpaceObject {
	
	public Rocket(String name, String typ, double positionX, double positionY, 
			double speedX, double speedY, double weight) {
		
		super(name, typ, positionX, positionY, speedX, speedY, weight);
	}
	
	@Override
	public void draw(Graphics2D g2)  {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawHighlight(Graphics2D g2, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean approximateHitTest(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double findRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void drawTrajectory(Graphics2D g2, int trajectoryLength) {
		// TODO Auto-generated method stub
		
	}
}
