package client.ai;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import server.Server;
import server.components.ArrayPosition;
import server.components.Bomb;
import server.components.Explosion;
import server.components.Figure;
import server.components.Player;
import client.Client;

public class EasyAi extends Ai implements Client{
	
	
	private Thread thread;
	private Thread thread2;
	private int movingDirection = NONE;
	private Figure figure;
	private boolean gameStarted = false;
	
	public EasyAi(String name,  Server server) {
		super(name, server);
		this.figure = new Figure(name, Color.BLUE, 2, 0, 0);
//		thread2 = new Thread(){
//			@Override
//			public void run(){
//				while(true){
//					if (gameStarted){
//						if (isAtEdge(figure)){
//							Bomb b = logic.getBomb(EasyAi.this);
//							for (Figure f : logic.getFiguresOther(EasyAi.this)){
//								if (f != figure
//										&& getExplosionArea(b).contains(f.getArrayPosition()))
//									logic.layBomb(EasyAi.this);
//							}
//							handleMovement();
//							
//							
//						}
//					}
//					try {
//						Thread.sleep(15);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//			}
//		};
		thread = new Thread(){
			@Override
			public void run(){
				while(true){
					if (gameStarted){
						if (isAtEdge(figure)){
							Bomb b = logic.getBomb(EasyAi.this);
							for (Figure f : logic.getFiguresOther(EasyAi.this)){
								if (f != figure
										&& getExplosionArea(b).contains(f.getArrayPosition()))
									logic.layBomb(EasyAi.this);
							}
							handleMovement();
							if (sleepTime>0){
								System.out.println(sleepTime);
								try {
									Thread.sleep(sleepTime);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								sleepTime = 0;
							}
							
						}
						if (movingDirection != NONE){
							logic.move(movingDirection, EasyAi.this);
							
						}
					}
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
//		thread2.start();
		thread.start();
		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Figure getFigure() {
		return figure;
	}

	@Override
	public void gameStarted() {
		gameStarted = true;
		System.out.println("game started");
	}

	@Override
	public void bombLaidDown(Bomb b) {
//		handleMovement();
		
	}

	@Override
	public void blockDestroyed() {
//		handleMovement();
		
	}

	@Override
	public void figureDied(Figure f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hasWon() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hasLost() {
		gameStarted = false;
		
	}

	@Override
	public void bombExploded(Bomb b, List<ArrayPosition> explosionArea) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void explosions(List<Explosion> explosions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerHasLost(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerHasWon(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAi() {
		return true;
	}

	@Override
	public void otherClientLoggedIn(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void otherClientLoggedOut(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chatMessageSent(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void figureMoved(Figure f, int direction) {
		if (f == figure){
//			handleMovement();
		}
		
	}
	
	private void handleMovement(){
		if (logic.getFigures() != null
				&& logic.getFigures().size() > 1){
//			System.out.println(figure.getX() + " x " + figure.getY());
		
//			System.out.println("is at edge at " + figure.getX() + " x " + figure.getY());
//			logic.getFigures().get(0).getArrayPosition()
			List<ArrayPosition> shortestPath = new LinkedList<ArrayPosition>();
			//IN EXLPOSION AREA
			List<ArrayPosition> explosion = getAllExplosionAreas();
			//PATH OUT OF EXPLOSION
			if (explosion.contains(figure.getArrayPosition())){;
				shortestPath = getNextPathOutOfExplosion(figure.getArrayPosition(), explosion, false, EasyAi.this);
//				System.out.println("Path to get out of explosion: " + shortestPath);
			}
			//NO EXPLOSION IN MOVEMENT
			//-> PATH TO NEXT FIGURE
			if (shortestPath.isEmpty()){
				shortestPath = getShortestPathDeijkstra(figure.getArrayPosition(), null, true, EasyAi.this);
//				if (!shortestPath.isEmpty())
//					System.out.println("Path to next figure: " + shortestPath);
			}
			//NO PATH TO NEXT FIGURE FOUND
			//-> PATH TO NEXT BLOCK
			if (shortestPath.isEmpty()){
				shortestPath = getShortestPathToNextBlock(figure.getArrayPosition(), EasyAi.this);
//				System.out.println("Path to next block: " + shortestPath);
			}
			//NEXT BLOCK REACHED
			if (shortestPath.isEmpty()){
				explosion.addAll(getExplosionArea(logic.getBomb(EasyAi.this)));
//				System.out.println("next block reached");
				List<ArrayPosition> shortestPath2 = getNextPathOutOfExplosion(figure.getArrayPosition(), explosion, true, EasyAi.this);
				//IF THERE IS A WAY OUT IF A BOMB IS LAID
				if (!shortestPath2.isEmpty())
					logic.layBomb(EasyAi.this);
			}
//			shortestPath = getShortestPathDeijkstra(figure.getArrayPosition(), logic.getFigures().get(0).getArrayPosition(), 
//				EasyAi.this);
			if (!shortestPath.isEmpty()){
				
				movingDirection = getDirections(figure.getArrayPosition(),
						getNextArrayPositionInPath(figure.getArrayPosition(), shortestPath));
//				System.out.println("direction change "+movingDirection + " " + shortestPath + " at " + figure.getX() + " x " + figure.getY());
//				System.out.println(shortestPath);
			}
			else{
				movingDirection = NONE;
			}
		}
		
		
	}

	@Override
	public void gameEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawOccurred() {
		// TODO Auto-generated method stub
		
	}
	
}
