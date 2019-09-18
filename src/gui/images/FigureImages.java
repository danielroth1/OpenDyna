package gui.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class FigureImages {
	
	public FigureImages(FigureImages template)
	{
		figureNorth = cloneBufferedImage(template.getFigureNorth());
		figureSouth = cloneBufferedImage(template.getFigureSouth());
		figureWest = cloneBufferedImage(template.getFigureWest());
		figureEast = cloneBufferedImage(template.getFigureEast());
		figureFront = cloneBufferedImage(template.getFigureFront());
	}

	public FigureImages(
			BufferedImage figureNorthTemplate,
			BufferedImage figureSouthTemplate,
			BufferedImage figureWestTemplate,
			BufferedImage figureEastTemplate,
			BufferedImage figureFrontTemplate) {
		
		figureNorth = figureNorthTemplate;
		figureSouth = figureSouthTemplate;
		figureWest = figureWestTemplate;
		figureEast = figureEastTemplate;
		figureFront = figureFrontTemplate;
		
	}
	
	static BufferedImage cloneBufferedImage(BufferedImage img) {
		BufferedImage b = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics g = b.getGraphics();
	    g.drawImage(img, 0, 0, null);
	    g.dispose();
		return b;
	}
	
	public void setColor(Color color)
	{
		setColor(color, figureNorth);
		setColor(color, figureSouth);
		setColor(color, figureWest);
		setColor(color, figureEast);
		setColor(color, figureFront);
	}
	
	public BufferedImage getFigureNorth() {
		return figureNorth;
	}
	public BufferedImage getFigureSouth() {
		return figureSouth;
	}
	public BufferedImage getFigureWest() {
		return figureWest;
	}
	public BufferedImage getFigureEast() {
		return figureEast;
	}
	public BufferedImage getFigureFront() {
		return figureFront;
	}
	
	private void setColor(Color color, BufferedImage image)
	{
		float[] hsvColor = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsvColor);
		float hue = hsvColor[0];
		
		// Set the color 
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color c = new Color(image.getRGB(x, y), true);
				
				if (c.getAlpha() > 0) {
					// convert to hsv
					float[] hsv = new float[3];
					Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);
					
					// change hue value
					hsv[0] = hue;
					c = new Color(Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]));
					
					// set new color
					image.setRGB(x, y, c.getRGB());
					
				}
			}
		}
	}

	private BufferedImage figureNorth;
	private BufferedImage figureSouth;
	private BufferedImage figureWest;
	private BufferedImage figureEast;
	private BufferedImage figureFront;

}
