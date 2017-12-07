package gui;

import java.util.List;

import server.components.ArrayPosition;
import server.components.Bomb;
import server.components.Explosion;
import server.components.Figure;
import gui.levelEditor.toolsPanel.Terrain;
import main.GuiControl;

/**
 * stores methodes to handle gui of terrain in game
 * @author Daniel Roth
 */
public class TerrainControl {
	
	private GuiControl guiControl;
	private GamePanel gamePanel;
	private Terrain[][] terrain;
	
	public TerrainControl(GuiControl guiControl) {
		this.guiControl = guiControl;
		this.gamePanel = guiControl.getGamePanel();
	}
	
	public void bombExploded(Bomb b, List<ArrayPosition> explosionArea){
		if (terrain != null){
			for (ArrayPosition a : explosionArea){
				Terrain t = terrain[a.getRow()][a.getColumn()];
				if (t != null){
					t.setBurned(true);
				}
			}
			update();
		}
	}
	
	public void explosions(List<Explosion> explosions){
		if (terrain != null){
			for (Explosion e : explosions){
				ArrayPosition a = e.getArrayPosition();
				Terrain t = terrain[a.getRow()][a.getColumn()];
				if (t != null)
					t.setBurned(true);
			}
			update();
		}
	}
	
	public void figureDied(Figure f){
		if (terrain != null){
			ArrayPosition a = f.getArrayPosition();
			Terrain t = terrain[a.getRow()][a.getColumn()];
			if (f != null)
				t.setBloody(true);
			update();
		}
	}
	
	public void setTerrain(Terrain[][] terrain){
		this.terrain = terrain;
		update();
	}
	
	public Terrain[][] getTerrain(){
		return terrain;
	}
	
	public void update(){
		gamePanel.setTerrain(terrain);
	}
	
}
