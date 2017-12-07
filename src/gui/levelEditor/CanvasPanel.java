package gui.levelEditor;

import java.awt.Dimension;

import gui.GamePanel;

import javax.swing.JPanel;

import server.components.ArrayPosition;
import server.components.PlayingField;

import maps.Map;

public class CanvasPanel extends GamePanel{
	
	private LeGuiControl leGuiControl;
	private LeLogicControl leLogicControl;
	private Map map;
	private PlayingField playingField;

	
	public CanvasPanel(LeLogicControl leLogicControl, LeGuiControl leGuiControl) {
		super(false, true);
		this.leLogicControl = leLogicControl;
		this.leGuiControl = leGuiControl;
		setImages(leGuiControl.getImages());
		
		setBackground(LeGuiControl.CANVAS_COLOR);
	}
	
	public void update(Map map){
		this.map = map;
		this.playingField = map.getPlayingField();
		setMovedWorld(playingField.getWorld());
		setTerrain(playingField.getTerrain());
		setStartingPositions(map.getStartingPositions());
		ArrayPosition size = map.getSize();
		Dimension newSize = new Dimension(size.getRow()*50+50, size.getColumn()*50+50);
		setMinimumSize(newSize);
		setPreferredSize(newSize);
		setMaximumSize(newSize);
//		System.out.println(getSize());
		repaint();
	}
	
	public void update(){
		update(map);
	}
	
	/**
	 * TODO: NOT IMPLEMENTED
	 * @param a
	 */
	public void update(ArrayPosition a){
		
	}
	
}
