package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;

public class Planet extends SpaceObject {
	
	public Planet(String name, String typ, double positionX, double positionY, 
			double speedX, double speedY, double weight) {
		
		super(name, typ, positionX, positionY, speedX, speedY, weight);
		this.radius = findRadius();
		this.drawing = new Ellipse2D.Double(this.scaledPositionX, this.scaledPositionY, 2*this.scaledRadius, 2*this.scaledRadius);
	}
	
	/**
	 * Polomer je vypocitany ze vzorce objemu koule 4/3 * PI * r^3.
	 * @return	polomer planety
	 */
	private double findRadius() {
		return Math.cbrt((3*weight)/(4*Math.PI));
	}

	/**
	 * Vykresli planetu na danych souradnicich.
	 */
	public void draw(Graphics2D g2, double positionX, double positionY, double radius) {
		
		g2.setColor(this.color);
		g2.fill(new Ellipse2D.Double(positionX, positionY, 2*radius, 2*radius));
		g2.setColor(Color.cyan);
		g2.drawString(this.name, (int)positionX, (int)(positionY + radius));
	}
	
	/**
	 * Vykresli planetu na jejich souradnicich.
	 */
	public void draw(Graphics2D g2) {
		this.drawing =  new Ellipse2D.Double(this.scaledPositionX, this.scaledPositionY, 2*this.scaledRadius, 2*this.scaledRadius);
		g2.setColor(this.color);
		g2.fill(new Ellipse2D.Double(this.scaledPositionX, this.scaledPositionY, 2*this.scaledRadius, 2*this.scaledRadius));
	}
	
	/**
	 * Vykresli obrys (zvyrazneni) planety
	 */
	public void drawHighlight(Graphics2D g2) {
		this.drawing =  new Ellipse2D.Double(this.scaledPositionX, this.scaledPositionY, 2*this.scaledRadius, 2*this.scaledRadius);
		g2.setColor(Color.WHITE);
		g2.draw(new Ellipse2D.Double(this.scaledPositionX, this.scaledPositionY, 2*this.scaledRadius, 2*this.scaledRadius));
	}

	/**
	 * Priblizny hit-test pro planetu.
	 * @param g2
	 * @param x		x-souradnice kliku
	 * @param y		y-souradnice kliku
	 * @return
	 */
	@Override
	public boolean approximateHitTest(double x, double y) {
		double tolerance = 10/this.scaledRadius;
		Ellipse2D area = new Ellipse2D.Double(this.scaledPositionX - tolerance, this.scaledPositionY - tolerance,
				2*this.scaledRadius + 2*tolerance, 2*this.scaledRadius + 2*tolerance);
		
		if (area.contains(x, y)) {
			return true;
		}
		else {
			return false;
		}
	}
}
