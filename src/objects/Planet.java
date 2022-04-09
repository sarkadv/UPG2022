package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * Trida reprezentujici planetu.
 */
public class Planet extends SpaceObject {
	
	/**
	 * Konstruktor pro vytvoreni instance planety.
	 * @param name			nazev
	 * @param typ			typ
	 * @param positionX		x-souradnice leveho horniho rohu
	 * @param positionY		y-souradnice leveho horniho rohu
	 * @param speedX		rychlost v ose x
	 * @param speedY		rychlost v ose y
	 * @param weight		vaha
	 */
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
	public double findRadius() {
		return Math.cbrt((3*weight)/(4*Math.PI));
	}

	/**
	 * Metoda vykresli planetu na jejich souradnicich.
	 */
	public void draw(Graphics2D g2) {
		this.drawing =  new Ellipse2D.Double(this.scaledPositionX, this.scaledPositionY, 2*this.scaledRadius, 2*this.scaledRadius);
		g2.setColor(this.color);
		g2.fill(new Ellipse2D.Double(this.scaledPositionX, this.scaledPositionY, 2*this.scaledRadius, 2*this.scaledRadius));
	}
	
	/**
	 * Metoda vykresli obrys (zvyrazneni) planety
	 */
	public void drawHighlight(Graphics2D g2, Color color) {
		this.drawing =  new Ellipse2D.Double(this.scaledPositionX, this.scaledPositionY, 2*this.scaledRadius, 2*this.scaledRadius);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(4));
		g2.draw(new Ellipse2D.Double(this.scaledPositionX, this.scaledPositionY, 2*this.scaledRadius, 2*this.scaledRadius));
	}

	/**
	 * Priblizny hit-test pro planetu.
	 * Tolerance je tim vetsi, cim mensi je planeta. Pro velke planety s blizi nule.
	 * @param g2	graficky kontext
	 * @param x		x-souradnice kliku
	 * @param y		y-souradnice kliku
	 * @return		vysledek hit-testu
	 */
	@Override
	public boolean approximateHitTest(double x, double y) {
		double tolerance = 15/this.scaledRadius;
		
		// nalezeni oblasti kolem planety
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
