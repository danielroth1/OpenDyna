package gui.levelEditor;

import gui.images.Images;
import gui.levelEditor.content.LoadFrame;
import gui.levelEditor.content.NewMapFrame;
import gui.levelEditor.content.SaveLoadFrame;
import gui.levelEditor.toolsPanel.Terrain;
import gui.levelEditor.toolsPanel.Tool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import server.components.ArrayPosition;
import server.components.Block;

import main.GameControl;
import main.GuiControl;
import maps.Map;
import maps.Texture;

public class LeGuiControl {
	
//	public static Color MENU_COLOR = new Color(168, 255, 187);
	public static Color MENU_COLOR = GuiControl.STANDARD_COLOR;
//	public static Color CANVAS_COLOR = new Color(228, 228, 228);
	public static Color CANVAS_COLOR = Color.WHITE;
	public static Dimension FRAME_SIZE = new Dimension((int)GuiControl.MENU_SIZE.getWidth() + 200, 
			(int)GuiControl.MENU_SIZE.getHeight() + 100);
	
	public static Dimension SMAL = new Dimension(300, 200);
	public static Dimension MEDIUM = new Dimension(600, 600);
	public static Dimension LARGE = new Dimension(1024, 768);
	
	private JFrame mainFrame;
	private GuiControl guiControl;
	private GameControl gameControl;
	private LeLogicControl leLogicControl;
	private LevelEditorPanel lePanel;
	private Images images;
	
	private NewMapFrame newMapFrame;
	private SaveLoadFrame saveLoadFrame;
	private LoadFrame loadFrame;

	public LeGuiControl(JFrame mainFrame, Images images) {
		this.mainFrame = mainFrame;
		this.images = images;
		
		mainFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				LeGuiControl.this.mainFrame.setVisible(false);
				LeGuiControl.this.newMapFrame.setVisible(false);
				LeGuiControl.this.saveLoadFrame.setVisible(false);
				LeGuiControl.this.loadFrame.setVisible(false);
			}
		});
		mainFrame.setSize(FRAME_SIZE);
		mainFrame.setLocationRelativeTo(null);
	}
	
	public void setComponents(GuiControl guiControl, GameControl gameControl, LeLogicControl leLogicControl){
		this.gameControl = gameControl;
		this.guiControl = guiControl;
		this.leLogicControl = leLogicControl;
		
		start();
	}
	
	/**
	 * is called after initializing the components
	 */
	private void start(){
		lePanel = new LevelEditorPanel(guiControl);
		lePanel.setComponents(this, leLogicControl);
		mainFrame.add(lePanel);
		newMapFrame = new NewMapFrame(leLogicControl, this);
		loadFrame = new LoadFrame(leLogicControl, this);
		saveLoadFrame = new SaveLoadFrame(leLogicControl, this);
		
		lePanel.setVisible(true);
	}
	
	public void setVisibleNewMapFrame(boolean b){
		newMapFrame.setVisible(b);
	}
	
	public void setVisibleLoadFrame(boolean b){
		if (b)
			saveLoadFrame.setState(SaveLoadFrame.LOAD);
		saveLoadFrame.setVisible(b);
	}
	
	public void setVisibleSaveFrame(boolean b){
		if (b)
			saveLoadFrame.setState(SaveLoadFrame.SAVE);
		saveLoadFrame.setVisible(b);
	}
	
	public void setVisibleFrame(boolean b){
		mainFrame.setVisible(b);
	}
	
	public Images getImages(){
		return images;
	}
	
	public void updateCanvasPanel(Map map){
		lePanel.updateCanvasPanel(map);
	}
	
	public void updateCanvasPanel(){
		lePanel.updateCanvasPanel();
	}
	
	/**
	 * NOT IMPLEMENTED
	 * @param a
	 */
	public void updateCanvasPanel(ArrayPosition a){
		lePanel.updateCanvasPanel(a);
	}
	
	/**
	 * returns the selected Texture in texturePanel
	 * @return Texture
	 */
	public Texture getSelectedTexture(){
		return lePanel.getSelectedTexture();
	}
	
	/**
	 * returns the selection in tools panel
	 * see ToolsPanel.TERRAIN and
	 * ToolsPanel.BLOCK
	 * @return String
	 */
	public String getSelection(){
		return lePanel.getSelection();
	}
	
	public Block getSelectedBlock(){
		return lePanel.getSelectedBlock();
	}
	
	public Terrain getSelectedTerrain(){
		return lePanel.getSelectedTerrain();
	}
	
	public Tool getSelectedTool(){
		return lePanel.getSelectedTool();
	}
	
	public void setTextNameSaveLoadFrame(String text){
		saveLoadFrame.setNameTextField(text);
	}
	
}
