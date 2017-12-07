package gui.levelEditor;

import javax.swing.JFrame;

import main.GameControl;
import main.GuiControl;

public class LeMainControl {
	
	private GuiControl guiControl;
	private GameControl gameControl;
	private LeLogicControl leLogicControl;
	private LeGuiControl leGuiControl;
	private LevelEditorPanel lePanel;
	private JFrame mainFrame;
	
	public LeMainControl(GameControl gameControl, GuiControl guiControl) {
		this.guiControl = guiControl;
		this.gameControl = gameControl;
		mainFrame = new JFrame("Level Editor");
		
		leGuiControl = new LeGuiControl(mainFrame, guiControl.getImages());
		leLogicControl = new LeLogicControl(gameControl, guiControl, leGuiControl);
		leGuiControl.setComponents(guiControl, gameControl, leLogicControl);
	}
	
	public JFrame getLevelEditorMainFrame(){
		return mainFrame;
	}
	
	public void showFrame(){
		mainFrame.setVisible(true);
	}
	
	public void hideFrame(){
		mainFrame.setVisible(false);
	}
}
