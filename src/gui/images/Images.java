package gui.images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import main.GameControl;
import maps.Texture;

/**
 * stores all images and image paths
 * @author Daniel Roth
 */
public class Images {
	
	private static String imagePathPng = "/gui/images/png/";
	private static String imagePathGif = "/gui/images/gif/";	
	
	private String figureNorthPath;
	private String figureSouthPath;
	private String figureWestPath;
	private String figureEastPath;
	private String figureFrontPath;
	private String terrainEarthPath;
	private String terrainWaterPath;
	private String terrainGrassPath;
	private String terrainBridgePath;
	private String terrainStonePath;
	
	private String blockStonePath;
	private String blockWoodPath;
	private String blockSolidStonePath;
	private String blockSolidWoodPath;
	private String bombPath;
	private String explosionPath;
	
	private ImageIcon figureNorth;
	private ImageIcon figureSouth;
	private ImageIcon figureWest;
	private ImageIcon figureEast;
	private BufferedImage figureFront;
	private BufferedImage terrainEarth;
	private BufferedImage terrainWater;
	private BufferedImage terrainGrass;
	private BufferedImage terrainBridge;
	private BufferedImage terrainStone;
	
	private BufferedImage blockStone;
	private BufferedImage blockWood;
	private BufferedImage blockSolidStone;
	private BufferedImage blockSolidWood;
	private BufferedImage bomb;
	private BufferedImage explosion;
	
	public static Color[] COLORS = GameControl.COLORS;
	
	
	public Images() {
		initiatePaths();
		initiateIcons();
		initiateFigures();
	}
	
	private void initiatePaths(){
		figureNorthPath = imagePathGif + "figureNorth.gif";
		figureSouthPath = imagePathGif + "figureSouth.gif";
		figureWestPath = imagePathGif + "figureWest.gif";
		figureEastPath = imagePathGif + "figureEast.gif";
		figureFrontPath = imagePathPng + "figureFront.png";
		terrainEarthPath = imagePathPng + "terrainEarth.png";
		terrainWaterPath = imagePathPng + "terrainWater.png";
		terrainGrassPath = imagePathPng + "terrainGrass.png";
		terrainBridgePath = imagePathPng + "terrainBridge.png";
		terrainStonePath = imagePathPng + "terrainStone.png";
		blockStonePath = imagePathPng + "blockStone.png";
		blockWoodPath = imagePathPng + "blockWood.png";
		blockSolidStonePath = imagePathPng + "blockSolidStone.png";
		blockSolidWoodPath = imagePathPng + "blockSolidWood.png";
		bombPath = imagePathPng + "bomb.png";
		explosionPath = imagePathPng + "explosion.png";
	}
	
	private void initiateIcons(){
		try {
//			figureNorth = ImageIO.read(getClass().getResource(figureNorthPath));
			figureNorth = new ImageIcon(getClass().getResource(figureNorthPath));
			figureSouth = new ImageIcon(getClass().getResource(figureSouthPath));
			figureWest = new ImageIcon(getClass().getResource(figureWestPath));
			figureEast = new ImageIcon(getClass().getResource(figureEastPath));
//			figureSouth = ImageIO.read(getClass().getResource(figureSouthPath));
//			figureWest = ImageIO.read(getClass().getResource(figureWestPath));
//			figureEast = ImageIO.read(getClass().getResource(figureEastPath));
			figureFront = ImageIO.read(getClass().getResource(figureFrontPath));
			terrainEarth = ImageIO.read(getClass().getResource(terrainEarthPath));
			terrainWater = ImageIO.read(getClass().getResource(terrainWaterPath));
			terrainGrass = ImageIO.read(getClass().getResource(terrainGrassPath));
			terrainBridge = ImageIO.read(getClass().getResource(terrainBridgePath));
			terrainStone = ImageIO.read(getClass().getResource(terrainStonePath));
			blockStone = ImageIO.read(getClass().getResource(blockStonePath));
			blockWood = ImageIO.read(getClass().getResource(blockWoodPath));
			blockSolidStone = ImageIO.read(getClass().getResource(blockSolidStonePath));
			blockSolidWood = ImageIO.read(getClass().getResource(blockSolidWoodPath));
			bomb = ImageIO.read(getClass().getResource(bombPath));
			explosion = ImageIO.read(getClass().getResource(explosionPath));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage(Texture texture){
		switch (texture){
		case WOOD:
			return blockWood;
		case STONE:
			return blockStone;
		case SOLID_WOOD:
			return blockSolidWood;
		case SOLID_STONE:
			return blockSolidStone;
		case BOMB:
			return bomb;
		case EXPLOSION:
			return explosion;
		case EARTH:
			return terrainEarth;
		case WATER:
			return terrainWater;
		case GRAS:
			return terrainGrass;
		case BRIDGE:
			return terrainBridge;
		default:
			return null;	
		}
	}
	
	private void initiateFigures(){
		
	}
	

	public static String getImagePath() {
		return imagePathPng;
	}
	
	private BufferedImage returnBufferedImage(BufferedImage img){
		BufferedImage b = new BufferedImage(50, 70, BufferedImage.TYPE_INT_ARGB);
		b.getGraphics().drawImage(img, 0, 20, null);
		return img;
	}

	public ImageIcon getFigureNorth() {
		return figureNorth;
	}

	public ImageIcon getFigureSouth() {
		return figureSouth;
	}

	public ImageIcon getFigureWest() {
		return figureWest;
	}

	public ImageIcon getFigureEast() {
		return figureEast;
	}

	public BufferedImage getTerrainEarth() {
		return terrainEarth;
	}

	public BufferedImage getTerrainWater() {
		return terrainWater;
	}

	public BufferedImage getTerrainGrass() {
		return terrainGrass;
	}

	public BufferedImage getTerrainBridge() {
		return terrainBridge;
	}

	public BufferedImage getTerrainStone() {
		return terrainStone;
	}

	public BufferedImage getFigureFront() {
		return returnBufferedImage(figureFront);
	}

	public BufferedImage getBlockStone() {
		return blockStone;
	}

	public BufferedImage getBlockWood() {
		return blockWood;
	}

	public BufferedImage getBlockSolidStone() {
		return blockSolidStone;
	}

	public BufferedImage getBlockSolidWood() {
		return blockSolidWood;
	}

	public BufferedImage getBomb() {
		return bomb;
	}

	public BufferedImage getExplosion() {
		return explosion;
	}
	
	
}
