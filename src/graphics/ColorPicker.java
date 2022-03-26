package graphics;

import java.awt.Color;
import java.util.Random;

public class ColorPicker {
	
	private static Color green = new Color(79, 95, 78);
	//private static Color purple = new Color(77, 84, 100);
	private static Color yellow = new Color(79, 96, 100);
	private static Color blue = new Color(79, 95, 78);
	private static Color pink = new Color(100, 83, 95);
	//private static Color red = new Color(100, 70, 78);
	
	public static Color[] colors = {green, yellow, blue, pink};
	
	private static Random r = new Random();
	
	private ColorPicker() {}
	
	public static Color randomColor() {
		return colors[r.nextInt(colors.length)];
		
	}

}
