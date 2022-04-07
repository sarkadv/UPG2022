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

/**
 * Trida pro nacitani ze souboru.
 */
public class FileLoader {
	
	/**
	 * Privatni konstruktor - nechceme vytvorit instanci.
	 */
	private FileLoader() {}
	
	/**
	 * Nacteni prvni radky souboru, ktera obsahuje gravitacni konstantu a casovy krok.
	 * @param path			cesta k souboru
	 * @return				pole nactenych hodnot	
	 * @throws IOException
	 */
	public static String[] loadFirstLine(String path) throws IOException {
		File file = new File(path);
		
		String[] stringsArray = new String[2];
		try (Scanner sc = new Scanner(file)) {
			String line = sc.nextLine();
			stringsArray = line.split(",");
		}
		
		return stringsArray;
		
	}
	
	/**
	 * Nacteni vsech (krome prvni) radek souboru, ktere obsahuji vesmirna telesa.
	 * @param path			cesta k souboru
	 * @return				kolekce nactenych hodnot	
	 * @throws IOException
	 */
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
				
				double weight = Math.abs(Double.parseDouble(stringsArray[6]));
				
				if (type.equals("Planet")) {
					spaceObjects.add(new Planet(name, type, positionX, positionY, speedX, speedY, weight));
				}
				/*
				else if (type.equals("Comet")) {
					spaceObjects.add(new Comet(name, type, positionX, positionY, speedX, speedY, weight));
				}
				else if (type.equals("Rocket")) {
					spaceObjects.add(new Rocket(name, type, positionX, positionY, speedX, speedY, weight));
				}
				*/
				else { // defaultne se vytvori planeta
					spaceObjects.add(new Planet(name, type, positionX, positionY, speedX, speedY, weight));
				}
			}
		}
		
		return spaceObjects;
	}
}
