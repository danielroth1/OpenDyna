package server.components;

import server.Square;

/**
 * item that improves abilities of a figure
 * @author Daniel Roth
 */
public class Item extends Square{
	
	private ItemType itemType;
	
	public Item(ItemType itemType) {
		super(new ArrayPosition(0, 0), itemType.getStandardTexture());
		this.itemType = itemType;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		setTexture(itemType.getStandardTexture());
		this.itemType = itemType;
	}

	@Override
	public String toString() {
		return itemType.toString();
	}
	
	
	
	
	
}
