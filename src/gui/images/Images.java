package gui.images;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import gui.ColorManager;
import main.GameControl;
import maps.Texture;
import server.components.ItemType;

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
	
	private String blockSolidStoneBackgroundPath;
	
	private String blockStonePath;
	private String blockWoodPath;
	private String blockSolidStonePath;
	private String blockSolidWoodPath;
	private String bombPath;
	private String explosionPath;
	
	FigureImages figureTemplate;
	private List<FigureImages> figureImages;
	
	private BufferedImage terrainEarth;
	private BufferedImage terrainWater;
	private BufferedImage terrainGrass;
	private BufferedImage terrainBridge;
	private BufferedImage terrainStone;
	
	private BufferedImage blockSolidStoneBackground;
	
	private BufferedImage blockStone;
	private BufferedImage blockWood;
	private BufferedImage blockSolidStone;
	private BufferedImage blockSolidWood;
	private BufferedImage bomb;
	private BufferedImage explosion;
	
	private Map<ItemType, BufferedImage> itemImages;
	
	public static Color[] COLORS = GameControl.COLORS;
	
	
	public Images() {
		initiatePaths();
		initiateIcons();
		initiateFigures();
	}
	
	private void initiatePaths(){
		blockSolidStoneBackgroundPath = imagePathPng + "blockSolidStoneBackground.png";
		
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
		figureImages = new ArrayList<FigureImages>();
		itemImages = new HashMap<ItemType, BufferedImage>();
		try {
			BufferedImage figureNorth = ImageIO.read(getClass().getResource(figureNorthPath));
			BufferedImage figureSouth = ImageIO.read(getClass().getResource(figureSouthPath));
			BufferedImage figureWest = ImageIO.read(getClass().getResource(figureWestPath));
			BufferedImage figureEast = ImageIO.read(getClass().getResource(figureEastPath));
			BufferedImage figureFront = ImageIO.read(getClass().getResource(figureFrontPath));
			
			figureTemplate = new FigureImages(
					figureNorth,
					figureSouth,
					figureWest,
					figureEast,
					figureFront);
			
			// Fill colors
			for (int i = 0; i < 8; i++) {
				FigureImages image = new FigureImages(figureTemplate);
				image.setColor(ColorManager.toColor(i));
				figureImages.add(image);
			}
			
			blockSolidStoneBackground = ImageIO.read(getClass().getResource(blockSolidStoneBackgroundPath));
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
			
			// items
			itemImages.put(ItemType.INCREASE_BOMBS, ImageIO.read(getClass().getResource(imagePathPng + "itemIncreaseBombs.png")));
			itemImages.put(ItemType.INCREASE_SPEED, ImageIO.read(getClass().getResource(imagePathPng + "itemIncreaseSpeed.png")));
			itemImages.put(ItemType.INCREASE_STRENGTH, ImageIO.read(getClass().getResource(imagePathPng + "itemIncreaseStrength.png")));
			
		} catch (IOException e) {
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
	
	public BufferedImage getImageItem(ItemType type){
		return itemImages.get(type);
	}
	
	private void initiateFigures(){
		
	}
	

	public static String getImagePath() {
		return imagePathPng;
	}

	public BufferedImage getFigureNorth(int i) {
		return figureImages.get(i).getFigureNorth();
	}

	public BufferedImage getFigureSouth(int i) {
		return figureImages.get(i).getFigureSouth();
	}

	public BufferedImage getFigureWest(int i) {
		return figureImages.get(i).getFigureWest();
	}

	public BufferedImage getFigureEast(int i) {
		return figureImages.get(i).getFigureEast();
	}
	
	public BufferedImage getFigureFront(int i) {
		return figureImages.get(i).getFigureFront();
	}
	
	public BufferedImage getBlockSolidStoneBackground() {
		return blockSolidStoneBackground;
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
