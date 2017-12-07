package main;

import gui.levelEditor.toolsPanel.Terrain;
import gui.levelEditor.toolsPanel.TerrainType;
import gui.profiles.Hotkeys;
import gui.profiles.Profile;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import server.Square;
import server.components.ArrayPosition;
import server.components.Block;
import server.components.Bomb;
import server.components.Figure;
import server.components.PlayingField;

import maps.Map;

/**
 * this class provides access to files in the home directory
 * @author Daniel Roth
 */
public class SaveLoad {
	
	private String gameDir = System.getProperty("user.home") + "//OpenDyna//";
	private String mapDir = System.getProperty("user.home") + "//OpenDyna//maps//";
	private File settingsFile = new File(gameDir+"Settings.txt");
	
	public SaveLoad() {
		initiateDir();
		
	}
	
	private void initiateDir(){
		File file = new File(gameDir);
		file.mkdirs();
		file = new File(gameDir + "maps//");
		file.mkdirs();
		
	}
	
	/**
	 * saves the given profiles in the home directory
	 * @param profiles List<Profile>
	 */
	public void saveProfiles(List<Profile> profiles){
		File file = settingsFile;
		String s = "PROFILES:\n";
		for (Profile p : profiles){
			s += getFormattedString(p);
		}
		try {
			Formatter f = new Formatter(file);
			f.format("%s", s);
			f.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!file.exists()){
			
		}
	}
	
