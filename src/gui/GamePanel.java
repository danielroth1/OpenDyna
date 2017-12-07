package gui;

import gui.images.Images;
import gui.levelEditor.toolsPanel.Terrain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import main.GuiControl;
import maps.Texture;

import server.Logic;
import server.Square;
import server.components.ArrayPosition;
import server.components.Block;
import server.components.Bomb;
import server.components.Explosion;
import server.components.Figure;

public class GamePanel extends JPanel{
	
	/**
	 * extra size of the figure
	 */
	public final static int EXTRA = 20;
	public int SCALE = 50;
	private List<Square> movedWorld = new LinkedList<Square>();
	/**
	 * deprecated
	 */
	private List<Square> world = new LinkedList<Square>();
	private List<Figure> figures = new LinkedList<Figure>();
	private List<Explosion> explosions = new LinkedList<Explosion>();
	private List<ArrayPosition> startingPositions = new LinkedList<ArrayPosition>();
	private Terrain[][] terrain;
	private Images images;
	private Image backgroundImage;
	private boolean border;
	private boolean editor;
	
	public GamePanel(){
		this(true, false);
	}
	
	public GamePanel(boolean border, boolean editor) {
		this.border = border;
		this.editor = editor;
		setLayout(null);
		SCALE = 0;
		if (border){
			setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
			SCALE = 50;
		}
		setBackground(Color.WHITE);
		setSize(GuiControl.SIZE);
		setVisible(true);
	}
	
	public List<Square> getWorld(){
		return world;
	}
	
	public boolean removeFromWorld(Square s){
		return world.remove(s);
	}
	
	public boolean addToWorld(Square s){
		return world.add(s);
	}
	
	public boolean addFigure(Figure f){
		return figures.add(f);
	}
	
	public boolean removeFigure(Figure f){
		return figures.remove(f);
	}
	
	public void setWorld(List<Square> world){
		this.world = world;
	}
	
	public void setMovedWorld(List<Square> movedWorld){
		this.movedWorld = movedWorld;
	}
	
	public List<Explosion> getExplosions() {
		return explosions;
	}

	public void setExplosions(List<Explosion> explosions) {
		this.explosions = explosions;
	}
	
	public boolean addToMovedWorld(Square s){
		return movedWorld.add(s);
	}

	public boolean removeFromMovedWorld(Square s){
		return movedWorld.remove(s);
	}
	
