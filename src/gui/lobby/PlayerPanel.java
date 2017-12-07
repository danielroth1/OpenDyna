package gui.lobby;

import gui.lobby.content.Slot;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import server.components.Player;

public class PlayerPanel extends JPanel{
	
	private List<Slot> slots = new LinkedList<Slot>();
	private int numberOfSlots;
	private int additionalSlots;
	private int maxNumberOfSlots;
	
	public PlayerPanel(int numberOfSlots, int maxNumberOfSlots) {
		setSlots(numberOfSlots, maxNumberOfSlots, true);
		setMinimumSize(new Dimension(400, 350));
		setPreferredSize(new Dimension(400, 350));
		setMaximumSize(new Dimension(400, 350));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), "Slots"), 
				BorderFactory.createEmptyBorder(10, 20, 20, 20)));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		revalidateSlots();
	}
	
	public void setSlots(int numberOfSlots, int maxNumberOfSlots, boolean newSlots){
		this.numberOfSlots = numberOfSlots;
		additionalSlots = 0;
		this.maxNumberOfSlots = maxNumberOfSlots;
//		slots = new LinkedList<Slot>();
		if (!newSlots)
			for (int i=slots.size(); i<numberOfSlots; i++){
				slots.add(new Slot(false));
			}
		else{
			slots = new LinkedList<Slot>();
			for (int i=0; i<numberOfSlots; i++){
				slots.add(new Slot(false));
			}
		}
		revalidateSlots();
		
	}
	
	/**
	 * add player to specific slot
	 * @param p Player to be added
	 * @param slot player has to spawn in
	 * @return true if successfully but false when slot is already given
	 */
	public boolean addPlayer(Player p, int slot){
		//create new slot if number of slots is too low
		if (slots.size()>= slot){
			//maximum number of slots reached
			if (numberOfSlots + additionalSlots >= maxNumberOfSlots)
				return false;
			Slot s = new Slot(true);
			s.setPlayer(p);
			slots.add(s);
			additionalSlots += 1;
			return true;
		}
		//slot is not empty
		else if (!slots.get(slot).isEmpty()){
			return false;
		}
		else{
			slots.get(slot).setPlayer(p);
			return true;
		}
			
	}
	
	/**
	 * add player to next open slot
	 * @param p Player to be added
	 * @return true if add was successfully else false
	 */
	public boolean addPlayer(Player p){
		for(Slot s : slots){
			if (s.isEmpty()){
				s.setPlayer(p);
				return true;
			}
		}
		return addPlayer(p, slots.size());
	}
	
	/**
	 * remove player from old slot and switch it to new at specific position
	 * @param p Player to switch
	 * @param slot new slotNumber
	 */
	public boolean switchPlayer(Player p, int slot){
		int oldPosition = 0;
		boolean found = false;
		for(int i=0; i<slots.size(); i++){
			Slot s = slots.get(i);
			if (s.getPlayer().equals(p)){
				oldPosition = i;
				found = true;
				break;
			}
		}
		//given slot position is bigger then slotCount
		if (slot >= slots.size()){
			slots.get(oldPosition).removePlayer();
			return addPlayer(p, slots.size());
		}
		//player is included in slots somewhere
		if (!found)
			return false;
		else{
			//new slot is not empty
			if (!slots.get(slot).isEmpty()){
				return false;
			}
			//else
			slots.get(oldPosition).setPlayer(p);
			slots.get(oldPosition).setPlayer(null);
			return true;
			
		}
	}
	
	public boolean removePlayer(Player p){
		for (int i=0; i<slots.size(); i++){
			Slot s = slots.get(i);
			if (s.getPlayer() == null)
				continue;
			if (s.getPlayer() == p){
				s.removePlayer();
				s.update();
				//observer slots
				if (i >= numberOfSlots){
					slots.remove(i);
					revalidateSlots();
				}
				
				return true;
			}
		}
		return false;
	}
	
	public void addPlayerList(List<Player> players){
		for (Player p : players){
			addPlayer(p);
		}
	}
	
	public void addSlot(Slot s){
		add(s);
		add(Box.createRigidArea(new Dimension(0, 10)));
	}
	
	public void revalidateSlots(){
		removeAll();
		for (Slot s : slots){
			s.setAlignmentX(LEFT_ALIGNMENT);
			addSlot(s);
		}
		revalidate();
		repaint();
	}
	
	public void update(){
		for (Slot s : slots){
			s.update();
		}
		repaint();
	}
	
}
