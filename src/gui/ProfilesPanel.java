package gui;

import gui.profiles.Hotkeys;
import gui.profiles.HotkeysPanel;
import gui.profiles.Profile;
import gui.profiles.ProfilePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.GameControl;
import main.GuiControl;

public class ProfilesPanel extends JPanel{
	
	private GuiControl guiControl;
	private GameControl gameControl;
	private HashMap<Profile, ProfilePanel> profiles = new HashMap<Profile, ProfilePanel>();
	private JButton applyBtn;
	private JButton backBtn;
	private JComboBox<Profile> profilesBox;
	private JLabel profileLabel = new JLabel("Profiles: ");
	private HotkeysPanel hotkeysPanel;
	
	public ProfilesPanel(GuiControl guiControl, GameControl gameControl) {
		this.guiControl = guiControl;
		this.gameControl = gameControl;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(GuiControl.STANDARD_COLOR);
		
		hotkeysPanel = new HotkeysPanel(this);
		applyBtn = new JButton("apply");
		backBtn = new JButton("back");
		profilesBox = new JComboBox<Profile>();
		profilesBox.setMaximumSize(new Dimension(200, 30));
		
		
		initiateProfiles();
		updateProfilesBox();
		
		profilesBox.setEditable(true);
		profilesBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				@SuppressWarnings("unchecked")
				JComboBox<Profile> cb = (JComboBox<Profile>)e.getSource();
				Profile p;
				String s;
				if (cb.getSelectedItem() instanceof Profile){
					p = (Profile) cb.getSelectedItem();
					hotkeysPanel.update(p);
				}
				else if (cb.getSelectedItem() instanceof String){
					s = (String) cb.getSelectedItem();
					ProfilesPanel.this.gameControl.profileNameChanged(s);
					System.out.println(s);
				}
				ProfilesPanel.this.gameControl.profileChanged();
			}
		});
		backBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ProfilesPanel.this.guiControl.backToMenue();
			}
		});
		applyBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ProfilesPanel.this.gameControl.applyProfiles();
			}
		});
		
		Box box = new Box(BoxLayout.LINE_AXIS);
		box.add(profileLabel);
		box.add(Box.createRigidArea(new Dimension(5, 0)));
		box.add(profilesBox);
		add(box);
		add(Box.createRigidArea(new Dimension(0, 10)));
		
//		Box boxName = new Box(BoxLayout.LINE_AXIS);
//		nameField.setMaximumSize(new Dimension(200, 30));
//		boxName.add(nameLabe);
//		boxName.add(Box.createRigidArea(new Dimension(5, 0)));
//		boxName.add(nameField);
//		add(boxName);
//		add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(hotkeysPanel);
		add(Box.createRigidArea(new Dimension(0, 10)));	
		Box buttonsBox = new Box(BoxLayout.LINE_AXIS);
		buttonsBox.add(backBtn);
		buttonsBox.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonsBox.add(applyBtn);
		add(buttonsBox);
		
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setSize(GuiControl.MENU_SIZE);
		setVisible(false);
	}
	
	private void initiateProfiles(){
//		Profile player1 = new Profile("Player1", Color.BLUE, "w", "s", "a", "d", "f");
//		Profile player2 = new Profile("Player2", Color.RED, "UP", "DOWN", "LEFT", "RIGHT", "NUMPAD0");
//		Profile player3 = new Profile("Player3", Color.YELLOW);
//		Profile player4 = new Profile("Player4", Color.GREEN);
//		
//		profiles.put(player1, new ProfilePanel(player1));
//		profiles.put(player2, new ProfilePanel(player2));
//		profiles.put(player3, new ProfilePanel(player3));
//		profiles.put(player4, new ProfilePanel(player4));
	}
	
	public void addProfile(String name, Color color){
		Profile profile = new Profile(name, color);
		profiles.put(profile, new ProfilePanel(profile));
	}
	
	public void addProfile(Profile p){
		profiles.put(p, new ProfilePanel(p));
		updateProfilesBox();
	}
	
	/**
	 * can be called to get profiles in order to change them
	 * how the user wants it
	 * @return Set<Profile> all profiles
	 */
	public Set<Profile> getProfiles(){
		return profiles.keySet();
	}
	
	@SuppressWarnings("unchecked")
	public List<Profile> getProfilesList(){
		List<Profile> list = new LinkedList<Profile>();
		for (Profile p : profiles.keySet()){
			list.add(p);
		}
		Collections.sort(list);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public void updateProfilesBox(){
		profilesBox.removeAllItems();
		List<Profile> list = new LinkedList<Profile>();
		for (Profile p : profiles.keySet()){
			if (!p.isKi())
				list.add(p);
		}
		Collections.sort(list);
		for (Profile p : list){
			profilesBox.addItem(p);
		}
	}
	
	public void updateProfilePanels(){
		for (ProfilePanel p : profiles.values())
			p.update();
		
	}
	
	public void updateKeysToSelectedProfile(){
		hotkeysPanel.getCurrentProfile().setHotkeys(hotkeysPanel.getFormatedHotkeys());
	}
	
	public void update(){
		updateProfilesBox();
		updateProfilePanels();
		revalidate();
		repaint();
	}

	@Override
	public void setVisible(boolean aFlag) {
		if (profilesBox.getItemCount() != 0)
			profilesBox.setSelectedIndex(0);
		super.setVisible(aFlag);
	}
	
	/**
	 * is called when a player object is changes in the hotkey panel.
	 */
	public void playerChanged(){
		hotkeysPanel.update(hotkeysPanel.getCurrentProfile());
	}
	
	public Profile getSelectedProfile(){
		if (profilesBox.getSelectedItem() instanceof Profile)
			return (Profile)profilesBox.getSelectedItem();
		else
			return hotkeysPanel.getCurrentProfile();
	}
	
	public Hotkeys getFormatedHotkeys(){
		return hotkeysPanel.getFormatedHotkeys();
	}
	
}
