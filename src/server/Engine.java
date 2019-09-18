package server;

import server.components.Figure;

public class Engine {
	
	public int speed = 4;
	
	private Logic logic;
	
	public Engine(Logic logic) {
		this.logic = logic;
	}
	
	public void move(Figure f, int direction){
		int speed = f.getSpeed();
		if (direction == Logic.UP){
			f.setY(f.getY()-speed);
		}
		if (direction == Logic.DOWN){
			f.setY(f.getY()+speed);
		}
		if (direction == Logic.LEFT){
			f.setX(f.getX()-speed);
		}
		if (direction == Logic.RIGHT){
			f.setX(f.getX()+speed);
		}
	}
	
}
