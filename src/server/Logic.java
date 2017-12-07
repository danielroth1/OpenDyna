package server;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import maps.Map;
import maps.MapFactory;

import server.components.ArrayPosition;
import server.components.Block;
import server.components.Bomb;
import server.components.ClientObject;
import server.components.Explosion;
import server.components.Figure;
import server.components.PlayingField;

import client.Client;

/**
 * handels game Logic
 * comunicates with server class
 * @author Daniel Roth
 */
public class Logic {
	
	public final static int NONE = 0;
	public final static int UP = 1;
	public final static int DOWN = 2;
	public final static int LEFT = 3;
	public final static int RIGHT = 4;
	public static List<Integer> DIRECTIONS = new LinkedList<Integer>();
	static{
		DIRECTIONS.add(UP);
		DIRECTIONS.add(DOWN);
		DIRECTIONS.add(LEFT);
		DIRECTIONS.add(RIGHT);
	}
	public int TOLERANCE = 20;
	public int rows;
	public int columns;
	
	private Server server;
	private Engine engine;
	private List<Client> clients = new LinkedList<Client>();
	private int[] movesX;
	private int[] movesY;
	private Thread thread;
	private List<Square> world;
	private List<Figure> figures;
	private PlayingField playingField;
	private Map map;
	private ArrayPosition[] layBomb;
	private ArrayPosition[] lastLaidBomb;
	private List<Bomb> bombs = new LinkedList<Bomb>();
	private List<Explosion> explosions = new LinkedList<Explosion>();
	private HashMap<Client, ClientObject> clientObjects = new HashMap<Client, ClientObject>();
	private boolean gameStarted;
	private boolean gameEnd;
	
