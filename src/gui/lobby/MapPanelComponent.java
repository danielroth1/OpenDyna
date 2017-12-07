package gui.lobby;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import server.Square;
import server.components.ArrayPosition;
import server.components.Block;

import main.GuiControl;
import maps.Map;

/**
 * shows the map itself
 * @author Daniel Roth
 */
public class MapPanelComponent extends JPanel{
	
	private Map map;
	private Square[][] array;
	private ArrayPosition a;
	private int factor;
	private boolean showMap;
	private int scale;
	
	public MapPanelComponent(int scale) {
		this.scale = scale;
		Dimension size = new Dimension(scale, scale);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	}
	
	public void update(){
		array = map.getPlayingField().getField();
		a = new ArrayPosition(map.getPlayingField().getRow(), map.getPlayingField().getColumn());
		if (a.getRow() < a.getColumn())
			factor = scale / a.getColumn();
		else
			factor = scale / a.getRow();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if (showMap){
			for(int r=0; r<a.getRow(); r++){
				for (int c=0; c<a.getColumn(); c++){
					
//					if (r%2 == 1
//							&& c%2 == 1){
//						g.setColor(Color.DARK_GRAY);
//						g.fillRect(c*factor, r*factor, factor, factor);
//					}
//					else{
						if (array[r][c] != null){
							if (array[r][c] instanceof Block){
								Block b = (Block)array[r][c];
								if (b.isDestructable())
									g.setColor(Color.LIGHT_GRAY);
								else
									g.setColor(Color.DARK_GRAY);
								g.fillRect(c*factor, r*factor, factor, factor);
							}
						}
						g.setColor(Color.WHITE);
//					}
				}
			}
			//STARTING POSITIONS
			g.setColor(Color.GREEN);
			for (ArrayPosition a : map.getStartingPositions()){
				g.fillRect(a.getColumn() * factor, a.getRow() * factor, factor, factor);
			}
			//GRID
			g.setColor(Color.BLACK);
			for(int r=0; r<a.getRow(); r++){
				for (int c=0; c<a.getColumn(); c++){
					g.drawRect(c*factor, r*factor, factor, factor);
					}
				}
			
		}
		
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
		update();
		showMap = true;
		repaint();
	}
	
	
}
