package client.ai;

import gui.levelEditor.content.NewMapFrame;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import client.Client;
import client.ai.component.PathElement;

import server.Logic;
import server.Server;
import server.Square;
import server.components.ArrayPosition;
import server.components.Block;
import server.components.Bomb;
import server.components.Explosion;
import server.components.Figure;
import server.components.PlayingField;

public class Ai {
	
	public static final int NONE = 0;
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public static final List<Integer> DIRECTIONS;
	
	static{
		DIRECTIONS = new LinkedList<Integer>();
		DIRECTIONS.add(UP);
		DIRECTIONS.add(DOWN);
		DIRECTIONS.add(LEFT);
		DIRECTIONS.add(RIGHT);
	}
	
	protected String name;
	protected Server server;
	protected Logic logic;
	protected int sleepTime = 0;
	
	public Ai(String name, Server server) {
		this.name = name;
		this.server = server;
		this.logic = server.getGameLogic();
	}
	
	protected int getRandomDirection(){
		List<Integer> list = new LinkedList<Integer>();
		list.add(UP);
		list.add(DOWN);
		list.add(LEFT);
		list.add(RIGHT);
		return list.get((int)(Math.random()*4));
	}
	
	protected List<ArrayPosition> getFields(ArrayPosition a, int length, int direction){
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		if (direction == UP){
			for (int i=a.getRow(); i>a.getRow()-length; i--){
				//edge reached
				
				list.add(new ArrayPosition(i, a.getColumn()));
				if (i <= 0)
					return list;
			}
		}
		if (direction == DOWN){
			for (int i=a.getRow(); i<a.getRow()+length; i++){
				//edge reached
				if (i >= logic.getPlayingField().getRow())
					return list;
				list.add(new ArrayPosition(i, a.getColumn()));
			}
		}
		if (direction == LEFT){
			for (int i=a.getColumn(); i>a.getColumn()-length; i--){
				//edge reached
				
				list.add(new ArrayPosition(a.getRow(), i));
				if (i <= 0)
					return list;
			}
		}
		if (direction == RIGHT){
			for (int i=a.getColumn(); i<a.getColumn()+length; i++){
				//edge reached
				if (i >= logic.getPlayingField().getColumn())
					return list;
				list.add(new ArrayPosition(a.getRow(), i));
			}
		}
		return list;
	}
	
	protected List<ArrayPosition> getExplosionAreas(List<Bomb> bombs){
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		for (Bomb b : bombs){
			list.addAll(getExplosionArea(b));
		}
		return list;
	}
	
	protected List<ArrayPosition> getExplosionArea(Bomb b){
		return getExplosionArea(b, new LinkedList<Bomb>());
	}
	
	protected List<ArrayPosition> getExplosionArea(Bomb b, List<Bomb> alreadySeen){
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		list.add(b.getArrayPosition());
		for (Integer direction : DIRECTIONS){
			list.addAll(getExplosionInOneDirection(b, direction, alreadySeen));
		}
		return list;
	}
	
	protected List<ArrayPosition> getExplosionInOneDirection(Bomb b, int direction){
		return getExplosionInOneDirection(b, direction, new LinkedList<Bomb>());
	}
	
	protected List<ArrayPosition> getExplosionInOneDirection(Bomb b, int direction, List<Bomb> alreadySeen){
		List<ArrayPosition> list = getFields(b.getArrayPosition(), b.getStrength()+1, direction);
		List<ArrayPosition> returnList = new LinkedList<ArrayPosition>();
		for (int s=0; s<list.size(); s++){
			ArrayPosition a = list.get(s);
			Square square = logic.getPlayingField().getSquare(a.getRow(), a.getColumn());
			if (square instanceof Bomb
					&& !alreadySeen.contains(b)){
				alreadySeen.add(b);
				for (ArrayPosition a2 : getExplosionArea(b, alreadySeen)){
					if (!returnList.contains(a2))
						returnList.add(a2);
				}
//				returnList.addAll(getExplosionArea(b, alreadySeen));
				return returnList;
			}
			if (square instanceof Block){
				if (!returnList.contains(a))
					returnList.add(a);
				return returnList;
			}
			if (!returnList.contains(a))
				returnList.add(a);
		}
		
		return returnList;
	}
	
	protected List<ArrayPosition> getAllExplosionAreas(){
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		List<Bomb> bombs = logic.getBombs();
		if (!bombs.isEmpty())
		for (int i=0; i<bombs.size(); i++){
			Bomb b;
			b = bombs.get(i);
			//to prevent adding explosion areas of a bomb more then once
//			if (!list.contains(b.getArrayPosition())){
				list.addAll(getExplosionArea(b));
//			}
		}
		return list;
	}
	
