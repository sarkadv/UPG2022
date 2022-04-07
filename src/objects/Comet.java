package objects;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Trida v budoucnu reprezentujici kometu.
 */
public class Comet extends SpaceObject {
	
	public Comet(String name, String typ, double positionX, double positionY, 
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
	
}