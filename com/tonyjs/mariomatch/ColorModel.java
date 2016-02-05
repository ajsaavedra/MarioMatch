package com.tonyjs.mariomatch;

import java.awt.Color;

public class ColorModel
{
	private static Color[] backgroundColors = new Color[] {
			new Color(99, 187, 181),
			new Color(222, 171, 66),
			new Color(223, 86, 94),
			new Color(77, 75, 82),
			new Color(105, 94, 133),
			new Color(85, 176, 112)};
	
	public static Color selectRandomBackgroundColor()
	{
		int randomColor = (int) (Math.random() * backgroundColors.length);
		return backgroundColors[randomColor];
	}
}
