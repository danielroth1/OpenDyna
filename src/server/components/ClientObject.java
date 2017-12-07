package server.components;

import client.Client;

public class ClientObject {
	
	private Client client;
	private boolean isAlive;
	private int bombs;
	private int bombsLeft;
	
	public ClientObject(Client client, boolean isAlive, int bombs) {
		this.client = client;
		this.isAlive = isAlive;
		this.bombs = bombs;
		bombsLeft = bombs;
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

	public int getBombsLeft() {
		return bombsLeft;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void setBombs(int bombs) {
		this.bombs = bombs;
	}

	public void setBombsLeft(int bombsLeft) {
		this.bombsLeft = bombsLeft;
	}
	
	
	
}
