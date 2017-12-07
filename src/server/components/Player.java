package server.components;

import java.awt.Color;

import client.Client;
import client.ai.EasyAi;

public class Player {
	
	private String name;
	private Color color;
	private boolean isAi;
	private boolean isFirst = false;
	
	public Player(Client c){
		name = c.getName();
		if (c instanceof EasyAi)
			isAi = true;
	}
	
	public Player(String name, boolean ai) {
		this.name = name;
		this.isAi = ai;
	}
	
	public Player(boolean isFirst){
		this.isFirst = isFirst;
	}
	
	public Player copy(){
		return new Player(name, isAi);
	}

	public boolean isAi() {
		return isAi;
	}

	public void setAi(boolean isAi) {
		this.isAi = isAi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	
	
	
}
