package gui.levelEditor.toolsPanel;

import maps.Texture;

public enum TerrainType {
	EARTH, WATER, GRAS, BRIDGE, STONE;
	
	private String name;
	private Texture standardTexture;
	
	static{
		EARTH.setComponents("Earth", Texture.EARTH);
		WATER.setComponents("Water", Texture.WATER);
		GRAS.setComponents("Gras", Texture.GRAS);
		BRIDGE.setComponents("Bridge", Texture.BRIDGE);
		STONE.setComponents("Stone", Texture.STONE);

	}
	
	private void setComponents(String name, Texture standardTexture){
		this.name = name;
		this.standardTexture = standardTexture;
	}

	public String getName() {
		return name;
	}

	public Texture getStandardTexture() {
		return standardTexture;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStandardTexture(Texture standardTexture) {
		this.standardTexture = standardTexture;
	}
	
	
}
