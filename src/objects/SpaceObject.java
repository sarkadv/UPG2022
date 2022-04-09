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
	
	/** celkova velikost rychlosti */
	protected double speed;
	
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
	public abstract double findRadius();
	
	public void computeAcceleration(List<SpaceObject> spaceObjects, int i, double GConstant, boolean collision) {
		double forceXSum = 0;
		double forceYSum = 0;
		
		SpaceObject objectI = this;
		
		for(int j = 0; j < spaceObjects.size(); j++) {
			SpaceObject objectJ = spaceObjects.get(j);
			
			if(i != j) {
				double distanceX = objectJ.positionX - objectI.positionX;
				double distanceY = objectJ.positionY - objectI.positionY;
				
				double distanceBetweenObjects = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
				
				if(collision == false) {
					if (distanceBetweenObjects < objectJ.radius + objectI.radius) {
						distanceBetweenObjects = objectJ.radius + objectI.radius;
					}
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
		this.setAcceleration(Vectors.vectorAddition(accelerationX, accelerationY));
		
	}
	
	public void checkForCollision(List<SpaceObject> spaceObjects, int i) {
		SpaceObject objectI = this;
		
		for(int j = 0; j < spaceObjects.size(); j++) {
			SpaceObject objectJ = spaceObjects.get(j);
			
			if(i != j) {
				double distanceX = objectJ.positionX - objectI.positionX;
				double distanceY = objectJ.positionY - objectI.positionY;
				
				double distanceBetweenObjects = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
				
				if(distanceBetweenObjects <= objectI.radius + objectJ.radius) {
					SpaceObject biggerObject = objectI;
					SpaceObject smallerObject = objectJ;
					
					if(objectI.weight < objectJ.weight) {
						biggerObject = objectJ;
						smallerObject = objectI;
					}
					
					double ratio = smallerObject.weight / biggerObject.weight;	// pomer vahy mensiho objektu ku vetsimu
					
					double newWeight = biggerObject.weight + smallerObject.weight;
					double newRadius = biggerObject.findRadius();
					
					double newSpeedX = biggerObject.speedX + (smallerObject.speedX)*ratio;
					double newSpeedY = biggerObject.speedY + (smallerObject.speedY)*ratio;
					double newSpeed = Vectors.vectorAddition(newSpeedX, newSpeedY);
					
					double newAccelerationX = biggerObject.accelerationX + (smallerObject.accelerationX)*ratio;
					double newAccelerationY = biggerObject.accelerationY + (smallerObject.accelerationY)*ratio;
					double newAcceleration = Vectors.vectorAddition(newAccelerationX, newAccelerationY);
					
					biggerObject.setWeight(newWeight);
					biggerObject.setRadius(newRadius);
					biggerObject.setSpeedX(newSpeedX);
					biggerObject.setSpeedY(newSpeedY);
					biggerObject.setSpeed(newSpeed);
					biggerObject.setAccelerationX(newAccelerationX);
					biggerObject.setAccelerationY(newAccelerationY);
					biggerObject.setAcceleration(newAcceleration);
					
					spaceObjects.remove(smallerObject);
					
				}
			}
			
		}
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

	public double getSpeed() {
		return this.speed;
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
	
	public void setWeight(double weight) {
		this.weight = weight;
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

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}

}
