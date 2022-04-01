package graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import main.Space;
import objects.SpaceObject;

public class DrawingPanel extends JPanel {
	
	private Space space;
	private long startTime = System.nanoTime();
	private long timeNow;
	private double simulationTimeS; // cas simulace v sekundach
	private double actualTimeS;		// opravdovy cas v sekundach
	private double lastComputingTime = startTime / 1000 / 1000;	// cas co probehnul od posledni simulace v ms
	private final int UPDATE_TIME = 17; // kazdych 17 ms update simulace - 1000 : 60 = 17 ms ... 60 snimku za vterinu
	private boolean simulationActive = true; // zmeni se na false kdyz uzivatel zastavi cas
	private double simulationStoppedWhen = 0;	// cas kdy uzivatel zastavil simulaci v sekundach
	private double simulationResumedWhen = 0; // cas kdy uzivatel znovu spustil simulaci v sekundach
	private double simulationStoppedFor = 0;		// jak dlouho byla simulace zastavena dohromady
	private final double INTEGRATION_CONST = 100;
	
	private List<SpaceObject> spaceObjects;
	private double GConstant;
	private double TStep;

	private double x_min, x_max, y_min, y_max;
	private double world_width, world_height;
	
	
	private SpaceObject currentToggled = null; // vybrana planeta pro zobrazeni informaci
	private boolean showingInfo = false;
	
	public DrawingPanel(Space space) {
		this.setPreferredSize(new Dimension(1200, 400));
		this.space = space;
		this.spaceObjects = space.getSpaceObjects();
		this.GConstant = space.getGConstant();
		this.TStep = space.getTStep();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		// cas co probehnul od posledni simulace
		lastComputingTime = (System.nanoTime() / 1000 / 1000.0) - lastComputingTime;
			
		if(lastComputingTime >= UPDATE_TIME) {
			updateSystem(lastComputingTime);
			lastComputingTime = 0;
		}
		
		x_min = spaceObjects.stream().mapToDouble(object -> object.getPositionX()).min().getAsDouble();
		x_max = spaceObjects.stream().mapToDouble(object -> object.getPositionX() + object.getRadius()*2).max().getAsDouble();
		y_min = spaceObjects.stream().mapToDouble(object -> object.getPositionY()).min().getAsDouble();
		y_max = spaceObjects.stream().mapToDouble(object -> object.getPositionY() + object.getRadius()*2).max().getAsDouble();

		world_width = x_max - x_min;
		
		world_height = y_max - y_min;
		
		double scale_x = this.getWidth() / world_width;	// pomer okna / sveta
														// pocet px na 1 jednotku realneho sveta
		double scale_y = this.getHeight() / world_height;
		
		double scale = Math.min(scale_x, scale_y);		// scale podle mensiho z pomeru
		
		g2.setColor(Color.RED);
		g2.drawRect((int)(x_min*scale), (int)(y_min*scale), (int)(world_width*scale), (int)(world_height*scale));
		
		for(SpaceObject object : spaceObjects) {		// nalezeni spravnych pozic po scalu
			double x = (object.getPositionX() - Math.abs(x_min))*scale;
			
			double y = (object.getPositionY() - Math.abs(y_min))*scale;
			
			double radius = object.getRadius()*scale;
			
			object.setScaledPositionX(x);
			object.setScaledPositionY(y);
			object.setScaledRadius(radius);	
		}
		
		for(SpaceObject object : spaceObjects) {		// vykresleni scaleovanych pozic
			object.draw(g2);
		}
		
		computeTime(g2); // vypocitani a vykresleni aktualniho casu simulace
		
		if(showingInfo) {	// vykresleni informaci o objektu
			double x = this.currentToggled.getPositionX();
			double y = this.currentToggled.getPositionY();
			double speedX = this.currentToggled.getSpeedX();
			double speedY = this.currentToggled.getSpeedY();
			double accelerationX = this.currentToggled.getAccelerationX();
			double accelerationY = this.currentToggled.getAccelerationY();
			double radius = this.currentToggled.getRadius();
			double weight = this.currentToggled.getWeight();
			String name = this.currentToggled.getName();
			
			String xString = String.format("%.3g", x);
			String yString = String.format("%.3g", y);
			String speedXString = String.format("%.3g", speedX);
			String speedYString = String.format("%.3g", speedY);
			String accelerationXString = String.format("%.3g", accelerationX);
			String accelerationYString = String.format("%.3g", accelerationY);
			String radiusString = String.format("%.3g", radius);
			String weightString = String.format("%.3g", weight);
			
			g2.setColor(Color.pink);
			g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
			g2.setColor(Color.pink);
			g2.drawString("X position: " + xString + " km", this.getWidth() - 225, 75);
			g2.drawString("Y position: " + yString + " km", this.getWidth() - 225, 100);
			g2.drawString("X speed: " + speedXString + " km/h", this.getWidth() - 225, 125);
			g2.drawString("Y speed: " + speedYString + " km/h", this.getWidth() - 225, 150);
			g2.drawString("X acceleration: " + accelerationXString + " km/h", this.getWidth() - 225, 175);
			g2.drawString("Y acceleration: " + accelerationYString + " km/h", this.getWidth() - 225, 200);
			g2.drawString("radius: " + radiusString + " km", this.getWidth() - 225, 225);
			g2.drawString("weight: " + weightString + " kg", this.getWidth() - 225, 250);
			g2.drawString("name: " + name, this.getWidth() - 225, 275);
			
			this.currentToggled.drawHighlight(g2);	// zvyrazneni prave vybraneho objektu
		}

	}
	