	public Terrain[][] getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain[][] terrain) {
		this.terrain = terrain;
	}
	
	public void setTerrain(ArrayPosition a, Terrain t){
		terrain[a.getRow()][a.getColumn()] = t;
	}
	
	public void setImages(Images images){
		this.images = images;
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	
	public List<ArrayPosition> getStartingPositions() {
		return startingPositions;
	}

	public void setStartingPositions(List<ArrayPosition> startingPositions) {
		this.startingPositions = startingPositions;
	}

	public void refresh(){
		movedWorld = new LinkedList<Square>();
		world = new LinkedList<Square>();
		figures = new LinkedList<Figure>();
		explosions = new LinkedList<Explosion>();
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (backgroundImage != null)
			g2.drawImage(backgroundImage, 0, 0, this);
		//TERRAIN
		if (terrain != null){
		for (int r=0; r<terrain.length; r++){
			for (int c=0; c<terrain[r].length; c++){
				Terrain t = terrain[r][c];
				int x = c * 50 + SCALE;
				int y = r * 50 + SCALE;
				if (t != null){
//					System.out.println(t.getTerrainType().getName());
					if (t.getTexture() != null)
						g2.drawImage(images.getImage(t.getTexture()), x, y, this);
				}
//				switch (t){
//					case EARTH:
//						g2.drawImage(images.getTerrainEarth(), x, y, this);
//						break;
//					case WATER:
//						g2.drawImage(images.getTerrainWater(), x, y, this);
//						break;
//					case GRAS:
//						g2.drawImage(images.getTerrainGrass(), x, y, this);
//						break;
//					case BRIDGE:
//						g2.drawImage(images.getTerrainBridge(), x, y, this);
//						break;
//					case STONE:
//						g2.drawImage(images.getTerrainStone(), x, y, this);
//						break;
//				}
//				if (t == Terrain.GRAS){
//					g2.drawImage(images.getTerrainGrass(), x, y, this);
//				}
			}
		}
		}
		
		//SHOW EXPLOSION
		for (int i=0; i<explosions.size(); i++){
			Explosion e = explosions.get(i);
			g2.drawImage(images.getImage(Texture.EXPLOSION), e.getX() + SCALE, e.getY() + SCALE, this);
			//POLYGON
//			g.setColor(Color.RED);
//			g.fillPolygon(e);
//			g.drawPolygon(e);
		}
		g.setColor(Color.BLACK);
		for (int i=0; i<movedWorld.size(); i++){
			Square s = movedWorld.get(i);
			Block b;
			if (s instanceof Block){
				b = (Block) s;
				g2.drawImage(images.getImage(b.getTexture()), b.getX() + SCALE, b.getY() + SCALE, this);
				//POLYGON
//				if (b.isDestructable()){
					//POLYGON
//					g.setColor(Color.LIGHT_GRAY);
//					g.fillPolygon(b);
//				}
//				else{
//					
//					g.setColor(Color.DARK_GRAY);
//					g.fillPolygon(b);
//				}
				g.setColor(Color.BLACK);
			}
			if (s instanceof Bomb){
				g2.drawImage(images.getImage(s.getTexture()), s.getX() + SCALE, s.getY() + SCALE, this);
				
				//POLYGON
//				g.setColor(Color.GRAY);
//				g.fillOval(s.getX() + 5, s.getY() + 5, 40, 40);
//				g.setColor(Color.BLACK);
//				g.drawOval(s.getX() + 5, s.getY() + 5, 40, 40);
			}
//			else
//				g.drawPolygon(s);
		}
//		for (Square s : world){
//			g.drawPolygon(s);
//		}
		
		if (editor){
			//GRID
			if (terrain != null){
				for (int r=0; r<=terrain.length; r++){
					g.drawLine(0, r*50, terrain[0].length*50, r*50);
				}
				for (int c=0; c<=terrain[0].length; c++){
					g.drawLine(c*50, 0, c*50, terrain.length*50);
				}
			}
			//STARTING POSITIONS
			g.setColor(Color.GREEN);
			for (ArrayPosition a : startingPositions){
				
				g.fillOval(a.getColumn()*50, a.getRow()*50, 50, 50);
			}
			g.setColor(Color.BLACK);
		}
		
		
		
		//FIGURE
		for (Figure f : figures){
			switch (f.getDirection()){
				case Logic.NONE:
					g2.drawImage(images.getFigureFront(), f.getX() + SCALE, f.getY()-EXTRA + SCALE, this);
					break;
				case Logic.UP:
					images.getFigureNorth().paintIcon(this, g2, f.getX() + SCALE, f.getY()-EXTRA + SCALE);
//					g2.drawImage(images.getFigureNorth(), f.getX() + SCALE, f.getY()-EXTRA + SCALE, this);
					break;
				case Logic.DOWN:
					images.getFigureSouth().paintIcon(this, g2, f.getX() + SCALE, f.getY()-EXTRA + SCALE);
//					g2.drawImage(images.getFigureSouth(), f.getX() + SCALE, f.getY()-EXTRA + SCALE, this);
					break;
				case Logic.LEFT:
					images.getFigureWest().paintIcon(this, g2, f.getX() + SCALE, f.getY()-EXTRA + SCALE);
//					g2.drawImage(images.getFigureWest(), f.getX() + SCALE, f.getY()-EXTRA + SCALE, this);
					break;
				case Logic.RIGHT:
					images.getFigureEast().paintIcon(this, g2, f.getX() + SCALE, f.getY()-EXTRA + SCALE);
//					g2.drawImage(images.getFigureEast(), f.getX() + SCALE, f.getY()-EXTRA + SCALE, this);
					break;
			}
				
			
			//POLYGONS
//			g2.setColor(f.getColor());
//			g2.fillPolygon(f);
//			g2.setColor(Color.BLACK);
//			g2.drawPolygon(f);
		}
		g2.setColor(Color.BLACK);
	}
	
}
