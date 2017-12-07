package maps;

import java.io.Serializable;
import java.util.List;

import server.components.ArrayPosition;
import server.components.PlayingField;

public class Map implements Serializable{
	
	private PlayingField playingField;
	private String name;
	private ArrayPosition size;
	private List<ArrayPosition> startingPositions;
	private int playerCount;
	
	public Map(PlayingField playingField, List<ArrayPosition> startingPositions, String name) {
		this.playingField = playingField;
		this.name = name;
		size = new ArrayPosition(playingField.getRow(), playingField.getColumn());
		this.startingPositions = startingPositions;
		playerCount = startingPositions.size();
		
	}
	
	public void shuffle(){
		
	}

	public PlayingField getPlayingField() {
		return playingField;
	}

	public String getName() {
		return name;
	}

	public ArrayPosition getSize() {
		return size;
	}

	public List<ArrayPosition> getStartingPositions() {
		return startingPositions;
	}

	public int getPlayerCount() {
		return playerCount;
	}
	
	public Map getCopy(){
		return new Map(playingField.copy(), startingPositions, name);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
}
