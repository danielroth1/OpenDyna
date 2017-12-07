package gui;

import gui.lobby.ChangeMapFrame;
import gui.lobby.ChatPanel;
import gui.lobby.GameInfoPanel;
import gui.lobby.MapPanel;
import gui.lobby.PlayerPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.GameControl;
import main.GuiControl;
import maps.Map;

/**
 * shows the current lobby with clients and map and stuff
 * @author Daniel Roth
 */
public class LobbyPanel extends JPanel{
	
	private GuiControl guiControl;
	private GameControl gameControl;
	private PlayerPanel playerPanel;
	private ChatPanel chatPanel;
	private MapPanel mapPanel;
	private GameInfoPanel gameInfoPanel;
	private ChangeMapFrame changeMapFrame;
	
	public LobbyPanel(GuiControl guiControl, GameControl gameControl) {
		this.guiControl = guiControl;
		this.gameControl = gameControl;
		initiateContentPanels();
		initiatePanel();
	}
	
	private void initiatePanel(){
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(playerPanel, 400, 400, 400)
						.addComponent(chatPanel, 400, 400, 400))
				.addGroup(layout.createParallelGroup()
						.addComponent(mapPanel)
						.addComponent(gameInfoPanel))
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
							.addComponent(playerPanel, 350, 350, 350)
							.addComponent(chatPanel))
					.addGroup(layout.createSequentialGroup()
							.addComponent(mapPanel, 300, 300, 300)
							.addComponent(gameInfoPanel))
							)
				);
		
		layout.setAutoCreateGaps(true);
		setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		setSize(GuiControl.MENU_SIZE);
		setVisible(false);
	}
	
	private void initiateContentPanels(){
		playerPanel = new PlayerPanel(4, 16);
		chatPanel = new ChatPanel(guiControl, gameControl);
		mapPanel = new MapPanel(guiControl);
		gameInfoPanel = new GameInfoPanel(guiControl, gameControl);
		changeMapFrame = new ChangeMapFrame(gameControl, guiControl);
		
		playerPanel.setBackground(GuiControl.STANDARD_COLOR);
		setBackground(GuiControl.STANDARD_COLOR);
		chatPanel.setBackground(GuiControl.STANDARD_COLOR);
		mapPanel.setBackground(GuiControl.STANDARD_COLOR);
		gameInfoPanel.setBackground(GuiControl.STANDARD_COLOR);

	}
	
	public PlayerPanel getPlayerPane(){
		return playerPanel;
	}
	
	public void update(){
		updateGameInfo();
	}
	
	public void setMap(Map map, boolean newSlots){
		playerPanel.setSlots(map.getPlayerCount(), 16, newSlots);
		mapPanel.setMap(map);
	}
	
	public void writeInChat(String s){
		chatPanel.showText(s);
	}
	
	public void updateGameInfo(){
		gameInfoPanel.update();
	}
	
	public ChatPanel getChatPanel(){
		return chatPanel;
	}

	public ChangeMapFrame getChangeMapFrame() {
		return changeMapFrame;
	}
	
	
	
}