	protected List<ArrayPosition> getPossibleLocations(ArrayPosition a, boolean handleBlocks, Client c){;
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		List<ArrayPosition> thPosLoc = new LinkedList<ArrayPosition>();
		ArrayPosition up = new ArrayPosition(a.getRow(), a.getColumn()-1);
		ArrayPosition down = new ArrayPosition(a.getRow(), a.getColumn()+1);
		ArrayPosition left = new ArrayPosition(a.getRow()-1, a.getColumn());
		ArrayPosition right = new ArrayPosition(a.getRow()+1, a.getColumn());
		thPosLoc.add(up);
		thPosLoc.add(down);
		thPosLoc.add(left);
		thPosLoc.add(right);
		thPosLoc.add(a);
		for (ArrayPosition a2 : thPosLoc){
			Point p = logic.FromArrayPositionToRealPosition(a2);
			Point pOld = logic.FromArrayPositionToRealPosition(a);
//			List<ArrayPosition> explosion = new LinkedList<ArrayPosition>();
//			if (handleBombs){
//				
//			}
			if ((!handleBlocks
					&&logic.legalPositionBounds(p.x, p.y)
					&&logic.getPlayingField().getSquare(PlayingField.getArrayPosition(p.x, p.y)) instanceof Block)
					||logic.legalPosition(p.x, p.y, pOld.x, pOld.y, c)){
				boolean contains = false;
				for (Explosion e : logic.getExplosions()){
					if (a2 != null && e != null)
						if (a2.equals(e.getArrayPosition()))
							contains = true;
				}
				if (!contains)
//					if (!explosion.contains(a2))
						list.add(a2);
			}
		}
		
		return list;
	}
	
	protected List<ArrayPosition> getShortestPath(ArrayPosition source, ArrayPosition aim, Client client){
		List<ArrayPosition> list = getShortestPathRec(source, new LinkedList<ArrayPosition>(), aim, client);
		System.out.println(list.size());
		return list;
	}
	
	private List<ArrayPosition> getShortestPathRec(ArrayPosition source, List<ArrayPosition> alreadySeen, ArrayPosition aim, Client client){
		if (source.equals(aim))
			return new LinkedList<ArrayPosition>();
		List<ArrayPosition> possibleLocations = getPossibleLocations(source, true, client);
		possibleLocations.remove(source);
		List<List<ArrayPosition>> listOfList = new LinkedList<List<ArrayPosition>>();
		List<ArrayPosition> smallest = new LinkedList<ArrayPosition>();
		for (ArrayPosition a : possibleLocations){
			if (!alreadySeen.contains(a)){
				List<ArrayPosition> clone = new LinkedList<ArrayPosition>();
				for (ArrayPosition a2 : alreadySeen){
					if (!clone.contains(a2))
						clone.add(a2);
				}
//				System.out.println(clone.size());
				if (!clone.contains(a)){
					clone.add(a);
//					System.out.println(a);
				}
				if (!clone.contains(source))
					clone.add(source);
				List<ArrayPosition> list;
				if (a.equals(aim)){
					list = new LinkedList<ArrayPosition>();
					list.add(aim);
					list.add(source);
					return list;
				}
				list = getShortestPathRec(a, clone, aim, client);
//				System.out.println(list.size());
				list.add(source);
				smallest = list;
//				if (source.equals(aim))
//					return list;
				if (!list.isEmpty())
					listOfList.add(list);
			}
			
		}
//		System.out.println(listOfList.size());
		//get smallest path of all
		
		for (List<ArrayPosition> l : listOfList){
			if (l.size() < smallest.size()
					&& l.contains(aim))
				smallest = l;
		}
//		System.out.println(smallest.size());
		return smallest;
	}
	
	protected boolean isAtEdge(Square s){
		boolean b = (s.getX()%50<=2 || s.getX()%50>=48)
				&& (s.getY()%50<=2 || s.getY()%50>=48);
		return b;
	}
	
	protected boolean isAtEdge(int x, int y){
		return x%50==0 && y%50==0;
	}
	
	protected int getDirections(ArrayPosition a1, ArrayPosition a2){
//		if (a1.getRow() == a2.getRow()
//				&& a1.getColumn() == a2.getColumn())
//			return NONE;
		if (a1 == null
				|| a2 == null)
			return NONE;
		if (a1.getRow() > a2.getRow())
			return UP;
		if (a1.getRow() < a2.getRow())
			return DOWN;
		if (a1.getColumn() > a2.getColumn())
			return LEFT;
		if (a1.getColumn() < a2.getColumn())
			return RIGHT;
		return NONE;
	}
	
	protected ArrayPosition getNextArrayPositionInPath(ArrayPosition source,
			List<ArrayPosition> path){
		for (ArrayPosition a : path){
			if (lieNextToEachOther(source, a))
				return a;
		}
		return null;
	}
	
