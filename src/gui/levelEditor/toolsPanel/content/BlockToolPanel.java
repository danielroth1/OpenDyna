package gui.levelEditor.toolsPanel.content;

import java.awt.Dimension;
import java.awt.ItemSelectable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import server.components.Block;
import server.components.Item;
import server.components.ItemType;

public class BlockToolPanel extends JPanel{
	
	private JCheckBox destructableBox;
	private JLabel itemLabel;
	private JComboBox<Item> itemComboBox;
	
	public BlockToolPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		destructableBox = new JCheckBox("destructable");
		add(destructableBox);
		
		Box itemBox = new Box(BoxLayout.LINE_AXIS);
		itemLabel = new JLabel("Item: ");
		itemComboBox = new JComboBox<Item>();
		itemComboBox.setMaximumSize(new Dimension(1000, 25));
		for (ItemType i : ItemType.values())
			itemComboBox.addItem(new Item(i));
		itemBox.add(itemLabel);
		itemBox.add(itemComboBox);
		itemBox.setAlignmentX(LEFT_ALIGNMENT);
		add(itemBox);
	}
	
	public boolean getDestructable(){
		return destructableBox.isSelected();
	}
	
	public Item getItem(){
		return (Item)itemComboBox.getSelectedItem();
	}

}
