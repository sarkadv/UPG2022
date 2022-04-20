import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
	
	/** instance vesmiru - obsahuje objekty a dulezite konstanty */
	private Space space;
	
	/** cas zalozeni instance v nanosekundach */
	private long startTime = System.nanoTime();
	
	/** cas simulace v sekundach */
	private double simulationTimeS;
	
	/** opravdovy nas cas v sekundach */
	private double actualTimeS;		
	
	/** jak casto se ma updatovat simulace v ms - 17 ms je 60 snimku za vterinu */
	private final int UPDATE_TIME = 17; 
	
	/** je simulace aktivni / pozastavena? */
	private boolean simulationActive = true; 
	
	/** cas zastaveni simulace v sekundach */
	private double simulationStoppedWhen = 0;
	
	/** cas pokracovani simulace v sekundach */
	private double simulationResumedWhen = 0; 
	
	/** cas zastavene simulace celkem v sekundach */
	private double simulationStoppedFor = 0;	
	
	/** kolikrat se v jednom updatu simulace prepocita zrychleni, rychlost, pozice, prekresleni */
	private final double UPDATE_CONST = 100;
	
	/** kolekce vsech objektu typovana na jejich spolecneho abstraktniho predka */
	private List<SpaceObject> spaceObjects;
	
	/** gravitacni konstanta */
	private double GConstant;
	
	/** casovy krok simulace */
	private double TStep;

	/** extremy pozic objektu */
	private double x_min, x_max, y_min, y_max;
	
	/** rozmery simulacniho sveta */
	private double world_width, world_height;
	
	/** rozmery okna aplikace */
	private double window_width, window_height;
	
	/** scalem nasobime souradnice, aby se vesly do okna */
	private double scale;
	
	/** uzivatelem prave vybrana planeta pro zobrazeni informaci */
	private SpaceObject currentToggled = null;
	
	/** jsou prave v pravem hornim rohu zobrazovany informace o planete? */
	private boolean showingInfo = false;
	
	/** kolize zapnuta / vypnuta */
	private boolean collisionOn = true;
	
	private int trajectoryLength = 60;
	
	/**
	 * Konstruktor nastavi panelu rozmery a dulezite instance + konstanty.
	 * @param space		instance vesmiru
	 */
	public DrawingPanel(Space space) {
		this.setPreferredSize(new Dimension(800, 600));
		this.space = space;
		this.spaceObjects = space.getSpaceObjects();
		this.GConstant = space.getGConstant();
		this.TStep = space.getTStep();
	}

	/** 
	 * Hlavni smycka aplikace.
	 * @param g		graficky kontext
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		double currentTime = (System.nanoTime() - this.startTime) / 1000 / 1000.0; // nynejsi cas od vytvoreni instance v ms
		if(currentTime % this.UPDATE_TIME < this.UPDATE_TIME) {	
			updateSystem(this.UPDATE_TIME, g2);		// simulace je prepocitana a prekreslena
		}
		
		computeTime(); // vypocitani aktualniho casu simulace
		drawTime(g2);	// vykresleni casu simulace
		
		if(showingInfo) {	
			drawInfo(g2);	// vykresleni informaci o objektu, pokud je nejaky vybran
		}

	}
	
	/**
	 * Metoda prepocita cas simulace a opravdovy cas.
	 */
	private void computeTime() {
		long timeNow = System.nanoTime();
		double currentTimePeriodS = (timeNow - startTime) / 1000 / 1000 / 1000.0;
		
		if(simulationActive) {
			this.simulationTimeS = currentTimePeriodS * this.TStep - this.simulationStoppedFor * this.TStep;
			this.actualTimeS = currentTimePeriodS - this.simulationStoppedFor;
		}
	}
	
	/**
	 * Metoda vykresli informace o case simulace a opravdovem case na obrazovku.
	 * @param g2	graficky kontext
	 */
	private void drawTime(Graphics2D g2) {
		String simulationTimeString = String.format("%.0g", simulationTimeS);
		String actualTimeString = String.format("%.0f", actualTimeS);
		
		g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
		g2.setColor(Color.MAGENTA);
		g2.drawString("simulation time: " + simulationTimeString + " s", this.getWidth() - 225, 25);
		g2.drawString("actual time: " + actualTimeString + " s", this.getWidth() - 225, 50); 
		g2.drawString("_____________________________", this.getWidth() - 225, 75);
	}
	
	/**
	 * Metoda prepocita zrychleni, rychlosti, pozice celeho systemu.
	 * Zaroven zkontroluje kolize, pokud jsou zapnute.
	 * @param t		 ubehly cas od posledniho updatu v ms
	 * @param g2	 graficky kontext
	 */
	private void updateSystem(double t, Graphics2D g2) {
		t = (t/1000.0) * this.TStep;	// prevod casu ms -> s, pote prevod na cas simulace
		
		if(simulationActive) {	// simulace aktivni, hybe se
			for(int j = 0; j < UPDATE_CONST; j++) {
				for(int i = 0; i < spaceObjects.size(); i++) {
					SpaceObject object = spaceObjects.get(i);
					object.computeAcceleration(spaceObjects, i, this.GConstant, this.collisionOn);
				}
					
				for(SpaceObject object : spaceObjects) {
					
					double currentSpeedX = object.getSpeedX();
					double currentSpeedY = object.getSpeedY();
					double deltaT = (t/UPDATE_CONST)*0.5;
					double currentAccelerationX = object.getAccelerationX();
					double currentAccelerationY = object.getAccelerationY();
					
					double newSpeedX = currentSpeedX + deltaT * currentAccelerationX;
					double newSpeedY = currentSpeedY + deltaT * currentAccelerationY;
					double newSpeed = Vectors.vectorAddition(newSpeedX, newSpeedY);
					
					object.setSpeedX(newSpeedX);
					object.setSpeedY(newSpeedY);
					object.setSpeed(newSpeed);
					
					double currentPositionX = object.getPositionX();
					double currentPositionY = object.getPositionY();
					
					double newPositionX = currentPositionX + deltaT * currentSpeedX;
					double newPositionY = currentPositionY + deltaT * currentSpeedY;
					
					object.setPositionX(newPositionX);
					object.setPositionY(newPositionY);
					
					newSpeedX = currentSpeedX + deltaT * currentAccelerationX;
					newSpeedY = currentSpeedY + deltaT * currentAccelerationY;
					newSpeed = Vectors.vectorAddition(newSpeedX, newSpeedY);
						
					object.setSpeedX(newSpeedX);
					object.setSpeedY(newSpeedY);
					object.setSpeed(newSpeed);
					
				}
				
				if(collisionOn) {	// jen pokud je zapnuta kolize
					for(int i = 0; i < spaceObjects.size(); i++) {
						SpaceObject object = spaceObjects.get(i);
						object.checkForCollision(spaceObjects, i);
					}
				}	
			}
		}
		updateDrawing(g2);

	}
	
	/**
	 * Metoda nejdrive najde spravne scaleovane pozice a rozmery pro objekty a pote je vykresli.
	 * @param g2	graficky kontext
	 */
	private void updateDrawing(Graphics2D g2) {
		// objekty s extremnimi souradnicemi
		SpaceObject x_minObject = findXMinSpaceObject();
		SpaceObject y_minObject = findYMinSpaceObject();
		SpaceObject x_maxObject = findXMaxSpaceObject();
		SpaceObject y_maxObject = findYMaxSpaceObject();
		
		x_min = x_minObject.getPositionX();
		y_min = y_minObject.getPositionY();
		x_max = x_maxObject.getPositionX() + x_maxObject.getRadius()*2;
		y_max = y_maxObject.getPositionY() + y_maxObject.getRadius()*2;

		world_width = Math.abs(x_max - x_min);
		world_height = Math.abs(y_max - y_min);
		
		window_width = this.getWidth();
		window_height = this.getHeight();
		
		// pomer okna / sveta
		double scale_x = (window_width - SpaceObject.MIN_RADIUS*4) / world_width;		
		double scale_y = (window_height - SpaceObject.MIN_RADIUS*4) / world_height;
		
		scale = Math.min(scale_x, scale_y);		// scale podle mensiho z pomeru
		
		SpaceObject.MAX_RADIUS = Math.min(window_width/2, window_height/2);	// nejvyssi mozny polomer objektu podle velikosti okna
		
		for(SpaceObject object : spaceObjects) {		// nalezeni spravnych pozic po scalu
			double x = (object.getPositionX() - x_min)*scale + window_width/2 - (world_width*scale)/2;
			double y = (object.getPositionY() - y_min)*scale + window_height/2 - (world_height*scale)/2;
			double radius = object.getRadius()*scale;
			
			object.setScaledRadius(radius);	
			object.setScaledPositionX(x);
			object.setScaledPositionY(y);
			
			if(simulationActive) {
				if(object.getTrajectoryX().size() > this.trajectoryLength) {
					object.getTrajectoryX().remove(0);
					object.getTrajectoryY().remove(0);
				}

				object.getTrajectoryX().add(object.getPositionX() );
				object.getTrajectoryY().add(object.getPositionY());
			}
			
			List<Double> scaledTrajectoryX = new ArrayList<Double>();
			List<Double> scaledTrajectoryY = new ArrayList<Double>();
			
			for(int i = 0; i < object.getTrajectoryX().size(); i++) {
				double trajectoryPointX = (object.getTrajectoryX().get(i) - x_min)*scale + window_width/2 - (world_width*scale)/2;
				scaledTrajectoryX.add(trajectoryPointX);
			}
			
			object.setScaledTrajectoryX(scaledTrajectoryX);
			
			for(int i = 0; i < object.getTrajectoryY().size(); i++) {
				double trajectoryPointY = (object.getTrajectoryY().get(i) - y_min)*scale + window_height/2 - (world_height*scale)/2;
				scaledTrajectoryY.add(trajectoryPointY);
			}
			
			object.setScaledTrajectoryY(scaledTrajectoryY);
			
			object.drawTrajectory(g2, this.trajectoryLength);
			object.draw(g2);	// vykresleni objektu
			
		}
	}

	/**
	 * Metoda zjisti, zda na nektery z objektu uzivatel kliknul a podle toho nastavi vybrany objekt.
	 * @param x		souradnice x kliknuti
	 * @param y		souradnice y kliknuti
	 * @return		true - na nektery z objektu uzivatel kliknul / jinak false
	 */
	public boolean isObjectClicked(double x, double y) {
		for(SpaceObject object : spaceObjects){
			if(object.approximateHitTest(x, y)) {
				this.currentToggled = object;
				return true;
			}
		}
		this.currentToggled = null;
		return false;
	}
	
	/**
	 * Metoda nastavi promennou pro zobrazovani informaci na true.
	 */
	public void showInfo() {
		this.showingInfo = true;
	}
	
	/**
	 * Metoda vykresli informace o vybranem objektu do praveho horniho rohu.
	 * @param g2	graficky kontext
	 */
	private void drawInfo(Graphics2D g2) {
		if(spaceObjects.contains(this.currentToggled)) {		// pokud kolekce stale obsahuje dany objekt
															// mohl byt odstranen kolizi
			
			this.currentToggled.drawHighlight(g2, Color.GREEN);	// zvyrazneni prave vybraneho objektu
			
			String name = this.currentToggled.getName();
			double x = this.currentToggled.getPositionX();
			double y = this.currentToggled.getPositionY();
			double speed = this.currentToggled.getSpeed();
			double acceleration = this.currentToggled.getAcceleration();
			double radius = this.currentToggled.getRadius();
			double weight = this.currentToggled.getWeight();
			
			// formatovani na format s exponentem 10
			String positionString = String.format("[%.3g, %.3g]", x, y);
			String speedString = String.format("%.3g", speed);
			String accelerationString = String.format("%.3g", acceleration);
			String radiusString = String.format("%.3g", radius);
			String weightString = String.format("%.3g", weight);
			
			g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
			g2.setColor(Color.MAGENTA);
			g2.drawString("name: " + name, this.getWidth() - 225, 100);
			g2.drawString("position: " + positionString + " km", this.getWidth() - 225, 125);
			g2.drawString("speed: " + speedString + " km/h", this.getWidth() - 225, 150);
			g2.drawString("acceleration: " + accelerationString + " km/h", this.getWidth() - 225, 175);
			g2.drawString("radius: " + radiusString + " km", this.getWidth() - 225, 200);
			g2.drawString("weight: " + weightString + " kg", this.getWidth() - 225, 225);
		}
		
	}
	
	/**
	 * Metoda nastavi promennou pro zobrazovani informaci na false.
	 */
	public void stopShowingInfo() {
		this.showingInfo = false;
	}
	
	/**
	 * Metoda prepina statusy simulace a uklada casy prepnuti.
	 */
	public void changeSimulationStatus() {
		if(simulationActive) {
			this.simulationActive = false;
			this.simulationStoppedWhen = System.nanoTime() / 1000 / 1000 / 1000.0;
		}
		else {
			this.simulationActive = true;
			this.simulationResumedWhen = System.nanoTime() / 1000 / 1000 / 1000.0;
			this.simulationStoppedFor += this.simulationResumedWhen - this.simulationStoppedWhen;
		}
	}
	
	/**
	 * Metoda najde objekt s nejmensi souradnici X.
	 * @return		objekt s nejmensi souradnici X
	 */
	private SpaceObject findXMinSpaceObject() {
		SpaceObject xMin = spaceObjects.get(0);
		
		for(int i = 1; i < spaceObjects.size(); i++) {
			if(spaceObjects.get(i).getPositionX() < xMin.getPositionX()) {
				xMin = spaceObjects.get(i);
			}
		}
		
		return xMin;
		
	}
	
	/**
	 * Metoda najde objekt s nejmensi souradnici Y.
	 * @return		objekt s nejmensi souradnici Y
	 */
	private SpaceObject findYMinSpaceObject() {
		SpaceObject yMin = spaceObjects.get(0);
		
		for(int i = 1; i < spaceObjects.size(); i++) {
			if(spaceObjects.get(i).getPositionY() < yMin.getPositionY()) {
				yMin = spaceObjects.get(i);
			}
		}
		
		return yMin;
		
	}
	
	/**
	 * Metoda najde objekt s nejvetsi souradnici X.
	 * @return		objekt s nejvetsi souradnici X
	 */
	private SpaceObject findXMaxSpaceObject() {
		SpaceObject xMax = spaceObjects.get(0);
		
		for(int i = 1; i < spaceObjects.size(); i++) {
			if(spaceObjects.get(i).getPositionX() + spaceObjects.get(i).getRadius()*2 > xMax.getPositionX() + xMax.getRadius()*2) {
				xMax = spaceObjects.get(i);
			}
		}
		
		return xMax;
		
	}
	
	/**
	 * Metoda najde objekt s nejvetsi souradnici Y.
	 * @return		objekt s nejvetsi souradnici Y
	 */
	private SpaceObject findYMaxSpaceObject() {
		SpaceObject yMax = spaceObjects.get(0);
		
		for(int i = 1; i < spaceObjects.size(); i++) {
			if(spaceObjects.get(i).getPositionY() + spaceObjects.get(i).getRadius()*2 > yMax.getPositionY() + yMax.getRadius()*2) {
				yMax = spaceObjects.get(i);
			}
		}
		
		return yMax;
	}
	
}
