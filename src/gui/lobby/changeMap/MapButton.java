package gui.lobby.changeMap;

import gui.lobby.MapPanelComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

import main.GuiControl;
import maps.Map;

public class MapButton extends JToggleButton{
	
	private JLabel nameLabel;
	private MapPanelComponent mapPanelComponent;
	private Map map;
	
	public MapButton(Map map){
		this.map = map;
		setToolTipText(map.getName());
		nameLabel = new JLabel(map.getName());
		nameLabel.setFont(getFont().deriveFont(Font.BOLD, (float) 14));
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setMaximumSize(new Dimension(118, 28));
//		nameLabel.setMinimumSize(new Dimension(118, 0));
		mapPanelComponent = new MapPanelComponent(118);
		mapPanelComponent.setMap(map);
		nameLabel.setAlignmentX(LEFT_ALIGNMENT);
		mapPanelComponent.setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		setBackground(GuiControl.STANDARD_COLOR);
		
		add(nameLabel);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(mapPanelComponent);
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}
