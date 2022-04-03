package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;
import util.Vectors;
import graphics.ColorPicker;

public abstract class SpaceObject {
	
	protected String name;
	protected String typ;
	
	protected double positionX;
	protected double positionY;
	
	protected double speedX;
	protected double speedY;
	
	protected double acceleration;
	protected double accelerationX;
	protected double accelerationY;
	
	protected double weight;
	protected double radius;
	
	public static final double MIN_SIZE = 2;	// po scaleovani alespon tento polomer
	public static double MAX_SIZE;	// po scaleovani max tento polomer
	
	protected double scaledPositionX;
	protected double scaledPositionY;
	protected double scaledRadius;
	
	protected Color color;
	protected Shape drawing;	 // graficka realizace objektu

	public SpaceObject(String name, String typ, double positionX, double positionY, 
					double speedX, double speedY, double weight) {
		
		this.name = name;
		this.typ = typ;
		this.positionX = positionX;
		this.positionY = positionY;
		this.speedX = speedX;
		this.speedY = speedY;
		this.weight = weight;
		this.color = ColorPicker.randomColor();
		
	}
	
	public abstract void draw(Graphics2D g2);
	public abstract void drawHighlight(Graphics2D g2, Color color);
	public abstract boolean approximateHitTest(double x, double y);
	
	public double computeAcceleration(List<SpaceObject> spaceObjects, int i, double GConstant) {
		double finalAccelerationVectorX = 0;
		double finalAccelerationVectorY = 0;
		double finalAccelerationSize = 0;
		
		for(int j = 0; j < spaceObjects.size(); j++) {
			SpaceObject objectI = spaceObjects.get(i);
			SpaceObject objectJ = spaceObjects.get(j);
			
			if(i != j) {
				double distanceVectorSize = Vectors.vectorSize(objectJ.positionX, objectJ.positionY, objectI.positionX, objectI.positionY);

				double accelerationX = GConstant * objectJ.weight * ((objectJ.positionX - objectI.positionX)/(distanceVectorSize*distanceVectorSize*distanceVectorSize));
				double accelerationY = GConstant * objectJ.weight * ((objectJ.positionY - objectI.positionY)/(distanceVectorSize*distanceVectorSize*distanceVectorSize));

				double acceleration = accelerationX + accelerationY;
				
				finalAccelerationVectorX += accelerationX;
				finalAccelerationVectorY += accelerationY;
				finalAccelerationSize += acceleration;
			}
		}

		this.setAccelerationX(finalAccelerationVectorX);
		this.setAccelerationY(finalAccelerationVectorY);
		
		return finalAccelerationSize;
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
		if(radius < MIN_SIZE) {
			this.scaledRadius = MIN_SIZE;
		}
		else if(radius > MAX_SIZE) {
			this.scaledRadius = MAX_SIZE;
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
