package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
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
	public static final double MIN_RADIUS = 1.5;	
	
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
	protected Shape shape;	

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
	
	/**
	 * Metoda vykresli objekt na jeho souradnicich prizpusobenych oknu.
	 * @param g2	graficky kontext
	 */
	public abstract void draw(Graphics2D g2);
	
	/**
	 * Metoda vykresli obrys (zvyrazneni) objektu.
	 * @param g2		graficky kontext
	 * @param color		barva zvyrazneni objektu
	 */
	public abstract void drawHighlight(Graphics2D g2, Color color);
	
	/**
	 * Priblizny hit-test pro objekt.
	 * Tolerance je tim vetsi, cim mensi je objekt. Pro velke objekty s blizi nule.
	 * @param g2	graficky kontext
	 * @param x		x-souradnice kliku
	 * @param y		y-souradnice kliku
	 * @return		vysledek hit-testu
	 */
	public abstract boolean approximateHitTest(double x, double y);
	
	/**
	 * Polomer je vypocitany z hmotnosti objektu.
	 * Hustota objektu je 1.
	 * @return	polomer objektu
	 */
	public abstract double findRadius();
	
	/**
	 * Metoda pomoci Newtonovy pohybove rovnice spocita zrychleni jednoho objektu, 
	 * ktere je ovlivnene vsemi ostatnimi objekty.
	 * @param spaceObjects		kolekce vsech objektu
	 * @param i					index zkoumaneho objektu
	 * @param GConstant			gravitacni konstanta
	 * @param collision			zapnuta kolize - true / false
	 */
	public void computeAcceleration(List<SpaceObject> spaceObjects, int i, double GConstant, boolean collision) {
		double forceXSum = 0;	// celkova sila v ose x
		double forceYSum = 0;	// celkova sila v ose y
		
		SpaceObject objectI = this;		// zkoumany objekt
		
		for(int j = 0; j < spaceObjects.size(); j++) {
			SpaceObject objectJ = spaceObjects.get(j);	// druhy objekt
			
			if(i != j) {	// indexy objektu se nerovnaji - nejde o stejny objekt
				double distanceX = objectJ.positionX - objectI.positionX;
				double distanceY = objectJ.positionY - objectI.positionY;
				
				double distanceBetweenObjects = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
				
				if(collision == false) {		// pokud je kolize vypnuta
					if (distanceBetweenObjects < objectJ.radius + objectI.radius) {
						// vzdalenost mezi objekty musi byt alespon soucet jejich polomeru
						// jinak dostavame prilis velke sily (deleni malym cislem) a objekty "vystreluji"
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
		this.setAcceleration(Vectors.vectorAddition(accelerationX, accelerationY));	// vysledne slozene zrychleni
		
	}
	
	/**
	 * Metoda zkontruluje, zda zkoumany objekt nekoliduje s nekterym z ostatnich.
	 * Pokud ano, provede kolizi a prepocita nove vlastnosti noveho objektu.
	 * @param spaceObjects		kolekce vsech objektu
	 * @param i					index zkoumaneho objektu
	 */
	public void checkForCollision(List<SpaceObject> spaceObjects, int i) {
		SpaceObject objectI = this;		// zkoumany objekt
		
		for(int j = 0; j < spaceObjects.size(); j++) {
			SpaceObject objectJ = spaceObjects.get(j);	// druhy objekt
			
			if(i != j) {	// indexy objektu se nerovnaji - nejde o stejny objekt
				double distanceX = objectJ.positionX - objectI.positionX;
				double distanceY = objectJ.positionY - objectI.positionY;
				
				double distanceBetweenObjects = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
				
				if(distanceBetweenObjects <= objectI.radius + objectJ.radius) {	// doslo ke kolizi
					// najdeme vetsi a mensi ze dvou objektu
					SpaceObject biggerObject = objectI;
					SpaceObject smallerObject = objectJ;
					
					if(objectI.weight < objectJ.weight) {
						biggerObject = objectJ;
						smallerObject = objectI;
					}
					
					double ratio = smallerObject.weight / biggerObject.weight;	// pomer vahy mensiho objektu ku vetsimu
					
					double newWeight = biggerObject.weight + smallerObject.weight;
					double newRadius = biggerObject.findRadius();	// aktualizace polomeru
					
					double newPositionX = (biggerObject.positionX + smallerObject.positionX) / 2.0;	// nova pozice je aritmeticky prumer pozic
					double newPositionY = (biggerObject.positionY + smallerObject.positionY) / 2.0;
					
					double newSpeedX = biggerObject.speedX + (smallerObject.speedX*ratio);	// mensi objekt nema na rychlost takovy vliv
																							// -> vynasobeni promennou ratio
					double newSpeedY = biggerObject.speedY + (smallerObject.speedY*ratio);
					double newSpeed = Vectors.vectorAddition(newSpeedX, newSpeedY);
					
					double newAccelerationX = biggerObject.accelerationX + (smallerObject.accelerationX)*ratio;
					double newAccelerationY = biggerObject.accelerationY + (smallerObject.accelerationY)*ratio;
					double newAcceleration = Vectors.vectorAddition(newAccelerationX, newAccelerationY);
					
					biggerObject.setWeight(newWeight);
					biggerObject.setRadius(newRadius);
					biggerObject.setPositionX(newPositionX);
					biggerObject.setPositionY(newPositionY);
					biggerObject.setSpeedX(newSpeedX);
					biggerObject.setSpeedY(newSpeedY);
					biggerObject.setSpeed(newSpeed);
					biggerObject.setAccelerationX(newAccelerationX);
					biggerObject.setAccelerationY(newAccelerationY);
					biggerObject.setAcceleration(newAcceleration);
					
					spaceObjects.remove(smallerObject);	// odstraneni mensiho objektu z kolekce
					
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
	
	/**
	 * Metoda nastavi scaleovany polomer objektu.
	 * Pokud je polomer mensi nez povoleny minimalni polomer, je nastaven povoleny minimalni polomer.
	 * Pokud je polomer vetsi nez povoleny maximalni polomer, je nastaven povoleny maximalni polomer.
	 * @param radius		novy polomer
	 */
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
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	
	public Shape getShape() {
		return this.shape;
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
