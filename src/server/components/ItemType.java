package server.components;

import maps.Texture;

public enum ItemType {
	INCREASE_SPEED(Texture.BOMB), 
	INCREASE_STRENGTH(Texture.BOMB), 
	INCREASE_BOMBS(Texture.BOMB);
	
	private Texture standardTexture;
	
	
	private ItemType(Texture standardTexture){
		this.standardTexture = standardTexture;
	}


	public Texture getStandardTexture() {
		return standardTexture;
	}
	
	

}