	public Logic(Server server) {
		this.server = server;
		engine = new Engine(this);
//		playingField = new PlayingField(rows, columns, true);
		setMap(MapFactory.getSelfCreatedMap());
		
		rows = playingField.getRow();
		columns = playingField.getColumn();
//		startNewGame(2);
		
		thread = new Thread(){
			public void run(){
				while(true){
					
					if (gameStarted){
						if (!gameEnd){
							//HANDLE HASWON
							if (getListOfLivingPlayers().size() <= 1){
								Client winner = null;
								if (getListOfLivingPlayers().size()==1){
									winner = getListOfLivingPlayers().get(0);
									winner.hasWon();
								}
								gameEnd = true;
								for (Client c : clients){
									if (winner != null)
										c.playerHasWon(winner.getName());
									else
										c.drawOccurred();
								}
								for (Client c : clients){
									c.gameEnd();
								}
							}
						}
					//HANDLE CONSEQUENCES OF EXPLOSION FOR FIGURE AND CLIENTS
					for (Client c : clients){
						ClientObject co = clientObjects.get(c);
						Figure f = c.getFigure();
						if (standsInExlopsion(f)){
							killFigure(c);
						}
					}
					
					//HANDLE EXPLOSIONS
					for (int j=0; j<explosions.size(); j++){
						Explosion e = explosions.get(j);
						e.setDuration(e.getDuration()-1);
						if (e.getDuration() <= 0){
							explosions.remove(e);
						}
						
					}
					if (!explosions.isEmpty()){
						for (Client c : clients){
							c.explosions(explosions);
						}
					}
						
					
					//BOMB EXPLOSION
					for (int j=0; j<bombs.size(); j++){
						Bomb b = bombs.get(j);
						b.setTimeLeft(b.getTimeLeft()-engine.speed);
						if (b.getTimeLeft() <= 0
								|| standsInExlopsion(b)){
							explodeBomb(b);
//							System.out.println(playingField.getWorld().contains(b) + " at " + b.getArrayPosition().getRow() + " x " + b.getArrayPosition().getColumn());
							j--;
						}
					}
					
					//for every pixel movement the new position gets calculated
					for (int i = 0; i < engine.speed; i++){
						
						for (Client c : clients){
							ClientObject co = clientObjects.get(c);
							//BOMB PLACEMENT
							if (co.isAlive()){
								if (co.getBombsLeft() > 0
										&& layBomb[clients.indexOf(c)] != null){
									ArrayPosition a = layBomb[clients.indexOf(c)];
									Bomb b = new Bomb(a, c.getFigure().getStrength(), 450, c);
									//cant overwrite laid bomb
									if (playingField.addSquare(b, a.getRow(), a.getColumn(), false)){
										bombs.add(b);
										co.setBombsLeft(co.getBombsLeft()-1);
										for (Client c2 : clients){
											c2.bombLaidDown(b);
										}
									}
								}
								//DIRECTION
								c.getFigure().setDirection(NONE);
								//MOVEMENT
								int SIZE = 50;
								if (c.getFigure().getY() % SIZE == 0){
									if (movesY[clients.indexOf(c)] == Logic.UP
											|| movesY[clients.indexOf(c)] == Logic.DOWN){
										if (c.getFigure().getX() % SIZE < TOLERANCE
												&& c.getFigure().getX() % SIZE != 0) //not at a cross
											move(c, Logic.LEFT);
										else if (c.getFigure().getX() % SIZE > (SIZE-TOLERANCE)
												&& c.getFigure().getX() % SIZE != 0)
											move(c, Logic.RIGHT);
									}
									else
										move(c, movesX[clients.indexOf(c)]);
								}
								if (c.getFigure().getX() % SIZE == 0){
									if (movesX[clients.indexOf(c)] == Logic.LEFT
											|| movesX[clients.indexOf(c)] == Logic.RIGHT){
										if (c.getFigure().getY() % SIZE < TOLERANCE
												&& c.getFigure().getY() % SIZE != 0)
											move(c, Logic.UP);
										else if (c.getFigure().getY() % SIZE > (SIZE-TOLERANCE)
												&& c.getFigure().getY() % SIZE != 0)
											move(c, Logic.DOWN);
									}
									else
										move(c, movesY[clients.indexOf(c)]);
								}
							}
							}
							//******************
						}
					}
					
					movesX = new int[clients.size()];
					movesY = new int[clients.size()];
					layBomb = new ArrayPosition[clients.size()];
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}
	
	public void addClient(Client c){
		clients.add(c);
		resetArrays();
		clientObjects.put(c, new ClientObject(c, true, 2));
		server.clientLoggedIn(c);
	}
	
	public boolean removeClient(Client c){
		//TODO question if the client should receive a message that he logged out
				server.clientLoggedOut(c);
		boolean b = clients.remove(c);
		clientObjects.remove(c);
		resetArrays();
		return b;
	}
	
	private void resetArrays(){
		movesX = new int[clients.size()];
		movesY = new int[clients.size()];
		layBomb = new ArrayPosition[clients.size()];
		lastLaidBomb = new ArrayPosition[clients.size()];
	}
	
	public List<Client> getClients(){
		return clients;
	}
	
	public void move(int direction, Client c){
		int index = 0;
		if (clients.contains(c)){
			index = clients.indexOf(c);
			if (direction == Logic.UP
					|| direction == Logic.DOWN)
				movesY[index] = direction;
			else
				movesX[index] = direction;
		}
	}
	
	/**
	 * lay a bomb
	 * @param c your client
	 * @return true if successful otherwise false
	 */
	public boolean layBomb(Client c){
		int x = c.getFigure().getX();
		int y = c.getFigure().getY();
		ArrayPosition a = playingField.getArrayPosition(x, y);
		if (standsOnEmptyFields(c.getFigure())){
			layBomb[clients.indexOf(c)] = a;
			lastLaidBomb[clients.indexOf(c)] = a;
			return true;
		}
		return false;
	}
	
	public void setWorldComponents(LinkedList<Square> world, LinkedList<Figure> figures){
		this.world = world;
		this.figures = figures;
	}
	
	public List<Square> getWorld(){
		return world;
	}
	
	public List<Figure> getFigures(){
		List<Figure> list = new LinkedList<Figure>();
		ClientObject c2;
		for (Client c : clients){
			c2 = clientObjects.get(c);
			if (c2.isAlive())
				list.add(c.getFigure());
		}
		return list;
	}
	
	public List<Figure> getFiguresOther(Client c){
		List<Figure> list = new LinkedList<Figure>();
		ClientObject c3;
		for (Client c2 : clients){
			if (c.getFigure() != c2.getFigure()){
				c3 = clientObjects.get(c2);
				if (c3.isAlive())
					list.add(c2.getFigure());
			}
		}
//		System.out.println(list.get(0).getArrayPosition().getRow() + " x " + list.get(0).getArrayPosition().getColumn());
//		System.out.println(list.size());
		return list;
	}
	
	public PlayingField getPlayingField(){
		return playingField.copy();
	}
	
	/**
	 * set a new square on the playingField
	 * NOTE: dangerouse: to much rights to the clients
	 * @param s Square to be set
	 * @param row int
	 * @param column int
	 */
	public void addSquareOnPlayingField(Square s, int row, int column){
		playingField.addSquare(s, row, column, true);
	}
	
	public void addSquareOnPlayingField(Square s, ArrayPosition a){
		playingField.addSquare(s, a.getRow(), a.getColumn(), true);
	}
	
	/**
	 * returns a list of blocked fields by a given figure
	 * it contains max. 2 Points
	 * NOTE: bugs with negative numbers
	 * @param x position
	 * @param y position
	 * @return List<ArrayPosition>
	 */
	public List<ArrayPosition> getBlockedFields(int x, int y){
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		ArrayPosition a1 = null;
		ArrayPosition a2 = null;
		//cross
		if (x % 50 == 0
				&& y % 50 == 0){
			a1 = new ArrayPosition(y/50, x/50);
		}
		else if (x%50 == 0){
			a1 = new ArrayPosition((y-y%50)/50, x/50);
			a2 = new ArrayPosition((y+(50-y%50))/50, x/50);
		}
		else if (y%50 == 0){
			a1 = new ArrayPosition(y/50, (x-x%50)/50);
			a2 = new ArrayPosition(y/50, (x+(50-x%50))/50);
		}
		//array index out of bounds prevention
		if (a1 != null
//				&& a1.getRow() < rows
//				&& a1.getColumn() < columns
//				&& a1.getRow() > 0
//				&& a1.getColumn() > 0
				)
			list.add(a1);
		if (a2 != null
//				&& a2.getRow() < rows
//				&& a2.getColumn() < columns
//				&& a2.getRow() > 0
//				&& a2.getColumn() > 0
				)
			list.add(a2);
		
		return list;
	}
	
	/**
	 * decides if figure at given field is at an illegal position
	 * @param x position
	 * @param y position
	 * @return true if position is legal
	 */
	public boolean legalPosition(int x, int y, int oldX, int oldY, Client c){
		if (x < 0
				|| y < 0)
			return false;
		List<ArrayPosition> list = getBlockedFields(x, y);
		List<ArrayPosition> list2 = getBlockedFields(oldX, oldY);
		
		boolean walkOnBomb = false;
		for (ArrayPosition a : list2){
			for (Client c2 : clients){
			if (lastLaidBomb[clients.indexOf(c2)] != null
					&& lastLaidBomb[clients.indexOf(c2)].equals(a))
				walkOnBomb = true;
			}
		}
		
		for (ArrayPosition a : list){
			//playing field bounds
			if 	(x < 0
					|| a.getRow() >= playingField.getRow()
					|| y < 0
					|| a.getColumn() >= playingField.getColumn())
				return false;
			//ON BOMB MOTION
			if (!playingField.isEmpty(a.getRow(), a.getColumn())){
				boolean lastbomb = false;
				for (Client c2 : clients){
					if ((lastLaidBomb[clients.indexOf(c2)] != null
							&& lastLaidBomb[clients.indexOf(c2)].equals(a))
							&& walkOnBomb){
						lastbomb = true;
						
					}
				}
				if (!lastbomb){
//					System.out.println("last bomb error");
					return false;
				}
			}
			//TODO ELSE ERROR
			//CURRENT FIELD EMPTY
//			else if (!playingField.isEmpty(a.getRow(), a.getColumn()))
//					return false;
					
			}
		return true;
	}
	
	public boolean legalPositionBounds(int x, int y){
		ArrayPosition a = PlayingField.getArrayPosition(x, y);
		if 	(x < 0
				|| a.getRow() >= playingField.getRow()
				|| y < 0
				|| a.getColumn() >= playingField.getColumn())
			return false;
		return true;
	}
	
	public Point getNewPosition(Figure f, int direction){
		int newX = f.getX();
		int newY = f.getY();
		if (direction == UP){
//			newY = newY - engine.speed;
			newY = newY - 1;
		}
		if (direction == DOWN){
//			newY = newY + engine.speed;
			newY = newY + 1;
		}
		if (direction == RIGHT){
//			newX = newX + engine.speed;
			newX = newX + 1;
		}
		if (direction == LEFT){
//			newX = newX - engine.speed;
			newX = newX - 1;
		}
		
		return new Point(newX, newY);
	}
	
	private void move(Client c, int direction){
		Point newPosition = getNewPosition(c.getFigure(), direction);
//		for (int i = 0; i < engine.speed; i++){
			newPosition = getNewPosition(c.getFigure(), direction);
			if (legalPosition(newPosition.x, newPosition.y, c.getFigure().getX(), c.getFigure().getY(), c)){
				engine.move(c.getFigure(), direction);
				c.getFigure().setDirection(direction);
			}
		c.figureMoved(c.getFigure(), direction);
//		}
		
	}
	
	public Point FromArrayPositionToRealPosition(ArrayPosition a){
		return new Point(a.getColumn() * 50, a.getRow()  * 50);
	}
	
	/**
	 * return true if the given figure stands on the given square
	 * on the playing field
	 * @param s given square
	 * @param f given figure
	 * @return true if it stands on the square, else false
	 */
	public boolean standsOnSquare(Square s, Figure f){
		List<ArrayPosition> blockedFields = getBlockedFields(f.getX(), f.getY());
		for (ArrayPosition a : blockedFields){
			if (playingField.getSquare(a.getRow(), a.getColumn()) == s){
				return true;
			}
		}
		return false;
	}
	
	public boolean standsOnEmptyFields(Square s){
		for (ArrayPosition a : getBlockedFields(s.getX(), s.getY())){
			if (playingField.getSquare(a.getRow(), a.getColumn()) != null)
				return false;
		}
		return true;
	}
	
	private void explodeBomb(Bomb b){
		List<ArrayPosition> explosionArea = new LinkedList<ArrayPosition>();
		//REMOVE BOMB
		bombs.remove(b);
		playingField.removeSquare(b.getArrayPosition().getRow(), b.getArrayPosition().getColumn());
		//refilling exploded bombs
		ClientObject co = clientObjects.get(b.getOwner());
		co.setBombsLeft(co.getBombsLeft() + 1);
		for (int i : DIRECTIONS){
			//ALL DIRECTIONS
			explosionArea.addAll(explodeInDirection(b.getArrayPosition(), b.getStrength(), i));
		}
		for (Client c : clients){
			//INFORM CLIENTS
			c.bombExploded(b, explosionArea);
		}
		for (ArrayPosition a : explosionArea){
			Explosion e = new Explosion(a, 40);
			explosions.add(e);
		}
	}
	
	/**
	 * an explosions in one direction
	 * @param a 
	 * @param strength
	 * @param direction
	 * @return list of ArrayPosition of exploded Squares
	 */
	private List<ArrayPosition> explodeInDirection(ArrayPosition a, int strength, int direction){
//		List<ArrayPosition> explodedFields = new LinkedList<ArrayPosition>();
		List<ArrayPosition> explodedFields = playingField.giveLineOfArrayPositions(a, strength, direction);
		for (Square s : playingField.giveLineOfSquaresFromOnePoint(a, strength, direction)){
			//CAUSES BOMB EXPLOSION CHAINS
			if (s instanceof Bomb){
				explodeBomb((Bomb)s);
			}
			else
//				explodedFields.add(s.getArrayPosition());
			if (s instanceof Block){
				Block b = (Block)s;
				if (b.isDestructable()){
					playingField.removeSquare(s.getArrayPosition().getRow(), s.getArrayPosition().getColumn());
//					//HANDLE ITEM DROP
//					playingField.addSquare(b.getItem(), a, true);
					for (Client c : clients){
						c.blockDestroyed();
					}
					return explodedFields;
				}
			}
		}
		return explodedFields;
	}

	public List<Explosion> getExplosions() {
		List<Explosion> list = new LinkedList<Explosion>();
		for (int i=0; i<explosions.size(); i++){
			list.add(explosions.get(i));
		}
//		list.addAll(explosions);
		return list;
	}
	
	private boolean standsInExlopsion(Square f){
		for (Explosion e : explosions){
			//FIGURE STANDS IN EXPLOSION
			if (PlayingField.getArrayPosition(f.getX(), f.getY()).equals(e.getArrayPosition())){
				return true;
			}	
		}
		return false;
	}
	
	private List<Client> getListOfLivingPlayers(){
		List<Client> list = new LinkedList<Client>();
		for (int i=0; i<clients.size(); i++){
			Client c = clients.get(i);
			ClientObject co = clientObjects.get(c);
			if (co.isAlive())
				list.add(c);
		}
		
		return list;
	}
	
	public void startNewGame(){
		gameStarted = true;
		gameEnd = false;
//		for (Client c : clients){
//			clientObjects.put(c, new ClientObject(c, true, bombs));
//		}
		List<ArrayPosition> positions = map.getStartingPositions();
		//SET STARTING POSITIONS
		for (int i=1; i<clients.size(); i++){
			Client c = clients.get(i);
			if (i<=positions.size())
				c.getFigure().setArrayPosition(positions.get(i-1));
			else{
				surrender(c);
				System.out.println("max player count reached");
			}
		}
		for(Client c : clients){
			c.gameStarted();
		}
	}
	
	public void forceEndOfGame(){
		gameStarted = false;
		gameEnd = true;
		for (Client c : clients){
			c.hasLost();
			for (Client c2 : clients){
				c2.hasLost();
			}
		}
	}
	
	public void setMap(Map map){
		this.map = map;
		playingField = map.getPlayingField();
		for(Client c : clients){
			c.mapChanged();
		}
	}
	
	public Map getMap(){
		return map.getCopy();
	}
	
	public List<Bomb> getBombs(){
		return bombs;
	}
	
	public Bomb getBomb(Client c){
		return new Bomb(c.getFigure().getArrayPosition(), c.getFigure().getStrength(), 450, c);
	}
	
	public void surrender(Client c){
		killFigure(c);
	}
	
	private void killFigure(Client c){
		ClientObject co = clientObjects.get(c); 
		if (co.isAlive()){
			for(Client c2 : clients){
				c2.figureDied(c.getFigure());
				c2.playerHasLost(c.getName());
			}
			c.hasLost();
			co.setAlive(false);
		}
	}
	
	public void shutDown(){
		clients = new LinkedList<Client>();
		server = null;
	}
	
}
