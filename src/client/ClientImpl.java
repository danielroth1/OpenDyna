package client;

import gui.TerrainControl;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;

import javax.swing.KeyStroke;

import main.GameControl;
import main.GuiControl;

import server.Logic;
import server.Server;
import server.components.ArrayPosition;
import server.components.Bomb;
import server.components.Explosion;
import server.components.Figure;
import server.components.Player;

public class ClientImpl implements Client{
	
	public static int UP = 0;
	public static int DOWN = 1;
	public static int LEFT = 2;
	public static int RIGHT = 3;
	
	private int up;
	private int down;
	private int left;
	private int right;
	private int bombBtn;
	private String name;
	private Figure figure;
	private HashSet<Integer> pressedKeys = new HashSet<Integer>();
	private Server server;
	private Logic logic;
	private GuiControl guiControl;
	private GameControl gameControl;
	private TerrainControl terrainControl;
	private boolean first;
	private boolean running = true;
	
	private Thread thread;
	
	public ClientImpl(String name, Server server, GuiControl guiControl, GameControl gameControl, boolean first){
		this(name, new Figure(name, Color.BLUE, 2, 0, 0), server, guiControl, gameControl, first);
	}
	
	public ClientImpl(String name, Figure figure, Server server, GuiControl guiControl, GameControl gameControl, boolean first) {
		this.name = name;
		this.figure = figure;
		this.server = server;
		this.gameControl = gameControl;
		this.guiControl = guiControl;
		this.first = first;
		this.logic = server.getGameLogic();
		this.terrainControl = guiControl.getTerrainControl();
		
		thread = new Thread(){
			public void run(){
				while(true){
					if (!running)
						return;
					if (pressedKeys.contains(up)){
						logic.move(Logic.UP, ClientImpl.this);
					}
					if (pressedKeys.contains(down)){
						logic.move(Logic.DOWN, ClientImpl.this);
					}
					if (pressedKeys.contains(left)){
						logic.move(Logic.LEFT, ClientImpl.this);
					}
					if (pressedKeys.contains(right)){
						logic.move(Logic.RIGHT, ClientImpl.this);
					}
					if (pressedKeys.contains(bombBtn)){
						logic.layBomb(ClientImpl.this);
					}
					updateGui();
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		};
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
	
	public HashSet<Integer> getPressedKeys(){
		return pressedKeys;
	}
	
	public void setKeyBindings(String up, String down, String left, String right, String bombBtn){
		guiControl.getGamePanel().getInputMap().put(KeyStroke.getKeyStroke(up), "up" + name);
		guiControl.getGamePanel().getInputMap().put(KeyStroke.getKeyStroke(down), "down" + name);
		guiControl.getGamePanel().getInputMap().put(KeyStroke.getKeyStroke(left), "left" + name);
		guiControl.getGamePanel().getInputMap().put(KeyStroke.getKeyStroke(right), "right" + name);
		guiControl.getGamePanel().getInputMap().put(KeyStroke.getKeyStroke(bombBtn), "bomb" + name);
		this.up = KeyStroke.getKeyStroke(up).getKeyCode();
		this.down = KeyStroke.getKeyStroke(down).getKeyCode();
		this.left = KeyStroke.getKeyStroke(left).getKeyCode();
		this.right = KeyStroke.getKeyStroke(right).getKeyCode();
		this.bombBtn = KeyStroke.getKeyStroke(bombBtn).getKeyCode();
	}
	
	public void updateGui(){
		if (first){
			guiControl.getGamePanel().setMovedWorld(logic.getPlayingField().getWorld());
			guiControl.update();
		}
	}

	@Override
	public void gameStarted() {
		if (first){
			guiControl.update();
		}
		
	}

	@Override
	public void bombLaidDown(Bomb b) {
		if (first){
//			guiControl.getGamePanel().addToWorld(b);
			guiControl.update();
		}
	}
	
	@Override
	public void bombExploded(Bomb b, List<ArrayPosition> explosionArea) {
		if (first){
//			guiControl.getGamePanel().removeFromWorld(b);
			terrainControl.bombExploded(b, explosionArea);
			guiControl.update();
		}
	}
	
	@Override
	public void explosions(List<Explosion> explosions) {
		if (first){
			terrainControl.explosions(explosions);
			guiControl.getGamePanel().setExplosions(explosions);
		}
		
	}

	@Override
	public void blockDestroyed() {
		if (first){
			guiControl.update();
		}
		
	}

	@Override
	public void figureDied(Figure f) {
		if (first){
			terrainControl.figureDied(f);
			guiControl.getGamePanel().removeFigure(f);
			guiControl.update();
//			System.out.println("Figure died");
		}
		
	}

	@Override
	public void hasWon() {
		if (first){
			guiControl.setWinLoosePanel(true, false, name);
			guiControl.showWinLosePanel();
		}
		
	}

	@Override
	public void hasLost() {
//		if (first){
//			guiControl.setWinLoosePanel(false);
//			guiControl.showWinLosePanel();
//		}
		
	}

	@Override
	public void playerHasLost(String name) {
		System.out.println("player " + name + " has lost!");
		
	}

	@Override
	public void playerHasWon(String name) {
		guiControl.setWinLoosePanel(true, false, name);
		System.out.println("player " + name + " has won!");
		
	}

	@Override
	public boolean isAi() {
		return false;
	}

	@Override
	public void otherClientLoggedIn(Player p) {
		if (first){
			if (!p.isFirst()){
				if (!guiControl.getLobbyPanel().getPlayerPane().addPlayer(p))
					System.out.println("no more space left in lobby");
				guiControl.getLobbyPanel().getPlayerPane().revalidateSlots();
				guiControl.getLobbyPanel().getPlayerPane().update();
			}
			
		}
	}

	@Override
	public void otherClientLoggedOut(Player p) {
		if (first){
			if (!guiControl.getLobbyPanel().getPlayerPane().removePlayer(p))
				System.out.println("player could not be removed from lobby");
			guiControl.getLobbyPanel().getPlayerPane().update();
			guiControl.getLobbyPanel().getPlayerPane().revalidateSlots();
			
		}
	}

	@Override
	public void chatMessageSent(String s) {
		if (first){
			guiControl.writeInChat(s);
		}
		
	}

	@Override
	public void mapChanged() {
//		terrainControl.setTerrain(logic.getMap().getPlayingField().getTerrain());
		guiControl.mapChanged(logic.getMap());
	}
	
	public void figureMoved(Figure f, int direction){
		
	}
	
	public boolean isFirst(){
		return first;
	}
	
	public void forceSurrender(){
		logic.surrender(this);
	}

	@Override
	public void gameEnd() {
		if (first){
			
			guiControl.showWinLosePanel();
			System.out.println("game end");
		}
		
	}
	
	public void shutDown(){
		running = false;
	}

	@Override
	public void drawOccurred() {
		if (first){
			guiControl.setWinLoosePanel(true, true, name);
		}
	}
	
	
}
