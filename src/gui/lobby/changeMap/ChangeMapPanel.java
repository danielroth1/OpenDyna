package gui.lobby.changeMap;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import main.GuiControl;
import maps.Map;

public class ChangeMapPanel extends JPanel{
	
	protected ButtonGroup buttons = new ButtonGroup();
	protected List<MapButton> buttonList = new LinkedList<MapButton>();
	private int buttonCount = 0;
	private Box currentBox = null;
	private MapButton saveBtn = null;
	
	public ChangeMapPanel(){
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(GuiControl.STANDARD_COLOR.darker());
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}
	
	/**
	 * reset and remove all buttons from the panel
	 */
	public void resetButtons(){
		removeAll();
		buttonCount = 0;
		buttons = new ButtonGroup();
		buttonList = new LinkedList<MapButton>();
	}
	
	/**
	 * add a new map button to the panel that shows the given map
	 * @param map Map
	 */
	public void addButton(Map map){
		MapButton button = new MapButton(map);
		button.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if (e.getStateChange() == ItemEvent.SELECTED)
					saveBtn = (MapButton)e.getItem();
			}
		});
		buttons.add(button);
		buttonList.add(button);
		addButton(button);
		buttonCount += 1;
	}
	
	/**
	 * adds the button to the panel
	 * @param button MapButton
	 */
	protected void addButton(MapButton button){
		if (buttonCount%4==0){
			currentBox = new Box(BoxLayout.LINE_AXIS);
			add(Box.createRigidArea(new Dimension(0, 5)));
			add(currentBox);
		}
		//rigid area between the buttons on vertical axis
		if (buttonCount%4==1){
			currentBox.add(Box.createRigidArea(new Dimension(5, 0)));
			currentBox.add(button);
			currentBox.add(Box.createRigidArea(new Dimension(5, 0)));
		}
		else if (buttonCount%4==2){
			currentBox.add(button);
			currentBox.add(Box.createRigidArea(new Dimension(5, 0)));
		}
		else
			currentBox.add(button);
	}
	
	public void addAllButtons(List<Map> maps){
		for (Map m : maps){
			if (m != null)
				addButton(m);
		}
	}
	
	public Map getSelectedMap(){
//		MapButton button = (MapButton)buttons.getSelection();
		if (saveBtn != null)
			return saveBtn.getMap();
		else
			return null;
	}
	
}
