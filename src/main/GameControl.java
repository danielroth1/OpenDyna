package main;

import gui.profiles.Hotkeys;
import gui.profiles.Profile;
import gui.profiles.ProfilePanel;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import maps.Map;
import maps.MapFactory;

import client.Client;
import client.ClientImpl;
import client.ai.EasyAi;
import server.Logic;
import server.Server;
import server.Square;
import server.components.ArrayPosition;
import server.components.Block;
import server.components.Figure;

public class GameControl {
	
	public static Color[] COLORS = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.PINK};
	private GuiControl guiControl;
	private SaveLoad saveLoad;
	private Server server;
	private List<Client> clients = new LinkedList<Client>();
	private LinkedList<Figure> figures = new LinkedList<Figure>();
	private Map map;
	private List<Profile> profilesInUse = new LinkedList<Profile>();
	private HashMap<Client, Profile> profilesMap = new HashMap<Client, Profile>();
	private Profile easyKi = new Profile("Easy Ki", Color.MAGENTA);
	private boolean mapsAlreadyRequested = false;
	
	public GameControl() {
		map = MapFactory.getSelfCreatedMap();
		saveLoad = new SaveLoad();
	}
	
	public void setComponents(GuiControl guiControl){
		this.guiControl = guiControl;
		start();
	}
	
	/**
	 * start after initializing components
	 */
	private void start(){
		//MANAGE PROFILES
		List<Profile> profiles = new LinkedList<Profile>();
		if (!saveLoad.isSettingsSet()){
			Profile player1 = new Profile("Player1", Color.BLUE, "w", "s", "a", "d", "f");
			Profile player2 = new Profile("Player2", Color.RED, "UP", "DOWN", "LEFT", "RIGHT", "NUMPAD0");
			Profile player3 = new Profile("Player3", Color.YELLOW);
			Profile player4 = new Profile("Player4", Color.GREEN);
			profiles.add(player1);
			profiles.add(player2);
			profiles.add(player3);
			profiles.add(player4);
			saveLoad.saveProfiles(profiles);
		}
//		Map map = saveLoad.loadMap("Standard map");
//		for (int i=0; i<10; i++){
//			saveLoad.saveMap(MapFactory.getRandomMap(13, 13, i*10, "RandomMap " + i));
//		}
		profiles = loadProfiles();
		for (Profile p : profiles){
			guiControl.addProfile(p);
		}
//		guiControl.addProfile(player1);
//		guiControl.addProfile(player2);
//		guiControl.addProfile(player3);
//		guiControl.addProfile(player4);
		easyKi.setKi(true);
		easyKi.setHotkeys(new Hotkeys());
		guiControl.addProfile(easyKi);
	}
	
	/**
	 * loads all maps from maps directory
	 * @return List<Map> maps
	 */
	private List<Map> loadAllMaps(){
		return saveLoad.loadMapsObject();
	}
	
	/**
	 * is called by changeMapFrame when it needs all maps loaded
	 */
	public void requestMaps(){
		if (!mapsAlreadyRequested)
			guiControl.setMapsInChangeMapFrame(loadAllMaps());
		mapsAlreadyRequested = true;
	}
	
	public void startNewGame(){
		if (server != null)
			server.shutDown();
		server = new Server();
		Logic logic = server.getGameLogic();
		for (Client c : clients){
			ClientImpl ci = null;
			if (c instanceof ClientImpl)
				ci = (ClientImpl) c;
			if (ci != null)
				ci.shutDown();
		}
		clients = new LinkedList<Client>();
		figures = new LinkedList<Figure>();
		profilesInUse = new LinkedList<Profile>();
		profilesMap = new HashMap<Client, Profile>();
		
		guiControl.refreshGui();
		//INITIATE CLIENTS
//		addClient(0, true, Color.BLUE);
//		addClient(1, false, Color.RED);
		
//		clientImpls.get(0).setKeyBindings("W", "S", "A", "D", "F");
//		clientImpls.get(1).setKeyBindings("UP", "DOWN", "LEFT", "RIGHT", "NUMPAD0");
		
//		server.getGameLogic().setWorldComponents(minimumWorld, figures);
		Profile p = new Profile("first", Color.BLACK);
		initiateFirstClient(addNewClient(p));
		server.updateGui();
		guiControl.updateGameInfo();
		
		guiControl.setMapsInChangeMapFrame(loadAllMaps());
		map = MapFactory.getRandomMap(13, 13, 100);
		guiControl.setMapInMapPanel(map, true);
		server.getGameLogic().setMap(map);
		
	}
	
	public void startBtnPressed(){
		server.getGameLogic().startNewGame();
	}
	
	public List<Client> getLocalClients(){
		return clients;
	}
	
	public void initiateFirstClient(Client c){
		ClientImpl ci;
		if (c instanceof ClientImpl){
			ci = (ClientImpl)c;
			ci.forceSurrender();
		}
//		clients.remove(c);
		profilesMap.remove(c);
	}
	
	public Client addNewClient(Hotkeys hotkeys){
		Client c = null;
		if (clients.isEmpty())
			c = addClient(clients.size(), true, 
					new Profile(null, COLORS[clients.size()]));
		else
			c = addClient(clients.size(), false, 
					new Profile(null, COLORS[clients.size()]));
		
		server.updateGui();
		guiControl.updateGameInfo();
		return c;
	}
	
	private boolean hasFirst(List<Client> list){
		ClientImpl c2;
		boolean first = false;
		for (Client c : list){
			if (c instanceof ClientImpl){
				c2 = (ClientImpl) c;
				if (c2.isFirst())
					first = true;
			}
		}
		return first;
	}
	
	public Client addNewClient(Profile p){
		Client c = null;
		if (!hasFirst(clients))
			c = addClient(clients.size(), true, p);
		else
			c = addClient(clients.size(), false, p);
		
		server.updateGui();
		guiControl.updateGameInfo();
		return c;
	}
	
	public void removeNewClient(){
		int clientPos = clients.size()-1;
		if (!(clientPos<0))
			removeClient(clients.get(clients.size()-1));
		else{
			guiControl.writeInChat("no more players left");
			System.out.println("no more players left");
		}
		server.updateGui();
		guiControl.updateGameInfo();
	}
	
	private Client addClient(int number, boolean first, Profile p){
		Hotkeys hotkeys = p.getHotkeys();
		Client client;
		
		if (p == easyKi){
			client = new EasyAi(p.getName(), server);
		}
		else if (!p.isNameSet())
			client = new ClientImpl("ClientName" + number, server, guiControl, this, first);
		else
			client = new ClientImpl(p.getName(), server, guiControl, this, first);
		client.getFigure().setColor(p.getColor());
		ClientImpl c = null;
		if (client instanceof ClientImpl){
			c = (ClientImpl) client;
		}
		if (hotkeys.isSet()){
			c.setKeyBindings(hotkeys.getUp(), hotkeys.getDown(), hotkeys.getLeft(), 
					hotkeys.getRight(), hotkeys.getBomb());
		}
		
		profilesMap.put(client, p);
		server.getGameLogic().addClient(client);
		clients.add(client);
		figures.add(client.getFigure());
		guiControl.getGamePanel().addFigure(client.getFigure());
		return client;
	}
	
	private void removeClient(Client c){
		server.getGameLogic().removeClient(c);
		clients.remove(c);
		figures.remove(c.getFigure());
		Profile p = profilesMap.get(c);
		if (p != null){
			profilesInUse.remove(p);
		}
		guiControl.getGamePanel().removeFigure(c.getFigure());
	}
	
	public void shuffleMap(){
		map.shuffle();
	}
	
	/**
	 * change the current map in game lobby
	 * @param map
	 */
	public void changeMapRequest(Map map){
		this.map = map;
		server.getGameLogic().setMap(map);
	}
	
	public void writeChatInLobby(String s){
		if (!clients.isEmpty())
			server.chatMessageSentInLobby(clients.get(0), s);
		else
			guiControl.writeInChat("add Client to write in chat");
	}
	
	public void randomMapChosen(int row, int column, int density){
		changeMapRequest(MapFactory.getRandomMap(row, column, density));
	}
	
	/**
	 * add client with a profile selected
	 * @param p selected profile
	 */
	public void addClientBtnPressed(Profile p){
		addNewClient(p);
		profilesInUse.add(p);
	}
	
	/**
	 * add client without a profile selected
	 */
	public void addClientBtnPressed(){
		addNewClient(new Hotkeys());
	}
	
	public List<Profile> getProfilesInUse(){
		return profilesInUse;
	}
	
	public List<Profile> getProfiles(){
		return guiControl.getProfiles();
	}
	
	public List<Profile> getProfilesNotIntUse(){
		List<Profile> list = getProfiles();
		List<Profile> inUse = getProfilesInUse();
		for (Profile p : inUse){
			if (list.contains(p)){
				//ki wouldnt be removed
				if (!p.isKi())
					list.remove(p);
			}
		}
		return list;
	}
	
	public void applyProfiles(){
		guiControl.getSelectedProfile().setHotkeys(guiControl.getFormatedHotkeys());
		saveProfiles();
	}
	
	public void profileChanged(){
		if (guiControl != null){
			guiControl.getSelectedProfile().setHotkeys(guiControl.getFormatedHotkeys());
		}
	}
	
	public void profileNameChanged(String name){
		guiControl.getSelectedProfile().setName(name);
	}
	
	public void backToMenueBtnPressed(){
		guiControl.backToMenue();
	}
	
	public void returnGameBtnPressed(){
		guiControl.hideWinLosePanel();
	}
	
	public List<Profile> loadProfiles(){
		return saveLoad.loadProfiles();
	}
	
	public void saveProfiles(){
		saveLoad.saveProfiles(guiControl.getHumanProfiles());
	}
	
	public SaveLoad getSaveLoad(){
		return saveLoad;
	}
}
