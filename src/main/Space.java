package main;
import java.util.List;

import objects.SpaceObject;

/**
 * Trida reprezentujici cely vesmir.
 */
public class Space {
	
	/** gravitacni konstanta */
	private final double G_CONST;
	
	/** casovy krok - jaky cas ubehne v simulaci za 1 s realneho casu */
	private final double T_CONST;	
	
	/** kolekce vesmirnych objektu */
	private List<SpaceObject> spaceObjects;
	
	/**
	 * Konstruktor pro vytvoreni instance vesmiru.
	 * @param GConstant		gravitacni konstanta
	 * @param TStep			casovy krok
	 * @param spaceObjects	kolekce vesmirnych objektu
	 */
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
