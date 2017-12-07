package gui.levelEditor;

import gui.levelEditor.toolsPanel.Terrain;
import gui.levelEditor.toolsPanel.Tool;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import server.components.Block;

import maps.Texture;

public class ToolsBar extends JPanel{
	
	private LeGuiControl leGuiControl;
	private LeLogicControl leLogicControl;
	private JScrollPane scrollPane;
	private TexturePanel texturePanel;
	private ToolsPanel toolsPanel;
	
	public ToolsBar(LeLogicControl leLogicControl, LeGuiControl leGuiControl) {
		this.leLogicControl = leLogicControl;
		this.leGuiControl = leGuiControl;
		
		texturePanel = new TexturePanel(leLogicControl, leGuiControl);
		toolsPanel = new ToolsPanel(leLogicControl, leGuiControl);
		scrollPane = new JScrollPane(texturePanel);
		toolsPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(scrollPane);
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(toolsPanel);
		setBorder(BorderFactory.createRaisedBevelBorder());
		setBackground(LeGuiControl.MENU_COLOR);
		
		//TEST
		for (Texture t : Texture.values()){
			texturePanel.addTextureButton(t);
		}
//		terrainPanel.addTerrainButton(Terrain.EARTH);
//		terrainPanel.addTerrainButton(Terrain.GRAS);
//		terrainPanel.addTerrainButton(Terrain.STONE);
//		terrainPanel.addTerrainButton(Terrain.WATER);
//		terrainPanel.addTerrainButton(Terrain.BRIDGE);
	}
	
	public Texture getSelectedTexture(){
		return texturePanel.getSelectedTexture();
	}
	
	/**
	 * returns the selection in tools panel
	 * @return
	 */
	public String getSelection(){
		return toolsPanel.getSelection();
	}
	
	public Block getSelectedBlock(){
		Block b = toolsPanel.getSelectedBlock();
		b.setTexture(getSelectedTexture());
		return b;
	}
	
	public Terrain getSelectedTerrain(){
		Terrain t = toolsPanel.getSelectedTerrain();
		t.setTexture(getSelectedTexture());
		return t;
	}
	
	public Tool getSelectedTool(){
		return toolsPanel.getSelectedTool();
	}
	
}
