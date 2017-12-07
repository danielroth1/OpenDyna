package gui.lobby;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.GuiControl;
import maps.Map;

public class MapPanel extends JPanel{
	
	private GuiControl guiControl;
	private MapPanelComponent mapPanelComponent;
	private Map map;
	private JButton changeMapBtn;
	private JButton shuffleBtn;
	
	public MapPanel(GuiControl guiControl) {
		this.guiControl = guiControl;
		initiateButtons();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		
		mapPanelComponent = new MapPanelComponent(250);
		
		Box box = new Box(BoxLayout.LINE_AXIS);
		box.add(changeMapBtn);
		box.add(Box.createRigidArea(new Dimension(5, 0)));
		box.add(shuffleBtn);
		
		add(mapPanelComponent);
		add(Box.createRigidArea(new Dimension(5, 5)));
		add(box);
		
		mapPanelComponent.setAlignmentX(Component.CENTER_ALIGNMENT);
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), "Map"), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}
	
	private void initiateButtons(){
		changeMapBtn = new JButton("Change map");
		shuffleBtn = new JButton("shuffle");
		
		changeMapBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.changeMapBtnPressed();
			}
		});
		shuffleBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.shuffleBtnPressed();
			}
		});
	}
	
	public void update(){
		
	}
	
	public void setMap(Map map){
		this.map = map;
		mapPanelComponent.setMap(map);
	}
	
	public Map getMap(){
		return map;
	}
	
	
}
