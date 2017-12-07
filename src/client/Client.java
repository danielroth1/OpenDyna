package client;

import java.util.List;

import server.components.ArrayPosition;
import server.components.Bomb;
import server.components.Explosion;
import server.components.Figure;
import server.components.Player;

public interface Client {
	
	public String getName();
	
	public Figure getFigure();
	
	public void gameStarted();
	
	/**
	 * is called when a bomb was laid down
	 */
	public void bombLaidDown(Bomb b);
	
	/**
	 * is called when a bomb just exploded
	 * @param b exploded Bomb
	 */
	public void bombExploded(Bomb b, List<ArrayPosition> explosionArea);
	
	/**
	 * is called when there are currently explosions on the playingField
	 * @param explosions current explosions
	 */
	public void explosions(List<Explosion> explosions);
	
	/**
	 * is called when a block got destroyed
	 */
	public void blockDestroyed();
	
	/**
	 * is called when a figureDied
	 */
	public void figureDied(Figure f);
	
	/**
	 * is called when the game is won
	 */
	public void hasWon();
	
	/**
	 * is called when the game is lost
	 */
	public void hasLost();
	
	/**
	 * is called when a player has lost the game
	 * @param name of the player
	 */
	public void playerHasLost(String name);
	
	/**
	 * is called when a player has won the game
	 * @param name of the player
	 */
	public void playerHasWon(String name);
	
	/**
	 * is called when a draw occurred
	 */
	public void drawOccurred();
	
	/**
	 * true if client is an ai
	 * @return boolean if ai
	 */
	public boolean isAi();
	
	/**
	 * is called when an other client has logged in to the server
	 */
	public void otherClientLoggedIn(Player p);
	
	/**
	 * is called when an other client has logged out of the server.
	 * @param p
	 */
	public void otherClientLoggedOut(Player p);
	
	/**
	 * is called when a chat message is sent
	 * @param s chat message
	 */
	public void chatMessageSent(String s);
	
	/**
	 * is called when the current map has changed
	 */
	public void mapChanged();
	
	/**
	 * is called when a figure moved
	 * @param f Figure
	 * @param direction int -> Logic.UP, DOWN, LEFT, RIGHT
	 */
	public void figureMoved(Figure f, int direction);
	
	/**
	 * is called when the game has end
	 */
	public void gameEnd();
}
