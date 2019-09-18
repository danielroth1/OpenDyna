package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import gui.GamePanel;
import gui.LobbyPanel;
import gui.ProfilesPanel;
import gui.StartPanel;
import gui.TerrainControl;
import gui.gamePanel.GamePanelBackground;
import gui.gamePanel.WinLosePanel;
import gui.images.Images;
import gui.levelEditor.LeMainControl;
import gui.levelEditor.toolsPanel.Terrain;
import gui.lobby.ChangeMapFrame;
import gui.lobby.GameInfoPanel;
import gui.profiles.Hotkeys;
import gui.profiles.Profile;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import maps.Map;

import client.Client;
import client.ClientImpl;

public class GuiControl {
	
	public static Dimension SIZE = new Dimension(1024, 768);
	public static Dimension MENU_SIZE = new Dimension(800, 600);
	public static Color STANDARD_COLOR = new Color(188, 71, 71);
	public static Dimension WIN_LOOSE_PANEL_SIZE = new Dimension(200, 300);
	public static Dimension CHANGE_MAP_FRAME_SIZE = new Dimension(610, 550);

	private GameControl gameControl;
	private Images images;
	private JFrame frame;
	private JPanel contentPanel;
	private StartPanel startPanel;
	private LobbyPanel lobbyPanel;
	private GamePanelBackground gamePanelBackground;
	private GamePanel gamePanel;
	private ProfilesPanel profilesPanel;
	private WinLosePanel winLosePanel;
	private LeMainControl leMainControl;
	private TerrainControl terrainControl;
	
