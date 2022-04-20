import java.io.IOException;
import java.util.List;

/**
 * Hlavni trida, ktera nacte informace ze souboru a vytvori odpovidajici instanci vesmiru.
 */
public class Galaxy_SP2022 {
	
	public static void main(String[] args) {

		String path = args[0];
		
		String[] firstLine = null;
		List<SpaceObject> spaceObjects = null;
		
		try {
			firstLine = FileLoader.loadFirstLine(path);		// nacteni prvni radky souboru
			spaceObjects = FileLoader.loadSpaceObjects(path);	// nacteni ostatnich radek s vesmirnymi objekty
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		double gConstant = Double.parseDouble(firstLine[0]);
		double tStep = Double.parseDouble(firstLine[1]);
		
		Space space = new Space(gConstant, tStep, spaceObjects);		// vytvoreni instance vesmiru
		
	 	WindowInitializer.init(space);	// predani instance do tridy vytvarejici okno

	}
}
