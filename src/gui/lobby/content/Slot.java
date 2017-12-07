package gui.lobby.content;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.GuiControl;

import server.components.Player;

public class Slot extends JPanel{
	
	public static final Color STANDARD_COLOR = GuiControl.STANDARD_COLOR.darker();
	public static final Color STANDARD_COLOR_OBSERVER = GuiControl.STANDARD_COLOR.brighter();
	private JLabel name;
	private JLabel colorTitle;
	private JLabel color;
	private boolean empty;
	private Player player;
	private boolean observer;
	
	public Slot(boolean observer) {
		this.observer = observer;
		player = null;
		empty = true;
		name = new JLabel("<empty slot>");
		color = new JLabel("      ");
		name.setForeground(Color.WHITE);
		color.setBackground(STANDARD_COLOR);
		color.setOpaque(true);
		
		setBackground(STANDARD_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(name);
		add(Box.createRigidArea(new Dimension(20, 0)));
		add(color);
		setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		setMinimumSize(new Dimension(200, 30));
		setPreferredSize(new Dimension(200, 30));
		setMaximumSize(new Dimension(200, 30));
	}
	
	public Slot(Player player){
		this.player = player;
		empty = false;
	}

	public boolean isEmpty() {
		return empty;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		empty = false;
		update();
	}
	
	public void removePlayer(){
		this.player = null;
		empty = true;
		update();
	}
	
	/**
	 * update the given information of the player
	 */
	public void update(){
		if (player == null){
			name.setText("<empty slot>");
			color.setBackground(STANDARD_COLOR);
		}
		else{
			name.setText(player.getName());
			if (player.getColor() != null)
				color.setBackground(player.getColor());
			if(observer)
				setBackground(STANDARD_COLOR_OBSERVER);
			else
				setBackground(STANDARD_COLOR);
		}
	}
	
	
}