	public GuiControl(GameControl gameControl) {
		this.gameControl = gameControl;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		images = new Images();
		frame = new JFrame("OpenDyna");
		contentPanel = (JPanel)frame.getContentPane();
		startPanel = new StartPanel(this);
		lobbyPanel = new LobbyPanel(this, gameControl);
		gamePanel = new GamePanel();
		gamePanelBackground = new GamePanelBackground(gamePanel);
		terrainControl = new TerrainControl(this);
		profilesPanel = new ProfilesPanel(this, gameControl);
		winLosePanel = new WinLosePanel(gameControl, this);
		
		
		gamePanel.setImages(images);
		gamePanel.add(winLosePanel);
		contentPanel.setLayout(new CardLayout());
		contentPanel.add(startPanel);
		contentPanel.add(lobbyPanel);
		contentPanel.add(profilesPanel);
//		contentPanel.add(winLosePanel);
//		contentPanel.add(gamePanelBackground);
		contentPanel.add(gamePanel);
		//dummy label
		JLabel label = new JLabel();
		label.setSize(new Dimension(0, 0));
		contentPanel.add(label);
//		contentPanel.setComponentZOrder(gamePanel, 5);
		
		start();
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		frame.setSize(MENU_SIZE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void start(){
//		gamePanelBackground.setVisible(false);
		gamePanel.setVisible(false);
		startPanel.setVisible(true);
		
		//INITIATE KEY LISTENERS
				gamePanel.addKeyListener(new KeyAdapter(){
					public void keyPressed(KeyEvent e){
						ClientImpl c2;
						for (Client c : gameControl.getLocalClients()){
							if (c instanceof ClientImpl){
								c2 = (ClientImpl)c;
								c2.getPressedKeys().add(e.getKeyCode());
							}
						}
					}
					
					public void keyReleased(KeyEvent e){
						ClientImpl c2;
						for (Client c : gameControl.getLocalClients()){
							if (c instanceof ClientImpl){
								c2 = (ClientImpl)c;
								c2.getPressedKeys().remove(e.getKeyCode());
							}
						}
					}
				});
	}
	
	public void startNewGame(){
		startPanel.setVisible(false);
		lobbyPanel.setVisible(true);
		gameControl.startNewGame();
		frame.setSize(MENU_SIZE);
	}
	
	public void startBtnPressed(){
		lobbyPanel.setVisible(false);
//		gamePanelBackground.setVisible(true);
		gamePanel.setVisible(true);
		gameControl.startBtnPressed();
		gamePanel.requestFocus();
		frame.setSize(SIZE);
		frame.setLocationRelativeTo(null);
	}
	
	public void addClient(Profile p){
		gameControl.addClientBtnPressed(p);
		lobbyPanel.update();
	}
	
	public void addClient(){
		gameControl.addClientBtnPressed();
		lobbyPanel.update();
	}
	
	public void removeClient(){
		gameControl.removeNewClient();
		lobbyPanel.update();
	}
	
	public void exit(){
		System.exit(0);
	}
	
	public void update(){
		gamePanel.repaint();
	}
	
	public GamePanel getGamePanel(){
		return gamePanel;
	}
	
	public LobbyPanel getLobbyPanel(){
		return lobbyPanel;
	}
	
	public void changeMapBtnPressed(){
		gameControl.requestMaps();
		lobbyPanel.getChangeMapFrame().setLocationRelativeTo(null);
		lobbyPanel.getChangeMapFrame().setVisible(true);
	}
	
	public void changeMapOkBtnPressed(){
		lobbyPanel.getChangeMapFrame().setVisible(false);
	}
	
	public void changeMapBackBtnPressed(){
		lobbyPanel.getChangeMapFrame().setVisible(false);
	}
	
	public void shuffleBtnPressed(){
		
	}
	
	public void setMapInMapPanel(Map map, boolean newSlots){
		lobbyPanel.setMap(map, newSlots);
		lobbyPanel.repaint();
	}
	
	public void writeInChat(String s){
		lobbyPanel.writeInChat(s);
	}
	
	public void mapChanged(Map map){
		terrainControl.setTerrain(map.getPlayingField().getTerrain());
		lobbyPanel.setMap(map, false);
	}
	
	public void openLevelEditor(){
		startPanel.setVisible(false);
	}
	
	public void showProfiles(){
		hideAll();
		profilesPanel.setVisible(true);
	}
	
	public void showLevelEditor(){
		setLevelEditorVisible(true);

	}
	
	public void backToMenue(){
		hideAll();
		startPanel.setVisible(true);
		frame.setSize(MENU_SIZE);
		frame.setLocationRelativeTo(null);
	}
	
	private void hideAll(){
		lobbyPanel.setVisible(false);
		gamePanel.setVisible(false);
		startPanel.setVisible(false);
		profilesPanel.setVisible(false);
		winLosePanel.setVisible(false);
	}
	
	public List<Profile> getProfiles(){
		return profilesPanel.getProfilesList();
	}
	
	public List<Profile> getHumanProfiles(){
		List<Profile> list = new LinkedList<Profile>();
		for (Profile p : getProfiles()){
			if (!p.isKi())
				list.add(p);
		}
		return list;
	}
	
	public List<Profile> getAIProfiles(){
		List<Profile> list = new LinkedList<Profile>();
		for (Profile p : getProfiles()){
			if (p.isKi())
				list.add(p);
		}
		return list;
	}
	
	public void updateGameInfo(){
		lobbyPanel.updateGameInfo();
	}
	
	public Profile getSelectedProfile(){
		return profilesPanel.getSelectedProfile();
	}
	
	public Hotkeys getFormatedHotkeys(){
		return profilesPanel.getFormatedHotkeys();
	}
	
	public void updateKeysToSelectedProfile(){
		profilesPanel.updateKeysToSelectedProfile();
	}
	
	public void setLevelEditorVisible(boolean b){
		if (leMainControl == null){
			leMainControl = new LeMainControl(gameControl, this);
		}
		if (b){
			leMainControl.showFrame();
		}
		else{
			leMainControl.hideFrame();
		}
	}
	
	public JFrame getMainFrame(){
		return frame;
	}

	public TerrainControl getTerrainControl() {
		return terrainControl;
	}
	
	public Terrain[][] getTerrain(){
		return terrainControl.getTerrain();
	}

	public Images getImages() {
		return images;
	}
	
	public void addProfile(Profile p){
		profilesPanel.addProfile(p);
	}
	
	public void showWinLosePanel(){
		winLosePanel.setVisible(true);
		System.out.println("show Win Lose Panel");
	}
	
//	public void backToMenue(){
//		winLosePanel.setVisible(false);
//		startPanel.setVisible(true);
//	}
	
	public void hideWinLosePanel(){
		winLosePanel.setVisible(false);
	}
	
	public void setWinLoosePanel(boolean won, boolean draw, String player){
		winLosePanel.setWon(won, draw, player);
	}
	
	public void refreshGui(){
		gamePanel.refresh();
		lobbyPanel.getChatPanel().resetText();
	}
	
	public void setMapsInChangeMapFrame(List<Map> maps){
		lobbyPanel.getChangeMapFrame().setMaps(maps);
	}
	
	public List<Map> getMapsInChangeMapFrame(List<Map> maps){
		return lobbyPanel.getChangeMapFrame().getMaps();
	}
	
}
