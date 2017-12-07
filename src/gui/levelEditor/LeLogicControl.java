package gui.levelEditor;

import gui.TerrainControl;
import gui.levelEditor.toolsPanel.Terrain;
import gui.levelEditor.toolsPanel.TerrainType;
import gui.levelEditor.toolsPanel.Tool;

import java.awt.Dimension;
import java.util.LinkedList;

import server.Square;
import server.components.ArrayPosition;
import server.components.Block;
import server.components.PlayingField;

import main.GameControl;
import main.GuiControl;
import main.SaveLoad;
import maps.Map;
import maps.Texture;

public class LeLogicControl {
	
	private GuiControl guiControl;
	private GameControl gameControl;
	private LeGuiControl leGuiControl;
	private PlayingField playingField;
	private Map map;
	private SaveLoad saveLoad;

	public LeLogicControl(GameControl gameControl, GuiControl guiControl, LeGuiControl leGuiControl) {
		this.gameControl = gameControl;
		this.guiControl = guiControl;
		this.leGuiControl = leGuiControl;
		saveLoad = gameControl.getSaveLoad();
	}
	
	public void newBtnPressed(){
		
		leGuiControl.setVisibleNewMapFrame(true);
	}
	
	public void saveBtnPressed(){
		if (map != null)
			leGuiControl.setTextNameSaveLoadFrame(map.getName());
		leGuiControl.setVisibleSaveFrame(true);
	}
	
	public void loadBtnPressed(){
		if (map != null)
			leGuiControl.setTextNameSaveLoadFrame(map.getName());
		leGuiControl.setVisibleLoadFrame(true);
	}
	
	public void exitBtnPressed(){
		leGuiControl.setVisibleFrame(false);
	}
	
	public void setNewMap(String name, ArrayPosition size, boolean isStatic, boolean random){
		playingField = new PlayingField(size.getRow(), size.getColumn(), isStatic);
		map = new Map(playingField, new LinkedList<ArrayPosition>(), name);
		leGuiControl.updateCanvasPanel(map);
	}
	
	public void buttonPressedInCanvasPanel(ArrayPosition a){
//		System.out.println(a);
		if (isLegalPosition(a)){
			Tool t = leGuiControl.getSelectedTool();
			switch(t){
			case ADD:
				setSquare(a, leGuiControl.getSelectedTexture(), false);
				break;
			case REPLACE:
				setSquare(a, leGuiControl.getSelectedTexture(), true);
				break;
			case REMOVE:
				removeSquare(a);
				break;
			case FILL:
				
				break;
			default:
				setSquare(a, leGuiControl.getSelectedTexture(), true);
			}
				
			
		}
	}
	private void removeSquare(ArrayPosition a){
		if (leGuiControl.getSelection() == ToolsPanel.BLOCK){
			playingField.removeSquare(a);
		}
		else if (leGuiControl.getSelection() == ToolsPanel.TERRAIN){
			playingField.removeTerrain(a);
		}
		else if (leGuiControl.getSelection() == ToolsPanel.STARTING_POSITION){
			map.getStartingPositions().remove(a);
		}
		leGuiControl.updateCanvasPanel();
	}
	
	private void setSquare(ArrayPosition a, Texture t, boolean overwrite){
		if (leGuiControl.getSelection() == ToolsPanel.BLOCK){
			Block b = leGuiControl.getSelectedBlock();
			b.setArrayPosition(a);
//			if (t != null)
//				.setTexture(t);
			playingField.addSquare(b, a, overwrite);
		}
		else if (leGuiControl.getSelection() == ToolsPanel.TERRAIN){
			//TODO: always gras type
//			Terrain ter = new Terrain(t, TerrainType.GRAS, false);
//			if (t == null)
//				ter.setTexture(TerrainType.GRAS.getStandardTexture());
//			ter.setTexture(t);
			playingField.setTerrain(leGuiControl.getSelectedTerrain(), a, overwrite);
		}
		else if (leGuiControl.getSelection() == ToolsPanel.STARTING_POSITION){
			if (!map.getStartingPositions().contains(a))
				map.getStartingPositions().add(a);
		}
//		b.setDestructable(true);
		
		leGuiControl.updateCanvasPanel();
	}
	
	public boolean isLegalPosition(ArrayPosition a){
		if (playingField == null
				|| a == null
				|| a.getRow()>=playingField.getRow()
				|| a.getColumn()>= playingField.getColumn())
			return false;
		return true;
	}
	
	/**
	 * translates a random point to an arrayPosition
	 * NO GAMELOGIC included. always rounds down
	 * if gameLogic needed see:
	 * PlayingField.getArrayPosition(int x, int y);
	 * @param x coordinate
	 * @param y coordinate
	 * @return ArrayPosition
	 */
	public static ArrayPosition getArrayPosition(int x, int y){
		//round down and divid by 50
		return new ArrayPosition((y-y%50)/50, (x-x%50)/50);
	}
	
	public SaveLoad getSaveLoad(){
		return saveLoad;
	}
	
	public void saveMap(String name){
		map.setName(name);
//		saveLoad.saveMap(map);
		saveLoad.saveMapObject(map);
	}
	
	public void loadMap(String name){
		this.map = saveLoad.loadMapObject(name);
		this.playingField = map.getPlayingField();
		leGuiControl.updateCanvasPanel(map);
	}
}
