package main;
import java.io.IOException;
import java.util.List;

import graphics.WindowInitializer;
import objects.SpaceObject;
import util.FileLoader;

public class Galaxy_SP2022 {
	
	public static void main(String[] args) {

		String path = args[0];
		
		String[] firstLine = null;
		List<SpaceObject> spaceObjects = null;
		
		try {
			firstLine = FileLoader.loadFirstLine(path);
			spaceObjects = FileLoader.loadSpaceObjects(path);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		double gConstant = Double.parseDouble(firstLine[0]);
		double tStep = Double.parseDouble(firstLine[1]);
		
		Space space = new Space(gConstant, tStep, spaceObjects);
		
	 	WindowInitializer.init(space);

	}
}
