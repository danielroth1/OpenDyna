package server.components;

import maps.Texture;
import server.Square;

public class Block extends Square{
	
	public static Texture STANDARD_TEXTURE = Texture.STONE;
	public static Texture STANDARD_TEXTURE_SOLID = Texture.SOLID_STONE;
	private Item item;
	private boolean destructable;

	/**
	 * a simple block
	 */
	public Block(){
		this(0, 0, null, true);
	}
	
	public Block(boolean destructable){
		this(0, 0, destructable);
	}
	
	public Block(int x, int y, boolean destructable){
		this(x, y, null, destructable);
	}
	
	public Block(int x, int y, Item item, boolean destructable) {
		super(x, y, STANDARD_TEXTURE);
		if (!destructable)
			setTexture(STANDARD_TEXTURE_SOLID);
		this.destructable = destructable;
		this.item = item;
	}
	
	public Block(ArrayPosition a, boolean destructable){
		super(a, STANDARD_TEXTURE);
		if (!destructable)
			setTexture(STANDARD_TEXTURE_SOLID);
		this.destructable = destructable;
	}
	
	public Block(int x, int y, Item item, boolean destructable, Texture texture){
		this(x, y, item, destructable);
		setTexture(texture);
	}
	
	public Block(ArrayPosition a, boolean destructable, Texture texture){
		this(a, destructable);
		setTexture(texture);
	}
	
	public boolean hasItem(){
		return item != null;
	}
	
	public Item getItem(){
		return item;
	}
	
	public void setItem(Item item){
		this.item = item;
	}
	
	public boolean isDestructable() {
		return destructable;
	}

	public void setDestructable(boolean destructable) {
		this.destructable = destructable;
	}
	
	
}
