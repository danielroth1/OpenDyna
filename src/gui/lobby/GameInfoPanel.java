package gui.lobby;

import gui.profiles.Profile;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.GameControl;
import main.GuiControl;

public class GameInfoPanel extends JPanel{
	
	private GuiControl guiControl;
	private GameControl gameControl;
	private InfoPanel infoPanel;
	private JComboBox<Profile> profiles = new JComboBox<Profile>();
	private JComboBox<Profile> profilesAI = new JComboBox<Profile>();
	
	private JButton startBtn;
	private JButton addClientBtn;
	private JButton addClientAIBtn;
	private JButton removeClientBtn;
	
	public GameInfoPanel(GuiControl guiControl, GameControl gameControl) {
		this.guiControl = guiControl;
		this.gameControl = gameControl;
		infoPanel = new InfoPanel();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(infoPanel);
		
		Box addBox = new Box(BoxLayout.LINE_AXIS);
		addBox.setAlignmentX(LEFT_ALIGNMENT);
		profiles.setMaximumSize(new Dimension(80, 30));
		addBox.add(addClientBtn);
		addBox.add(Box.createRigidArea(new Dimension(10, 0)));
		addBox.add(profiles);
		
		Box addAIBox = new Box(BoxLayout.LINE_AXIS);
		addAIBox.setAlignmentX(LEFT_ALIGNMENT);
		profilesAI.setMaximumSize(new Dimension(80, 30));
		addAIBox.add(addClientAIBtn);
		addAIBox.add(Box.createRigidArea(new Dimension(10, 0)));
		addAIBox.add(profilesAI);
		
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(addBox);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(addAIBox);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(removeClientBtn);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(startBtn);
		
		
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), 
						"Game Info"), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
	}
	
	public void update(){
		profiles.removeAllItems();
		for (Profile p : gameControl.getProfilesNotIntUse(false)){
			profiles.addItem(p);
		}
		
		profilesAI.removeAllItems();
		for (Profile p : guiControl.getAIProfiles()){
			profilesAI.addItem(p);
		}
		
	}
	
	private void initiateButtons(){
		startBtn = new JButton("Start Game");
		addClientBtn = new JButton("Add Player");
		addClientAIBtn = new JButton("Add AI");
		removeClientBtn = new JButton("Remove Player");
		
		startBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.startBtnPressed();
			}
		});
		
		addClientBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Profile p = (Profile)profiles.getSelectedItem();
				if (p != null)
					guiControl.addClient(p);
				else
					guiControl.addClient();
			}
		});
		
		addClientAIBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Profile p = (Profile)profilesAI.getSelectedItem();
				if (p != null)
					guiControl.addClient(p);
				else
					guiControl.addClient();
			}
		});
		
		removeClientBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.removeClient();
			}
		});
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (infoPanel != null)
			infoPanel.setBackground(bg);
	}



	private class InfoPanel extends JPanel{
		private JLabel randomLabel = new JLabel("Random map:");
		private JLabel sizeLabel = new JLabel("Size: ");
		private JLabel densityLabel = new JLabel("Density: ");
		private JTextField sizeY = new JTextField(1);
		private JTextField sizeX = new JTextField(1);
		private JTextField densityTextField =  new JTextField(1);
		private JLabel densityLabelInfo = new JLabel("(0 - 100)");
		private JButton randomMapBtn = new JButton("Set Random Map");
		
		
		public InfoPanel(){
			BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
			setLayout(layout);
			initiateButtons();
			
			add(randomLabel);
			add(Box.createRigidArea(new Dimension(0, 5)));
			
			sizeX.setMaximumSize(new Dimension(80, 30));
			sizeY.setMaximumSize(new Dimension(80, 30));
			Box sizeBox = new Box(BoxLayout.LINE_AXIS);
			sizeBox.setAlignmentX(LEFT_ALIGNMENT);
			sizeBox.add(sizeLabel);
			sizeBox.add(sizeX);
			sizeBox.add((new JLabel(" X ")));
			sizeBox.add(sizeY);
			add(sizeBox);
			
			densityTextField.setMaximumSize(new Dimension(80, 30));
			Box densityBox = new Box(BoxLayout.LINE_AXIS);
			densityBox.setAlignmentX(LEFT_ALIGNMENT);
			densityBox.add(densityLabel);
			densityBox.add(densityTextField);
			densityBox.add(densityLabelInfo);
			add(Box.createRigidArea(new Dimension(0, 5)));
			add(densityBox);
			add(Box.createRigidArea(new Dimension(0, 5)));
			add(randomMapBtn);
			sizeX.setText("13");
			sizeY.setText("13");
			densityTextField.setText("100");
			
			
			randomLabel.setAlignmentX(LEFT_ALIGNMENT);
			randomMapBtn.setAlignmentX(LEFT_ALIGNMENT);
//			setMaximumSize(new Dimension(100, 400));
			
			randomMapBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int x = Integer.decode(sizeX.getText());
					int y = Integer.decode(sizeY.getText());
					int density = Integer.decode(densityTextField.getText());
					if (x < 0
							|| x > 100
							|| y < 0
							|| y > 100)
						System.out.println("wrong field size");
					else if (density < 0
							|| density > 100){
						System.out.println("illegal density value");
					}
					else{
						gameControl.randomMapChosen(y, x, density);
					}
				}
			});
			
		}

//		@Override
//		public Component add(Component comp) {
//			super.add(Box.createRigidArea(new Dimension(0, 10)));
//			return super.add(comp);
//		}
		
		
		
		
		
	}
	
}
