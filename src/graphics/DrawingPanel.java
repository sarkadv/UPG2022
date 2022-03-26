package graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import main.Space;
import objects.SpaceObject;

public class DrawingPanel extends JPanel {
	
	private Space space;
	private long startTime = System.currentTimeMillis();
	private List<SpaceObject> spaceObjects;

	private double x_min, x_max, y_min, y_max;
	private double world_width, world_height;
	
	
	private SpaceObject currentToggled = null; // vybrana planeta pro zobrazeni informaci
	private boolean showingInfo = false;
	
	public DrawingPanel(Space space) {
		this.setPreferredSize(new Dimension(1200, 400));
		this.space = space;
		this.spaceObjects = space.getSpaceObjects();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
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
		
		for(SpaceObject object : spaceObjects) {		// nalezeni spravnych pozic po scalu
			double x = (object.getPositionX() - x_min)*scale;
			
			double y = (object.getPositionY() - y_min)*scale + this.getHeight()/2 - world_height*scale;
			
			double radius = object.getRadius()*scale;
			
			object.setScaledPositionX(x);
			object.setScaledPositionY(y);
			object.setScaledRadius(radius);
		}
		
		for(SpaceObject object : spaceObjects) {		// vykresleni scaleovanych pozic
			object.draw(g2);
		}
		
		if(showingInfo) {	// vykresleni informaci o objektu
			double x = this.currentToggled.getPositionX();
			double y = this.currentToggled.getPositionY();
			double radius = this.currentToggled.getRadius();
			double weight = this.currentToggled.getWeight();
			String name = this.currentToggled.getName();
			
			g2.setColor(Color.pink);
			g2.drawString("X Position: " + x, this.getWidth() - 225, 25);
			g2.drawString("Y Position: " + y, this.getWidth() - 225, 50);
			g2.drawString("Radius: " + radius, this.getWidth() - 225, 75);
			g2.drawString("Weight: " + weight, this.getWidth() - 225, 100);
			g2.drawString("Name: " + name, this.getWidth() - 225, 125);
			
			this.currentToggled.drawHighlight(g2);	// zvyrazneni prave vybraneho objektu
		}

	}
	
	public boolean isObjectClicked(double x, double y) {
		for(SpaceObject object : spaceObjects){
			if(object.getDrawing().contains(x, y)) {
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
	
}
