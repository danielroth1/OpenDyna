package server.components;

import gui.levelEditor.toolsPanel.Terrain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import server.Logic;
import server.Square;

/**
 * represents a playing field array for a bomber man game
 * the array is Square[][] where every slot is a figure, a bomb or
 * any other object of type square
 * @author Daniel Roth
 */
public class PlayingField implements Serializable{
	
	private Square[][] field;
	private Terrain[][] terrain;
	private int row;
	private int column;
	private List<Square> world = new LinkedList<Square>();
	private List<Square> figures = new LinkedList<Square>();
	private boolean staticWorld;
	
	public PlayingField(int row, int column, boolean staticWorld) {
		this.row = row;
		this.column = column;
		this.staticWorld = staticWorld;
		field = new Square[row][column];
		if (staticWorld){
			for (int r=1; r<row; r++){
				for (int c=1; c<column; c++){
					field[r][c] = new Block(new ArrayPosition(r, c), false);
					c++;
				}
				r++;
			}
		}
		setField(field);
		terrain = new Terrain[row][column];
	}
	
	public PlayingField(int row, int column, Terrain[][] terrain, boolean staticWorld) {
		this(row, column, staticWorld);
		this.terrain = terrain;
	}
	
	public PlayingField(int row, int column, Square[][] field, Terrain[][] terrain, boolean staticWorld) {
		this(row, column, terrain, staticWorld);
		setField(field);
	}
	
	public boolean addSquare(Square s, int row, int column, boolean overwrite){
		if (field[row][column] != null
				&& overwrite == false){
			return false;
		}
		if (s instanceof Figure){
			if (figures.contains(field[row][column]))
				figures.remove(field[row][column]);
			figures.add(s);
		}
		else{
			if (world.contains(field[row][column]))
				world.remove(field[row][column]);
			world.add(s);
		}
		field[row][column] = s;
		
		
		return true;
	}
	
	public boolean addSquare(Square s, ArrayPosition a, boolean overwrite){
		return addSquare(s, a.getRow(), a.getColumn(), overwrite);
	}
	
	public void removeSquare(int row, int column){
		Square s = field[row][column];
		
//		Bomb b = (Bomb) s;
		if (s != null)
//		System.out.println("remove at " + s.getArrayPosition().getRow() + " x " + s.getArrayPosition().getColumn());
		
		if (s != null){
			if (world.contains(s))
//				System.out.println("remove square");
//			if (s instanceof Figure)
				figures.remove(s);
//			else
				world.remove(s);
		}
		field[row][column] = null;
	}
	
	public void removeSquare(ArrayPosition a){
		removeSquare(a.getRow(), a.getColumn());
	}
	
	public void setField(Square[][] field){
		this.field = field;
		for (int r=0; r<row; r++){
			for (int c=0; c<column; c++){
				Square s = field[r][c];
				if (s != null){
					if (s instanceof Figure)
						figures.add(s);
					else
						world.add(s);
				}
			}
		}
	}
	
	public boolean isEmpty(int row, int column){
		if (row >= this.row
				|| column >= this.column)
			return false;
		if (field[row][column] == null){
			return true;
		}
		return false;
	}
	
	public Square getSquare(int row, int column){
		return field[row][column];
	}
	
	public Square getSquare(ArrayPosition a){
		return field[a.getRow()][a.getColumn()];
	}
	
	public PlayingField copy(){
		PlayingField copy = new PlayingField(row, column, staticWorld);
		copy.setField(field.clone());
		copy.setFigures(figures);
		copy.setWorld(world);
		copy.setTerrain(terrain);
		return copy;
	}
	
	public void setField(List<Square> world){
		for (Square s : world){
			addSquare(s, (int)s.getX(), (int)s.getY(), true);
		}
	}
	
