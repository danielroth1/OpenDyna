package gui.levelEditor.toolsPanel;

import java.awt.Color;
import java.awt.Image;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import maps.Texture;

public class Terrain implements Serializable{
	
	private boolean burned;
	private TerrainType terrainType;
	private Texture texture;
	private boolean bloody = false;
	
	public Terrain(Texture texture, TerrainType terrainType, boolean burned){
		this.texture = texture;
		this.terrainType = terrainType;
		this.burned = burned;
		
	}
	
	public Terrain(TerrainType terrainType, boolean burned){
		texture = terrainType.getStandardTexture();
		this.terrainType = terrainType;
		this.burned = burned;
	}
	
	public Terrain(TerrainType terrainType){
		texture = terrainType.getStandardTexture();
		this.terrainType = terrainType;
		burned = false;
	}
	
//	public Terrain(String name, Color backgroundColor, Image texture) {
//		this.name = name;
//		this.backgroundColor = backgroundColor;
//		this.texture = texture;
//	}

	public boolean isBurned() {
		return burned;
	}

	public void setBurned(boolean burned) {
		this.burned = burned;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public TerrainType getTerrainType() {
		return terrainType;
	}

	public void setTerrainType(TerrainType terrainType) {
		this.terrainType = terrainType;
	}

	public boolean isBloody() {
		return bloody;
	}

	public void setBloody(boolean bloody) {
		this.bloody = bloody;
	}
	
	
}
