package server.components;

import client.Client;

public class ClientObject {
	
	private Client client;
	private boolean isAlive;
	private int bombs;
	private int placedBombs;
	
	public ClientObject(Client client, boolean isAlive, int bombs) {
		this.client = client;
		this.isAlive = isAlive;
		this.bombs = bombs;
		placedBombs = 0;
	}

	public Client getClient() {
		return client;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public int getBombs() {
		return bombs;
	}

	public int getPlacedBombs() {
		return placedBombs;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void setBombs(int bombs) {
		this.bombs = bombs;
	}
	
	public int getBombsLeft() {
		return Math.max(0, bombs - placedBombs);
	}

	public void setPlacedBombs(int placedBombs) {
		this.placedBombs = placedBombs;
	}
	
	
	
}