	/**
	 * reads all profiles in home directory and returns them
	 * @return List<Profile>
	 */
	public List<Profile> loadProfiles(){
		List<Profile> list = new LinkedList<Profile>();
		try {
			Scanner scanner = new Scanner(settingsFile);
			String line = "";
			Profile p = null;
			while(scanner.hasNext()){
				line = scanner.nextLine();
				if (line.startsWith("Name: ")){
					p = new Profile("", Color.BLACK);
					p.setName(line.substring(6));
				}
				if (line.startsWith("Color: ")){
					Scanner scannerColor = new Scanner(line.substring(7));
					String r = "255";
					String g = "255";
					String b = "255";
					r = scannerColor.next(); //red
					g = scannerColor.next(); //green
					b = scannerColor.next(); //blue
					scannerColor.close();
//					System.out.println("r: " + r + " g: " + g + " b: " + b);
					p.setColor(new Color((int)Integer.valueOf(r), (int)Integer.valueOf(g), 
							(int)Integer.valueOf(b)));
				}
				if (line.startsWith("Hotkeys")){
					String up = scanner.nextLine().substring(9);
					String down = scanner.nextLine().substring(9);
					String left = scanner.nextLine().substring(9);
					String right = scanner.nextLine().substring(9);
					String bomb = scanner.nextLine().substring(9);
					p.setHotkeys(new Hotkeys(up, down, left, right, bomb));
//					System.out.println("up: " + up + ",down: " + down + ", left: " + left + ", right: " + right);
					//add profile at the end of hotkeys
					list.add(p);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean isSettingsSet(){
		return settingsFile.exists();
	}
	
	/**
	 * returns a formatted string for a given profile in the way it is written in
	 * the text document
	 * @param p Profile
	 * @return formatted String
	 */
	private String getFormattedString(Profile p){
		String s = "Name: " + p.getName() + "\n" +
				"Color: " + colorToString(p.getColor()) + "\n" +
						"Hotkeys\n" +
						"  up:    " + p.getHotkeys().getUp() + "\n" + 
						"  down:  " + p.getHotkeys().getDown() + "\n" + 
						"  left:  " + p.getHotkeys().getLeft() + "\n" + 
						"  right: " + p.getHotkeys().getRight() + "\n" + 
						"  bomb:  " + p.getHotkeys().getBomb()
						+ "\n\n";
		
		return s;
	}
	
	private String colorToString(Color c){
		String s = "";
		s += c.getRed()+" "+c.getGreen()+" "+c.getBlue()+" ";
		
		return s;
	}
	
	/**
	 * load all maps out of maps folder
	 * @return List<Map>
	 */
	public List<Map> loadMaps(){
		List<Map> list = new LinkedList<Map>();
		for (File f : new File(mapDir).listFiles()){
			list.add(loadMap(f));
		}
		return list;
	}
	
	/**
	 * load all maps out of maps folder
	 * @return List<Map>
	 */
	public List<Map> loadMapsObject(){
		List<Map> list = new LinkedList<Map>();
		for (File f : new File(mapDir).listFiles()){
			String name = f.getName();
			String substring = name.substring(name.length()-4, name.length());
			if (substring.equals(".map"))
				list.add(loadMapObject(f));
		}
		return list;
	}
	
	/**
	 * save a map in the maps folder
	 * @param map to be saved
	 * @return boolean, true if successful
	 */
	public boolean saveMap(Map map){
		File file = new File(gameDir + "maps//" + map.getName() + ".txt");
		
		try {
			Formatter f = new Formatter(file);
			f.format("%s", getFormattedMap(map));
			f.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * load a single map out of maps folder
	 * @param name of the map to be loaded
	 * @return Map object
	 */
	public Map loadMap(String name){
		try {
			Scanner scanner = new Scanner(new File(mapDir + name + ".txt"));
			String line = "";
			line = scanner.nextLine();
			line = scanner.nextLine();
			//INITIATE ROW AND COLUMN
			Scanner scannerRowColumn = new Scanner(line);
			scannerRowColumn.next(); //Size: 
			scannerRowColumn.next(); //r: 
			int row = Integer.valueOf(scannerRowColumn.next());
			scannerRowColumn.next();
			int column = Integer.valueOf(scannerRowColumn.next());
			scannerRowColumn.close();
			
			scanner.nextLine();//Starting Positions:
			//INITIATE STARTING POSITIONS
			List<ArrayPosition> startingPositions = new LinkedList<ArrayPosition>();
			
			while(true){
				int r = 0;
				int c = 0;
				line = scanner.nextLine();
				if (!line.startsWith("r:")){
					//player count reached
					break;
					}
				scannerRowColumn = new Scanner(line);
				scannerRowColumn.next(); //load r: 
				r = Integer.valueOf(scannerRowColumn.next());
				scannerRowColumn.next(); //load c: 
				c = Integer.valueOf(scannerRowColumn.next());
				scannerRowColumn.close(); 
				startingPositions.add(new ArrayPosition(r, c));
				}
			scanner.nextLine();//  
			scanner.nextLine();//PlayingField:
			scanner.nextLine();//Field:
			//INITIATE FIELD
			Square[][] field = new Square[row][column];
			
			for (int r=0; r<row; r++){
				line = scanner.nextLine();
				char[] chars = line.toCharArray();
				for (int c=0; c<column; c++){
//					System.out.println(String.valueOf(chars[c]));
					Square s = getFormattedSquare(String.valueOf(chars[c]));
					if (s != null)
						s.setArrayPosition(new ArrayPosition(r, c));
					field[r][c] = s;
				}
			}
			
			scanner.nextLine();// 
			scanner.nextLine();//Terrain:
			//INITIATE TERRAIN
			Terrain[][] terrain = new Terrain[row][column];
			
			for (int r=0; r<row; r++){
				line = scanner.nextLine();
				char[] chars = line.toCharArray();
				for (int c=0; c<column; c++){
					TerrainType tt = getFormattedTerrain(String.valueOf(chars[c]));
					terrain[r][c] = new Terrain(tt.getStandardTexture(), tt, false);
				}
			}
			scanner.close();
			
			//INITIATE PLAYINGFIELD
			PlayingField playingField = new PlayingField(row, column, field, terrain, true);
			
			Map map = new Map(playingField, startingPositions, name);
			return map;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public Map loadMap(File file){
		String name = file.getName();
		return loadMap(file.getName().substring(0, name.length()-4));
	}
	
	public void saveMapObject(Map map){
		try {
			FileOutputStream stream = new FileOutputStream(new File(gameDir + "maps//" + map.getName() + ".map"));
			ObjectOutputStream save = new ObjectOutputStream(stream);
			save.writeObject(map);
			save.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map loadMapObject(File file){
		String name = file.getName();
		return loadMapObject(file.getName().substring(0, name.length()-4));
	}
	
	public Map loadMapObject(String name){
		Map map = null;
		try {
			FileInputStream stream = new FileInputStream(new File(mapDir + name + ".map"));
			ObjectInputStream load = new ObjectInputStream(stream);
			map = (Map) load.readObject();
			load.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * returns a formatted string for a given map in the way it is written in
	 * the text document
	 * @param map
	 * @return
	 */
	private String getFormattedMap(Map map){
		String s = "";
		s += "Name: " + map.getName() + "\n" +
				"Size: r: " + map.getSize().getRow() + " c: " + map.getSize().getColumn() + "\n" +
				"Starting Positions:\n" +
				getFormattedStartingPositions(map) +
				"Player Count: " + map.getPlayerCount() + "\n\n" +
				"PlayingField:\n" +
				"Field:\n" +
				getFormattedField(map) + "\n" +
				"Terrain:\n" +
				getFormattedTerrain(map);
		return s;
	}
	
	private String getFormattedStartingPositions(Map map){
		String s = "";
		for (ArrayPosition a : map.getStartingPositions()){
			s += "r: " + a.getRow() + " c: " + a.getColumn() + "\n";
		}
		
		return s;
	}
	
	private String getFormattedField(Map map){
		PlayingField f = map.getPlayingField();
		String s = "";
		for (int r=0; r<map.getSize().getRow(); r++){
			for (int c=0; c<map.getSize().getColumn(); c++){
				s += getFormattedSquare(f.getSquare(r, c));
			}
			s += "\n";
		}
		return s;
	}
	
	private String getFormattedTerrain(Map map){
		Terrain[][] t = map.getPlayingField().getTerrain();
		String s = "";
		for (int r=0; r<map.getSize().getRow(); r++){
			for (int c=0; c<map.getSize().getColumn(); c++){
				if (t[r][c] == null)
					s+= " ";
				else
					s += getFormattedTerrain(t[r][c].getTerrainType());
			}
			s += "\n";
		}
		return s;
	}
	
	private String getFormattedTerrain(TerrainType t){
		if (t == TerrainType.GRAS)
			return "G";
		if (t == TerrainType.WATER)
			return "W";
		if (t == TerrainType.EARTH)
			return "E";
		if (t == TerrainType.BRIDGE)
			return "B";
		if (t == TerrainType.STONE)
			return "S";
		return " ";
	}
	
	private TerrainType getFormattedTerrain(String s){
		if (s == "G")
			return TerrainType.GRAS;
		if (s == "W")
			return TerrainType.WATER;
		if (s == "E")
			return TerrainType.EARTH;
		if (s == "B")
			return TerrainType.BRIDGE;
		if (s == "S")
			return TerrainType.STONE;
		return TerrainType.GRAS;
	}
	
	private String getFormattedSquare(Square square){
		String s = "";
		if (square == null)
			return " ";
		if (square instanceof Block){
			Block b = (Block)square;
			if (b.isDestructable())
				return "B";
			else
				return "#";
		}
		if (square instanceof Bomb){
			return "E";
		}
		if (square instanceof Figure){
			return "F";
		}
		return s;
	}
	
	private Square getFormattedSquare(String string){
		Square s = null;
		if (string.equals(" "))
			return null;
		if (string.equals("B")){
			return new Block(true);
		}
		if (string.equals("#")){
			return new Block(false);
		}
		if (string.equals("E")){
			return null; //TODO: BOMB placement
//			return new Bomb();
		}
		if (string.equals("F")){
			return null; //TODO: FIGURE placement
		}
		return s;
	}
	
	
	
}