	protected boolean lieNextToEachOther(ArrayPosition a1, ArrayPosition a2){
		if (a1.getColumn()+1 == a2.getColumn()
				&& a1.getRow() == a2.getRow())
				return true;
		if (a1.getColumn()-1 == a2.getColumn()
				&& a1.getRow() == a2.getRow())
				return true;
		if (a1.getRow()+1 == a2.getRow()
				&&	a1.getColumn() == a2.getColumn())
				return true;
		if (a1.getRow()-1 == a2.getRow()
				&& a1.getColumn() == a2.getColumn())
				return true;
		return false;
	}
	
	protected PathElement getShortestPathElement(List<PathElement> pathElements){
		PathElement p = null;
		for (PathElement p2 : pathElements){
			p = p2;
			break;
		}
		for (PathElement p2 : pathElements){
			if (p != null
					&& p2.getValue() < p.getValue())
				p = p2;
		}
		return p;
	}
	
	protected double calculateValue(ArrayPosition a1, ArrayPosition a2){
		return 1 + Math.sqrt(Math.pow(a1.getRow()-a2.getRow(), 2) + 
				Math.pow(a1.getColumn()-a2.getColumn(), 2));
	}
	
	protected List<ArrayPosition> getShortestPathDeijkstra(ArrayPosition a1, ArrayPosition a2, boolean nextPlayer, List<ArrayPosition> explosion, Client c){
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		if (a2 != null
				&& a1.equals(a2))
			return list;
		List<ArrayPosition> alreadySeen = new LinkedList<ArrayPosition>();
		List<PathElement> reachable = new LinkedList<PathElement>();
		PathElement p;
		PathElement saveP = null;
		
		//initialization
		p = new PathElement(a1, 0, null);
		alreadySeen.add(a1);
		//EXPLOSIONS
		for (ArrayPosition a : getPossibleLocations(a1, true, c))
			if (!explosion.contains(a))
				reachable.add(new PathElement(a, calculateValue(a1, a), p));
//		System.out.println(reachable.size());
		while(true){
			p = getShortestPathElement(reachable);
			//FIGURE SEEN
			if (nextPlayer){
				boolean breakOut = false;
				for (Figure f : logic.getFiguresOther(c)){
					if (p != null
					&&p.getArrayPosition().equals(f.getArrayPosition())){
//						System.out.println("figure detected");
						breakOut = true;
						break;
					}
				}
				if (breakOut){
//					if (p == null)
//						p = saveP;
					break;
				}
			
			}
			//AIM POSITION SEEN
			if (reachable.isEmpty()
					|| (a2 != null
						&&p.getArrayPosition().equals(a2))){
				if (p == null)
//					p = saveP;
				break;
			}
			alreadySeen.add(p.getArrayPosition());
			reachable.remove(p);
			saveP = p;
//			System.out.println(logic.getPlayingField().getRow() + " X " + logic.getPlayingField().getColumn());
			for (ArrayPosition a : getPossibleLocations(p.getArrayPosition(), true, c))
				if (!alreadySeen.contains(a)
						&& !reachable.contains(new PathElement(a, calculateValue(p.getArrayPosition(), a), p)))
					if (!explosion.contains(a))
						reachable.add(new PathElement(a, calculateValue(p.getArrayPosition(), a), p));
		}
		
		while(true){
			if (p == null)
				break;
			list.add(p.getArrayPosition());
			p = p.getLastPathElement();
		}
//		System.out.println(list);
		return list;
	}
	
	protected List<ArrayPosition> getShortestPathDeijkstra(ArrayPosition a1, ArrayPosition a2, boolean nextPlayer, Client c){
		//EXPLOSIONS
		List<ArrayPosition> explosion = getAllExplosionAreas();
		for (Explosion e : logic.getExplosions()){
			if (e != null)
				explosion.add(e.getArrayPosition());
		}
		return getShortestPathDeijkstra(a1, a2, nextPlayer, explosion, c);
	}
	
