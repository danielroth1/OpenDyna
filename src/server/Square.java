package server;

import java.awt.Polygon;

import maps.Texture;

import server.components.ArrayPosition;
import server.components.PlayingField;

public class Square extends Polygon{
	
	private int x;
	private int y;
	private int width;
	private int height;
	private ArrayPosition arrayPosition;
	private Texture texture;

	public Square(int x, int y, int width, int height, Texture texture) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.arrayPosition = PlayingField.getArrayPosition(x, y);
		this.texture = texture;
		
		xpoints[0] = x;
		ypoints[0] = y;
		
		xpoints[1] = x + width;
		ypoints[1] = y;
		
		xpoints[2] = x + width;
		ypoints[2] = y + height;
		
		xpoints[3] = x;
		ypoints[3] = y + height;
		
		npoints = 4;
		
	}
	
	public Square(int x, int y, Texture texture){
		this(x, y, 50, 50, texture);
	}
	
	public Square(ArrayPosition a, Texture texture){
		this(a.getColumn()*50, a.getRow()*50, texture);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setX(int x) {
		setPosition(x, y);
	}

	public void setY(int y) {
		setPosition(x, y);
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
		updatePosition();
	}
	
	public ArrayPosition getArrayPosition() {
		ArrayPosition a = PlayingField.getArrayPosition(x, y);
		arrayPosition = a;
		return a;
	}

	public void setArrayPosition(ArrayPosition a) {
		this.arrayPosition = a;
		setPosition(a.getColumn() * 50, a.getRow() * 50);
	}
	
	private void updatePosition(){
		xpoints[0] = x;
		ypoints[0] = y;
		
		xpoints[1] = x + width;
		ypoints[1] = y;
		
		xpoints[2] = x + width;
		ypoints[2] = y + height;
		
		xpoints[3] = x;
		ypoints[3] = y + height;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	
}
