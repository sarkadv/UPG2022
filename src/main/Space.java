package main;
import java.util.List;

import objects.SpaceObject;

public class Space {
	
	private final double gConstant;	// gravitacni konstanta
	private final double tStep;		// casovy krok - jaky cas ubehne za 1 s realneho casu
	private List<SpaceObject> spaceObjects;
	
	public Space(double gConstant, double tStep, List<SpaceObject> spaceObjects) {
		this.gConstant = gConstant;
		this.tStep = tStep;
		this.spaceObjects = spaceObjects;
	}

	public double getgConstant() {
		return gConstant;
	}

	public double gettStep() {
		return tStep;
	}

	public List<SpaceObject> getSpaceObjects() {
		return spaceObjects;
	}

}
