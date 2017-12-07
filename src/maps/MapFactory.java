package maps;

import gui.levelEditor.toolsPanel.Terrain;
import gui.levelEditor.toolsPanel.TerrainType;

import java.util.LinkedList;
import java.util.List;

import server.components.ArrayPosition;
import server.components.Block;
import server.components.PlayingField;

public class MapFactory {
	
	public MapFactory() {
		
	}
	
	public static Map getStandardMap(int row, int column){
		List<ArrayPosition> startingPositions = new LinkedList<ArrayPosition>();
		startingPositions.add(new ArrayPosition(0, 0));
		startingPositions.add(new ArrayPosition(row, column));
		startingPositions.add(new ArrayPosition(row, 0));
		startingPositions.add(new ArrayPosition(0, column));
		Map map = getRandomMap(row, column, 100);
		map.setName("Standard map");
//		new Map(new PlayingField(row, column, true), startingPositions, "Standard map")
		return map;
	}
	
	public static Map getSelfCreatedMap(){
		PlayingField field = new PlayingField(15, 15, true);
		for (int r=0; r<15; r++){
			for (int c=0; c<15; c++){
				field.addSquare(new Block(new ArrayPosition(r, c), true), r, c, false);
			}
		}
		field.removeSquare(0, 0);
		field.removeSquare(0, 1);
		field.removeSquare(1, 0);
		
		field.removeSquare(14, 0);
		field.removeSquare(14, 1);
		field.removeSquare(13, 0);
		
		field.removeSquare(0, 14);
		field.removeSquare(0, 13);
		field.removeSquare(1, 14);
		
		field.removeSquare(14, 14);
		field.removeSquare(14, 13);
		field.removeSquare(13, 14);
		
		//TERRAIN
		Terrain[][] terrain = new Terrain[14][14];
		for (int r=0; r<14; r++){
			for (int c=0; c<14; c++){
				terrain[r][c] = new Terrain(TerrainType.GRAS, false);
			}
		}
		field.setTerrain(terrain);
		
		List<ArrayPosition> startingPositions = new LinkedList<ArrayPosition>();
		startingPositions.add(new ArrayPosition(0, 0));
		startingPositions.add(new ArrayPosition(14, 14));
		startingPositions.add(new ArrayPosition(14, 0));
		startingPositions.add(new ArrayPosition(0, 14));
		
		return new Map(field, startingPositions, "Map1");
	}
	
	public static Map getRandomMap(int row, int column, double density){
		return getRandomMap(row, column, density, "Random map");
	}
	
	public static Map getRandomMap(int row, int column, double density, String name){
		PlayingField field = new PlayingField(row, column, true);
		int numberOfSquares = (int)((density/100) * (row * column));
		List<ArrayPosition> allSquares = new LinkedList<ArrayPosition>();
		for (int r=0; r<row; r++){
			for (int c=0; c<column; c++){
				allSquares.add(new ArrayPosition(r, c));
			}
		}
		while(!allSquares.isEmpty()){
			if (numberOfSquares <= 0){
				break;
			}
			int randomIndex = (int)(Math.random() * allSquares.size());
			ArrayPosition a = allSquares.get(randomIndex);
			field.addSquare(new Block(a, true), a.getRow(), a.getColumn(), false);
			allSquares.remove(randomIndex);
			numberOfSquares -= 1;
		}
		
		field.removeSquare(0, 0);
		field.removeSquare(0, 1);
		field.removeSquare(1, 0);
		
		field.removeSquare(row-1, 0);
		field.removeSquare(row-1, 1);
		field.removeSquare(row-2, 0);
		
		field.removeSquare(0, column-1);
		field.removeSquare(0, column-2);
		field.removeSquare(1, column-1);
		
		field.removeSquare(row-1, column-1);
		field.removeSquare(row-1, column-2);
		field.removeSquare(row-2, column-1);
		
		//TERRAIN
		Terrain[][] terrain = new Terrain[row][column];
		for (int r=0; r<row; r++){
			for (int c=0; c<column; c++){
				terrain[r][c] = new Terrain(TerrainType.GRAS);
			}
		}
		field.setTerrain(terrain);
		
		List<ArrayPosition> startingPositions = new LinkedList<ArrayPosition>();
		startingPositions.add(new ArrayPosition(0, 0));
		startingPositions.add(new ArrayPosition(row-1, column-1));
		startingPositions.add(new ArrayPosition(row-1, 0));
		startingPositions.add(new ArrayPosition(0, column-1));
		Map map = new Map(field, startingPositions, name);
		
		
		return map;
	}
}
