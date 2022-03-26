package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import graphics.ColorPicker;

public abstract class SpaceObject {
	
	protected String name;
	protected String typ;
	
	protected double positionX;
	protected double positionY;
	
	protected double speedX;
	protected double speedY;
	
	protected double weight;
	protected double radius;
	
	public double MIN_SIZE = 2;	// po scaleovani alespon tento polomer
	public double MAX_SIZE = 500;	// po scaleovani max tento polomer
	
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
	
	public abstract void draw(Graphics2D g2, double positionX, double positionY, double radius);
	public abstract void draw(Graphics2D g2);
	public abstract void drawHighlight(Graphics2D g2);

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
		if(radius < this.MIN_SIZE) {
			this.scaledRadius = this.MIN_SIZE;
		}
		else if(radius > this.MAX_SIZE) {
			this.scaledRadius = this.MAX_SIZE;
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

}