	private void computeTime(Graphics2D g2) {
		timeNow = System.nanoTime();
		double currentTimePeriodS = (timeNow - startTime) / 1000 / 1000 / 1000.0;
		
		if(simulationActive) {
			this.simulationTimeS = currentTimePeriodS * this.TStep - this.simulationStoppedFor * this.TStep;
			this.actualTimeS = currentTimePeriodS - this.simulationStoppedFor;
		}
		
		String simulationTimeString = String.format("%.0g", simulationTimeS);
		String actualTimeString = String.format("%.0f", actualTimeS);
		
		g2.setColor(Color.pink);
		g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
		g2.drawString("actual Time: " + actualTimeString + " s", this.getWidth() - 225, 25); // vypsani aktualniho casu simulace
		g2.drawString("simulation Time: " + simulationTimeString + " s", this.getWidth() - 225, 50); // vypsani aktualniho casu simulace
	}
	
	/**
	 * Update zrychleni, rychlosti, pozic celeho systemu.
	 * @param t	 ubehnuty cas od posledniho updatu v ms
	 */
	public void updateSystem(double t) {
		t = t / 1000.0;	// prevod casu ms -> s
		
		if(simulationActive) {
			for(int j = 0; j < INTEGRATION_CONST; j++) {
				for(int i = 0; i < spaceObjects.size(); i++) {
					SpaceObject object = spaceObjects.get(i);
					object.setAcceleration(object.computeAcceleration(spaceObjects, i, this.GConstant));
				}
					
				for(SpaceObject object : spaceObjects) {	
					object.setSpeedX(object.getSpeedX() + (t/INTEGRATION_CONST*0.5*object.getAccelerationX()));
					object.setSpeedY(object.getSpeedY() + (t/INTEGRATION_CONST*0.5*object.getAccelerationY()));
						
					object.setPositionX(object.getPositionX() + (t/INTEGRATION_CONST * object.getSpeedX()));
					object.setPositionY(object.getPositionY() + (t/INTEGRATION_CONST * object.getSpeedY()));
						
					object.setSpeedX(object.getSpeedX() + (t/INTEGRATION_CONST*0.5*object.getAccelerationX()));
					object.setSpeedY(object.getSpeedY() + (t/INTEGRATION_CONST*0.5*object.getAccelerationY()));
				}
			}
		}

	}

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
	
	public void showInfo() {
		this.showingInfo = true;
	}
	
	public void stopShowingInfo() {
		this.showingInfo = false;
	}
	
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
	
}
