package gui.profiles;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gui.ProfilesPanel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import main.GuiControl;

public class HotkeysPanel extends JPanel{
	
	private Profile currentProfile;
	private ProfilesPanel profilesPanel;
	private HashMap<Integer, JTextField> textFields = new HashMap<Integer, JTextField>();
	
	public HotkeysPanel(ProfilesPanel profilesPanel) {
		this.profilesPanel = profilesPanel;
		
		setBackground(GuiControl.STANDARD_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		initiateTextFields();
		setBorder(BorderFactory.createTitledBorder("Hotkeys"));
		setMaximumSize(new Dimension(400, 450));
		setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public void update(Profile p){
		currentProfile = p;
		setHotkeys(p.getHotkeys());
	}
	
	public Profile getCurrentProfile(){
		return currentProfile;
	}
	
	/**
	 * sets to the textFields the given hotkeys
	 * @param hotkeys
	 */
	public void setHotkeys(Hotkeys hotkeys){
		for (Map.Entry<Integer, JTextField> e : textFields.entrySet()){
			int h = e.getKey();
			e.getValue().setText(hotkeys.getHotkey(h));
		}
	}
	
	private void initiateTextFields(){
		textFields.put(Hotkeys.UP, addHotkey("Up"));
		textFields.put(Hotkeys.DOWN, addHotkey("Down"));
		textFields.put(Hotkeys.LEFT, addHotkey("Left"));
		textFields.put(Hotkeys.RIGHT, addHotkey("Right"));
		textFields.put(Hotkeys.BOMB, addHotkey("Bomb"));
	}
	
	private JTextField addHotkey(String name){
		Box box = new Box(BoxLayout.LINE_AXIS);
		JLabel label = new JLabel(name + ": ");
		label.setMaximumSize(new Dimension(100, 30));
		JTextField textField = new JTextField();
		textField.setColumns(1);
		textField.setMaximumSize(new Dimension(200, 30));
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		box.add(label);
		box.add(textField);
		box.setAlignmentX(LEFT_ALIGNMENT);
		add(box);
		
		return textField;
	}
	
	public Hotkeys getFormatedHotkeys(){
		Hotkeys h = new Hotkeys(textFields.get(Hotkeys.UP).getText(), 
				textFields.get(Hotkeys.DOWN).getText(), 
				textFields.get(Hotkeys.LEFT).getText(), 
				textFields.get(Hotkeys.RIGHT).getText(), 
				textFields.get(Hotkeys.BOMB).getText());
		return h;
	}
	
}
