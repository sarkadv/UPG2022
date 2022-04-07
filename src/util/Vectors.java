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

}