	protected List<ArrayPosition> getNextPathOutOfExplosion(ArrayPosition currentPos, List<ArrayPosition> explosion, boolean intelligentBombPlacement, Client c){
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		
		if (!explosion.contains(currentPos))
			return list;
		list.add(currentPos);
		list.remove(currentPos);
		List<ArrayPosition> alreadySeen = new LinkedList<ArrayPosition>();
		List<PathElement> possibleLocations = new LinkedList<PathElement>();
		PathElement p;
		Bomb b = null;
		alreadySeen.add(currentPos);
		for (ArrayPosition a : getPossibleLocations(currentPos, true, c))
			possibleLocations.add(new PathElement(a, 1, null));
		while(true){
			p = getShortestPathElement(possibleLocations);
			
			if (p != null){
				//BOMB TIME LEFT
				b = getBombWithExplosionradiusAtAndSmallestTimeLeft(p.getArrayPosition());
				//everything is lost
				if (intelligentBombPlacement){
					if (b != null
						&& b.getTimeLeft()<400){
						sleepTime = b.getTimeLeft() * 4;
						return new LinkedList<ArrayPosition>();
					}
				}
				if(!explosion.contains(p.getArrayPosition())){
					if (b != null){
//						if (intelligentBombPlacement){
//							if (b.getTimeLeft()<125){
//								sleepTime = b.getTimeLeft();
//							}
//						}
//						else{
//							sleepTime = b.getTimeLeft();
//						}
					}
					break;
				}
			}
			
			//aim not reachable
			if (possibleLocations.isEmpty()){
				b = getBombWithExplosionradiusAtAndSmallestTimeLeft(currentPos);
				if (b != null){
//					if (b.getTimeLeft()<125){
					sleepTime = b.getTimeLeft() * 4;
//					}
				}
				return new LinkedList<ArrayPosition>();
			}
				
			if (p != null)
				for (ArrayPosition a : getPossibleLocations(p.getArrayPosition(), true, c)){
					if (!alreadySeen.contains(p.getArrayPosition())){
						possibleLocations.add(new PathElement(a, 1, p));
					}
				}
			possibleLocations.remove(p);
			alreadySeen.add(p.getArrayPosition());
			
		}
		
		while(true){
			if (p == null)
				break;
			list.add(p.getArrayPosition());
			p = p.getLastPathElement();
			
		}
		
		return list;
	}
	
	protected List<ArrayPosition> getShortestPathToNextBlock(ArrayPosition currentPosition, Client c){
		
		List<ArrayPosition> alreadySeen = new LinkedList<ArrayPosition>();
		List<PathElement> possibleLocation = new LinkedList<PathElement>();
		PathElement p = new PathElement(currentPosition, 1, null);
		//ALREADY SEEN
		alreadySeen.add(currentPosition);
		//EXPLOSIONS
		List<ArrayPosition> explosion = getAllExplosionAreas();
		for (Explosion e : logic.getExplosions()){
			if (e != null)
				explosion.add(e.getArrayPosition());
		}
		//POSSIBLE LOCATIONS
		for (ArrayPosition a : getPossibleLocations(currentPosition, false, c)){
			if (!explosion.contains(a))
				possibleLocation.add(new PathElement(a, calculateValue(a, currentPosition), p));
		}
		while(true){
			p = getShortestPathElement(possibleLocation);
			if (possibleLocation.isEmpty()){
				break;
			}
			Square s = logic.getPlayingField().getSquare(p.getArrayPosition().getRow(), p.getArrayPosition().getColumn());
			Block b;
			if (s instanceof Block){
				b = (Block)s;
				if (b.isDestructable()){
//					System.out.println("destructable");
					if (p != null
							&&p.getLastPathElement() != null
							&&p.getLastPathElement().getArrayPosition().equals(currentPosition))
						return new LinkedList<ArrayPosition>();
					break;
				}
				else{
					possibleLocation.remove(p);
					continue;
				}
			}
			
			for (ArrayPosition a : getPossibleLocations(p.getArrayPosition(), false, c)){
				PathElement p2 = new PathElement(a, calculateValue(a, p.getArrayPosition()), p);
				if (!possibleLocation.contains(p2)
						&& !alreadySeen.contains(a)
						&& !explosion.contains(a))
					possibleLocation.add(p2);
			}
			possibleLocation.remove(p);
			alreadySeen.add(p.getArrayPosition());
		}
		
		List<ArrayPosition> list = new LinkedList<ArrayPosition>();
		
		while(true){
			if (p == null)
				break;
			list.add(p.getArrayPosition());
			p = p.getLastPathElement();
		}
		
		return list;
	}
	
	public List<Bomb> getBombsWithExplosionradiusAt(ArrayPosition a){
		List<Bomb> list = new LinkedList<Bomb>();
		for (Bomb b : logic.getBombs()){
			if (getExplosionArea(b).contains(a))
				list.add(b);
		}
		return list;
	}
	
	protected Bomb getBombWithExplosionradiusAtAndSmallestTimeLeft(ArrayPosition a){
		Bomb bomb = null;
		for (int i=0; i<logic.getBombs().size(); i++){
			Bomb b = logic.getBombs().get(i);
			if (getExplosionArea(b).contains(a)){
				if (bomb == null
						||b.getTimeLeft()<bomb.getTimeLeft())
					bomb = b;
					
			}
		}
		return bomb;
	}
}
