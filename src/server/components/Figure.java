package server.components;

import java.awt.Color;

import server.Logic;
import server.Square;

public class Figure extends Square{
	
	
	private String name;
	private Color color;
	private int strength;
	private int direction;
	
	public Figure(String name, Color color, int strength, int x, int y) {
		super(x, y, 50, 50, null);
		this.name = name;
		this.color = color;
		this.strength = strength;
		this.direction = Logic.NONE;
	}
	
	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public String getName(){
		return name;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColor(Color color){
		this.color = color;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
//	@Override
//	public boolean equals(Object o){
//		Figure f = (Figure) o;
//		return f.getName().equals(name)
//				&& f.getColor().equals(color);
//	}
	
	
}
