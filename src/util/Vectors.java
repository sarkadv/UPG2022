package util;

/**
 * Trida pro pocitani s vektory.
 */
public class Vectors {
	
	/**
	 * privatni konstruktor - nechceme vytvorit instanci.
	 */
	private Vectors() {};
	
	/**
	 * Vypocet velikosti vektoru o 2 slozkach.
	 * @param x1	x-souradnice pocatecniho bodu
	 * @param y1	y-souradnice pocatecniho bodu
	 * @param x2	x-souradnice koncoveho bodu
	 * @param y2	y-souradnice koncoveho bodu
	 * @return		velikost vektoru
	 */
	public static double vectorSize(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
	}
	
	/**
	 * Metoda vypocte velikost finalniho vektoru slozeneho ze dvou vektoru.
	 * @param vector1	1. vektor - rovnobezny s osou x
	 * @param vector2	2. vektor - rovnobezny s osou y
	 * @return		velikost slozeneho vektoru
	 */
	public static double vectorAddition(double vector1, double vector2) {
		double x1 = 0;	// x-ova souradnice 1. bodu
		double y1 = 0;	// y-ova souradnice 1. bodu
		
		double x2 = vector1;	// x-ova souradnice 2. bodu
		double y2 = vector2;	// y-ova souradnice 2. bodu
		
		return vectorSize(x1, y1, x2, y2);
	}

}
