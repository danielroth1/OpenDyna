package server.components;

import maps.Texture;
import client.Client;
import server.Square;

public class Bomb extends Square{
	
	public static Texture STANDARD_TEXTURE = Texture.BOMB;
	private int strength;
	private int timeLeft;
	private Client owner;

	public Bomb(int x, int y, int strength, int timeLeft, Client owner) {
		super(x, y, STANDARD_TEXTURE);
		this.strength = strength;
		this.timeLeft = timeLeft;
		this.owner = owner;
	}
	
	public Bomb(ArrayPosition a, int strength, int timeLeft, Client owner){
		super(a, STANDARD_TEXTURE);
		this.strength = strength;
		this.timeLeft = timeLeft;
		this.owner = owner;
	}
	
	
	public int getTimeLeft(){
		return timeLeft;
	}
	
	public void setTimeLeft(int timeLeft){
		this.timeLeft = timeLeft;
	}
	
	public int getStrength(){
		return strength;
	}
	
	public void setStrength(int strength){
		this.strength = strength;
	}
	
	public Client getOwner() {
		return owner;
	}

	public void setOwner(Client owner) {
		this.owner = owner;
	}
	
	@Override
	public String toString(){
		return "[Bomb t=" + timeLeft + "ms] by " + owner;
	}

}
