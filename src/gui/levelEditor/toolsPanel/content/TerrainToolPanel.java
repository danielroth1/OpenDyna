package gui.levelEditor.toolsPanel.content;

import java.awt.Dimension;

import gui.levelEditor.toolsPanel.TerrainType;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TerrainToolPanel extends JPanel{
	
	private JLabel typeLabel;
	private JComboBox<TerrainType> typeComboBox;
	
	public TerrainToolPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		Box typeBox = new Box(BoxLayout.LINE_AXIS);
		typeBox.setMaximumSize(new Dimension(1000, 25));
		typeLabel = new JLabel("Type: ");
		typeComboBox = new JComboBox<TerrainType>();
		for (TerrainType t : TerrainType.values()){
			typeComboBox.addItem(t);
		}
		typeBox.add(typeLabel);
		typeBox.add(typeComboBox);
		add(typeBox);
		
	}
	
	public TerrainType getTerraintType(){
		return (TerrainType)typeComboBox.getSelectedItem();
	}
	
}
