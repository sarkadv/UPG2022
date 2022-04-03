package graphics;

import java.awt.Color;
import java.util.Random;

/**
 * Trida slouzici pro uchovavani vhodnych barev.
 */
public class ColorPicker {
	
	private static Color green = new Color(229, 255, 204);
	private static Color purple = new Color(229, 204, 255);
	private static Color yellow = new Color(255, 255, 204);
	private static Color blue = new Color(204, 255, 255);
	private static Color pink = new Color(255, 204, 229);
	private static Color orange = new Color(255, 229, 204);
	
	public static Color[] colors = {green, purple, yellow, blue, pink, orange};
	
	private static Random r = new Random();
	
	private ColorPicker() {}
	
	public static Color randomColor() {
		return colors[r.nextInt(colors.length)];
		
	}

}
