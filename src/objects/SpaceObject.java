package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;
import util.Vectors;
import graphics.ColorPicker;

/**
 * Abstraktni trida predstavujici vesmirny objekt.
 */
public abstract class SpaceObject {
	
	/** nazev */
	protected String name;
	
	/** typ */
	protected String typ;
	
	/** x-souradnice */
	protected double positionX;
	
	/** y-souradnice */
	protected double positionY;
	
	/** x-ova slozka rychlosti */
	protected double speedX;
	
	/** y-ova slozka rychlosti */
	protected double speedY;
	
	/** celkova velikost zrychleni */
	protected double acceleration;
	
	/** x-ova slozka zrychleni */
	protected double accelerationX;
	
	/** y-ova slozka zrychleni */
	protected double accelerationY;
	
	/** vaha */
	protected double weight;
	
	/** polomer */
	protected double radius;
	
	/** minimalni polomer po scaleovani objektu */
	public static final double MIN_RADIUS = 2;	
	
	/** maximalni polomer po scaleovani objektu */
	public static double MAX_RADIUS;
	
	/** x-souradnice po scaleovani */
	protected double scaledPositionX;
	
	/** y-souradnice po scaleovani */
	protected double scaledPositionY;
	
	/** polomer po scaleovani */
	protected double scaledRadius;
	
	/** barva */
	protected final Color color;
	
	/** graficka realizace objektu */
	protected Shape drawing;	

	/**
	 * Konstruktor pro vytvoreni abstraktni casti vesmirneho objektu.
	 * @param name			nazev
	 * @param typ			typ
	 * @param positionX		x-souradnice
	 * @param positionY		y-souradnice
	 * @param speedX		x-ova slozka rychlosti
	 * @param speedY		y-ova slozka rychlosti
	 * @param weight		vaha
	 */
	public SpaceObject(String name, String typ, double positionX, double positionY, 
					double speedX, double speedY, double weight) {
		
		this.name = name;
		this.typ = typ;
		this.positionX = positionX;
		this.positionY = positionY;
		this.speedX = speedX;
		this.speedY = speedY;
		this.weight = weight;
		this.color = ColorPicker.randomColor();		// vyber nahodne barvy pro objekt
		
	}
	
	public abstract void draw(Graphics2D g2);
	public abstract void drawHighlight(Graphics2D g2, Color color);
	public abstract boolean approximateHitTest(double x, double y);
	
	public void computeAcceleration(List<SpaceObject> spaceObjects, int i, double GConstant) {
		double forceXSum = 0;
		double forceYSum = 0;
		
		for(int j = 0; j < spaceObjects.size(); j++) {
			SpaceObject objectI = spaceObjects.get(i);
			SpaceObject objectJ = spaceObjects.get(j);
			
			if(i != j) {
				double distanceX = objectJ.positionX - objectI.positionX;
				double distanceY = objectJ.positionY - objectI.positionY;
				double distanceBetweenObjects = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
				
				if(Math.abs(distanceBetweenObjects) < 0.05) {
					distanceBetweenObjects = 0.05;
				}
				
				double force = (GConstant * objectI.weight * objectJ.weight)/(distanceBetweenObjects * distanceBetweenObjects);
				
				double forceX = force * (distanceX / distanceBetweenObjects);
				double forceY = force * (distanceY / distanceBetweenObjects);
				
				forceXSum += forceX;
				forceYSum += forceY;
				
			}
			
		}
		
		double accelerationX = forceXSum / this.weight;
		double accelerationY = forceYSum / this.weight;	
		
		this.setAccelerationX(accelerationX);
		this.setAccelerationY(accelerationY);
		
	}
	
	public void computeAcceleration2(List<SpaceObject> spaceObjects, int i, double GConstant) {
		double finalAccelerationVectorX = 0;
		double finalAccelerationVectorY = 0;
		
		for(int j = 0; j < spaceObjects.size(); j++) {
			SpaceObject objectI = this;
			SpaceObject objectJ = spaceObjects.get(j);
			
			if(i != j) {
				double distanceVectorSize = Vectors.vectorSize(objectJ.positionX, objectJ.positionY, objectI.positionX, objectI.positionY);

				double accelerationX = GConstant * objectJ.weight * ((objectJ.positionX - objectI.positionX) / (distanceVectorSize * distanceVectorSize * distanceVectorSize));
				double accelerationY = GConstant * objectJ.weight * ((objectJ.positionY - objectI.positionY) / (distanceVectorSize * distanceVectorSize * distanceVectorSize));

				
				finalAccelerationVectorX += accelerationX;
				finalAccelerationVectorY += accelerationY;
			}
		}

		this.setAccelerationX(finalAccelerationVectorX);
		this.setAccelerationY(finalAccelerationVectorY);
	}
	
	public String getName() {
		return this.name;
	}

	public String getTyp() {
		return this.typ;
	}

	public double getPositionX() {
		return this.positionX;
	}

	public double getPositionY() {
		return this.positionY;
	}

	public double getSpeedX() {
		return this.speedX;
	}

	public double getSpeedY() {
		return this.speedY;
	}

	public double getWeight() {
		return this.weight;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public void setPositionX(double new_x) {
		this.positionX = new_x;
	}
	
	public void setPositionY(double new_y) {
		this.positionY = new_y;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public void setScaledPositionX(double new_x) {
		this.scaledPositionX = new_x;
	}
	
	public void setScaledPositionY(double new_y) {
		this.scaledPositionY = new_y;
	}
	
	public void setScaledRadius(double radius) {
		if(radius < MIN_RADIUS) {
			this.scaledRadius = MIN_RADIUS;
		}
		else if(radius > MAX_RADIUS) {
			this.scaledRadius = MAX_RADIUS;
		}
		else {
			this.scaledRadius = radius;
		}
	}
	
	public double getScaledPositionX() {
		return this.scaledPositionX;
	}

	public double getScaledPositionY() {
		return this.scaledPositionY;
	}
	
	public double getScaledRadius() {
		return this.scaledRadius;
	}
	
	public void setDrawing(Shape drawing) {
		this.drawing = drawing;
	}
	
	public Shape getDrawing() {
		return this.drawing;
	}
	
	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}

	public double getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}

	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}

}
