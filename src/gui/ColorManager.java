package gui;

import java.awt.Color;

public class ColorManager {

	public ColorManager() {
	}
	
	public static Color toColor(int value)
	{
		Color c = Color.GRAY;
		switch(value)
		{
		case 0:
			c = Color.BLUE;
			break;
		case 1:
			c = Color.RED;
			break;
		case 2:
			c = Color.YELLOW;
			break;
		case 3:
			c = Color.GREEN;
			break;
		case 4:
			c = Color.ORANGE;
			break;
		case 5:
			c = Color.CYAN;
			break;
		case 6:
			c = Color.MAGENTA;
			break;
		case 7:
			c = Color.GRAY;
			break;
		}
		return c;
	}
	
	public static int toValue(Color color)
	{

		int colorValue = 7; // default is gray.
		if (color.equals(Color.BLUE))
			colorValue = 0;
		else if (color.equals(Color.RED))
			colorValue = 1;
		else if (color.equals(Color.YELLOW))
			colorValue = 2;
		else if (color.equals(Color.GREEN))
			colorValue = 3;
		else if (color.equals(Color.ORANGE))
			colorValue = 4;
		else if (color.equals(Color.CYAN))
			colorValue = 5;
		else if (color.equals(Color.MAGENTA))
			colorValue = 6;
		else if (color.equals(Color.GRAY))
			colorValue = 7;
		
		return colorValue;
	}

}
