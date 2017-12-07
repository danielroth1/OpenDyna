package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import main.GuiControl;

public class StartPanel extends JPanel{
	
	private GuiControl guiControl;
	private JButton startBtn;
	private JButton levelEditorBtn;
	private JButton profilesBtn;
	private JButton exitBtn;
	private JPanel panel;
	
	public StartPanel(GuiControl guiControl) {
		this.guiControl = guiControl;
		panel = new JPanel();
		initiateStartPanel();
		initiatePanel();
		
		initiateButtons();
		panel.add(startBtn);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(profilesBtn);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(levelEditorBtn);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(exitBtn);
//		panel.setBackground(Color.BLUE);
		
		panel.setBackground(GuiControl.STANDARD_COLOR);
		setBackground(GuiControl.STANDARD_COLOR);
		setSize(GuiControl.MENU_SIZE);
	}
	
	private void initiateButtons(){
		startBtn = new JButton("Start New Game");
		levelEditorBtn = new JButton("Level editor");
		profilesBtn = new JButton("Profiles");
		exitBtn = new JButton("Exit");
		startBtn.setAlignmentX(CENTER_ALIGNMENT);
		profilesBtn.setAlignmentX(CENTER_ALIGNMENT);
		levelEditorBtn.setAlignmentX(CENTER_ALIGNMENT);
		exitBtn.setAlignmentX(CENTER_ALIGNMENT);
		startBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.startNewGame();
			}
		});
		
		exitBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.exit();
			}
		});
		
		levelEditorBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.showLevelEditor();
			}
		});
		profilesBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.showProfiles();
			}
		});
		
		
	}
	
	private void initiateStartPanel(){
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(Box.createVerticalGlue());
		add(panel);
		add(Box.createVerticalGlue());
		
		panel.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	private void initiatePanel(){
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
	}
	
	@Override
	public void setVisible(boolean b){
		super.setVisible(b);
		panel.setVisible(b);
	}
	
	
}
