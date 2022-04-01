package main;
import java.util.List;

import objects.SpaceObject;

public class Space {
	
	private final double G_CONST;	// gravitacni konstanta
	private final double T_CONST;		// casovy krok - jaky cas ubehne za 1 s realneho casu
	private List<SpaceObject> spaceObjects;
	
	public Space(double GConstant, double TStep, List<SpaceObject> spaceObjects) {
		this.G_CONST = GConstant;
		this.T_CONST = TStep;
		this.spaceObjects = spaceObjects;
	}

	public double getGConstant() {
		return G_CONST;
	}

	public double getTStep() {
		return T_CONST;
	}

	public List<SpaceObject> getSpaceObjects() {
		return spaceObjects;
	}

}
