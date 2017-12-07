package server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import client.Client;
import client.ClientImpl;

import server.components.Player;

public class Server {
	
	private Logic logic;
	private ServerGui serverGui;
	private HashMap<Client, Player> players = new HashMap<Client, Player>();
	
	public Server() {
		logic = new Logic(this);
		serverGui = new ServerGui(this);
	}
	
	public Logic getGameLogic(){
		return logic;
	}
	
	public void updateGui(){
		serverGui.update();
	}
	
	/**
	 * get a list of player objects that represent the currently logged in clients
	 * @return List<Player> of player objects
	 */
	public List<Player> getPlayers(){
		List<Player> players = new LinkedList<Player>();
		for (Client c : logic.getClients()){
			players.add(new Player(c.getName(), c.isAi()));
		}
		return players;
	}
	
	public void clientLoggedIn(Client c){
		ClientImpl ci = null;
		if (c instanceof ClientImpl){
			ci = (ClientImpl) c;
		}
		
		if (ci == null
				|| !ci.isFirst()){
			Player p = new Player(c.getName(), c.isAi());
			p.setColor(c.getFigure().getColor());
			players.put(c, p);
			for (Client c2 : logic.getClients()){
				c2.otherClientLoggedIn(p);
				}
			System.out.println("Client logged in " + c.getName());
			}
	}
	
	public void clientLoggedOut(Client c){
		Player p = players.get(c);
		players.remove(c);
		for (Client c2 : logic.getClients()){
			c2.otherClientLoggedOut(p);
		}
		
		System.out.println("Client logged out " + c.getName());
	}
	
	public void chatMessageSentInLobby(Client c, String s){
		for (Client c2 : logic.getClients()){
			c2.chatMessageSent(c.getName() + ": " + s);
		}
	}
	
	public void shutDown(){
		logic.shutDown();
		players = new HashMap<Client, Player>();
		
		serverGui.showGui(false);
		serverGui = null;
	}
	
}
