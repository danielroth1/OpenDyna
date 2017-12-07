package gui.levelEditor.content;

import gui.levelEditor.LeGuiControl;
import gui.levelEditor.LeLogicControl;

import javax.swing.JFrame;

public class LoadFrame extends JFrame{
	
	private LeGuiControl guiControl;
	private LeLogicControl logicControl;
	
	public LoadFrame(LeLogicControl logicControl, LeGuiControl guiControl) {
		this.logicControl = logicControl;
		this.guiControl = guiControl;
	}
	
}