	public static ArrayPosition getArrayPosition(int x, int y){
		ArrayPosition a = null;
		//cross
		if (x % 50 == 0
				&& y % 50 == 0){
			a = new ArrayPosition(y/50, x/50);
		}
		else if (x%50 == 0){
			if (y%50 < 25)
				a = new ArrayPosition((y-y%50)/50, x/50);
			else
				a = new ArrayPosition((y+(50-y%50))/50, x/50);
		}
		else if (y%50 == 0){
			if (x%50 < 25)
				a = new ArrayPosition(y/50, (x-x%50)/50);
			else
				a = new ArrayPosition(y/50, (x+(50-x%50))/50);
		}
		
		return a;
	}
	
	public List<Square> getFieldAsList(){
		List<Square> wholeField = new LinkedList<Square>();
		wholeField.addAll(world);
		wholeField.addAll(figures);
		return world;
	}
	
	public List<Square> getWorld(){
		return world;
	}
	
	public List<Square> getFigures(){
		return figures;
	}
	
	public void setWorld(List<Square> world){
		this.world = world;
	}
	
	public void setFigures(List<Square> figures){
		this.figures = figures;
	}
	
	/**
	 * gives the squares from a given point with a given direction within the range of fields
	 * in a List<Square>
	 * @param a ArrayPosition starting position
	 * @param distance number of fields/strength
	 * @param direction direction in witch to get the field line
	 * UP, DOWN, LEFT, RIGHT
	 * @return List<Square> with squares in the line
	 */
	public List<Square> giveLineOfSquaresFromOnePoint(ArrayPosition a, int distance, int direction){
		List<Square> list = new LinkedList<Square>();
		for (ArrayPosition a1 : giveLineOfArrayPositions(a, distance, direction)){
			if (field[a1.getRow()][a1.getColumn()] instanceof Square){
				list.add(field[a1.getRow()][a1.getColumn()]);
			}
		}
		return list;
	}
	
	public List<ArrayPosition> giveLineOfArrayPositions(ArrayPosition a, int distance, int direction){
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		int row = a.getRow();
		int column = a.getColumn();
		if (direction == Logic.UP){
			for(int r = row; r >= row-distance; r--){
				//edge reached
				if (r<0)
					return list;
				list.add(new ArrayPosition(r, column));
				if (field[r][column] != null)
					return list;
			}
		}
		if (direction == Logic.DOWN){
			for(int r = row; r <= row+distance; r++){
				//edge reached
				if (r>=this.row)
					return list;
				list.add(new ArrayPosition(r, column));
				if (field[r][column] != null)
					return list;
					
			}
		}
		if (direction == Logic.LEFT){
			for(int c = column; c >= column-distance; c--){
				//edge reached
				if (c<0)
					return list;
				list.add(new ArrayPosition(row, c));
				if (field[row][c] != null)
					return list;
			}
		}
		if (direction == Logic.RIGHT){
			for(int c = column; c <= column+distance; c++){
				//edge reached
				if (c>=this.column)
					return list;
				list.add(new ArrayPosition(row, c));
				if (field[row][c] != null)
					return list;
			}
		}
		
		return list;
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	public Square[][] getField(){
		return field;
	}

	public Terrain[][] getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain[][] terrain) {
		this.terrain = terrain;
	}
	
	public void setTerrain(Terrain t, ArrayPosition a){
		terrain[a.getRow()][a.getColumn()] = t;
	}
	
	public void removeTerrain(ArrayPosition a){
		terrain[a.getRow()][a.getColumn()] = null;
	}
	
	public void setTerrain(Terrain t, ArrayPosition a, boolean overwrite){
		if (!overwrite
				&& a.getRow() >=0
				&& a.getColumn() >= 0){
			//set only if the terrain at the given position is null
			if (terrain[a.getRow()][a.getColumn()] == null)
				terrain[a.getRow()][a.getColumn()] = t;
		}
		else if(a.getRow() >=0
				&& a.getColumn() >= 0) //do overwrite
			terrain[a.getRow()][a.getColumn()] = t;
	}
	
}
