package util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import objects.Comet;
import objects.Planet;
import objects.Rocket;
import objects.SpaceObject;

public class FileLoader {
	
	private FileLoader() {}
	
	public static String[] loadFirstLine(String path) throws IOException {
		File file = new File(path);
		
		String[] stringsArray = new String[2];
		try (Scanner sc = new Scanner(file)) {
			String line = sc.nextLine();
			stringsArray = line.split(",");
		}
		
		return stringsArray;
		
	}
	
	public static List<SpaceObject> loadSpaceObjects(String path) throws IOException {
		File file = new File(path);
		
		List<SpaceObject> spaceObjects = new ArrayList<SpaceObject>();
		
		String[] stringsArray = new String[7];
		
		try(Scanner sc = new Scanner(file)) {
			sc.nextLine();	// prvni radka neobsahuje vesmirna telesa
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				stringsArray = line.split(",");
				
				String name = stringsArray[0];
				String type = stringsArray[1];
				
				double positionX = Double.parseDouble(stringsArray[2]);
				double positionY = Double.parseDouble(stringsArray[3]);
				
				double speedX = Double.parseDouble(stringsArray[4]);
				double speedY = Double.parseDouble(stringsArray[5]);
				
				double weight = Double.parseDouble(stringsArray[6]);
				
				if (type.equals("Planet")) {
					spaceObjects.add(new Planet(name, type, positionX, positionY, speedX, speedY, weight));
				}
				else if (type.equals("Comet")) {
					spaceObjects.add(new Comet(name, type, positionX, positionY, speedX, speedY, weight));
				}
				else if (type.equals("Rocket")) {
					spaceObjects.add(new Rocket(name, type, positionX, positionY, speedX, speedY, weight));
				}
				else {
					throw new IOException("Nespravne zapsane typy vesmirnych teles.");
				}
			}
		}
		
		return spaceObjects;
	}
}
